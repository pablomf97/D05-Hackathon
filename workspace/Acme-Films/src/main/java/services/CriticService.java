package services;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.Critic;

import repositories.CriticRepository;

@Transactional
@Service
public class CriticService {
	@Autowired
	private CriticRepository criticRepository;
	
	
	public Collection<Critic> top3CriticsMoreProfessional(){
		List<Critic> l =(List<Critic> )this.criticRepository.top3CriticsMoreProfessional();
		return l.subList(0, 3);
	}
	public Collection<Critic> criticsWithHighestRatingReview(){
		return this.criticRepository.criticsWithHighestRatingReview();
	}
	
}
