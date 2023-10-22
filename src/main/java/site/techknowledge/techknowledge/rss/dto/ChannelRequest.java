package site.techknowledge.techknowledge.rss.dto;

import lombok.Builder;
import lombok.Getter;

public class ChannelRequest {

	@Getter
	public static class Create{
		private String rssUrl;
		private String channelNameKR;
		private String channelNameEN;
	}

}
