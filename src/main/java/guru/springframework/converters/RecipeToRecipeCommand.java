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

import java.util.HashSet;

@AllArgsConstructor
@Component
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {

    private final CategoryToCategoryCommand categoryConveter;
    private final IngredientToIngredientCommand ingredientConverter;
    private final NotesToNotesCommand notesConverter;

    @Synchronized
    @Nullable
    @Override
    public RecipeCommand convert(Recipe source) {
        if (source == null) {
            return null;
        }

        final SetConverter<Ingredient, IngredientCommand> ingredientSetConverter = new SetConverterImpl();
        final SetConverter<Category, CategoryCommand> categorySetConverter = new SetConverterImpl();

        final RecipeCommand command = RecipeCommand.builder()
                .id(source.getId())
                .cookTime(source.getCookTime())
                .prepTime(source.getPrepTime())
                .description(source.getDescription())
                .difficulty(source.getDifficulty())
                .servings(source.getServings())
                .source(source.getSource())
                .url(source.getUrl())
                .directions(source.getDirections())
                .notes(notesConverter.convert(source.getNotes()))
                .categories(categorySetConverter.convert(source.getCategories(), new HashSet<CategoryCommand>(), this.categoryConveter))
                .ingredients(ingredientSetConverter.convert(source.getIngredients(), new HashSet<IngredientCommand>(), this.ingredientConverter))
                .build();

        return command;
    }

}
