package guru.springframework.converters;

import org.springframework.core.convert.converter.Converter;

import java.util.Set;

public interface SetConverter<S, T> {

    Set<T> convert(Set<S> source, Set<T> target, Converter<S, T> converter);

}
