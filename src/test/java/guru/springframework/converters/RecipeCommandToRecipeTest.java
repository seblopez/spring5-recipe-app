package guru.springframework.converters;

import guru.springframework.commands.*;
import guru.springframework.domain.Difficulty;
import guru.springframework.domain.Recipe;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RecipeCommandToRecipeTest {

    private final CategoryCommandToCategory categoryConverter = new CategoryCommandToCategory();
    private final UnitOfMeasureCommandToUnitOfMeasure uomConverter = new UnitOfMeasureCommandToUnitOfMeasure();
    private final IngredientCommandToIngredient ingredientConverter = new IngredientCommandToIngredient(uomConverter);
    private final NotesCommandToNotes notesConverter = new NotesCommandToNotes();

    private final RecipeCommandToRecipe converter = new RecipeCommandToRecipe(categoryConverter, ingredientConverter, notesConverter);

    @Test
    public void convertOk() {
        final Set<CategoryCommand> categoryCommands = new HashSet<>();
        categoryCommands.add(CategoryCommand.builder()
                .id(230L)
                .description("Italian")
                .build());

        final UnitOfMeasureCommand each = UnitOfMeasureCommand.builder()
                .id(565L)
                .description("Each")
                .build();

        final UnitOfMeasureCommand kilogram = UnitOfMeasureCommand.builder()
                .id(234L)
                .description("Kilogram")
                .build();

        final NotesCommand notes = NotesCommand.builder()
                .id(3565L)
                .recipeNotes("Notes 1m, 2")
                .build();

        final Set<IngredientCommand> ingredientCommands = new HashSet<>();
        ingredientCommands.add(IngredientCommand.builder()
                .id(23L)
                .unitOfMeasure(kilogram)
                .amount(BigDecimal.ONE)
                .description("Potato")
                .build());
        ingredientCommands.add(IngredientCommand.builder()
                .id(43L)
                .amount(BigDecimal.valueOf(2))
                .description("Egg")
                .build());

        final RecipeCommand source = RecipeCommand.builder()
                .id(23224L)
                .description("Potato Gnocchi")
                .difficulty(Difficulty.EASY)
                .prepTime(30)
                .cookTime(60)
                .notes(notes)
                .categories(categoryCommands)
                .ingredients(ingredientCommands)
                .build();

        final Recipe target = this.converter.convert(source);

        assertNotNull(target);
        assertEquals(source.getId(), target.getId());
        assertEquals(source.getCookTime(), target.getCookTime());
        assertEquals(source.getPrepTime(), target.getPrepTime());
        assertEquals(source.getNotes().getRecipeNotes(), target.getNotes().getRecipeNotes());
        assertEquals(source.getCategories().size(), target.getCategories().size());
        assertEquals(source.getIngredients().size(), target.getIngredients().size());

    }

}
