package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.domain.Category;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryCommandToCategoryTest {

    private final CategoryCommandToCategory converter = new CategoryCommandToCategory();

    @Test
    public void convertOk() throws Exception {

        final Long id = 23L;
        final String peruvian = "Peruvian";
        CategoryCommand source = CategoryCommand.builder()
                .id(id)
                .description(peruvian)
                .build();

        Category target = this.converter.convert(source);

        assertNotNull(target);
        assertEquals(source.getId(), target.getId());
        assertEquals(source.getDescription(), target.getDescription());

    }

    @Test
    public void testNullObject() throws Exception  {
        assertNull(this.converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception  {
        assertNotNull(this.converter.convert(CategoryCommand.builder().build()));
    }
}
