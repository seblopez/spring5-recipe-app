package guru.springframework.services.jpa;

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
import static org.mockito.Mockito.*;

public class RecipeServiceJpaTest {

    RecipeService recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        this.recipeService = new RecipeServiceJpa(this.recipeRepository);
    }

    @Test
    public void getAll() {

        Recipe recipe = Recipe.builder()
                .id(2l)
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
                .id(2l)
                .description("Ñoquis")
                .build();

        when(this.recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        final Recipe recipeFound = this.recipeService.getById(2l);

        assertEquals(recipe, recipeFound);
        verify(this.recipeRepository).findById(anyLong());
        verify(this.recipeRepository, never()).findAll();

    }

    @Test
    public void getByIdReturnsRuntimeException() {

        when(this.recipeRepository.findById(anyLong())).thenReturn(Optional.empty());

        exception.expect(RuntimeException.class);
        exception.expectMessage("Recipe id 2 not found");

        this.recipeService.getById(2l);

    }

}
