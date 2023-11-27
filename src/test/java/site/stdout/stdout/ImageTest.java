package site.stdout.stdout;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageTest {

	@DisplayName(value = "이미지 리사이징 테스트")
	@Test
	public void test() throws IOException {
		Path path = Path.of("/Users/j/j/stdout/stdout-server/images", "Rectangle635.PNG");
		byte[] bytes = Files.readAllBytes(path);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

		BufferedImage inputImage = ImageIO.read(bais);
		int height = inputImage.getHeight();
		int width = inputImage.getWidth();

		int min = Math.min(height, width);

		BufferedImage outputImage = new BufferedImage(min, min, inputImage.getType());

		Graphics2D graphics2D = outputImage.createGraphics();
		graphics2D.drawImage(inputImage, 0, 0, min, min, null);
		graphics2D.dispose();

		ImageIO.write(outputImage, "png", new File("src/main/resources/good.png"));
	}

//
//	@DisplayName(value = "이미지 리사이징 테스트")
//	@Test
//	public void test() throws IOException {
//
//		File file = new File("/Users/j/j/stdout/stdout-server/images/Rectangle635.png");
//		BufferedImage image = ImageIO.read(file);
//
//
//		long maxFileSizeInBytes = 50 * 1024;
//		BufferedImage bufferedImage = resizeImage(image, maxFileSizeInBytes);
//
//
//		ImageIO.write(bufferedImage, "", new File("/Users/j/j/stdout/stdout-server/images/good.png"));
//	}
//
//	private static BufferedImage resizeImage(BufferedImage originalImage, long maxFileSizeInBytes) {
//		// 이미지 압축을 위한 설정
//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//		float quality = 0.7f; // 이미지 품질 (0.0f ~ 1.0f 사이의 값, 1에 가까울수록 품질이 높음)
//
//		// 원본 이미지를 지정된 용량 이하로 압축
//		do {
//			try {
//				// 이미지를 OutputStream에 쓰기
//				ImageIO.write(originalImage, "png", outputStream);
//
//				// 현재 압축된 이미지 크기 확인
//				long currentFileSize = outputStream.size();
//
//				// 압축 품질을 낮춰가면서 용량이 목표치 이하가 될 때까지 반복
//				if (currentFileSize > maxFileSizeInBytes) {
//					outputStream.reset();
//					quality -= 0.1;
//					ImageIO.write(originalImage, "png", getOutputStream(outputStream, quality));
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		} while (outputStream.size() > maxFileSizeInBytes);
//
//		// 압축된 이미지를 BufferedImage로 읽어오기
//		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
//		try {
//			return ImageIO.read(inputStream);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}
//
//	private static ImageOutputStream getOutputStream(OutputStream outputStream, float quality) {
//		try {
//			// 이미지를 압축하기 위한 ImageWriter 생성
//			ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("png").next();
//			ImageWriteParam writeParam = imageWriter.getDefaultWriteParam();
//
//			// 압축 품질 설정
//			writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//			writeParam.setCompressionQuality(quality);
//
//			// ImageOutputStream 생성
//			return ImageIO.createImageOutputStream(outputStream);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}

}
