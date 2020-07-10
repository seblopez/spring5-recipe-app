package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Category;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class RecipeControllerTest {

    @Mock
    RecipeService recipeService;

    @InjectMocks
    RecipeController recipeController;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(this.recipeController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();

    }

    @Test
    public void show() throws Exception {

        final Set<Ingredient> ingredients = new HashSet<>();
        final UnitOfMeasure pound = UnitOfMeasure.builder()
                .id(243243L)
                .description("Pound")
                .build();

        final UnitOfMeasure unit = UnitOfMeasure.builder()
                .id(2434L)
                .description("Unit")
                .build();

        final UnitOfMeasure pinch = UnitOfMeasure.builder()
                .id(432L)
                .description("Pinch")
                .build();

        ingredients.add(Ingredient.builder()
                .id(234L)
                .description("Flour")
                .amount(BigDecimal.valueOf(2))
                .unitOfMeasure(pound)
                .build());

        ingredients.add(Ingredient.builder()
                .id(10L)
                .description("Egg")
                .amount(BigDecimal.valueOf(2))
                .unitOfMeasure(unit)
                .build());

        ingredients.add(Ingredient.builder()
                .id(943L)
                .description("Salt")
                .amount(BigDecimal.ONE)
                .unitOfMeasure(pinch)
                .build());

        final Set<Category> categories = new HashSet<>();
        categories.add(Category.builder()
                .id(323L)
                .description("Latin")
                .build());

        categories.add(Category.builder()
                .id(234L)
                .description("Fast food")
                .build());

        Recipe recipe = Recipe.builder()
                .id(1L)
                .description("Tortas fritas")
                .categories(categories)
                .ingredients(ingredients)
                .build();

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(this.recipeController)
                .build();

        when(this.recipeService.getById(anyLong())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void newRecipe() throws Exception {
        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));

    }

    @Test
    public void testPostNewRecipeForm() throws Exception {
        RecipeCommand command = RecipeCommand.builder()
                .id(2L)
                .description("Cabra de Monte")
                .prepTime(2)
                .cookTime(30)
                .servings(4)
                .directions("Cook it now!")
                .url("http://www.recipes.com")
                .source("")
                .build();

        when(recipeService.saveRecipeCommand(any(RecipeCommand.class))).thenReturn(command);

        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "Cabra de Monte")
                .param("prepTime", "2")
                .param("cookTime", "30")
                .param("servings", "4")
                .param("directions", "Cook it now!")
                .param("url", "http://www.recipes.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/show"));
    }

    @Test
    public void testGetUpdateView() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(2L);

        when(recipeService.getCommandById(anyLong())).thenReturn(command);

        mockMvc.perform(get("/recipe/2/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testDeleteAction() throws Exception {
        mockMvc.perform(get("/recipe/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(recipeService).deleteById(anyLong());

    }

    @Test
    public void testRecipeNotFoundExceptionHandling() throws Exception {
        when(this.recipeService.getById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/3/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    public void testNumberFormatExceptionHandling() throws Exception {
        mockMvc.perform(get("/recipe/3as/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }
}
