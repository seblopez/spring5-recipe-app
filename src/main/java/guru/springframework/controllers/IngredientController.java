package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/recipe/{recipeId}")
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

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

    @GetMapping
    @RequestMapping("/ingredient/{ingredientId}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId,
                           @PathVariable String ingredientId, Model model){
        model.addAttribute("ingredient", this.ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId)));
        model.addAttribute("uomList", this.unitOfMeasureService.listAllUoms());

        return "recipe/ingredient/ingredientform";

    }

    @PostMapping("/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command){
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        log.debug(MessageFormat.format("Saved Recipe Id {0}",savedCommand.getRecipeId()));
        log.debug(MessageFormat.format("Saved Ingredient Id {0}", savedCommand.getId()));

        return MessageFormat.format("redirect:/recipe/{0}/ingredient/{1}/show", savedCommand.getRecipeId(), savedCommand.getId());

    }

}
