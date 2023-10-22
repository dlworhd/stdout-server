package site.techknowledge.techknowledge.rss.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import site.techknowledge.techknowledge.rss.dto.ChannelRequest;
import site.techknowledge.techknowledge.rss.entity.Channel;
import site.techknowledge.techknowledge.rss.repository.ChannelRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ChannelService {

	private final ChannelRepository channelRepository;
//	private final RestTemplate restTemplate;


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

			System.out.println("Channel Link: " + channelLink);
			System.out.println("Channel Icon: " + channelIcon);

			channelRepository.save(
					Channel.builder()
							.channelIcon(channelIcon)
							.channelNameKR(request.getChannelNameKR())
							.channelNameEN(request.getChannelNameEN())
							.channelLink(channelLink)
							.rssFeedLink(request.getRssUrl())
							.build());

		} catch (Exception e) {
			e.printStackTrace();
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
