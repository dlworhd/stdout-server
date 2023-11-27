package site.stdout.stdout.rss.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.stdout.stdout.rss.dto.ChannelItemResponse;
import site.stdout.stdout.rss.service.ChannelItemService;
import site.stdout.stdout.rss.service.ScheduleService;
import site.stdout.stdout.rss.type.ReadType;

@Slf4j
@RestController
@RequestMapping("/v1/items")
@RequiredArgsConstructor
public class ChannlItemsController {

	private final ChannelItemService channelItemService;
	private final ScheduleService scheduleService;

	@PostMapping("/schedule")
	public ResponseEntity<Void> schedule(){
		channelItemService.saveItems();
		return ResponseEntity.ok().build();
	}

	@GetMapping
	public ResponseEntity<Page<ChannelItemResponse>> readItems(
			@RequestParam ReadType type,
			Pageable pageable
			){
		return ResponseEntity.ok(channelItemService.readItems(type, pageable));
	}

	@GetMapping("/cache-evict")
	public ResponseEntity<Void> cacheEvict(){
		scheduleService.cacheEvict();
		return ResponseEntity.ok().build();
	}
}
