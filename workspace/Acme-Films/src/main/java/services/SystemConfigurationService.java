package services;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.SystemConfigurationRepository;
import domain.SystemConfiguration;

@Service
@Transactional
public class SystemConfigurationService {
	
	/* Working repository */

	@Autowired
	private SystemConfigurationRepository systemConfigurationRepository;
	
	/* Other methods */

	/* Find system configuration */
	public SystemConfiguration findMySystemConfiguration() {
		final SystemConfiguration result;

		result = this.systemConfigurationRepository.findSystemConf();

		return result;
	}
	
	/* Find welcome message */
	public Map<String, String> findWelcomeMessage() {
		final Map<String, String> result;

		result = this.findMySystemConfiguration().getWelcomeMessage();

		return result;
	}
}