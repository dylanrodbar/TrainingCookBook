package initial.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import initial.models.Recipe;
import initial.repositories.RecipesRepository;

@RestController
@RequestMapping("/recipes")
public class RecipesController {
	
	private RecipesRepository recipeRepository;
	
	public RecipesController(RecipesRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}
	
	@GetMapping("")
	public List<Recipe> getAllRecipes() {
		return recipeRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public Recipe getRecipeById(@PathVariable String id) {
		Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
		
		Recipe recipe = optionalRecipe.get();
		return recipe;
		
	}
	
	@PostMapping("")
	public Recipe addRecipe(@RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("ingredients") String ingredients, @RequestParam("preparation") String preparation) {
		
		Recipe recipe = new Recipe(name, description, ingredients, preparation);
		recipeRepository.save(recipe);
		
        return recipe;
	
	}
	
	@PutMapping("/{id}")
	public Recipe updateRecipe(@PathVariable String id, @RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("ingredients") String ingredients, @RequestParam("preparation") String preparation) {
		Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
		Recipe recipe = optionalRecipe.get();
		
		if(optionalRecipe.isPresent()) {
			recipe.setName(name);
			recipe.setDescription(description);
			recipe.setIngredients(ingredients);
			recipe.setPreparation(preparation);
		}
		
		return recipe;
	}
	
	@DeleteMapping("/{id}")
	public Recipe deleteRecipe(@PathVariable String id) {
		Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
		
		Recipe recipe = optionalRecipe.get();
		
		if(optionalRecipe.isPresent()) {
			recipeRepository.delete(recipe);
		}
		
		return recipe;
		
		
	}

}
