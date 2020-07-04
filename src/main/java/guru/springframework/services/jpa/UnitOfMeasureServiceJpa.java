package guru.springframework.services.jpa;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.SetConverter;
import guru.springframework.converters.SetConverterImpl;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import guru.springframework.services.UnitOfMeasureService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class UnitOfMeasureServiceJpa implements UnitOfMeasureService {

    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    @Override
    public Set<UnitOfMeasureCommand> listAllUoms() {
        final Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();
        this.unitOfMeasureRepository.findAll().forEach(unitOfMeasure -> unitOfMeasures.add(unitOfMeasure));

        SetConverter<UnitOfMeasure, UnitOfMeasureCommand> uomConverter = new SetConverterImpl<>();
        final Set<UnitOfMeasureCommand> unitOfMeasureCommands = uomConverter.convert(unitOfMeasures, this.unitOfMeasureToUnitOfMeasureCommand);
        return unitOfMeasureCommands;
    }
}
