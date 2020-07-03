package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

@Slf4j
@RequestMapping("/recipe")
@AllArgsConstructor
@Controller
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    @RequestMapping("/{id}/show")
    public String show(@PathVariable String id, Model model) {

        final Recipe recipe = this.recipeService.getById(Long.valueOf(id));
        model.addAttribute("recipe", recipe);

        return "recipe/show";

    }

    @GetMapping
    @RequestMapping("/new")
    public String newRecipe(Model model) {

        model.addAttribute("recipe", new RecipeCommand());

        return "recipe/recipeform";

    }

    @GetMapping
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

    @GetMapping
    @RequestMapping("/{id}/delete")
    public String deleteById(@PathVariable Long id) {
        this.recipeService.deleteById(id);
        log.debug(MessageFormat.format("Deleted recipe with id {0}", id));
        return "redirect:/";
    }

}
