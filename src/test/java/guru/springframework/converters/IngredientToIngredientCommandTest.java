package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.UnitOfMeasure;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class IngredientToIngredientCommandTest {

    private final UnitOfMeasureToUnitOfMeasureCommand uomConverter = new UnitOfMeasureToUnitOfMeasureCommand();
    private final IngredientToIngredientCommand converter = new IngredientToIngredientCommand(uomConverter);

    @Test
    public void convertOk() throws Exception {
        UnitOfMeasure uom = UnitOfMeasure.builder()
                .id(324L)
                .description("Grams")
                .build();

        final Ingredient source = Ingredient.builder()
                .id(265L)
                .amount(BigDecimal.valueOf(500))
                .unitOfMeasure(uom)
                .description("Chocolate")
                .build();

        final IngredientCommand target = this.converter.convert(source);

        assertNotNull(target);
        assertEquals(source.getId(), target.getId());
        assertEquals(source.getAmount(), target.getAmount());
        assertEquals(source.getDescription(), target.getDescription());
        assertEquals(source.getUnitOfMeasure().getId(), target.getUnitOfMeasure().getId());
        assertEquals(source.getUnitOfMeasure().getDescription(), target.getUnitOfMeasure().getDescription());
    }

    @Test
    public void convertNullOk() throws Exception {
        assertNull(this.converter.convert(null));
    }

    @Test
    public void convertEmptyOk() throws Exception  {
        assertNotNull(this.converter.convert(Ingredient.builder().build()));
    }

}
