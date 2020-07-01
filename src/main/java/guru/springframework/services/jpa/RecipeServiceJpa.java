package guru.springframework.services.jpa;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.services.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class RecipeServiceJpa implements RecipeService {

    private final RecipeRepository recipeRepository;

    @Override
    public Set<Recipe> getAll() {
        final Set<Recipe> recipes = new HashSet<>();
        this.recipeRepository.findAll().forEach(recipes::add);
        return recipes;
    }

    @Override
    public Recipe getById(Long id) {
        final Recipe recipe = this.recipeRepository.findById(id).orElseThrow(() -> {
            final String message = MessageFormat.format("Recipe id {0} not found", id);
            log.debug(message);
            return new RuntimeException(message);
        });

        return recipe;

    }

}
