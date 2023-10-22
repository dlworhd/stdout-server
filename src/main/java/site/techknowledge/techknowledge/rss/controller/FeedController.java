package site.techknowledge.techknowledge.rss.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.techknowledge.techknowledge.rss.service.FeedService;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class FeedController {

	private final FeedService feedService;

	@PostMapping
	public ResponseEntity<String> getRss(@RequestBody String src){
		return ResponseEntity.ok(feedService.rss(src));
	}

}
