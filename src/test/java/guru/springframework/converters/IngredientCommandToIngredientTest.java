package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class IngredientCommandToIngredientTest {
    private final UnitOfMeasureCommandToUnitOfMeasure uomConverter = new UnitOfMeasureCommandToUnitOfMeasure();
    private final IngredientCommandToIngredient converter = new IngredientCommandToIngredient(uomConverter);

    @Test
    public void convertOk() throws Exception {
        UnitOfMeasureCommand uomCmd = UnitOfMeasureCommand.builder()
                .id(324L)
                .description("Kilo")
                .build();

        final IngredientCommand source = IngredientCommand.builder()
                .id(2154L)
                .description("Flour")
                .amount(BigDecimal.ONE)
                .unitOfMeasure(uomCmd)
                .build();

        final Ingredient target = this.converter.convert(source);

        assertNotNull(target);
        assertEquals(source.getId(), target.getId());
        assertEquals(source.getAmount(), target.getAmount());
        assertEquals(source.getDescription(), target.getDescription());
        assertEquals(source.getUnitOfMeasure().getId(), target.getUnitOfMeasure().getId());
        assertEquals(source.getUnitOfMeasure().getDescription(), target.getUnitOfMeasure().getDescription());

    }

    @Test
    public void convertNullOk() throws Exception  {
        assertNull(this.converter.convert(null));
    }

    @Test
    public void convertEmptyOk() throws Exception  {
        assertNotNull(this.converter.convert(IngredientCommand.builder().build()));
    }

}
