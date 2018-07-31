package initial.models;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Recipe {
	
	@Id
	public String _id;
	
	
	private String name;
	private String description;
	private String ingredients;
	private String preparation;
	private ArrayList<Image> images;
	private ArrayList<Video> videos;
	
	
	public Recipe() {}
	
	public Recipe(String name, String description, String ingredients, String preparation, ArrayList<Image> images, ArrayList<Video> videos) {
		this.name = name;
		this.description = description;
		this.ingredients = ingredients;
		this.preparation = preparation;
		this.images = images;
		this.videos = videos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}
	
	public String getPreparation() {
		return preparation;
	}

	public void setPreparation(String preparation) {
		this.preparation = preparation;
	}

	public ArrayList<Image> getImages() {
		return images;
	}

	public void setImages(ArrayList<Image> images) {
		this.images = images;
	}
	
	public ArrayList<Video> getVideos() {
		return videos;
	}

	public void setVideos(ArrayList<Video> videos) {
		this.videos = videos;
	}
	
	
	
	
	

}
