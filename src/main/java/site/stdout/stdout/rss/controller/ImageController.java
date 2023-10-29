package site.stdout.stdout.rss.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.nio.file.Path;

@RestController
@RequestMapping("/v1/images")
public class ImageController {

	@Value("${images-dir}")
	private String imageDirectory;

	@GetMapping("/{file}")
	public ResponseEntity<byte[]> getImage(@PathVariable String file){

		Path path = Path.of(imageDirectory, file);
		try {
			FileInputStream fis = new FileInputStream(path.toFile());
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(fis.readAllBytes());
		}catch (Exception ex){
			ex.printStackTrace();
			return ResponseEntity.ok().build();
		}
	}
}
