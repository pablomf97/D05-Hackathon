package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.GenreRepository;
import domain.Genre;

@Component
@Transactional
public class StringToGenreConverter implements Converter<String, Genre> {

	@Autowired
	GenreRepository	actorRepository;

	@Override
	public Genre convert(final String text) {
		Genre result;

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