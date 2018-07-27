package initial.unit.controllers;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import initial.controllers.AuthenticationController;
import initial.models.Users;
import initial.repositories.UsersRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@MockBean
	private AuthenticationController authenticationController;
	
	
	
	@Test
	public void testShouldReturn200WhenSignedUp() throws Exception {
		
		Users user = new Users("user-test", "password-test");
		
		
		mvc.perform(post("/sign-up")
				.with(user("dylan1234").password("dylan12345"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(user))
				.with(csrf()))
                .andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "usertest", password = "pwdtest")
	public void testShouldReturn200WhenLogged() throws Exception {
	    mvc.perform(get("/login"))
	        .andExpect(status().isOk());
	}
	
	
	
	
	

}
