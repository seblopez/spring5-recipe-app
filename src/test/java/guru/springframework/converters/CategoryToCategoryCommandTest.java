package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.domain.Category;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryToCategoryCommandTest {

    private final CategoryToCategoryCommand categoryToCategoryCommand = new CategoryToCategoryCommand();

    @Test
    public void convertOk() throws Exception {
        final Long id = 234L;
        final String mexican = "Mexican";

        final Category source = Category.builder()
                .id(id)
                .description(mexican)
                .build();

        final CategoryCommand target = this.categoryToCategoryCommand.convert(source);

        assertNotNull(target);
        assertEquals(source.getId(), target.getId());
        assertEquals(source.getDescription(), target.getDescription());

    }

    @Test
    public void testNullOk() throws Exception {
        assertNull(this.categoryToCategoryCommand.convert(null));
    }

    @Test
    public void testEmptyOk() throws Exception {
        assertNotNull(this.categoryToCategoryCommand.convert(Category.builder().build()));
    }

}
