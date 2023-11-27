package site.stdout.stdout.rss.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.stdout.stdout.rss.dto.ChannelCreate;
import site.stdout.stdout.rss.entity.Channel;
import site.stdout.stdout.rss.repository.ChannelRepository;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ChannelService {

	private final ChannelRepository channelRepository;

	@Value("${images-dir}")
	private String imageDirectory;

	@Value("${images-temp-dir}")
	private String imageTempDirectory;

	@Value("${api.image}")
	private String imageApiAddress;

	@Transactional
	public void createChannel(ChannelCreate.Request request) {
		String channelIcon = request.getChannelIcon();

		int index = channelIcon.lastIndexOf("/temp_");
		String fileName = channelIcon.substring(index);
		String newName = fileName.replace("/temp_", "");

		Path source = Path.of(imageTempDirectory, fileName);
		Path target = Path.of(imageDirectory, fileName.replace("/temp_", ""));

		try {
			Files.write(target, Files.readAllBytes(source));
			Files.delete(source);
			Channel newChannel = channelRepository.save(
					Channel.builder()
							.icon(imageApiAddress + newName)
							.name(request.getChannelName())
							.subname(request.getChannelSubname())
							.isInternational(request.isInternational())
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
