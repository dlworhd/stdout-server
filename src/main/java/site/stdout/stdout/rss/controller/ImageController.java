package site.stdout.stdout.rss.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/v1/images")
public class ImageController {

	@Value("${images-dir}")
	private String imageDirectory;

	@Value("${images-temp-dir}")
	private String imageTempDirectory;

	@Value("${api.image}")
	private String imageApiAddress;

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

	@PostMapping
	public ResponseEntity<String> saveImage(@RequestParam MultipartFile file){

		if(file.isEmpty()){
			return null;
		}

		String fileName = "temp_" + UUID.randomUUID() + file.getOriginalFilename().replaceAll(" ", "");

		try {
			file.transferTo(Path.of(imageTempDirectory, fileName));

			return ResponseEntity.ok(imageApiAddress + fileName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
