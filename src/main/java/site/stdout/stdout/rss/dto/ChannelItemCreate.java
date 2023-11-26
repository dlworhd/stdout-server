package site.stdout.stdout.rss.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import site.stdout.stdout.rss.config.ItemDeserializer;

@Getter
@Builder
@EqualsAndHashCode(of = { "guid" })
@JsonDeserialize(using = ItemDeserializer.class)
public class ChannelItemCreate {

	private String guid;
	private String title;
	private String thumbnail;
	private String description;
	private String link;
	private String published;

}
