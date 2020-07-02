package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/recipe")
@AllArgsConstructor
@Controller
public class RecipeController {

    private final RecipeService recipeService;

    @RequestMapping("/show/{id}")
    public String show(@PathVariable String id, Model model) {

        final Recipe recipe = this.recipeService.getById(Long.valueOf(id));
        model.addAttribute("recipe", recipe);

        return "recipe/show";


    }
}
