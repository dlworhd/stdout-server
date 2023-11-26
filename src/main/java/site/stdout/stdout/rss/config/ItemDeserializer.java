package site.stdout.stdout.rss.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import site.stdout.stdout.rss.dto.ChannelItemCreate;

import java.io.IOException;

public class ItemDeserializer extends StdDeserializer<ChannelItemCreate> {

	public ItemDeserializer() {
		this(null);
	}

	public ItemDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public ChannelItemCreate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {


		JsonNode node = jp.getCodec().readTree(jp);

		String guid = node.get("Guid").asText();
		String title = node.get("Title").asText();
		String thumbnail = node.get("Thumbnail").asText();
		String description = node.get("Description").asText();
		String link = node.get("Link").asText();
		String published = node.get("Published").asText();


		return ChannelItemCreate.builder()
				.guid(guid)
				.title(title)
				.thumbnail(thumbnail)
				.description(description)
				.link(link)
				.published(published)
				.build();

	}
}
