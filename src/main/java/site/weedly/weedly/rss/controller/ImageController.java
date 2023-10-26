package site.weedly.weedly.rss.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

	@Value("${dir.images}")
	private String IMAGE_DIRECTORY;

	@GetMapping("/{file}")
	public ResponseEntity<byte[]> getImage(@PathVariable String file){

		Path path = Path.of(IMAGE_DIRECTORY, file);

		try {
			FileInputStream fis = new FileInputStream(path.toFile());
			return ResponseEntity.ok(fis.readAllBytes());
		}catch (Exception ex){
			ex.printStackTrace();
			return ResponseEntity.ok().build();
		}
	}
}
