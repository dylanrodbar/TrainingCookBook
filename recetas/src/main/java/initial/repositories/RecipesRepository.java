package initial.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import initial.models.Recipe;
import initial.models.Users;

public interface RecipesRepository  extends MongoRepository<Recipe, String>{
	
}
