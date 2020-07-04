package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Category;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import lombok.AllArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {

        private final CategoryCommandToCategory categoryConveter;
        private final IngredientCommandToIngredient ingredientConverter;
        private final NotesCommandToNotes notesConverter;

        @Synchronized
        @Nullable
        @Override
        public Recipe convert(RecipeCommand source) {
            if (source == null) {
                return null;
            }

            SetConverter<IngredientCommand, Ingredient> ingredientSetConverter = new SetConverterImpl<>();
            SetConverter<CategoryCommand, Category> categorySetConverter = new SetConverterImpl<>();

            final Recipe recipe = Recipe.builder()
                    .id(source.getId())
                    .description(source.getDescription())
                    .difficulty(source.getDifficulty())
                    .servings(source.getServings())
                    .prepTime(source.getPrepTime())
                    .cookTime(source.getCookTime())
                    .directions(source.getDirections())
                    .ingredients(ingredientSetConverter.convert(source.getIngredients(), this.ingredientConverter))
                    .categories(categorySetConverter.convert(source.getCategories(), this.categoryConveter))
                    .notes(this.notesConverter.convert(source.getNotes()))
                    .source(source.getSource())
                    .url(source.getUrl())
                    .build();

            return recipe;

        }

}
