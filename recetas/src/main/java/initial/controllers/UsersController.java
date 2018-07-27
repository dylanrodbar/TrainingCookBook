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
	public List<Users> getAllUsers() {
		return userRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public Users getUserById(@PathVariable String id) {
		Optional<Users> optionalUser = userRepository.findById(id);
		
		Users user = optionalUser.get();
		return user;
	}
	
	@PostMapping("")
	public Users addUser(@RequestParam("username") String username, @RequestParam("password") String password) {
		
		Users user = new Users(username, password);
		userRepository.save(user);
		
		return user;
	
	}
	
	@PutMapping("/{id}")
	public Users updateUser(@PathVariable String id, @RequestParam("username") String username, @RequestParam("password") String password) {
		Optional<Users> optionalUser = userRepository.findById(id);
		Users user = optionalUser.get();
		
		if(optionalUser.isPresent()) {
			user.setUsername(username);
			user.setPassword(password);
		}
		
		return user;
	}
	
	@DeleteMapping("/{id}")
	public Users deleteUser(@PathVariable String id) {
		Optional<Users> optionalUser = userRepository.findById(id);
		
		Users user = optionalUser.get();
		
		if(optionalUser.isPresent()) {
			userRepository.delete(user);
		}
		
		return user;
		
	}
	
	
	
}
