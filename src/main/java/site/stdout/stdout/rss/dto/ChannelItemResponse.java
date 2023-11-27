package site.stdout.stdout.rss.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Builder
public class ChannelItemResponse implements Serializable {

		private Long id;
		private String channelSubname;
		private String channelIcon;
		private String channelItemTitle;
		private String channelItemThumbnail;
		private String channelItemDescription;
		private String channelItemLink;
		private LocalDate pubDate;

}
