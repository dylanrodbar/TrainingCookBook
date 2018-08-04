package initial.controllers;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import initial.models.Video;

@RestController
@RequestMapping("/videos")
public class VideoController {

	@GetMapping
	public StreamingResponseBody stream(@RequestParam("link") String link) throws MalformedURLException, IOException {
		
		final InputStream videoFileStream = new URL(link).openStream();
		return (os) -> {
			readAndWrite(videoFileStream, os);
		};
		
	}
	
	private void readAndWrite(final InputStream is, OutputStream os)
			throws IOException {
		byte[] data = new byte[2048];
		int read = 0;
		while ((read = is.read(data)) > 0) {
			os.write(data, 0, read);
		}
		os.flush();
	}
}
