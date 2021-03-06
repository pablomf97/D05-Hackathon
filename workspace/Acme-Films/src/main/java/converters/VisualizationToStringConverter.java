package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Visualization;

@Component
@Transactional
public class VisualizationToStringConverter implements Converter<Visualization, String> {
	@Override
	public String convert(final Visualization actor) {
		String result;

		if (actor == null)
			result = null;
		else
			result = String.valueOf(actor.getId());
		return result;
	}
}
