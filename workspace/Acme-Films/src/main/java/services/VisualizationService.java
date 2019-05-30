package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.VisualizationRepository;
import domain.Actor;
import domain.Visualization;

@Transactional
@Service
public class VisualizationService {
	
	// Managed repository ------------------------------------
	
	@Autowired
	private VisualizationRepository visualizationRepository;
	
	// Supporting services -----------------------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private Validator validator;
	
	public Visualization create() {
		Actor principal;
		Visualization result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MODERATOR"),
				"not.allowed");

		result = new Visualization();

		return result;
	}

	public Collection<Visualization> findAll() {
		Collection<Visualization> result;
		result = this.visualizationRepository.findAll();

		return result;
	}

	public Visualization findOne(final int visualizationId) {
		Visualization result;
		result = this.visualizationRepository.findOne(visualizationId);

		return result;
	}
	
	public Visualization save(final Visualization visualization) {
		Actor principal;
		Visualization result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MODERATOR"), "not.allowed");
		
		Assert.notNull(visualization.getSiteName());
		Assert.notNull(visualization.getPrice());
		Assert.notNull(visualization.getLink());
		Assert.notNull(visualization.getFilm());
		
		Assert.isTrue(this.findOne(visualization.getId()).getFilm().equals(visualization.getFilm()));
		
		result = this.visualizationRepository.save(visualization);

		return result;
	}
	
	public void delete(final Visualization visualization) {
		Actor principal;

		Assert.notNull(visualization);
		Assert.isTrue(visualization.getId() != 0, "wrong.id");

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MODERATOR"),
				"not.allowed");

		this.visualizationRepository.delete(visualization.getId());
	}
	
	// Other business methods -------------------------------
	
	public Visualization reconstruct(final Visualization visualization,
			 BindingResult binding) {
		Visualization result;
		Actor principal;
		
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MODERATOR"),
				"not.allowed");
		
		if (visualization.getId() == 0) {
			result = this.create();
		} else {
			result = this.findOne(visualization.getId());
		}
		
		result.setSiteName(visualization.getSiteName());
		result.setPrice(visualization.getPrice());
		result.setLink(visualization.getLink());

		this.validator.validate(result, binding);
		
		return result;
	}
	
}

