package site.techknowledge.techknowledge.rss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.techknowledge.techknowledge.rss.entity.Channel;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

	boolean existsByChannelLink(String channelLink);


}
