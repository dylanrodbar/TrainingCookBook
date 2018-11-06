package initial.models;

import org.springframework.data.annotation.Id;

public class Video {
	
	
	private String link;
	
	public Video() {}
	
	public Video(String link) {
		this.link = link;
	}
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}


}
