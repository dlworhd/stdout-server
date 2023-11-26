package site.stdout.stdout.rss.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.stdout.stdout.rss.dto.ChannelCreate;
import site.stdout.stdout.rss.entity.Channel;
import site.stdout.stdout.rss.repository.ChannelRepository;

@Service
@RequiredArgsConstructor
public class ChannelService {

	private final ChannelRepository channelRepository;

	@Transactional
	public void createChannel(ChannelCreate.Request request) {
		try {
			Channel newChannel = channelRepository.save(
					Channel.builder()
							.name(request.getChannelName())
							.isNational(request.isNational())
							.build()
			);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Transactional
	public void deleteChannel(Long channelId) {
		if (!channelRepository.existsById(channelId)) {
			throw new IllegalArgumentException("존재하지 않는 채널입니다.");
		}

		channelRepository.deleteById(channelId);
	}

}
