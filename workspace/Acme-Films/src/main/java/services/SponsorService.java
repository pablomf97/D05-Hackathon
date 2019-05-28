package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.SponsorRepository;

@Transactional
@Service
public class SponsorService {
	
	@Autowired
	private SponsorRepository sponsorRepository;
	
	public Double[] statsSponsorshipsPerSponsor(){
		return this.sponsorRepository.statsSponsorshipsPerSponsor();
	}

}
