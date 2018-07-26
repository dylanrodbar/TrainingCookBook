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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static initial.constants.TestUrl.USERS;

@RunWith(SpringRunner.class)
@WebMvcTest(UsersController.class)
public class UsersControllerTest {
	
	
	// se usa para simular datos
	@Autowired
	private MockMvc mvc;

	@MockBean
	private UsersController userController;

	@Test
	public void getUsers() throws Exception {
		
	    Users user = new Users();
	    user.setUsername("dylanrodbar");

	    List<Users> allUsers = singletonList(user);

	    // se asegura de que el metodo getAllUsers() retorne una lista
	    given(userController.getAllUsers()).willReturn(allUsers);

	    // se hace un request get a /users para confirmar que se devuelva un json de tamano 1, ademas de comprobar que el username retornado
	    // sea igual al user creado anteriormente
	    mvc.perform(get(USERS)
	            .with(user("dylan1234").password("dylan12345"))
	            .contentType(APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$", hasSize(1)))
	            .andExpect(jsonPath("$[0].username", is(user.getUsername())));
	}
	
	
	
	

}
