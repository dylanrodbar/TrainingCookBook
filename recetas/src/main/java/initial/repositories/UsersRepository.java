package initial.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import initial.models.Image;
import initial.models.Users;

public interface UsersRepository extends MongoRepository<Users, String> {
	
	Users findByUsername(String username);
	

}
