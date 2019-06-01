
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CriticRepository;
import repositories.PersonalDataRepository;
import domain.Actor;
import domain.Critic;
import domain.Curricula;
import domain.PersonalData;

@Transactional
@Service
public class PersonalDataService {

	//Repository
	@Autowired
	private PersonalDataRepository	personalDataRepository;

	//Services
	@Autowired
	private ActorService			actorService;

	@Autowired
	private CurriculaService		curriculaService;

	@Autowired
	private CriticRepository		criticRepository;


	//Create
	public PersonalData create() {
		Critic principal;
		PersonalData result;

		principal = (Critic) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "CRITIC"));

		result = new PersonalData();

		return result;
	}
	//Save
	public PersonalData save(final PersonalData data, final int curriculaId) {
		Critic principal;
		PersonalData dataDB = new PersonalData();
		Curricula principalCurriculas;
		Curricula currentCurricula;
		PersonalData result;

		principal = (Critic) this.actorService.findByPrincipal();

		principalCurriculas = principal.getCurricula();

		if (data.getId() != 0) {
			currentCurricula = this.curriculaService.findOne(curriculaId);

			Assert.isTrue(currentCurricula.getPersonalData().getId() == data.getId());
			Assert.isTrue(principalCurriculas.equals(currentCurricula));
			Assert.isTrue(this.criticRepository.findCriticByCurriculaId(currentCurricula.getId()).getId() == principal.getId());

			dataDB = this.personalDataRepository.findOne(data.getId());

			dataDB.setLinkedIn(data.getLinkedIn());
			dataDB.setFullName(data.getFullName());
			dataDB.setStatement(data.getStatement());
			dataDB.setPhoneNumber(data.getPhoneNumber());

			result = this.personalDataRepository.save(dataDB);

		} else {
			Assert.notNull(data.getLinkedIn());
			Assert.notNull(data.getFullName());
			Assert.notNull(data.getStatement());

			currentCurricula = this.curriculaService.findOne(curriculaId);

			Assert.isTrue(principalCurriculas.equals(currentCurricula));
			Assert.isTrue(this.criticRepository.findCriticByCurriculaId(currentCurricula.getId()).getId() == principal.getId());

			result = this.personalDataRepository.save(data);

			currentCurricula.setPersonalData(data);
		}

		return result;

	}

	//Finds
	public PersonalData findOne(final int dataId) {
		final PersonalData result = this.personalDataRepository.findOne(dataId);

		return result;
	}

	public Collection<PersonalData> findAll() {
		final Collection<PersonalData> result = this.personalDataRepository.findAll();

		return result;
	}

	public void delete(final int personalDataId) {

		this.personalDataRepository.delete(personalDataId);

	}

	public void flush() {

		this.personalDataRepository.flush();
	}
	public PersonalData saveNewCurricula(final PersonalData personalData) {
		Actor principal;
		PersonalData result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "CRITIC"));
		Assert.isTrue(personalData.getId() == 0);
		Assert.notNull(personalData.getLinkedIn());
		Assert.notNull(personalData.getFullName());
		Assert.notNull(personalData.getStatement());

		result = this.personalDataRepository.save(personalData);

		return result;
	}

	public void checkOwnerPersonalData(final Integer id) {
		final Actor principal = this.actorService.findByPrincipal();
		final Curricula c = this.curriculaService.getCurriculaByPersonalData(id);
		Assert.isTrue(this.criticRepository.findCriticByCurriculaId(c.getId()).equals(principal));
	}
}
