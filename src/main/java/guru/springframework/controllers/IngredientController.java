package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.MessageFormat;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/recipe/{recipeId}")
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    @GetMapping
    @RequestMapping("/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model) {
        log.debug(MessageFormat.format("Retrieving ingredients for recipe Id {0}", recipeId));
        model.addAttribute("recipe", this.recipeService.getCommandById(Long.valueOf(recipeId)));

        return "recipe/ingredient/list";

    }

    @GetMapping
    @RequestMapping("/ingredient/{ingredientId}/show")
    public String showIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {

        final IngredientCommand ingredientCommand = this.ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId));

        model.addAttribute("ingredient", ingredientCommand);

        return "recipe/ingredient/show";
    }

}
