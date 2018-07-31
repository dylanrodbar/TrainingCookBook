package initial.unit.controllers;

import static initial.constants.TestUrl.RECIPES;
import static initial.constants.TestUrl.RECIPE_ID;
import static java.util.Collections.singletonList;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import static java.util.Collections.singletonList;

import initial.controllers.AuthenticationController;
import initial.controllers.RecipesController;
import initial.controllers.UsersController;
import initial.models.Image;
import initial.models.Recipe;
import initial.models.Video;
import junit.framework.Assert;
import static org.mockito.BDDMockito.given;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static initial.constants.TestUrl.RECIPES;

@RunWith(SpringRunner.class)
@WebMvcTest(RecipesController.class)
public class RecipesControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private RecipesController recipesController;
	

	
	@Test
	public void shouldReturn200WhenGettingAllRecipes() throws Exception {
		mvc.perform(get(RECIPES)
	            .with(user("dylan1234").password("dylan12345"))
	            .contentType(APPLICATION_JSON))
	            .andExpect(status().isOk());
	}
	
	@Test
	public void shouldReturn200WhenGettingAllRecipeImages() throws Exception {
		
		mvc.perform(get("/recipes/1/images")
	            .with(user("dylan1234").password("dylan12345"))
	            .contentType(APPLICATION_JSON))
	            .andExpect(status().isOk());
		
	}
	
	@Test
	public void shouldReturn200WhenGettingARecipeById() throws Exception {
		
		mvc.perform(get("/recipes/1")
	            .with(user("dylan1234").password("dylan12345"))
	            .contentType(APPLICATION_JSON))
	            .andExpect(status().isOk());
		
	}
	
	@Test
	public void shouldReturnImageListResponseEntityWhenGettingImagesForARecipe() throws Exception {
		Recipe recipe = new Recipe();
		recipe.setName("nametest");
		recipe.setDescription("descriptiontest");
		recipe.setIngredients("ingredientstest");
		recipe.setPreparation("preparationtest");
		
		ArrayList<String> images = new ArrayList<>();
		ArrayList<String> videos = new ArrayList<>();
		
		images.add("image test");
		videos.add("video test");
		
		ResponseEntity response = new ResponseEntity(images, HttpStatus.OK);
		
		given(recipesController.getAllRecipeImages(recipe._id)).willReturn(response);
	}
	
	@Test
	public void shouldReturnImageListResponseEntityWhenGettingVideosForARecipe() throws Exception {
		Recipe recipe = new Recipe();
		recipe.setName("nametest");
		recipe.setDescription("descriptiontest");
		recipe.setIngredients("ingredientstest");
		recipe.setPreparation("preparationtest");
		
		ArrayList<Image> images = new ArrayList<>();
		ArrayList<Video> videos = new ArrayList<>();
		
		images.add(new Image("link1"));
		videos.add(new Video("link2"));
		
		ResponseEntity response = new ResponseEntity(videos, HttpStatus.OK);
		
		given(recipesController.getAllRecipeImages(recipe._id)).willReturn(response);
	}
	
	
	@Test
	public void shouldReturnRecipeListResponseEntityWhenGettingAllRecipes() throws Exception {
		
		Recipe recipe = new Recipe();
		recipe.setName("nametest");
		recipe.setDescription("descriptiontest");
		recipe.setIngredients("ingredientstest");
		recipe.setPreparation("preparationtest");
		
		ArrayList<Image> images = new ArrayList<>();
		ArrayList<Video> videos = new ArrayList<>();
		
		images.add(new Image("link1"));
		videos.add(new Video("link2"));
		
		List<Recipe> recipes = singletonList(recipe);
		
		ResponseEntity response = new ResponseEntity(recipes, HttpStatus.OK);
		
		given(recipesController.getAllRecipes()).willReturn(response);
	}
	


}
