package site.weedly.weedly.rss.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Builder
public class FeedResponse implements Serializable {

		private Long id;
		private String channelName;
		private String channelLink;
		private String channelIcon;
		private String title;
		private String thumbnail;
		private String description;
		private String link;
		private LocalDate pubDate;

}
