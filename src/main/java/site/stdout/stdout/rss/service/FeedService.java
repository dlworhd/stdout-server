package site.stdout.stdout.rss.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import site.stdout.stdout.rss.dto.FeedRequest;
import site.stdout.stdout.rss.dto.FeedResponse;
import site.stdout.stdout.rss.entity.Channel;
import site.stdout.stdout.rss.entity.Feed;
import site.stdout.stdout.rss.repository.ChannelRepository;
import site.stdout.stdout.rss.repository.FeedRepository;
import site.stdout.stdout.rss.type.ReadType;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {

	private final FeedRepository feedRepository;
	private final ChannelRepository channelRepository;
	private final RedisTemplate<String, String> redisTemplate;

	public static final List<DateTimeFormatter> DATE_FORMATTERS = List.of(
			DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US),
			DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US),
			DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)
	);

	public void allChannelRss(){
		List<Channel> channels = channelRepository.findAll();
		rss(channels);
	}

	public void rss(List<Channel> channels) {
		Set<Feed> feeds = new HashSet<>();
		Map<String, Channel> rssMap = channels.stream().collect(Collectors.toMap(Channel::getRssFeedLink, channel -> channel));

		for (String rssUrl : rssMap.keySet()) {
			log.info("[{}]", rssUrl);

			try {
				// XML 파서 초기화
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();

				// RSS 피드를 URL로부터 읽기
				URL url = new URL(rssUrl);
				Document document = builder.parse(url.openStream());

				// <item> 요소 목록 파싱
				Element channel = (Element) document.getElementsByTagName("channel").item(0);
				NodeList itemList = channel.getElementsByTagName("item");

				for (int i = 0; i < itemList.getLength(); i++) {
					try {

						Element item = (Element) itemList.item(i);
						String itemTitle = item.getElementsByTagName("title").item(0).getTextContent();
						String itemLink = item.getElementsByTagName("link").item(0).getTextContent();
						String itemPubDate = item.getElementsByTagName("pubDate").item(0).getTextContent();
						String itemDescription = item.getElementsByTagName("description").item(0).getTextContent();
						String itemGuid = item.getElementsByTagName("guid").item(0).getTextContent();

						String cleanItemTitle = Jsoup.clean(itemTitle, Whitelist.none());
						String cleanItemDescription = Jsoup.clean(itemDescription, Whitelist.none());

						if(itemLink.contains("tistory")){
							cleanItemDescription = cleanItemDescription.replaceAll("반응형", "").replaceAll("&nbsp;", "").replaceAll("&quot;", "");
						}

						String resizingCleanItemDescription = cleanItemDescription.length() > 200 ? cleanItemDescription.substring(0, 200) : cleanItemDescription;

						// RestTemplate 생성
						RestTemplate restTemplate = new RestTemplate();
						String html = restTemplate.getForObject(itemLink, String.class);

						org.jsoup.nodes.Document htmlDoc = Jsoup.parse(html);

						// 'og:image'를 property로 가지는 meta 요소 찾기
						org.jsoup.nodes.Element ogImageMeta = htmlDoc.select("meta[property=og:image]").first();

						String ogImageUrl = null;
						if (ogImageMeta != null) {
							// 'content' 속성 값 추출
							ogImageUrl = ogImageMeta.attr("content");
						}


						LocalDate pubDate = null;
						for (DateTimeFormatter formatter : DATE_FORMATTERS) {
							try {
								pubDate = LocalDate.parse(itemPubDate, formatter);
								break;  // 일치하는 패턴을 찾으면 반복문 종료
							} catch (DateTimeParseException e) {
								// 해당 패턴으로는 파싱 실패
							}
						}

						feeds.add(
								Feed.builder()
										.title(cleanItemTitle)
										.link(itemLink)
										.thumbnail(ogImageUrl)
										.description(resizingCleanItemDescription)
										.pubDate(LocalDate.parse(pubDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
										.channel(rssMap.get(rssUrl))
										.guid(itemGuid)
										.build()
						);
					} catch (Exception e) {
						e.printStackTrace();

					}
				}

				List<Feed> filteredFeeds = feeds.stream().filter(feed -> isEmptyGuid(feed.getGuid())).collect(Collectors.toList());
				feedRepository.saveAll(filteredFeeds);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public boolean isEmptyGuid(String guid) {
		return !feedRepository.existsByGuid(guid);
	}

	@Transactional(readOnly = true)
	public Page<FeedResponse> readAll(Pageable pageable, ReadType type) {

		if(type == ReadType.ALL){
			return feedRepository.readAll(pageable);
		}

		if(type == ReadType.DOMESTIC){
			return feedRepository.readAllByDOMESTIC(pageable);
		}

		if(type == ReadType.INTERNATIONAL){
			return feedRepository.readAllByINTERNATIONAL(pageable);
		}

		throw new IllegalArgumentException("타입이 올바르지 않습니다.");

	}

	@Transactional
	public void reWrite(FeedRequest.ReWrite request) {
		List<Feed> feeds = feedRepository.findByChannel_Id(request.getChannelId());
		log.info("[Feed Size] {}", feeds.size());
		for (int i = 0; i < feeds.size(); i++) {
			Feed feed = feeds.get(i);
			String cleanDescription = Jsoup.clean(feed.getDescription(), Whitelist.none());
			feed.changeDescription(cleanDescription);
		}
	}

}
