package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.ProfessionalData;

@Component
@Transactional
public class ProfessionalDataToStringConverter implements Converter<ProfessionalData, String> {
	@Override
	public String convert(final ProfessionalData actor) {
		String result;

		if (actor == null)
			result = null;
		else
			result = String.valueOf(actor.getId());
		return result;
	}
}
