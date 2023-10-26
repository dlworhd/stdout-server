package site.weedly.weedly.rss.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.weedly.weedly.rss.dto.FeedRequest;
import site.weedly.weedly.rss.dto.FeedResponse;
import site.weedly.weedly.rss.service.FeedService;
import site.weedly.weedly.rss.service.ScheduleService;
import site.weedly.weedly.rss.type.ReadType;

@RestController
@RequestMapping("/api/feeds")
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
	public ResponseEntity<Page<FeedResponse>> readAll(Pageable pageable, @RequestParam ReadType type){
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
