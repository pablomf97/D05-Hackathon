package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Saga;

@Component
@Transactional
public class SagaToStringConverter implements Converter<Saga, String> {
	@Override
	public String convert(final Saga actor) {
		String result;

		if (actor == null)
			result = null;
		else
			result = String.valueOf(actor.getId());
		return result;
	}
}
