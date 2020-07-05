package guru.springframework.controllers;

import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;

@Slf4j
@AllArgsConstructor
@Controller
public class ImageController {

    private final ImageService imageService;
    private final RecipeService recipeService;

    @GetMapping("recipe/{recipeId}/image")
    public String showUploadForm(@PathVariable String recipeId, Model model){
        model.addAttribute("recipe", recipeService.getCommandById(Long.valueOf(recipeId)));

        return "recipe/imageuploadform";
    }

    @PostMapping("recipe/{recipeId}/image")
    public String handleImagePost(@PathVariable String recipeId, @RequestParam("file") MultipartFile file) {
        imageService.saveImageFile(Long.valueOf(Long.valueOf(recipeId)), file);
        return MessageFormat.format("redirect:/recipe/{0}/show", recipeId);
    }

}
