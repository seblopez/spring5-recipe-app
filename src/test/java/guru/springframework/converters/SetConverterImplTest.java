package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.domain.Category;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SetConverterImplTest {

    @Test
    public void convertOk() {

        final SetConverter<Category, CategoryCommand> converter = new SetConverterImpl<>();

        final Set<Category> categories = new HashSet<>();
        categories.add(Category.builder().id(353L).description("Mexican").build());
        categories.add(Category.builder().id(345L).description("Italian").build());
        categories.add(Category.builder().id(366L).description("American").build());

        final Set<CategoryCommand> target = converter.convert(categories, new CategoryToCategoryCommand());

        assertNotNull(target);
        assertEquals(3, target.size());

    }

    @Test
    public void convertNullOkReturningEmpySet() {
        SetConverter<Category, CategoryCommand> converter = new SetConverterImpl<>();
        final Set<CategoryCommand> categoryCommands = converter.convert(null, new CategoryToCategoryCommand());
        assertNotNull(categoryCommands);
        assertEquals(0, categoryCommands.size());
    }

    @Test
    public void convertEmpySetOkReturningEmpySet() {
        SetConverter<Category, CategoryCommand> converter = new SetConverterImpl<>();
        final Set<CategoryCommand> categoryCommands = converter.convert(null, new CategoryToCategoryCommand());
        assertNotNull(categoryCommands);
        assertEquals(0, categoryCommands.size());
    }

}
