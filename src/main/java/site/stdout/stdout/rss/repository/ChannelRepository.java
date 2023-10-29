package site.stdout.stdout.rss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.stdout.stdout.rss.entity.Channel;

import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

	boolean existsByChannelLink(String channelLink);
	List<Channel> findByRssFeedLinkIn(List<String> rssFeedLinks);

}
