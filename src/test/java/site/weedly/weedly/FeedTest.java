package site.weedly.weedly;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.junit.jupiter.api.Test;

public class FeedTest {

	@Test
	void whitelist(){
		String html = "<h2 data-ke-size=\"size26\">들어가기 전에</h2>\n" +
				"<p data-ke-size=\"size16\">안녕하세요 Mobile Application Team iOS 개발자 강수진입니다.</p>\n" +
				"<p data-ke-size=\"size16\">&nbsp;</p>\n" +
				"<p data-ke-size=\"size16\">오늘은&nbsp;</p>\n" +
				"<p data-ke-";
		String none = Jsoup.clean(html, Whitelist.none());

		String basic = Jsoup.clean(html, Whitelist.basic());

		String withImage = Jsoup.clean(html, Whitelist.basicWithImages());

		String relaxed = Jsoup.clean(html, Whitelist.relaxed());

		String simpleText = Jsoup.clean(html, Whitelist.simpleText());

		System.out.println("-----------------------------");
		System.out.println(none);
		System.out.println("-----------------------------");

		System.out.println(basic);
		System.out.println("-----------------------------");

		System.out.println(withImage);
		System.out.println("-----------------------------");

		System.out.println(relaxed);
		System.out.println("-----------------------------");

		System.out.println(simpleText);
	}
}
