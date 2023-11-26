package site.stdout.stdout.rss.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final ChannelItemService channelItemService;

	//24시간
	@Scheduled(initialDelay = 86_400_000, fixedDelay = 86_400_000)
	public void handleRss(){
//		feedService.allChannelRss();
		cacheEvict();
	}

	//24시간
	@CacheEvict(value = "feeds", allEntries = true, cacheManager = "redisCacheManager")
	public void cacheEvict(){
		log.info("[{}] Cache Evict", LocalDateTime.now());
	}

}
