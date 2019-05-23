package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Moderator;

@Component
@Transactional
public class ModeratorToStringConverter implements Converter<Moderator, String> {
	@Override
	public String convert(final Moderator actor) {
		String result;

		if (actor == null)
			result = null;
		else
			result = String.valueOf(actor.getId());
		return result;
	}
}
