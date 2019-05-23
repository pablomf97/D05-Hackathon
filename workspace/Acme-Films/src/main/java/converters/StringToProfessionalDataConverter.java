package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.ProfessionalDataRepository;
import domain.ProfessionalData;

@Component
@Transactional
public class StringToProfessionalDataConverter implements Converter<String, ProfessionalData> {

	@Autowired
	ProfessionalDataRepository	actorRepository;

	@Override
	public ProfessionalData convert(final String text) {
		ProfessionalData result;

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