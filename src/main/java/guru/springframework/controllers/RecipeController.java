package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.services.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.MessageFormat;

@Slf4j
@RequestMapping("/recipe")
@AllArgsConstructor
@Controller
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/{id}/show")
    public String show(@PathVariable String id, Model model) {
        final Recipe recipe = this.recipeService.getById(Long.valueOf(id));
        model.addAttribute("recipe", recipe);

        return "recipe/show";

    }

    @GetMapping("/new")
    public String newRecipe(Model model) {

        model.addAttribute("recipe", new RecipeCommand());

        return "recipe/recipeform";

    }

    @GetMapping("/{id}/update")
    public String updateRecipe(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", this.recipeService.getCommandById(id));
        return "recipe/recipeform";
    }

    @PostMapping
    public String saveOrUpdate(@ModelAttribute RecipeCommand command) {
        final RecipeCommand savedRecipeCommand = this.recipeService.saveRecipeCommand(command);
        return MessageFormat.format("redirect:/recipe/{0}/show", savedRecipeCommand.getId());
    }

    @GetMapping("/{id}/delete")
    public String deleteById(@PathVariable Long id) {
        this.recipeService.deleteById(id);
        log.debug(MessageFormat.format("Deleted recipe with id {0}", id));
        return "redirect:/";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(Exception exception) {
        log.error("Handling not found exception");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404error");
        modelAndView.addObject("exception", exception);

        return modelAndView;

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView handleNumberFormat(Exception exception) {
        log.error("Number format found exception");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("400error");
        modelAndView.addObject("exception", exception);

        return modelAndView;

    }


}
