package guru.springframework.services.jpa;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.services.IngredientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Slf4j
@AllArgsConstructor
@Service
public class IngredientServiceJpa implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        final Recipe recipe = this.recipeRepository.findById(recipeId)
                .orElseThrow(() -> {
                    final String errorMessage = MessageFormat.format("Recipe Id {0} not found", recipeId);
                    log.error(errorMessage);
                    return new RuntimeException(errorMessage);
                });

        final Ingredient ingredient = recipe.getIngredients()
                .stream()
                .filter(i -> i.getId().equals(ingredientId))
                .findFirst()
                .orElseThrow(() -> {
                    final String errorMessage = MessageFormat.format("Ingredient Id {0} not found", ingredientId);
                    log.error(errorMessage);
                    return new RuntimeException(errorMessage);
                });

        return this.ingredientToIngredientCommand.convert(ingredient);

    }
}
