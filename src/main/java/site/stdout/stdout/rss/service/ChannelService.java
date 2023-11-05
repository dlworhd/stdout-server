package site.stdout.stdout.rss.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import site.stdout.stdout.rss.dto.ChannelRequest;
import site.stdout.stdout.rss.entity.Channel;
import site.stdout.stdout.rss.repository.ChannelRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {

	private final ChannelRepository channelRepository;
	private final FeedService feedService;

	@Transactional
	public void createChannel(ChannelRequest.Create request) {
		try {
			// XML 파서 초기화
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// RSS 피드를 URL로부터 읽기
			URL url = new URL(request.getRssUrl());
			Document document = builder.parse(url.openStream());

			// <channel> 요소 파싱
			Element channel = (Element) document.getElementsByTagName("channel").item(0);
			String channelLink = channel.getElementsByTagName("link").item(0).getTextContent();
			String channelIcon = "http://www.google.com/s2/favicons?domain=" + url;

			if (channelRepository.existsByChannelLink(channelLink)) {
				throw new IllegalArgumentException("이미 존재하는 채널입니다.");
			}

			Channel newChannel = channelRepository.save(
					Channel.builder()
							.channelIcon(channelIcon)
							.channelName(request.getChannelNameKR())
							.channelLink(channelLink)
							.rssFeedLink(request.getRssUrl())
							.build());


			feedService.rss(List.of(newChannel));
		} catch (Exception e) {
		}

	}

	@Transactional
	public void deleteChannel(Long channelId) {
		if (!channelRepository.existsById(channelId)) {
			throw new IllegalArgumentException("존재하지 않는 채널입니다.");
		}

		channelRepository.deleteById(channelId);
	}
}
