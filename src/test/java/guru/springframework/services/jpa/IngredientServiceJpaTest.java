package guru.springframework.services.jpa;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureCommandToUnitOfMeasure;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.services.IngredientService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class IngredientServiceJpaTest {

    @Mock
    RecipeRepository recipeRepository;

    final IngredientToIngredientCommand ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());

    final IngredientCommandToIngredient ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());

    IngredientService ingredientService;

    Recipe recipe;

    final UnitOfMeasure teaspoon = UnitOfMeasure.builder().id(23L).description("Teaspoon").build();
    final UnitOfMeasure tablespoon = UnitOfMeasure.builder().id(23L).description("Tablespoon").build();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        this.ingredientService = new IngredientServiceJpa(this.recipeRepository, this.ingredientToIngredientCommand, this.ingredientCommandToIngredient);

        this.recipe = Recipe.builder()
                .id(345L)
                .description("Pizza dough")
                .build();

        this.recipe.addIngredient(Ingredient.builder()
                .id(873L)
                .amount(BigDecimal.valueOf(250))
                .unitOfMeasure(UnitOfMeasure.builder().id(34L).description("Gram").build())
                .description("Flour")
                .build());

        this.recipe.addIngredient(Ingredient.builder()
                .id(324L)
                .amount(BigDecimal.ONE)
                .unitOfMeasure(teaspoon)
                .description("Salt")
                .build());

        this.recipe.addIngredient(Ingredient.builder()
                .id(340L)
                .amount(BigDecimal.ONE)
                .unitOfMeasure(teaspoon)
                .description("Olive oil")
                .build());

        this.recipe.addIngredient(Ingredient.builder()
                .id(234L)
                .amount(BigDecimal.ONE)
                .unitOfMeasure(tablespoon)
                .description("Dry yeast")
                .build());

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
        this.ingredientService.findByRecipeIdAndIngredientId(232L, 123L);

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

        // when
        when(this.recipeRepository.findById(anyLong())).thenReturn(recipe);
        exception.expect(RuntimeException.class);
        exception.expectMessage("Ingredient Id 544 not found");

        // then
        this.ingredientService.findByRecipeIdAndIngredientId(232L, 544L);

    }

    @Test
    public void saveNewIngredientCommandOk() {
        // given
        IngredientCommand ingredientCommand = IngredientCommand.builder()
                .recipeId(345L)
                .amount(BigDecimal.valueOf(.25))
                .unitOfMeasure(UnitOfMeasureCommand.builder().id(33L).description("Pound").build())
                .description("Flour")
                .build();

        Recipe savedRecipe = Recipe.builder()
                .id(345L)
                .description("Pizza dough")
                .build();

        savedRecipe.addIngredient(Ingredient.builder()
                .id(873L)
                .amount(BigDecimal.valueOf(250))
                .unitOfMeasure(UnitOfMeasure.builder().id(34L).description("Gram").build())
                .description("Flour")
                .build());

        savedRecipe.addIngredient(Ingredient.builder()
                .id(324L)
                .amount(BigDecimal.ONE)
                .unitOfMeasure(teaspoon)
                .description("Salt")
                .build());

        savedRecipe.addIngredient(Ingredient.builder()
                .id(340L)
                .amount(BigDecimal.ONE)
                .unitOfMeasure(teaspoon)
                .description("Olive oil")
                .build());

        savedRecipe.addIngredient(Ingredient.builder()
                .id(234L)
                .amount(BigDecimal.ONE)
                .unitOfMeasure(tablespoon)
                .description("Dry yeast")
                .build());

        final Long newIngredientId = 2323L;

        savedRecipe.addIngredient(Ingredient.builder()
                .id(newIngredientId)
                .amount(BigDecimal.valueOf(.25))
                .unitOfMeasure(UnitOfMeasure.builder().id(33L).description("Pound").build())
                .description("Flour")
                .build());

        // when
        when(this.recipeRepository.findById(anyLong())).thenReturn(Optional.of(this.recipe));
        when(this.recipeRepository.save(any(Recipe.class))).thenReturn(savedRecipe);

        // then
        final IngredientCommand savedIngredientCommand = this.ingredientService.saveIngredientCommand(ingredientCommand);

        assertNotNull(savedIngredientCommand);
        assertEquals(newIngredientId, savedIngredientCommand.getId());
        assertEquals(ingredientCommand.getUnitOfMeasure().getId(), savedIngredientCommand.getUnitOfMeasure().getId());
        assertEquals(ingredientCommand.getDescription(), savedIngredientCommand.getDescription());
        assertEquals(ingredientCommand.getRecipeId(), savedIngredientCommand.getRecipeId());
        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository).save(any(Recipe.class));

    }

    @Test
    public void saveExistingIngredientCommandOk() {
        // given
        IngredientCommand ingredientCommand = IngredientCommand.builder()
                .id(873L)
                .recipeId(345L)
                .amount(BigDecimal.valueOf(1.5))
                .unitOfMeasure(UnitOfMeasureCommand.builder().id(3676L).description("Cup").build())
                .description("Flour")
                .build();

        Recipe savedRecipe = Recipe.builder()
                .id(345L)
                .description("Pizza dough")
                .build();

        savedRecipe.addIngredient(Ingredient.builder()
                .id(873L)
                .amount(BigDecimal.valueOf(1.5))
                .unitOfMeasure(UnitOfMeasure.builder().id(3676L).description("Cup").build())
                .description("Flour")
                .build());

        savedRecipe.addIngredient(Ingredient.builder()
                .id(324L)
                .amount(BigDecimal.ONE)
                .unitOfMeasure(teaspoon)
                .description("Salt")
                .build());

        savedRecipe.addIngredient(Ingredient.builder()
                .id(340L)
                .amount(BigDecimal.ONE)
                .unitOfMeasure(teaspoon)
                .description("Olive oil")
                .build());

        savedRecipe.addIngredient(Ingredient.builder()
                .id(234L)
                .amount(BigDecimal.ONE)
                .unitOfMeasure(tablespoon)
                .description("Dry yeast")
                .build());

        // when
        when(this.recipeRepository.findById(anyLong())).thenReturn(Optional.of(this.recipe));
        when(this.recipeRepository.save(any(Recipe.class))).thenReturn(savedRecipe);

        // then
        final IngredientCommand savedIngredientCommand = this.ingredientService.saveIngredientCommand(ingredientCommand);

        assertNotNull(savedIngredientCommand);
        assertEquals(ingredientCommand.getId(), savedIngredientCommand.getId());
        assertEquals(ingredientCommand.getUnitOfMeasure().getId(), savedIngredientCommand.getUnitOfMeasure().getId());
        assertEquals(ingredientCommand.getDescription(), savedIngredientCommand.getDescription());
        assertEquals(ingredientCommand.getRecipeId(), savedIngredientCommand.getRecipeId());
        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository).save(any(Recipe.class));

    }

    @Test
    public void deleteByRecipeIdAndIngredientIdOk() {
        final Long recipeId = this.recipe.getId();
        final Long ingredientId = 324L;

        Recipe savedRecipe = Recipe.builder()
                .id(345L)
                .description("Pizza dough")
                .build();

        savedRecipe.addIngredient(Ingredient.builder()
                .id(873L)
                .amount(BigDecimal.valueOf(1.5))
                .unitOfMeasure(UnitOfMeasure.builder().id(3676L).description("Cup").build())
                .description("Flour")
                .build());

        savedRecipe.addIngredient(Ingredient.builder()
                .id(340L)
                .amount(BigDecimal.ONE)
                .unitOfMeasure(teaspoon)
                .description("Olive oil")
                .build());

        savedRecipe.addIngredient(Ingredient.builder()
                .id(234L)
                .amount(BigDecimal.ONE)
                .unitOfMeasure(tablespoon)
                .description("Dry yeast")
                .build());

        when(this.recipeRepository.findById(recipeId)).thenReturn(Optional.of(this.recipe));
        when(this.recipeRepository.save(any(Recipe.class))).thenReturn(savedRecipe);

        this.ingredientService.deleteByRecipeIdAndIngredientId(recipeId, ingredientId);

        assertTrue(savedRecipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .findFirst()
                .isEmpty());
        verify(this.recipeRepository).findById(anyLong());
        verify(this.recipeRepository).save(any(Recipe.class));

    }

    @Test
    public void deleteByRecipeIdAndIngredientIdNotFoundOk() {
        final Long recipeId = this.recipe.getId();
        final Long ingredientId = 524L;

        Recipe savedRecipe = Recipe.builder()
                .id(345L)
                .description("Pizza dough")
                .build();

        savedRecipe.addIngredient(Ingredient.builder()
                .id(873L)
                .amount(BigDecimal.valueOf(1.5))
                .unitOfMeasure(UnitOfMeasure.builder().id(3676L).description("Cup").build())
                .description("Flour")
                .build());

        savedRecipe.addIngredient(Ingredient.builder()
                .id(340L)
                .amount(BigDecimal.ONE)
                .unitOfMeasure(teaspoon)
                .description("Olive oil")
                .build());

        savedRecipe.addIngredient(Ingredient.builder()
                .id(234L)
                .amount(BigDecimal.ONE)
                .unitOfMeasure(tablespoon)
                .description("Dry yeast")
                .build());

        when(this.recipeRepository.findById(recipeId)).thenReturn(Optional.of(this.recipe));

        this.ingredientService.deleteByRecipeIdAndIngredientId(recipeId, ingredientId);

        assertTrue(savedRecipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .findFirst()
                .isEmpty());
        verify(this.recipeRepository).findById(anyLong());
        verify(this.recipeRepository, never()).save(any(Recipe.class));

    }
}
