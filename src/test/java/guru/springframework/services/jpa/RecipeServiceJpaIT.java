package guru.springframework.services.jpa;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.*;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
public class RecipeServiceJpaIT {

    public static final String NEW_DESCRIPTION = "New Description";

    Recipe initialRecipe;

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Autowired
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Before
    public void setUp() {
        final Category category = this.categoryRepository.findCategoryByDescription("Mexican").orElse(Category.builder().build());
        final UnitOfMeasure each = this.unitOfMeasureRepository.findUnitOfMeasureByDescription("Each").orElse(UnitOfMeasure.builder().build());
        final UnitOfMeasure pound = this.unitOfMeasureRepository.findUnitOfMeasureByDescription("Pound").orElse(UnitOfMeasure.builder().build());
        final UnitOfMeasure teaspoon = this.unitOfMeasureRepository.findUnitOfMeasureByDescription("Teaspoon").orElse(UnitOfMeasure.builder().build());
        final Set<Category> categories = new HashSet<>();
        categories.add(category);

        final Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(Ingredient.builder()
                .amount(BigDecimal.valueOf(12))
                .description("Chicken Wings")
                .unitOfMeasure(each)
                .build());

        ingredients.add(Ingredient.builder()
                .amount(BigDecimal.ONE)
                .description("Flour")
                .unitOfMeasure(pound)
                .build());

        ingredients.add(Ingredient.builder()
                .amount(BigDecimal.valueOf(3))
                .description("Egg")
                .unitOfMeasure(each)
                .build());

        ingredients.add(Ingredient.builder()
                .amount(BigDecimal.ONE)
                .unitOfMeasure(teaspoon)
                .description("Chile poblano")
                .build());

        ingredients.add(Ingredient.builder()
                .amount(BigDecimal.ONE)
                .unitOfMeasure(teaspoon)
                .description("Salt")
                .build());

        ingredients.add(Ingredient.builder()
                .amount(BigDecimal.ONE)
                .unitOfMeasure(teaspoon)
                .description("Pepper")
                .build());

        this.initialRecipe = Recipe.builder()
                .description("Pollos Hermanos")
                .categories(categories)
                .prepTime(30)
                .cookTime(10)
                .difficulty(Difficulty.KIND_OF_HARD)
                .ingredients(ingredients)
                .directions("Some directions")
                .source("Some source")
                .servings(4)
                .notes(Notes.builder()
                        .recipeNotes("Some notes")
                        .build())
                .url("http://www.polloshermanos.com/")
                .build();


    }

    @Transactional
    @Test
    public void testSaveOfDescription() throws Exception {
        //given
        Iterable<Recipe> recipes = recipeRepository.findAll();
        Recipe testRecipe = recipes.iterator().next();
        RecipeCommand testRecipeCommand = recipeToRecipeCommand.convert(testRecipe);

        //when
        testRecipeCommand.setDescription(NEW_DESCRIPTION);
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(testRecipeCommand);

        //then
        assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
        assertEquals(testRecipe.getId(), savedRecipeCommand.getId());
        assertEquals(testRecipe.getCategories().size(), savedRecipeCommand.getCategories().size());
        assertEquals(testRecipe.getIngredients().size(), savedRecipeCommand.getIngredients().size());
    }

    @Transactional
    @Test
    public void testSaveNewRecipeOk() throws Exception {
        //given
        Recipe testRecipe = this.initialRecipe;
        RecipeCommand testRecipeCommand = recipeToRecipeCommand.convert(testRecipe);

        //when
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(testRecipeCommand);

        //then
        assertNotNull(savedRecipeCommand);
        assertNotNull(savedRecipeCommand.getId());
        log.debug(MessageFormat.format("Recipe id {0}", savedRecipeCommand.getId()));
        assertEquals(testRecipe.getCategories().size(), savedRecipeCommand.getCategories().size());
        assertEquals(testRecipe.getIngredients().size(), savedRecipeCommand.getIngredients().size());

    }

}
