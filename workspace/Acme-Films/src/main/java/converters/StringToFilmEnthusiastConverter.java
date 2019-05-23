package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.FilmEnthusiastRepository;
import domain.FilmEnthusiast;

public class StringToFilmEnthusiastConverter implements
		Converter<String, FilmEnthusiast> {

	@Autowired
	FilmEnthusiastRepository actorRepository;

	@Override
	public FilmEnthusiast convert(final String text) {
		FilmEnthusiast result;

		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.actorRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}