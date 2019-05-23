package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Genre;

@Component
@Transactional
public class GenreToStringConverter implements Converter<Genre, String> {
	@Override
	public String convert(final Genre actor) {
		String result;

		if (actor == null)
			result = null;
		else
			result = String.valueOf(actor.getId());
		return result;
	}
}
