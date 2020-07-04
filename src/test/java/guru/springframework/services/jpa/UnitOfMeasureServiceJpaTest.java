package guru.springframework.services.jpa;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import guru.springframework.services.UnitOfMeasureService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UnitOfMeasureServiceJpaTest {

    UnitOfMeasureService unitOfMeasureService;

    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.unitOfMeasureService = new UnitOfMeasureServiceJpa(this.unitOfMeasureRepository, this.unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    public void listAllUomsOk() {
        // when
        Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();
        unitOfMeasures.add(UnitOfMeasure.builder()
                .id(2334L)
                .description("Each")
                .build());

        unitOfMeasures.add(UnitOfMeasure.builder()
                .id(343L)
                .description("Pound")
                .build());

        unitOfMeasures.add(UnitOfMeasure.builder()
                .id(454L)
                .description("Dozen")
                .build());

        unitOfMeasures.add(UnitOfMeasure.builder()
                .id(312L)
                .description("Pinch")
                .build());

        when(this.unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasures);

        final Set<UnitOfMeasureCommand> unitOfMeasureCommands = this.unitOfMeasureService.listAllUoms();

        assertNotNull(unitOfMeasureCommands);
        assertEquals(4, unitOfMeasureCommands.size());
        verify(this.unitOfMeasureRepository).findAll();

    }
}
