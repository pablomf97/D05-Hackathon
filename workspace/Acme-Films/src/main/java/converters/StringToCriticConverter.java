package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.CriticRepository;
import domain.Critic;

public class StringToCriticConverter implements
		Converter<String, Critic> {

	@Autowired
	CriticRepository actorRepository;

	@Override
	public Critic convert(final String text) {
		Critic result;

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