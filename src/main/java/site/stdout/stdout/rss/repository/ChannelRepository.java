package site.stdout.stdout.rss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.stdout.stdout.rss.entity.Channel;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

}
