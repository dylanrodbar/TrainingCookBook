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

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import initial.controllers.AuthenticationController;
import initial.controllers.RecipesController;
import initial.controllers.UsersController;
import initial.models.Recipe;
import initial.models.Users;

@RunWith(SpringRunner.class)
@WebMvcTest(RecipesController.class)
public class RecipesControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private RecipesController recipesController;
	
	@Test
	public void testShouldReturnAllRecipes() throws Exception {
		
	    Recipe recipe = new Recipe();
	    recipe.setName("new recipe test");
	    recipe.setDescription("new description test");
	    recipe.setIngredients("new ingredients test");
	    recipe.setPreparation("new preparation test");

	    List<Recipe> allRecipes = singletonList(recipe);

	    given(recipesController.getAllRecipes()).willReturn(allRecipes);

	    mvc.perform(get(RECIPES)
	            .with(user("dylan1234").password("dylan12345"))
	            .contentType(APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$", hasSize(1)))
	            .andExpect(jsonPath("$[0].name", is(recipe.getName())))
	            .andExpect(jsonPath("$[0].description", is(recipe.getDescription())))
	            .andExpect(jsonPath("$[0].ingredients", is(recipe.getIngredients())))
	            .andExpect(jsonPath("$[0].preparation", is(recipe.getPreparation())));	
	            	
	}
	
	
	
	@Test
	public void shouldReturn200WhenCreateANewRecipe() throws Exception {
		
		Recipe recipe = new Recipe("new", "new", "new", "new");
		
		mvc.perform(post("/recipes")
				.with(user("dylan1234").password("dylan12345"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf())
				.param("name", recipe.getName())
	            .param("description", recipe.getDescription())
	            .param("ingredients",recipe.getIngredients())
	            .param("preparation", recipe.getPreparation())) 
                .andExpect(status().isOk());
	}
	
	

}
