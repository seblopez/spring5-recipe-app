package guru.springframework.services.jpa;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.exceptions.SaveException;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.services.IngredientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@Slf4j
@AllArgsConstructor
@Service
public class IngredientServiceJpa implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        final Recipe recipe = this.recipeRepository.findById(recipeId)
                .orElseThrow(notFound("Recipe", recipeId));

        final Ingredient ingredient = recipe.getIngredients()
                .stream()
                .filter(i -> i.getId().equals(ingredientId))
                .findFirst()
                .orElseThrow(notFound("Ingredient", ingredientId));

        return this.ingredientToIngredientCommand.convert(ingredient);

    }

    private Supplier<NotFoundException> notFound(String field, Long id) {
        return () -> {
        final String errorMessage = MessageFormat.format("{0} Id {1} not found", field, id);
        log.error(errorMessage);
        return new NotFoundException(errorMessage);
        };
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {

        final Long recipeId = ingredientCommand.getRecipeId();

        final Recipe recipe = this.recipeRepository.findById(recipeId)
                .orElseThrow(getRuntimeExceptionSupplier("Recipe", recipeId));

        final Ingredient ingredientToSave = this.ingredientCommandToIngredient.convert(ingredientCommand);

        final Set<Ingredient> ingredients = recipe.getIngredients();

        final Long ingredientId = ingredientCommand.getId();

        if(ingredientId != null) {
            final Optional<Ingredient> optionalIngredient = ingredients
                    .stream()
                    .filter(i -> i.getId().equals(ingredientId))
                    .findFirst();

            if(optionalIngredient.isPresent()) {
                final Ingredient ingredientInDB = optionalIngredient.get();
                ingredientInDB.setDescription(ingredientToSave.getDescription());
                ingredientInDB.setUnitOfMeasure(ingredientToSave.getUnitOfMeasure());
                ingredientInDB.setAmount(ingredientToSave.getAmount());
            } else {
                recipe.addIngredient(ingredientToSave);
            }
        } else {
            recipe.addIngredient(ingredientToSave);
        }

        final Recipe savedRecipe = this.recipeRepository.save(recipe);

        final Ingredient savedIngredient = savedRecipe.getIngredients()
                .stream()
                .filter(ingredient ->
                        ingredient.getDescription().equals(ingredientToSave.getDescription())
                                && ingredient.getAmount().equals(ingredientToSave.getAmount())
                                && ingredient.getUnitOfMeasure().getId().equals(ingredientToSave.getUnitOfMeasure().getId()))
                .findFirst()
                .orElseThrow(() -> {
                    final String errorMessage = MessageFormat.format("Ingredient {0} was not properly saved", ingredientToSave.getDescription());
                    log.error(errorMessage);
                    return new SaveException(errorMessage);
                });

        return this.ingredientToIngredientCommand.convert(savedIngredient);

    }

    @Override
    @Transactional
    public void deleteByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        final Recipe recipe = this.recipeRepository.findById(recipeId).orElseThrow(getRuntimeExceptionSupplier("Recipe", recipeId));

        final Optional<Ingredient> optionalIngredient = recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId)).findFirst();

        if(optionalIngredient.isPresent()) {
            final Ingredient ingredientToDelete = optionalIngredient.get();
            ingredientToDelete.setRecipe(null);
            recipe.getIngredients().remove(ingredientToDelete);
            this.recipeRepository.save(recipe);
        } else {
            final String errorMessage = MessageFormat.format("Ingredient Id {0} for Recipe Id {1} not found ", ingredientId, recipeId);
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }

    }

    private Supplier<NotFoundException> getRuntimeExceptionSupplier(String field, Long id) {
        return () -> {
            final String message = MessageFormat.format("{0} id {1} not found", field, id);
            log.debug(message);
            return new NotFoundException(message);
        };
    }

}
