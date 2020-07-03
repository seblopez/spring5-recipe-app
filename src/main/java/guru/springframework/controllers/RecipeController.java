package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.MessageFormat;

@RequestMapping("/recipe")
@AllArgsConstructor
@Controller
public class RecipeController {

    private final RecipeService recipeService;

    @RequestMapping("/{id}/show")
    public String show(@PathVariable String id, Model model) {

        final Recipe recipe = this.recipeService.getById(Long.valueOf(id));
        model.addAttribute("recipe", recipe);

        return "recipe/show";

    }

    @RequestMapping("/new")
    public String newRecipe(Model model) {

        model.addAttribute("recipe", new RecipeCommand());

        return "recipe/recipeform";

    }

    @RequestMapping("/{id}/update")
    public String updateRecipe(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", this.recipeService.getCommandById(id));
        return "recipe/recipeform";
    }

    @PostMapping
    public String saveOrUpdate(@ModelAttribute RecipeCommand command) {
        final RecipeCommand savedRecipeCommand = this.recipeService.saveRecipeCommand(command);
        final String redirect = MessageFormat.format("redirect:/recipe/{0}/show", savedRecipeCommand.getId());
        return redirect;
    }

}
