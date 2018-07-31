package initial.integration.controllers;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import initial.models.Image;
import initial.models.Recipe;
import initial.models.Users;
import initial.models.Video;
import initial.repositories.RecipesRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipesControllerIT {
	
	@Autowired
	private RecipesRepository recipesRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
    private TestRestTemplate restTemplate;
	
	@Before
    public void setup() throws Exception {
        mongoTemplate.dropCollection(Recipe.class);
    }
    @After
    public void tearDown() throws Exception {
        mongoTemplate.dropCollection(Recipe.class);
    }
    
    @Test
    public void testShouldReturn200WhenGettingAllRecipes() throws Exception {
    	
    	ArrayList<Image> images = new ArrayList<>();
    	ArrayList<Video> videos = new ArrayList<>();
    	images.add(new Image("linktestimage1"));
    	images.add(new Image("linktestimage2"));
    	videos.add(new Video("linktestvideo1"));
    	Recipe recipe = new Recipe("nametest", "descriptiontest", "ingredientstest", "preparationtest", images, videos);
    	
    	recipesRepository.save(recipe);
    	
    	
    	
    	ResponseEntity<? extends ArrayList<Recipe>> responseEntity = restTemplate.getForEntity("/recipes", (Class<? extends ArrayList<Recipe>>)ArrayList.class);
    	
    	System.out.println("asasdasdas");
    	System.out.println(responseEntity.getBody().toString());
    	
    	assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    	
    	
    }
    
    @Test
    public void testShouldReturn200WhenGettingARecipeById() throws Exception {
    	
    	ArrayList<Image> images = new ArrayList<>();
    	ArrayList<Video> videos = new ArrayList<>();
    	images.add(new Image("linktestimage10"));
    	images.add(new Image("linktestimage11"));
    	videos.add(new Video("linktestvideo12"));
    	Recipe recipe = new Recipe("nametest2", "descriptiontest2", "ingredientstest2", "preparationtest2", images, videos);
    	
    	String id = recipesRepository.save(recipe)._id;
    	
    	
        
    	ResponseEntity responseEntity = restTemplate.getForEntity("/recipes/"+id, String.class);
    	
    	String idExpected = "{\"_id\":\""+id+"\"";
    	String expectedResponse = idExpected+",\"name\":\"nametest2\",\"description\":\"descriptiontest2\""
    			+ ",\"ingredients\":\"ingredientstest2\",\"preparation\":\"preparationtest2\",\"images\":[{\"link\":\"linktestimage10\"}"
    			+ ",{\"link\":\"linktestimage11\"}],\"videos\":[{\"link\":\"linktestvideo12\"}]}"; 
    	
    	
    
    	assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    	
    }
    
    @Test
    public void testShouldReturnTheExpectedRecipeWhenGettingItById() throws Exception {
    	ArrayList<Image> images = new ArrayList<>();
    	ArrayList<Video> videos = new ArrayList<>();
    	images.add(new Image("linktestimage10"));
    	images.add(new Image("linktestimage11"));
    	videos.add(new Video("linktestvideo12"));
    	Recipe recipe = new Recipe("nametest2", "descriptiontest2", "ingredientstest2", "preparationtest2", images, videos);
    	
    	String id = recipesRepository.save(recipe)._id;
    	
    	
        
    	ResponseEntity responseEntity = restTemplate.getForEntity("/recipes/"+id, String.class);
    	
    	String idExpected = "{\"_id\":\""+id+"\"";
    	String expectedResponse = idExpected+",\"name\":\"nametest2\",\"description\":\"descriptiontest2\""
    			+ ",\"ingredients\":\"ingredientstest2\",\"preparation\":\"preparationtest2\",\"images\":[{\"link\":\"linktestimage10\"}"
    			+ ",{\"link\":\"linktestimage11\"}],\"videos\":[{\"link\":\"linktestvideo12\"}]}"; 
    	
    	
   
    	assertEquals(expectedResponse, responseEntity.getBody().toString());
    }

}
