package guru.springframework.converters;

import guru.springframework.commands.NotesCommand;
import guru.springframework.domain.Notes;
import org.junit.Test;

import static org.junit.Assert.*;

public class NotesToNotesCommandTest {

    private final NotesToNotesCommand converter = new NotesToNotesCommand();

    @Test
    public void convertOk() throws Exception {
        final Notes source = Notes.builder()
                .id(756L)
                .recipeNotes("Start by doing this, then that")
                .build();

        final NotesCommand target = this.converter.convert(source);

        assertNotNull(target);
        assertEquals(source.getId(), target.getId());
        assertEquals(source.getRecipeNotes(), target.getRecipeNotes());

    }

    @Test
    public void convertNullOk() throws Exception  {
        assertNull(this.converter.convert(null));
    }

    @Test
    public void convertEmptyOk() throws Exception  {
        assertNotNull(this.converter.convert(Notes.builder().build()));
    }


}
