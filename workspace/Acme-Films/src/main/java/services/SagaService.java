package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SagaRepository;
import domain.Actor;
import domain.Saga;

@Transactional
@Service
public class SagaService {
	
	// Managed repository ------------------------------------
	
	@Autowired
	private SagaRepository sagaRepository;
	
	// Supporting services -----------------------------------
	
	@Autowired
	private ActorService actorService;
	
	public Saga create() {
		Actor principal;
		Saga result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MODERATOR"),
				"not.allowed");

		result = new Saga();

		return result;
	}

	public Collection<Saga> findAll() {
		Collection<Saga> result;
		result = this.sagaRepository.findAll();

		return result;
	}

	public Saga findOne(final int sagaId) {
		Saga result;
		result = this.sagaRepository.findOne(sagaId);

		return result;
	}
	
	public Saga save(final Saga saga) {
		Actor principal;
		Saga result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MODERATOR"), "not.allowed");
		
		Assert.notNull(saga.getTitle());
		
		result = this.sagaRepository.save(saga);

		return result;
	}
	
	public void delete(final Saga saga) {
		Actor principal;

		Assert.notNull(saga);
		Assert.isTrue(saga.getId() != 0, "wrong.id");

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MODERATOR"),
				"not.allowed");

		this.sagaRepository.delete(saga.getId());
	}
	
	// Other business methods -------------------------------
	
	
}

