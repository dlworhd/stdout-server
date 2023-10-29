package site.stdout.stdout.rss.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.stdout.stdout.rss.dto.FeedResponse;
import site.stdout.stdout.rss.entity.Feed;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {

	boolean existsByGuid(String guid);


	@Cacheable(value="feeds", key = "'all' + #pageable.pageNumber + '-' + #pageable.pageSize", cacheManager = "redisCacheManager")
	@Query("" +
			"SELECT new site.stdout.stdout.rss.dto.FeedResponse(f.id, c.channelName, c.channelLink, c.channelIcon, f.title, f.thumbnail, f.description, f.link, f.pubDate) FROM Feed f " +
			"LEFT JOIN f.channel c " +
			"ORDER BY f.pubDate DESC"
	)
	Page<FeedResponse> readAll(Pageable pageable);

	@Cacheable(value="feeds", key = "'International' + '-' + #pageable.pageNumber + '-' + #pageable.pageSize", cacheManager = "redisCacheManager")
	@Query("" +
			"SELECT new site.stdout.stdout.rss.dto.FeedResponse(f.id, c.channelName, c.channelLink, c.channelIcon, f.title, f.thumbnail, f.description, f.link, f.pubDate) FROM Feed f " +
			"LEFT JOIN f.channel c " +
			"WHERE c.isNational = TRUE " +
			"ORDER BY f.pubDate DESC"
	)
	Page<FeedResponse> readAllByINTERNATIONAL(Pageable pageable);

	@Cacheable(value="feeds", key = "'Domestic' + '-' + #pageable.pageNumber + '-' + #pageable.pageSize", cacheManager = "redisCacheManager")
	@Query("" +
			"SELECT new site.stdout.stdout.rss.dto.FeedResponse(f.id, c.channelName, c.channelLink, c.channelIcon, f.title, f.thumbnail, f.description, f.link, f.pubDate) FROM Feed f " +
			"LEFT JOIN f.channel c " +
			"WHERE c.isNational = FALSE " +
			"ORDER BY f.pubDate DESC"
	)
	Page<FeedResponse> readAllByDOMESTIC(Pageable pageable);

	@Query("SELECT f FROM Feed f LEFT JOIN FETCH f.channel c WHERE c.id = :channelId")
	List<Feed> findByChannel_Id(@Param("channelId") Long channelId);
}
