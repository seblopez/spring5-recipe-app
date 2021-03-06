package guru.springframework.services.jpa;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.services.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@Slf4j
@AllArgsConstructor
@Service
public class RecipeServiceJpa implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    @Override
    public Set<Recipe> getAll() {
        final Set<Recipe> recipes = new HashSet<>();
        this.recipeRepository.findAll().forEach(recipes::add);
        return recipes;
    }

    @Override
    public Recipe getById(Long id) {
        final Recipe recipe = this.recipeRepository.findById(id).orElseThrow(notFound(id));

        return recipe;

    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
        final Recipe recipe = this.recipeCommandToRecipe.convert(recipeCommand);
        final Recipe savedRecipe = this.recipeRepository.save(recipe);

        log.debug(MessageFormat.format("Saved recipe Id {0}", savedRecipe.getId()));

        return this.recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    @Transactional
    public RecipeCommand getCommandById(Long id) {
        final RecipeCommand recipeCommand = this.recipeToRecipeCommand.convert(getById(id));
        return recipeCommand;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.recipeRepository.deleteById(id);
    }

    private Supplier<NotFoundException> notFound(Long id) {
        return () -> {
            final String message = MessageFormat.format("Recipe id {0} not found", id);
            log.error(message);
            return new NotFoundException(message);
        };
    }

}
