package initial.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import initial.models.Image;
import initial.models.Recipe;

public interface RecipesRepository  extends MongoRepository<Recipe, String>{
	

}
