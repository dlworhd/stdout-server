package site.stdout.stdout.rss.dto;

import lombok.Getter;

public class ChannelCreate {

	@Getter
	public static class Request {
		private String channelIcon;
		private String channelName;
		private String channelSubname;
		private boolean isInternational;
	}

}
