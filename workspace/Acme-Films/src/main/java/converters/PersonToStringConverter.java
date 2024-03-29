package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Person;

@Component
@Transactional
public class PersonToStringConverter implements Converter<Person, String> {
	@Override
	public String convert(final Person actor) {
		String result;

		if (actor == null)
			result = null;
		else
			result = String.valueOf(actor.getId());
		return result;
	}
}
