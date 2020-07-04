package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import lombok.AllArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class IngredientToIngredientCommand implements Converter<Ingredient, IngredientCommand> {

    private final UnitOfMeasureToUnitOfMeasureCommand uomConverter;

    @Synchronized
    @Nullable
    @Override
    public IngredientCommand convert(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }

        IngredientCommand ingredientCommand = IngredientCommand.builder()
                .id(ingredient.getId())
                .amount(ingredient.getAmount())
                .recipeId(resolveRecipeId(ingredient))
                .unitOfMeasure(uomConverter.convert(ingredient.getUnitOfMeasure()))
                .description(ingredient.getDescription())
                .build();

        return ingredientCommand;
    }

    private Long resolveRecipeId(Ingredient ingredient) {
        final Recipe recipe = ingredient.getRecipe();
        return recipe != null ? recipe.getId() : null;
    }
}
