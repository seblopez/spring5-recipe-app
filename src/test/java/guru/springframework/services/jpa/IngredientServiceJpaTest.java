package guru.springframework.services.jpa;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.services.IngredientService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IngredientServiceJpaTest {

    @Mock
    RecipeRepository recipeRepository;

    IngredientToIngredientCommand ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());

    IngredientService ingredientService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ingredientService = new IngredientServiceJpa(recipeRepository, ingredientToIngredientCommand);
    }

    @Test
    public void findByRecipeIdAndIngredientId() {
        // given
        final Ingredient porkShoulder = Ingredient.builder()
                .id(445L)
                .description("Pork shoulder")
                .build();

        final Ingredient orangeJuice = Ingredient.builder()
                .id(123L)
                .description("Orange juice")
                .build();

        final Long recipeId = 232L;
        final Recipe carnitas = Recipe.builder()
                .id(recipeId)
                .description("Carnitas")
                .build();

        carnitas.addIngredient(porkShoulder);
        carnitas.addIngredient(orangeJuice);

        Optional<Recipe> recipe = Optional.of(carnitas);

        final IngredientCommand orangeJuiceCommand = IngredientCommand.builder()
                .id(123L)
                .description("Orange juice")
                .build();

        // when
        when(this.recipeRepository.findById(anyLong())).thenReturn(recipe);

        // then
        final IngredientCommand retrievedIngredientCommand = this.ingredientService.findByRecipeIdAndIngredientId(recipeId, 123L);

        assertNotNull(retrievedIngredientCommand);
        assertEquals(orangeJuiceCommand.getId(), retrievedIngredientCommand.getId());
        assertEquals(recipeId, retrievedIngredientCommand.getRecipeId());
        verify(this.recipeRepository).findById(anyLong());

    }


    @Test
    public void getByIdReturnsRuntimeExceptionDueToNotExistingRecipe() {
        // when
        when(this.recipeRepository.findById(anyLong())).thenReturn(Optional.empty());
        exception.expect(RuntimeException.class);
        exception.expectMessage("Recipe Id 232 not found");

        // then
        final IngredientCommand retrievedIngredientCommand = this.ingredientService.findByRecipeIdAndIngredientId(232L, 123L);

    }

    @Test
    public void getByIdReturnsRuntimeExceptionDueToNotExistingIngredient() {

        // given
        Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(Ingredient.builder()
                .id(445L)
                .description("Pork shoulder")
                .build());

        ingredients.add(Ingredient.builder()
                .id(123L)
                .description("Orange juice")
                .build());

        Optional<Recipe> recipe = Optional.of(Recipe.builder()
                .id(232L)
                .description("Carnitas")
                .ingredients(ingredients)
                .build());

        final IngredientCommand orangeJuice = IngredientCommand.builder()
                .id(123L)
                .description("Orange juice")
                .build();
        // when
        when(this.recipeRepository.findById(anyLong())).thenReturn(recipe);
        exception.expect(RuntimeException.class);
        exception.expectMessage("Ingredient Id 544 not found");

        // then
        final IngredientCommand retrievedIngredientCommand = this.ingredientService.findByRecipeIdAndIngredientId(232L, 544L);

    }

}
