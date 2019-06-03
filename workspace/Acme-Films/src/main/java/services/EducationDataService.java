
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CriticRepository;
import repositories.EducationDataRepository;
import domain.Actor;
import domain.Critic;
import domain.Curricula;
import domain.EducationData;

@Transactional
@Service
public class EducationDataService {

	//Repository

	@Autowired
	private EducationDataRepository	educationDataRepository;

	//Services

	@Autowired
	private ActorService			actorService;

	@Autowired
	private CurriculaService		curriculaService;

	@Autowired
	private CriticRepository		criticRepository;


	//Create

	public EducationData create() {
		Critic principal;
		EducationData result;

		principal = (Critic) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "CRITIC"));

		result = new EducationData();

		return result;
	}

	//Save
	public EducationData save(final EducationData data, final int curriculaId) {
		Critic principal;
		EducationData dataDB = new EducationData();
		Curricula principalCurriculas;
		Curricula currentCurricula;
		EducationData result;

		principal = (Critic) this.actorService.findByPrincipal();

		principalCurriculas = principal.getCurricula();

		//Checking creating a data in a own curricula

		if (data.getId() != 0) {

			currentCurricula = this.curriculaService.findOne(curriculaId);

			Assert.isTrue(currentCurricula.getEducationData().contains(data));
			Assert.isTrue(principalCurriculas.equals(currentCurricula));
			Assert.isTrue(this.criticRepository.findCriticByCurriculaId(currentCurricula.getId()).getId() == principal.getId());

			dataDB = this.educationDataRepository.findOne(data.getId());

			dataDB.setDegree(data.getDegree());
			dataDB.setInstitution(data.getInstitution());
			dataDB.setMark(data.getMark());
			dataDB.setStartDate(data.getStartDate());

			result = this.educationDataRepository.save(dataDB);

		} else {
			Assert.notNull(data.getDegree());
			Assert.notNull(data.getInstitution());
			Assert.notNull(data.getStartDate());

			if (!(data.getEndDate() == null))
				Assert.isTrue(data.getStartDate().before(data.getEndDate()));

			currentCurricula = this.curriculaService.findOne(curriculaId);

			Assert.isTrue(principalCurriculas.equals(currentCurricula));
			Assert.isTrue(this.criticRepository.findCriticByCurriculaId(currentCurricula.getId()).getId() == principal.getId());

			result = this.educationDataRepository.save(data);

			currentCurricula.getEducationData().add(data);
		}

		return result;

	}

	//Delete
	public void delete(final EducationData data) {
		Critic principal;
		Curricula principalCurriculas;
		Curricula currentCurricula;
		EducationData db = new EducationData();

		db = this.educationDataRepository.findOne(data.getId());

		principal = (Critic) this.actorService.findByPrincipal();

		principalCurriculas = principal.getCurricula();

		currentCurricula = this.curriculaService.getCurriculaByEducationData(data.getId());

		Assert.isTrue(this.criticRepository.findCriticByCurriculaId(currentCurricula.getId()).getId() == principal.getId());
		Assert.isTrue(principalCurriculas.equals(currentCurricula));
		Assert.isTrue(currentCurricula.getEducationData().contains(db));

		Assert.notNull(data.getDegree());
		Assert.notNull(data.getInstitution());

		currentCurricula.getEducationData().remove(db);

		this.educationDataRepository.delete(db);
	}

	//Finds
	public EducationData findOne(final int dataId) {
		final EducationData result = this.educationDataRepository.findOne(dataId);

		return result;
	}

	public Collection<EducationData> findAll() {
		final Collection<EducationData> result = this.educationDataRepository.findAll();

		return result;
	}

	public void flush() {
		this.educationDataRepository.flush();
	}

	public void deleteEDCritic(final EducationData ed) {
		this.educationDataRepository.delete(ed);

	}
	public void checkOwnerEducationData(final Integer id) {
		final Actor principal = this.actorService.findByPrincipal();
		final Curricula c = this.curriculaService.getCurriculaByEducationData(id);
		Assert.isTrue(this.criticRepository.findCriticByCurriculaId(c.getId()).equals(principal));
	}

}
