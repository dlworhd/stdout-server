package site.weedly.weedly.rss.dto;

import lombok.Getter;

public class FeedRequest {

	@Getter
	public static class ReWrite{
		private Long channelId;
	}
}
