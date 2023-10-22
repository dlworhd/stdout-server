package site.techknowledge.techknowledge.rss.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import site.techknowledge.techknowledge.rss.repository.FeedRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class FeedService {

	private final FeedRepository feedRepository;

	public String rss(String rssUrl) {

		try {
			// XML 파서 초기화
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			// RSS 피드를 URL로부터 읽기
			JSONObject jsonObject = new JSONObject(rssUrl);
			URL url = new URL(jsonObject.getString("src"));
			Document document = builder.parse(url.openStream());

			// <channel> 요소 파싱
			Element channel = (Element) document.getElementsByTagName("channel").item(0);
			String channelTitle = channel.getElementsByTagName("title").item(0).getTextContent();
			String channelLink = channel.getElementsByTagName("link").item(0).getTextContent();

			System.out.println("Channel Title: " + channelTitle);
			System.out.println("Channel Link: " + channelLink);

			// <item> 요소 목록 파싱
			NodeList itemList = document.getElementsByTagName("item");
			for (int i = 0; i < 10; i++) {
				Element item = (Element) itemList.item(i);
				String itemTitle = item.getElementsByTagName("title").item(0).getTextContent();
				String itemLink = item.getElementsByTagName("link").item(0).getTextContent();
				String itemCreator = item.getElementsByTagName("dc:creator").item(0).getTextContent();
				String itemPubDate = item.getElementsByTagName("pubDate").item(0).getTextContent();
				String itemDescription = item.getElementsByTagName("description").item(0).getTextContent();

				System.out.println("Item Title: " + itemTitle);
				System.out.println("Item Link: " + itemLink);
				System.out.println("Item Creator: " + itemCreator);
				System.out.println("Item PubDate: " + itemPubDate);
				System.out.println("Item Description: " + itemDescription);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public String getRSS(String rssUrl) {
		try {

			// XML 파서 초기화
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			JSONObject jsonObject = new JSONObject(rssUrl);
			URL url = new URL(jsonObject.getString("src"));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String line;
			StringBuilder xml = new StringBuilder();

			while ((line = br.readLine()) != null) {
				xml.append(line);
			}

			br.close();
			connection.disconnect();
			return xml.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
