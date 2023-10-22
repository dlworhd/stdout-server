package site.techknowledge.techknowledge.rss.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.techknowledge.techknowledge.rss.dto.ChannelRequest;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Channel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String channelIcon;
	@Column(name = "channel_name_kr")
	private String channelNameKR;
	@Column(name = "channel_name_en")
	private String channelNameEN;
	private String channelLink;
	private String rssFeedLink;

}
