package guru.springframework.converters;

import guru.springframework.commands.NotesCommand;
import guru.springframework.domain.Notes;
import org.junit.Test;

import static org.junit.Assert.*;

public class NotesCommandToNotesTest {

    private final NotesCommandToNotes converter = new NotesCommandToNotes();

    @Test
    public void convertOk() throws Exception {
        final NotesCommand source = NotesCommand.builder()
                .id(756L)
                .recipeNotes("Start by doing this, then that")
                .build();

        final Notes target = this.converter.convert(source);

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
        assertNotNull(this.converter.convert(NotesCommand.builder().build()));
    }

}
