package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.FilmEnthusiast;

@Component
@Transactional
public class FilmEnthusiastToStringConverter implements Converter<FilmEnthusiast, String> {
	@Override
	public String convert(final FilmEnthusiast actor) {
		String result;

		if (actor == null)
			result = null;
		else
			result = String.valueOf(actor.getId());
		return result;
	}
}
