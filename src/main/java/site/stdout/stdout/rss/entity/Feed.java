package site.stdout.stdout.rss.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@BatchSize(size = 20)
public class Feed extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String guid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id")
	private Channel channel;
	private String title;

	@Column(columnDefinition = "TEXT")
	private String thumbnail;
	private String description;
	private LocalDate pubDate;
	private String link;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Feed feed)) return false;
		return channel.equals(feed.channel) && title.equals(feed.title);
	}

	@Override
	public int hashCode() {
		return Objects.hash(channel, title);
	}

	public void changeDescription(String cleanDescription) {
		this.description = cleanDescription;
	}
}
