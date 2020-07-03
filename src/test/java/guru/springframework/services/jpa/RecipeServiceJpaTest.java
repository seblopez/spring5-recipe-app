package guru.springframework.services.jpa;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.services.RecipeService;
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
import static org.mockito.Mockito.*;

public class RecipeServiceJpaTest {

    RecipeService recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        this.recipeService = new RecipeServiceJpa(this.recipeRepository, this.recipeCommandToRecipe, this.recipeToRecipeCommand);
    }

    @Test
    public void getAll() {

        Recipe recipe = Recipe.builder()
                .id(2L)
                .description("Ñoquis")
                .build();

        Set<Recipe> mockRecipes = new HashSet<>();
        mockRecipes.add(recipe);

        when(this.recipeRepository.findAll()).thenReturn(mockRecipes);

        final Set<Recipe> recipes = this.recipeService.getAll();

        assertEquals(1, recipes.size());
        verify(this.recipeRepository).findAll();

    }

    @Test
    public void getByIdReturnsOk() {

        Recipe recipe = Recipe.builder()
                .id(2L)
                .description("Ñoquis")
                .build();

        when(this.recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        final Recipe recipeFound = this.recipeService.getById(2L);

        assertEquals(recipe, recipeFound);
        verify(this.recipeRepository).findById(anyLong());
        verify(this.recipeRepository, never()).findAll();

    }

    @Test
    public void getByIdReturnsRuntimeException() {

        when(this.recipeRepository.findById(anyLong())).thenReturn(Optional.empty());

        exception.expect(RuntimeException.class);
        exception.expectMessage("Recipe id 2 not found");

        this.recipeService.getById(2L);

    }

    @Test
    public void saveRecipeCommand() {

        final Long id = 345L;
        final String description = "Mock Recipe";

        RecipeCommand recipeCommand = RecipeCommand.builder()
                .description(description)
                .build();

        Recipe recipe = Recipe.builder()
                .id(id)
                .description(description).build();

        RecipeCommand savedRecipeCommandMock = RecipeCommand.builder()
                .id(id)
                .description(description)
                .build();

        when(this.recipeCommandToRecipe.convert(recipeCommand)).thenReturn(recipe);
        when(this.recipeRepository.save(any(Recipe.class))).thenReturn(recipe);
        when(this.recipeToRecipeCommand.convert(recipe)).thenReturn(savedRecipeCommandMock);

        final RecipeCommand savedRecipeCommand = this.recipeService.saveRecipeCommand(recipeCommand);

        assertNotNull(savedRecipeCommand);
        assertEquals(recipe.getId(), savedRecipeCommand.getId());
        assertEquals(recipe.getDescription(), savedRecipeCommand.getDescription());
        verify(this.recipeCommandToRecipe).convert(any(RecipeCommand.class));
        verify(this.recipeRepository).save(any(Recipe.class));
        verify(this.recipeToRecipeCommand).convert(any(Recipe.class));

    }

    @Test
    public void getCommandByIdOk() {
        final Long id = 2L;
        final String description = "Ñoquis";

        Recipe recipe = Recipe.builder()
                .id(id)
                .description(description)
                .build();

        RecipeCommand recipeCommand = RecipeCommand.builder()
                .id(id)
                .description(description)
                .build();

        when(this.recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        when(this.recipeToRecipeCommand.convert(any(Recipe.class))).thenReturn(recipeCommand);

        final RecipeCommand recipeCommandFound = this.recipeService.getCommandById(id);

        assertEquals(recipeCommand, recipeCommandFound);
        verify(this.recipeRepository).findById(anyLong());
        verify(this.recipeRepository, never()).findAll();
        verify(this.recipeToRecipeCommand).convert(any(Recipe.class));
    }

    @Test
    public void getCommandByIdReturnsRuntimeException() {

        when(this.recipeRepository.findById(anyLong())).thenReturn(Optional.empty());
        exception.expect(RuntimeException.class);
        exception.expectMessage("Recipe id 2 not found");

        this.recipeService.getCommandById(2L);

    }

}
