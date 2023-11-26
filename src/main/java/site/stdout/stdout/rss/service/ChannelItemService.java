package site.stdout.stdout.rss.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.stdout.stdout.rss.dto.ChannelItemCreate;
import site.stdout.stdout.rss.entity.Channel;
import site.stdout.stdout.rss.entity.ChannelItem;
import site.stdout.stdout.rss.repository.ChannelRepository;
import site.stdout.stdout.rss.repository.ChannelItemRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelItemService {

	private final ChannelItemRepository channelItemRepository;
	private final ChannelRepository channelRepository;
	private final RedisTemplate<String, String> redisTemplate;



	public static final List<DateTimeFormatter> DATE_FORMATTERS = List.of(
			DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US),
			DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US),
			DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)
	);

	@Scheduled(cron = "* * 1, 13 * * *")
	@Transactional
	public void saveItems(){
		ObjectMapper objectMapper = new ObjectMapper();

		List<Channel> channels = channelRepository.findAll();

		ValueOperations<String, String> ops = redisTemplate.opsForValue();

		for (Channel channel : channels) {
			String value = ops.get(channel.getName() + "_ITEM");

			try {
				List<ChannelItemCreate> channelItemCreates = objectMapper.readValue(value, new TypeReference<List<ChannelItemCreate>>() {});
				List<ChannelItemCreate> distinctedChannelItemsCreates = channelItemCreates.stream().distinct().collect(Collectors.toList());

				distinctedChannelItemsCreates.stream().forEach(x -> {
					log.info("{}",x.getGuid());
				});


				List<ChannelItem> channelItems = distinctedChannelItemsCreates.stream()
						.map(item -> ChannelItem.from(item, channel))
						.collect(Collectors.toList());
				channelItemRepository.saveAllAndFlush(channelItems);

			} catch (Exception e) {
				log.warn(e.getMessage());
			}
//			ops.set(channel.getName() + "_ITEM", null);
		}

	}

}
