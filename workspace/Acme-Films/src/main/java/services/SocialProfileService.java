package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.SocialProfileRepository;

@Transactional
@Service
public class SocialProfileService {
	
	
	@Autowired
	private SocialProfileRepository profileRepository;
	
	
	public Integer socialProfilesInSystem(){
		return this.profileRepository.socialProfilesInSystem();
	}
	
}
