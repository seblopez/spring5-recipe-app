package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.MessageFormat;

@Slf4j
@RequestMapping("/recipe")
@AllArgsConstructor
@Controller
public class RecipeController {

    private final RecipeService recipeService;
    private static final String RECIPE_RECIPEFORM_URL = "recipe/recipeform";

    @GetMapping("/{id}/show")
    public String show(@PathVariable String id, Model model) {
        final Recipe recipe = this.recipeService.getById(Long.valueOf(id));
        model.addAttribute("recipe", recipe);

        return "recipe/show";

    }

    @GetMapping("/new")
    public String newRecipe(Model model) {

        model.addAttribute("recipe", new RecipeCommand());

        return RECIPE_RECIPEFORM_URL;

    }

    @GetMapping("/{id}/update")
    public String updateRecipe(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", this.recipeService.getCommandById(id));
        return RECIPE_RECIPEFORM_URL;
    }

    @PostMapping
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                log.debug(error.toString());
            });
            return RECIPE_RECIPEFORM_URL;
        }

        final RecipeCommand savedRecipeCommand = this.recipeService.saveRecipeCommand(command);
        return MessageFormat.format("redirect:/recipe/{0}/show", savedRecipeCommand.getId());
    }

    @GetMapping("/{id}/delete")
    public String deleteById(@PathVariable Long id) {
        this.recipeService.deleteById(id);
        log.debug(MessageFormat.format("Deleted recipe with id {0}", id));
        return "redirect:/";
    }

}
