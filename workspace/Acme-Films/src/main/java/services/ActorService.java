package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import domain.Actor;

import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;

@Transactional
@Service
public class ActorService {

	@Autowired
	private ActorRepository actorRepository;

	/**
	 * Get all actors from database
	 * 
	 * @return Collection<Actor>
	 */
	public Collection<Actor> findAll() {
		Collection<Actor> result = null;
		result = this.actorRepository.findAll();
		return result;
	}

	/**
	 * Find an actor by id in the database
	 * 
	 * @param actorId
	 * @return actor
	 */
	public Actor findOne(final int actorId) {
		final Actor result = this.actorRepository.findOne(actorId);
		return result;
	}

	/**
	 * Find the logged actor
	 * 
	 * @return actor
	 */
	
	/**
	 * Find the logged actor
	 * 
	 * @return actor
	 */

	public Actor findByPrincipal() {
		Actor result = null;
		final UserAccount userAccount = LoginService.getPrincipal();
		result = this.actorRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		return result;
	}

	/**
	 * Check the authority of the actor
	 * 
	 * @param actor
	 * @param authority
	 * @return boolean
	 */
	public boolean checkAuthority(final Actor actor, final String authority) {
		Assert.notNull(actor);
		boolean result = false;
		if (actor.getUserAccount().getAuthorities().iterator().next()
				.getAuthority().equals(authority))
			result = true;
		return result;
	}
}
