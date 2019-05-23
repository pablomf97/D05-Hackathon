package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.ModeratorRepository;
import domain.Moderator;

@Component
@Transactional
public class StringToModeratorConverter implements Converter<String, Moderator> {

	@Autowired

	ModeratorRepository actorRepository;

	@Override
	public Moderator convert(final String text) {
		Moderator result;

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