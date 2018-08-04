package initial.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import initial.models.Image;
import initial.models.Recipe;
import initial.models.Video;
import initial.repositories.RecipesRepository;

@RestController
@RequestMapping("/recipes")
public class RecipesController {
	
	private RecipesRepository recipeRepository;
	
	@Autowired
    @Qualifier("com.cloudinary.cloud_name")
    String mCloudName;

    @Autowired
    @Qualifier("com.cloudinary.api_key")
    String mApiKey;

    @Autowired
    @Qualifier("com.cloudinary.api_secret")
    String mApiSecret;
	
	public RecipesController(RecipesRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}
	
	@GetMapping("")
	public ResponseEntity getAllRecipes() {
		List<Recipe> recipes = recipeRepository.findAll();
		return new ResponseEntity(recipes, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity getRecipeById(@PathVariable String id) {
		Optional<Recipe> optionalRecipe =  recipeRepository.findById(id);
		Recipe recipe;
		
		if(optionalRecipe.isPresent()) {
			
			recipe = optionalRecipe.get();
			return new ResponseEntity(recipe, HttpStatus.OK);
		
		}
		
		return new ResponseEntity(HttpStatus.NOT_FOUND);
		
	}
	
	@GetMapping("/{id}/images")
	public ResponseEntity getAllRecipeImages(@PathVariable String id) {
		Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
		Recipe recipe;
		
		if(optionalRecipe.isPresent()) {
			
			recipe = optionalRecipe.get();
			return new ResponseEntity<>(recipe.getImages(), HttpStatus.OK);
		
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
		
	@GetMapping("/{id}/videos")
	public ResponseEntity getAllRecipeVideos(@PathVariable String id) {
		Optional<Recipe> optionalRecipe =  recipeRepository.findById(id);
		Recipe recipe;
		
		if(optionalRecipe.isPresent()) {
			
			recipe = optionalRecipe.get();
			return new ResponseEntity<>(recipe.getVideos(), HttpStatus.OK);
		
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("")
	public ResponseEntity addRecipe(@RequestBody Recipe recipe) {
		if(recipe.getName() == null ||
			recipe.getDescription() == null ||
			recipe.getIngredients() == null ||
			recipe.getPreparation() == null ||
			recipe.getImages()      == null ||
			recipe.getVideos()      == null) {
			
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		recipeRepository.save(recipe);
		return new ResponseEntity<>(recipe, HttpStatus.OK);
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity updateRecipe(@PathVariable String id, @RequestBody Recipe recipe) {
		Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
		Recipe recipeUpdate;
		
		
		if(optionalRecipe.isPresent()) {
			recipeUpdate = optionalRecipe.get();
			recipeUpdate.setName(recipe.getName());
			recipeUpdate.setDescription(recipe.getDescription());
			recipeUpdate.setIngredients(recipe.getIngredients());
			recipeUpdate.setPreparation(recipe.getPreparation());
			recipeUpdate.setImages(recipe.getImages());
			recipeUpdate.setVideos(recipe.getVideos());
			recipeRepository.save(recipeUpdate);
			return new ResponseEntity<>(recipeUpdate, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity deleteRecipe(@PathVariable String id) {
		Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
		
		Recipe recipe;
		
		if(optionalRecipe.isPresent()) {
			recipe = optionalRecipe.get();
			recipeRepository.delete(recipe);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/{id}/images/file")
	public ResponseEntity addImageFile(@PathVariable String id, @RequestParam("file") MultipartFile link) {
		Cloudinary c=new Cloudinary("cloudinary://"+mApiKey+":"+mApiSecret+"@"+mCloudName);
		
		Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
		
		Recipe recipe;
		
		if(optionalRecipe.isPresent()) {
			recipe = optionalRecipe.get();
			ArrayList<Image> images = recipe.getImages();
			try
	        {
	            File f=Files.createTempFile("temp", link.getOriginalFilename()).toFile();
	            link.transferTo(f);
	
	            Map response=c.uploader().upload(f, ObjectUtils.emptyMap());
	            JSONObject json=new JSONObject(response);
	            String url=json.getString("url");
	            images.add(new Image(url));
	            recipe.setImages(images);
	            recipeRepository.save(recipe);
	            return new ResponseEntity<>(recipe, HttpStatus.OK);
	        }
	        catch(Exception e)
	        {
	            return null;
	        }
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	@PutMapping("/{id}/images/link")
	public ResponseEntity addImageLink(@PathVariable String id, @RequestBody Image image) {
		Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
		
		Recipe recipe;
		recipe = optionalRecipe.get();
		ArrayList<Image> images = recipe.getImages();
		if(optionalRecipe.isPresent()) {
			
			images.add(image);
            recipe.setImages(images);
            recipeRepository.save(recipe);
            return new ResponseEntity<>(recipe, HttpStatus.OK);
			
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
	}
	
	@PutMapping("/{id}/videos/link")
	public ResponseEntity addVideoLink(@PathVariable String id, @RequestBody Video video) {
		Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
		
		Recipe recipe;
		recipe = optionalRecipe.get();
		ArrayList<Video> videos = recipe.getVideos();
		if(optionalRecipe.isPresent()) {
			
			videos.add(video);
            recipe.setVideos(videos);
            recipeRepository.save(recipe);
            return new ResponseEntity<>(recipe, HttpStatus.OK);
			
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
	}
	
	
	
	
	

}
