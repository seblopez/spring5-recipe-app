package guru.springframework.services;

import guru.springframework.domain.Recipe;

import java.util.Set;

public interface RecipeService {

    Set<Recipe> getAll();

    Recipe getById(Long id);

}
