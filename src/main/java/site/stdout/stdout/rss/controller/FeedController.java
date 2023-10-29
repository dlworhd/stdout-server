package site.stdout.stdout.rss.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.stdout.stdout.rss.dto.FeedRequest;
import site.stdout.stdout.rss.dto.FeedResponse;
import site.stdout.stdout.rss.service.FeedService;
import site.stdout.stdout.rss.service.ScheduleService;
import site.stdout.stdout.rss.type.ReadType;

@Slf4j
@RestController
@RequestMapping("/v1/feeds")
@RequiredArgsConstructor
public class FeedController {

	private final FeedService feedService;
	private final ScheduleService scheduleService;

	@PostMapping("/schedule")
	public ResponseEntity<Void> schedule(){
		feedService.allChannelRss();
		return ResponseEntity.ok().build();
	}

	@GetMapping
	public ResponseEntity<Page<FeedResponse>> readAll(Pageable pageable, @RequestParam ReadType type, HttpServletRequest request){
		return ResponseEntity.ok(feedService.readAll(pageable, type));
	}

	@GetMapping("/cache-evict")
	public ResponseEntity<Void> cacheEvict(){
		scheduleService.cacheEvict();
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/re-write")
	public ResponseEntity<Void> reWrite(@RequestBody FeedRequest.ReWrite request){
		feedService.reWrite(request);
		return ResponseEntity.ok().build();
	}
}
