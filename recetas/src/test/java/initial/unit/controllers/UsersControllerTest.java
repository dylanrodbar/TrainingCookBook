package initial.unit.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import initial.controllers.UsersController;
import initial.models.Users;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static initial.constants.TestUrl.USERS;

@RunWith(SpringRunner.class)
@WebMvcTest(UsersController.class)
public class UsersControllerTest {
	
	
	@Autowired
	private MockMvc mvc;

	@MockBean
	private UsersController userController;

	@Test
	public void getUsers() throws Exception {
		
	    Users user = new Users();
	    user.setUsername("dylanrodbar");

	    List<Users> allUsers = singletonList(user);

	    mvc.perform(get(USERS)
	            .with(user("dylan1234").password("dylan12345"))
	            .contentType(APPLICATION_JSON))
	            .andExpect(status().isOk());
	}
	
	
	
	

}
