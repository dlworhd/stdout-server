package site.stdout.stdout.rss.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import site.stdout.stdout.rss.dto.ChannelItemCreate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@BatchSize(size = 20)
public class ChannelItem extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String guid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id")
	private Channel channel;

	private String title;

	@Column(columnDefinition = "TEXT")
	private String thumbnail;

	private String description;

	private String link;
	private LocalDate publishedAt;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ChannelItem channelItem)) return false;
		return channel.equals(channelItem.channel) && title.equals(channelItem.title);
	}

	@Override
	public int hashCode() {
		return Objects.hash(channel, title);
	}

	public void changeDescription(String cleanDescription) {
		this.description = cleanDescription;
	}

	public static ChannelItem from(ChannelItemCreate channelItemCreate, Channel channel) {
		List<DateTimeFormatter> DATE_FORMATTERS = List.of(
				DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US),
				DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US),
				DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)
		);
		LocalDate publishedAt = null;

		for (DateTimeFormatter formatter : DATE_FORMATTERS) {
			try {
				publishedAt = LocalDate.parse(channelItemCreate.getPublished(), formatter);
				break;  // 일치하는 패턴을 찾으면 반복문 종료
			} catch (DateTimeParseException e) {
				// 해당 패턴으로는 파싱 실패
			}
		}

		String description = channelItemCreate.getDescription();
		if (description.length() > 100) {
			description = description.substring(0, 100);
		}


		return ChannelItem.builder()
				.channel(channel)
				.title(channelItemCreate.getTitle())
				.guid(channelItemCreate.getGuid())
				.thumbnail(channelItemCreate.getThumbnail())
				.description(description)
				.link(channelItemCreate.getLink())
				.publishedAt(publishedAt)
				.build();

	}
}
