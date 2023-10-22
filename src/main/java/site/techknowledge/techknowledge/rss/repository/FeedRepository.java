package site.techknowledge.techknowledge.rss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.techknowledge.techknowledge.rss.entity.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long> {
}
