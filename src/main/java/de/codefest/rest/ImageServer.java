package de.codefest.rest;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.io.ByteStreams;

@RestController
public class ImageServer {

	@RequestMapping(path = "img", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public HttpEntity<?> img(@RequestParam String path) {

		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(path + ".jpg");

		byte[] byteArray;
		try {
			byteArray = ByteStreams.toByteArray(resourceAsStream);
		} catch (IOException e) {
			// passiert eh nie
			return ResponseEntity.ok().build();
		}

		return ResponseEntity.ok(byteArray);
	}

}
