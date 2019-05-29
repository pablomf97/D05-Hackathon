package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PersonRepository;
import domain.Actor;
import domain.Person;
import domain.Position;

@Transactional
@Service
public class PersonService {
	
	// Managed repository ------------------------------------
	
	@Autowired
	private PersonRepository personRepository;
	
	// Supporting services -----------------------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private Validator validator;
	
	public Person create() {
		Actor principal;
		Person result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MODERATOR"),
				"not.allowed");

		result = new Person();
		
		result.setPositions(new ArrayList<Position>());

		return result;
	}

	public Collection<Person> findAll() {
		Collection<Person> result;
		result = this.personRepository.findAll();

		return result;
	}

	public Person findOne(final int personId) {
		Person result;
		result = this.personRepository.findOne(personId);

		return result;
	}
	
	public Person save(final Person person) {
		Actor principal;
		Person result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MODERATOR"), "not.allowed");
		
		Assert.isTrue(person.getId() == 0);
		
		Assert.notNull(person.getName());
		Assert.notNull(person.getSurname());
		Assert.notNull(person.getNationality());
		Assert.notNull(person.getBirthDate());
		Assert.notNull(person.getGender());
		
		result = this.personRepository.save(person);

		return result;
	}
	
	public void delete(final Person person) {
		Actor principal;

		Assert.notNull(person);
		Assert.isTrue(person.getId() != 0, "wrong.id");

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MODERATOR"),
				"not.allowed");

		//TODO: delete from collection in Film
		
		this.personRepository.delete(person.getId());
	}
	
	// Other business methods -------------------------------
	
}
