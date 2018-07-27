package initial.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


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


import initial.models.Users;
import initial.repositories.UsersRepository;


@RestController
@RequestMapping("/users")
public class UsersController {

	private UsersRepository userRepository;
	
	public UsersController(UsersRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@GetMapping("")
	public ResponseEntity getAllUsers() {
		List<Users> users = userRepository.findAll();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity getUserById(@PathVariable String id) {
		
		Optional<Users> optionalUser = userRepository.findById(id);
		Users user;
		
		if(optionalUser.isPresent()) {
			user = optionalUser.get();
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
		
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
		
		
	}
	
	@PostMapping("")
	public ResponseEntity addUser(@RequestParam("username") String username, @RequestParam("password") String password) {
		
		Users userInDB = userRepository.findByUsername(username);
		
		if (userInDB == null) {
			
			Users user = new Users(username, password);
			userRepository.save(user);
			return new ResponseEntity<>(user, HttpStatus.OK);
		
		} 
		
		return new ResponseEntity<>(HttpStatus.CONFLICT);
	
	}
	
	@PutMapping("/{id}")
	public ResponseEntity updateUser(@PathVariable String id, @RequestBody Users user) {
		
		Optional<Users> optionalUser = userRepository.findById(id);
		Users userUpdate;
		
		
		if(optionalUser.isPresent()) {
			userUpdate = optionalUser.get();
			userUpdate.setPassword(user.getPassword());
			userRepository.save(userUpdate);
			return new ResponseEntity<>(userUpdate, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity deleteUser(@PathVariable String id) {
		
		Optional<Users> optionalUser = userRepository.findById(id);
		
		Users user;
		
		if(optionalUser.isPresent()) {
			user = optionalUser.get();
			userRepository.delete(user);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
	}
	
	
	
}
