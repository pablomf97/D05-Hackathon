package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.ModeratorRepository;

@Transactional
@Service
public class ModeratorService {

	@Autowired
	private ModeratorRepository moderatorRepository;
	
	public Double[] statsReviewsPerModerator(){
		return this.moderatorRepository.statsReviewsPerModerator();
	}
	
}
