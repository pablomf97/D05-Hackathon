package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PositionRepository;
import domain.Actor;
import domain.Person;
import domain.Position;
import domain.SystemConfiguration;

@Service
@Transactional
public class PositionService {

	// Managed repository ------------------------------
	@Autowired
	private PositionRepository positionRepository;

	// Supporting services -----------------------

	@Autowired
	private ActorService	actorService;
	
	@Autowired
	private SystemConfigurationService systemConfigurationService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private Validator validator;

	// CRUD methods -----------------------------------

	public Position create() {
		Position result;
		Actor principal;
		
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "MODERATOR"),
				"not.allowed");

		result = new Position();
		
		result.setChildPositions(new ArrayList<Position>());

		return result;
	}
	
	public Position findOne(final int positionId) {
		Position result;

		result = this.positionRepository.findOne(positionId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Position> findAll() {
		Collection<Position> result;

		result = this.positionRepository.findAll();
		Assert.notNull(result);

		return result;
	}
	
	public Position save(final Position position) {
		Position result;
		SystemConfiguration systemConf;
		Set<String> idiomasPosition;
		Actor principal;
		
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "MODERATOR"),
				"not.allowed");
		
		//root = this.findRoot();
		//Assert.isTrue(position.getId() != root.getId());
		//Assert.notNull(position.getParentPosition());
		Assert.notNull(position.getName());
		
		systemConf = systemConfigurationService.findMySystemConfiguration();
		Set<String> idiomasSystemConf = new HashSet<String>(systemConf
				.getWelcomeMessage().keySet());
		idiomasPosition = position.getName().keySet();
		Assert.isTrue(idiomasSystemConf.equals(idiomasPosition));

		result = this.positionRepository.save(position);
		Assert.notNull(result);
		
		return result;
	}

	public void delete(final Position position) {
		Actor principal;
		
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "MODERATOR"),
				"not.allowed");
		
		Assert.notNull(position);
		Assert.isTrue(position.getId() != 0);
		
		this.positionRepository.delete(position);
	}

	// Other business methods
	
	public Position reconstruct(Position position, String nameES,
			String nameEN, BindingResult binding) {
		Position res;

		if (position.getId() == 0) {
			position.setName(new HashMap<String, String>());

			position.getName().put("Español", nameES);
			position.getName().put("English", nameEN);
			
			res = position;
		} else {
			res = this.positionRepository.findOne(position.getId());

			position.setName(new HashMap<String, String>());

			position.getName().put("Español", nameES);
			position.getName().put("English", nameEN);

			res.setName(position.getName());
			
		}
		this.validator.validate(res, binding);

		return res;
	}
	
	public boolean checkPosition (int positionId) {
		Collection<Person> persons;
		boolean canBeDeleted = true;
		
		persons = this.personService.personsWithPosition(positionId);
		
		if(persons.size() > 0) {
			canBeDeleted = false;
		}
		
		return canBeDeleted;
	}
	
	public Collection<Position> parsePositions (String [] array) {
		Collection<Position> result = new ArrayList<>();
		String a = null;
		Integer n = 0;
		Position pos = null;
		
		for (int i = 0; i <= array.length - 1; i++) {
			a = array[i];
			n = Integer.parseInt(a);
			pos = this.findOne(n);
			result.add(pos);
		}
		return result;
	}
}

