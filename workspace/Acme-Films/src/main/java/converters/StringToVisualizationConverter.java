package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.VisualizationRepository;
import domain.Visualization;

@Component
@Transactional
public class StringToVisualizationConverter implements Converter<String, Visualization> {

	@Autowired

	VisualizationRepository actorRepository;

	@Override
	public Visualization convert(final String text) {
		Visualization result;

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