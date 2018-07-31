package initial.controllers;

import static initial.constants.SecurityConstants.EXPIRATION_TIME;
import static initial.constants.SecurityConstants.SECRET;

import java.util.Date;
import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;




import initial.models.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import initial.repositories.UsersRepository;

@RestController
@RequestMapping("/")
public class AuthenticationController {
	
	
	private UsersRepository userRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public AuthenticationController(UsersRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@PostMapping("/sign-up")
	public ResponseEntity signUp(@RequestBody Users user) {
		
		Users userInDB = userRepository.findByUsername(user.getUsername());
		
		if (userInDB == null) {
			
			String encPassword = bCryptPasswordEncoder.encode(user.getPassword());
			user.setPassword(encPassword);
			userRepository.save(user);
			
			String token = Jwts.builder()
	                .setSubject(user.getUsername())
	                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
	                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
	                .compact();
			
			HashMap<String, String> returnUser = new HashMap<>();
			
			
			returnUser.put("username", user.getUsername());
	        returnUser.put("token", token);
			
	        return new ResponseEntity<>(returnUser, HttpStatus.OK);
		
		} 
		
		return new ResponseEntity<>(HttpStatus.CONFLICT);
		
	}
	

}
