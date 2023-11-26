package site.stdout.stdout;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.junit.jupiter.api.Test;

public class ChannelItemTest {

	@Test
	void whitelist(){
		String str = "반응형 &nbsp;&nbsp; 안녕";

		String newStr = str.replaceAll("반응형", "").replaceAll("&nbsp;", "");

		System.out.println(newStr);

	}
}
