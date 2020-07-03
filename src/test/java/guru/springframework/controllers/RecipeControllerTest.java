package guru.springframework.controllers;

import guru.springframework.domain.Category;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class RecipeControllerTest {

    @Mock
    RecipeService recipeService;

    @InjectMocks
    RecipeController recipeController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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

        mockMvc.perform(get("/recipe/show/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void newRecipe() throws Exception {
        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(this.recipeController)
                .build();

        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));

    }

}
