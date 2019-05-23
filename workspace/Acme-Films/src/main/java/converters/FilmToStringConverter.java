package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Film;

@Component
@Transactional
public class FilmToStringConverter implements Converter<Film, String> {
	@Override
	public String convert(final Film actor) {
		String result;

		if (actor == null)
			result = null;
		else
			result = String.valueOf(actor.getId());
		return result;
	}
}
