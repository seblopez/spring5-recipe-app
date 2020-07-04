package guru.springframework.converters;

import org.springframework.core.convert.converter.Converter;

import java.util.HashSet;
import java.util.Set;

public class SetConverterImpl<S, T> implements SetConverter<S, T> {
    @Override
    public Set<T> convert(Set<S> source, Converter<S, T> converter) {
        Set<T> target = new HashSet<>();
        if(source != null && source.size() > 0) {
            source.forEach(s -> target.add(converter.convert(s)));
        }
        return target;
    }
}
