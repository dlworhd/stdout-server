package site.stdout.stdout.rss.dto;

import lombok.Getter;

public class ChannelRequest {

	@Getter
	public static class Create{
		private String rssUrl;
		private String channelNameKR;
		private String channelNameEN;
	}

}
