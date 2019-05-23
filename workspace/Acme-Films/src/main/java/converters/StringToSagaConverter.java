package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.SagaRepository;
import domain.Saga;

@Component
@Transactional
public class StringToSagaConverter implements Converter<String, Saga> {

	@Autowired

	SagaRepository actorRepository;

	@Override
	public Saga convert(final String text) {
		Saga result;

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