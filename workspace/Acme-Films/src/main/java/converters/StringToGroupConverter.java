package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.GroupRepository;
import domain.Forum;

@Component
@Transactional
public class StringToGroupConverter implements Converter<String, Forum> {

	@Autowired
	GroupRepository	actorRepository;

	@Override
	public Forum convert(final String text) {
		Forum result;

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