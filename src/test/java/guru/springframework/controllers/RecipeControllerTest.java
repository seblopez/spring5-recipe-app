package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

        Recipe recipe = Recipe.builder()
                .id(1L)
                .description("Tortas fritas")
                .build();

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(this.recipeController)
                .build();

        when(this.recipeService.getById(anyLong())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/show/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attribute("recipe", recipe));

    }
}
