package initial.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import initial.models.Image;
import initial.models.Recipe;
import initial.models.Users;
import initial.repositories.RecipesRepository;

@RestController
@RequestMapping("/recipes")
public class RecipesController {
	
	private RecipesRepository recipeRepository;
	
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
	
	
	
	
	

}
