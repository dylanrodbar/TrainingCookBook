package initial.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import initial.models.Users;

public interface UsersRepository extends MongoRepository<Users, String> {
	
	Users findByUsername(String username);

}
