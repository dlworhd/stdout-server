package site.weedly.weedly.rss.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate<String, String> redisTemplate(){
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		RedisSerializer stringRedisSerializer = new StringRedisSerializer();

		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setValueSerializer(stringRedisSerializer);

		return redisTemplate;
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory(){
		return new LettuceConnectionFactory("localhost", 6379);
	}
}
