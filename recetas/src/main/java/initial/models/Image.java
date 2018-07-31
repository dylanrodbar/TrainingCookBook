package initial.models;

import org.springframework.data.annotation.Id;

public class Image {
	
	private String link;
	
	public Image(String link) {
		this.link = link;
	}
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}


}
