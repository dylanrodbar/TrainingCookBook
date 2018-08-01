package initial.integration.controllers;


import static initial.constants.SecurityConstants.EXPIRATION_TIME;
import static initial.constants.SecurityConstants.SECRET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import initial.models.Image;
import initial.models.Recipe;
import initial.models.Users;
import initial.models.Video;
import initial.repositories.RecipesRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipesControllerIT {
	
	@Autowired
	private RecipesRepository recipesRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
    private TestRestTemplate restTemplate;
	
	private String tokenAuth;
	
	
	
	
	@Before
    public void setup() throws Exception {
		
		String token = Jwts.builder()
                .setSubject("test")
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
    	
    	this.tokenAuth = token;
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
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Auth " + tokenAuth);
    	
        HttpEntity entity = new HttpEntity(headers);
    	
    	ResponseEntity responseEntity = restTemplate.exchange(
                "/recipes", 
                HttpMethod.GET, 
                entity, 
                String.class);
    	
    	
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
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Auth " + tokenAuth);
    	
        HttpEntity entity = new HttpEntity(headers);
    	
    	ResponseEntity responseEntity = restTemplate.exchange(
                "/recipes/"+id, 
                HttpMethod.GET, 
                entity, 
                String.class);
    	
        
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
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Auth " + tokenAuth);
    	
        HttpEntity entity = new HttpEntity(headers);
    	
    	ResponseEntity responseEntity = restTemplate.exchange(
                "/recipes/"+id, 
                HttpMethod.GET, 
                entity, 
                String.class);
        
    
    	String idExpected = "{\"_id\":\""+id+"\"";
    	String expectedResponse = idExpected+",\"name\":\"nametest2\",\"description\":\"descriptiontest2\""
    			+ ",\"ingredients\":\"ingredientstest2\",\"preparation\":\"preparationtest2\",\"images\":[{\"link\":\"linktestimage10\"}"
    			+ ",{\"link\":\"linktestimage11\"}],\"videos\":[{\"link\":\"linktestvideo12\"}]}"; 
    	
    	
   
    	assertEquals(expectedResponse, responseEntity.getBody().toString());
    }
    
    
    @Test
    public void shouldReturn200WhenAddingANewRecipe() throws Exception {
    	ArrayList<Image> images = new ArrayList<>();
    	ArrayList<Video> videos = new ArrayList<>();
    	images.add(new Image("linktestimage10"));
    	images.add(new Image("linktestimage11"));
    	videos.add(new Video("linktestvideo12"));
    	Recipe recipe = new Recipe("nametest2", "descriptiontest2", "ingredientstest2", "preparationtest2", images, videos);
    	
    	String id = recipesRepository.save(recipe)._id;
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Auth " + tokenAuth);
    	
        HttpEntity entity = new HttpEntity(recipe, headers);
    	
    	ResponseEntity responseEntity = restTemplate.exchange(
                "/recipes", 
                HttpMethod.POST, 
                entity, 
                String.class);
    	
    	assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    
    @Test
    public void shouldReturnRecipeWhenAddingANewRecipe() throws Exception {
    	ArrayList<Image> images = new ArrayList<>();
    	ArrayList<Video> videos = new ArrayList<>();
    	images.add(new Image("linktestimage10"));
    	images.add(new Image("linktestimage11"));
    	videos.add(new Video("linktestvideo12"));
    	Recipe recipe = new Recipe("nametest2", "descriptiontest2", "ingredientstest2", "preparationtest2", images, videos);
    	
    	String id = recipesRepository.save(recipe)._id;
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Auth " + tokenAuth);
    	
        HttpEntity entity = new HttpEntity(recipe, headers);
    	
    	ResponseEntity<Recipe> responseEntity = restTemplate.exchange(
                "/recipes", 
                HttpMethod.POST, 
                entity, 
                Recipe.class);
    	
    	
    	assertEquals(recipe.getName(), responseEntity.getBody().getName());
    	assertEquals(recipe.getDescription(), responseEntity.getBody().getDescription());
    	assertEquals(recipe.getIngredients(), responseEntity.getBody().getIngredients());
    	assertEquals(recipe.getPreparation(), responseEntity.getBody().getPreparation());
    	assertEquals(recipe.getImages().size(), responseEntity.getBody().getImages().size());
    	assertEquals(recipe.getVideos().size(), responseEntity.getBody().getVideos().size());
    }
    
    @Test
    public void shouldReturn422WhenAddingANewBadRecipe() throws Exception {
    	ArrayList<Image> images = new ArrayList<>();
    	ArrayList<Video> videos = new ArrayList<>();
    	images.add(new Image("linktestimage10"));
    	images.add(new Image("linktestimage11"));
    	videos.add(new Video("linktestvideo12"));
    	Recipe recipe = new Recipe(null, "descriptiontest2", "ingredientstest2", "preparationtest2", images, videos);
    	
    	String id = recipesRepository.save(recipe)._id;
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Auth " + tokenAuth);
    	
        HttpEntity entity = new HttpEntity(recipe, headers);
    	
    	ResponseEntity responseEntity = restTemplate.exchange(
                "/recipes", 
                HttpMethod.POST, 
                entity, 
                String.class);
    	
    	assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }
    
    @Test
    public void shouldReturn200WhenUpdatingARecipe() throws Exception {
    	ArrayList<Image> images = new ArrayList<>();
    	ArrayList<Video> videos = new ArrayList<>();
    	images.add(new Image("linktestimage10"));
    	images.add(new Image("linktestimage11"));
    	videos.add(new Video("linktestvideo12"));
    	Recipe recipe = new Recipe("nametest2", "descriptiontest2", "ingredientstest2", "preparationtest2", images, videos);
    	Recipe updateRecipe = new Recipe("nametest3", "descriptiontest3", "ingredientstest3", "preparationtest3", images, videos);
    	
    	String id = recipesRepository.save(recipe)._id;
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Auth " + tokenAuth);
        
        HttpEntity entity = new HttpEntity(updateRecipe, headers);
        
        ResponseEntity responseEntity = restTemplate.exchange(
                "/recipes/"+id, 
                HttpMethod.PUT, 
                entity, 
                String.class);
    	
    	assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        
    }
    
    
    @Test
    public void shouldReturnCorrectDataWhenUpdatingARecipe() throws Exception {
    	ArrayList<Image> images = new ArrayList<>();
    	ArrayList<Video> videos = new ArrayList<>();
    	images.add(new Image("linktestimage10"));
    	images.add(new Image("linktestimage11"));
    	videos.add(new Video("linktestvideo12"));
    	Recipe recipe = new Recipe("nametest2", "descriptiontest2", "ingredientstest2", "preparationtest2", images, videos);
    	Recipe updateRecipe = new Recipe("nametest3", "descriptiontest3", "ingredientstest3", "preparationtest3", images, videos);
    	
    	String id = recipesRepository.save(recipe)._id;
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Auth " + tokenAuth);
        
        HttpEntity entity = new HttpEntity(updateRecipe, headers);
        
        ResponseEntity<Recipe> responseEntity = restTemplate.exchange(
                "/recipes/"+id, 
                HttpMethod.PUT, 
                entity, 
                Recipe.class);
    	
    	assertEquals(responseEntity.getBody().getName(), updateRecipe.getName());
    	assertEquals(responseEntity.getBody().getDescription(), updateRecipe.getDescription());
    	assertEquals(responseEntity.getBody().getIngredients(), updateRecipe.getIngredients());
    	assertEquals(responseEntity.getBody().getPreparation(), updateRecipe.getPreparation());
        
    }
    
    @Test
    public void shouldReturn404WhenUpdatingAWringRecipe() throws Exception {
    	ArrayList<Image> images = new ArrayList<>();
    	ArrayList<Video> videos = new ArrayList<>();
    	images.add(new Image("linktestimage10"));
    	images.add(new Image("linktestimage11"));
    	videos.add(new Video("linktestvideo12"));
    	Recipe recipe = new Recipe("nametest2", "descriptiontest2", "ingredientstest2", "preparationtest2", images, videos);
    	Recipe updateRecipe = new Recipe("nametest3", "descriptiontest3", "ingredientstest3", "preparationtest3", images, videos);
    	
    	String id = recipesRepository.save(recipe)._id;
    	String badId = "1";
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Auth " + tokenAuth);
        
        HttpEntity entity = new HttpEntity(updateRecipe, headers);
        
        ResponseEntity<Recipe> responseEntity = restTemplate.exchange(
                "/recipes/"+badId, 
                HttpMethod.PUT, 
                entity, 
                Recipe.class);
    	
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        
    }
    
    @Test
    public void shouldReturn200WhenDeletingARecipe() throws Exception {
    	ArrayList<Image> images = new ArrayList<>();
    	ArrayList<Video> videos = new ArrayList<>();
    	images.add(new Image("linktestimage10"));
    	images.add(new Image("linktestimage11"));
    	videos.add(new Video("linktestvideo12"));
    	Recipe recipe = new Recipe("nametest2", "descriptiontest2", "ingredientstest2", "preparationtest2", images, videos);
    	Recipe updateRecipe = new Recipe("nametest3", "descriptiontest3", "ingredientstest3", "preparationtest3", images, videos);
    	
    	String id = recipesRepository.save(recipe)._id;
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Auth " + tokenAuth);
        
        HttpEntity entity = new HttpEntity(updateRecipe, headers);
        
        ResponseEntity responseEntity = restTemplate.exchange(
                "/recipes/"+id, 
                HttpMethod.DELETE, 
                entity, 
                String.class);
    	
    	assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    
    @Test
    public void shouldReturn404WhenDeletingAWrongRecipe() throws Exception {
    	ArrayList<Image> images = new ArrayList<>();
    	ArrayList<Video> videos = new ArrayList<>();
    	images.add(new Image("linktestimage10"));
    	images.add(new Image("linktestimage11"));
    	videos.add(new Video("linktestvideo12"));
    	Recipe recipe = new Recipe("nametest2", "descriptiontest2", "ingredientstest2", "preparationtest2", images, videos);
    	Recipe updateRecipe = new Recipe("nametest3", "descriptiontest3", "ingredientstest3", "preparationtest3", images, videos);
    	
    	String id = recipesRepository.save(recipe)._id;
    	String badId = "1";
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Auth " + tokenAuth);
        
        HttpEntity entity = new HttpEntity(updateRecipe, headers);
        
        ResponseEntity responseEntity = restTemplate.exchange(
                "/recipes/"+badId, 
                HttpMethod.DELETE, 
                entity, 
                String.class);
    	
    	assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}
