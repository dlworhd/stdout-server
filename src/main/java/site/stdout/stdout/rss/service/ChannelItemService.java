package site.stdout.stdout.rss.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.stdout.stdout.rss.dto.ChannelItemCreate;
import site.stdout.stdout.rss.dto.ChannelItemResponse;
import site.stdout.stdout.rss.entity.Channel;
import site.stdout.stdout.rss.entity.ChannelItem;
import site.stdout.stdout.rss.repository.ChannelRepository;
import site.stdout.stdout.rss.repository.ChannelItemRepository;
import site.stdout.stdout.rss.type.ReadType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static site.stdout.stdout.rss.type.ReadType.*;

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

	@Scheduled(cron = "0 0 13 * * *")
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

				List<ChannelItem> channelItems = distinctedChannelItemsCreates.stream()
						.map(item -> ChannelItem.from(item, channel))
						.collect(Collectors.toList());
				channelItemRepository.saveAll(channelItems);

			} catch (Exception e) {
				log.warn(e.getMessage());
			}
//			ops.set(channel.getName() + "_ITEM", null);
		}

	}

	@Transactional(readOnly = true)
	public Page<ChannelItemResponse> readItems(ReadType type, Pageable pageable) {
		if(type == ALL){
			return channelItemRepository.readAll(pageable);
		}
		if(type == DOMESTIC){
			return channelItemRepository.readAllByDOMESTIC(pageable);
		}

		if(type == INTERNATIONAL){
			return channelItemRepository.readAllByINTERNATIONAL(pageable);
		}

		throw new IllegalArgumentException();
	}

}
