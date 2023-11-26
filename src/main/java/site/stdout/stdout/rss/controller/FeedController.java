package site.stdout.stdout.rss.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.stdout.stdout.rss.service.ChannelItemService;
import site.stdout.stdout.rss.service.ScheduleService;

@Slf4j
@RestController
@RequestMapping("/v1/feeds")
@RequiredArgsConstructor
public class FeedController {

	private final ChannelItemService channelItemService;
	private final ScheduleService scheduleService;

	@PostMapping("/schedule")
	public ResponseEntity<Void> schedule(){
		channelItemService.saveItems();
		return ResponseEntity.ok().build();
	}

}
