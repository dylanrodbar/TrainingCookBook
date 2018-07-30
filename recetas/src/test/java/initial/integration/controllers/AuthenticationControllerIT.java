package initial.integration.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import initial.models.Users;
import initial.repositories.UsersRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerIT {
	
	@Autowired
    private UsersRepository repository;
	
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Before
    public void setup() throws Exception {
        mongoTemplate.dropCollection(Users.class);
    }
    @After
    public void tearDown() throws Exception {
        mongoTemplate.dropCollection(Users.class);
    }
    
    
    
    @Test
    public void testShouldReturn200WhenSignedUp() throws Exception {
    	Users user = new Users("usertest1", "passwordtest1");
    	ResponseEntity<Users> responseEntity = restTemplate.postForEntity("/sign-up", user, Users.class);
    	
    	Users userResponse = responseEntity.getBody();
    	assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    
    @Test
    public void testShouldEvaluateUsernameInsertedOnDatabase() throws Exception {
    	Users user = new Users("usertest2", "passwordtest2");
    	
    	ResponseEntity<Users> responseEntity = restTemplate.postForEntity("/sign-up", user, Users.class);
    	
    	Users userResponse = responseEntity.getBody();
    	assertEquals(user.getUsername(), userResponse.getUsername());
    	
    }
    
    @Test
    public void testShouldReturn200WhenLoggedIn() throws Exception {
    	Users userTest = new Users("usertest3", "passwordtest3");
    	
    	ResponseEntity<Users> responseEntitySignUp = restTemplate.postForEntity("/sign-up", userTest, Users.class);
    	
    	ResponseEntity<Users> responseEntityLogin = restTemplate.postForEntity("/login", new Users(userTest.getUsername(), userTest.getPassword()), Users.class);
    	
    	Users user = responseEntityLogin.getBody();
    	assertEquals(HttpStatus.OK, responseEntityLogin.getStatusCode());
    	
    }
    
    
    @Test
    public void testShouldReturn401WhenBadLoggedIn() throws Exception {
    	Users userTest = new Users("usertest4", "passwordtest4");
    	Users userTestNotStored = new Users("usertest5", "passwordtest5");
    	
    	ResponseEntity<Users> responseEntitySignUp = restTemplate.postForEntity("/sign-up", userTest, Users.class);
    	
    	ResponseEntity<Users> responseEntityLogin = restTemplate.postForEntity("/login", new Users(userTestNotStored.getUsername(), userTestNotStored.getPassword()), Users.class);
    	
    	Users user = responseEntityLogin.getBody();
    	assertEquals(HttpStatus.UNAUTHORIZED, responseEntityLogin.getStatusCode());
    	
    }
	
}
