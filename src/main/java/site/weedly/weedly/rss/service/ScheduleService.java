package site.weedly.weedly.rss.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import site.weedly.weedly.rss.repository.ChannelRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final RedisTemplate<String, String> redisTemplate;
	private final FeedService feedService;
	private final ChannelRepository channelRepository;

	//24시간
	@Scheduled(initialDelay = 86_400_000, fixedDelay = 86_400_000)
	public void handleRss(){
		feedService.allChannelRss();
		cacheEvict();
	}

	//24시간
//	@Scheduled(initialDelay = 86_400_000, fixedRate = 86_400_000)
	@CacheEvict(value = "feeds", allEntries = true)
	public void cacheEvict(){
		log.info("[{}] Cache Evict", LocalDateTime.now());
	}
}
