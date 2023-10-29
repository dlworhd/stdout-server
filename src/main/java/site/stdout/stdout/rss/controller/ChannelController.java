package site.stdout.stdout.rss.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.stdout.stdout.rss.dto.ChannelRequest;
import site.stdout.stdout.rss.service.ChannelService;

@RestController
@RequestMapping("/v1/channel")
@RequiredArgsConstructor
public class ChannelController {

	private final ChannelService channelService;

	@PostMapping
	public ResponseEntity<Void> create(@RequestBody ChannelRequest.Create request){
		channelService.createChannel(request);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{channelId}")
	public ResponseEntity<Void> delete(@PathVariable Long channelId){
		channelService.deleteChannel(channelId);
		return ResponseEntity.ok().build();
	}

}
