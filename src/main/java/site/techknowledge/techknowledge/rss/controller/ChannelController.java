package site.techknowledge.techknowledge.rss.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.techknowledge.techknowledge.rss.dto.ChannelRequest;
import site.techknowledge.techknowledge.rss.service.ChannelService;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {

	private final ChannelService channelService;

	@PostMapping
	public ResponseEntity<Void> create(@RequestBody ChannelRequest.Create request){
		channelService.createChannel(request);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{channelId}")
	public ResponseEntity<Void> create(@PathVariable Long channelId){
		channelService.deleteChannel(channelId);
		return ResponseEntity.ok().build();
	}

}
