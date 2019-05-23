package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Forum;

@Component
@Transactional
public class GroupToStringConverter implements Converter<Forum, String> {
	@Override
	public String convert(final Forum actor) {
		String result;

		if (actor == null)
			result = null;
		else
			result = String.valueOf(actor.getId());
		return result;
	}
}
