package site.stdout.stdout.rss.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.stdout.stdout.rss.dto.ChannelItemResponse;
import site.stdout.stdout.rss.entity.ChannelItem;

import java.util.List;

public interface ChannelItemRepository extends JpaRepository<ChannelItem, Long> {

	boolean existsByGuid(String guid);


	@Cacheable(value="items", key = "'all' + #pageable.pageNumber + '-' + #pageable.pageSize", cacheManager = "redisCacheManager")
	@Query("" +
			"SELECT new site.stdout.stdout.rss.dto.ChannelItemResponse(i.id, c.subname, c.icon, i.title, i.thumbnail, i.description, i.link, i.publishedAt) FROM ChannelItem i " +
			"LEFT JOIN i.channel c " +
			"ORDER BY i.publishedAt DESC"
	)
	Page<ChannelItemResponse> readAll(Pageable pageable);

	@Cacheable(value="items", key = "'International' + '-' + #pageable.pageNumber + '-' + #pageable.pageSize", cacheManager = "redisCacheManager")
	@Query("" +
			"SELECT new site.stdout.stdout.rss.dto.ChannelItemResponse(i.id, c.subname, c.icon, i.title, i.thumbnail, i.description, i.link, i.publishedAt) FROM ChannelItem i " +
			"LEFT JOIN i.channel c " +
			"WHERE c.isInternational = TRUE " +
			"ORDER BY i.publishedAt DESC"
	)
	Page<ChannelItemResponse> readAllByINTERNATIONAL(Pageable pageable);

	@Cacheable(value="items", key = "'Domestic' + '-' + #pageable.pageNumber + '-' + #pageable.pageSize", cacheManager = "redisCacheManager")
	@Query("" +
			"SELECT new site.stdout.stdout.rss.dto.ChannelItemResponse(i.id, c.subname, c.icon, i.title, i.thumbnail, i.description, i.link, i.publishedAt) FROM ChannelItem i " +
			"LEFT JOIN i.channel c " +
			"WHERE c.isInternational = FALSE " +
			"ORDER BY i.publishedAt DESC"
	)
	Page<ChannelItemResponse> readAllByDOMESTIC(Pageable pageable);

	@Query("SELECT i FROM ChannelItem i LEFT JOIN FETCH i.channel c WHERE c.id = :channelId")
	List<ChannelItem> findByChannel_Id(Long channelId);
}
