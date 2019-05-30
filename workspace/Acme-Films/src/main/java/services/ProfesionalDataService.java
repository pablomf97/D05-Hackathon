
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CriticRepository;
import repositories.ProfessionalDataRepository;
import domain.Actor;
import domain.Critic;
import domain.Curricula;
import domain.ProfessionalData;

@Transactional
@Service
public class ProfesionalDataService {

	//Repository
	@Autowired
	private ProfessionalDataRepository	professionalDataRepository;

	//Services
	@Autowired
	private ActorService				actorService;

	@Autowired
	private CurriculaService			curriculaService;

	@Autowired
	private CriticRepository			criticRepository;


	//Create
	public ProfessionalData create() {
		Critic principal;
		ProfessionalData result;

		principal = (Critic) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "CRITIC"));

		result = new ProfessionalData();

		return result;
	}

	//Save
	public ProfessionalData save(final ProfessionalData data, final int curriculaId) {
		Critic principal;
		ProfessionalData dataDB = new ProfessionalData();
		Curricula principalCurriculas;
		Curricula currentCurricula;
		ProfessionalData result;

		principal = (Critic) this.actorService.findByPrincipal();

		principalCurriculas = principal.getCurricula();

		if (data.getId() != 0) {

			currentCurricula = this.curriculaService.findOne(curriculaId);

			Assert.isTrue(currentCurricula.getProfessionalData().contains(data));
			Assert.isTrue(principalCurriculas.equals(currentCurricula));
			Assert.isTrue(this.criticRepository.findCriticByCurriculaId(currentCurricula.getId()).getId() == principal.getId());

			dataDB = this.professionalDataRepository.findOne(data.getId());

			dataDB.setTitle(data.getTitle());
			dataDB.setDescription(data.getDescription());
			dataDB.setStartDate(data.getStartDate());
			dataDB.setEndDate(data.getEndDate());

			result = this.professionalDataRepository.save(dataDB);

		} else {
			Assert.notNull(data.getTitle());
			Assert.notNull(data.getDescription());
			Assert.notNull(data.getStartDate());

			if (!(data.getEndDate() == null))
				Assert.isTrue(data.getStartDate().before(data.getEndDate()), "Start date must be before then end date");

			currentCurricula = this.curriculaService.findOne(curriculaId);

			Assert.isTrue(principalCurriculas.equals(currentCurricula));
			Assert.isTrue(this.criticRepository.findCriticByCurriculaId(currentCurricula.getId()).getId() == principal.getId());

			result = this.professionalDataRepository.save(data);

			currentCurricula.getProfessionalData().add(data);
		}

		return result;

	}

	//Delete
	public void delete(final ProfessionalData data) {
		Critic principal;
		Curricula principalCurriculas;
		Curricula currentCurricula;
		ProfessionalData db = new ProfessionalData();

		db = this.professionalDataRepository.findOne(data.getId());

		principal = (Critic) this.actorService.findByPrincipal();

		principalCurriculas = principal.getCurricula();

		currentCurricula = this.curriculaService.getCurriculaByProfessionalData(data.getId());

		Assert.isTrue(this.criticRepository.findCriticByCurriculaId(currentCurricula.getId()).getId() == principal.getId());
		Assert.isTrue(principalCurriculas.equals(currentCurricula));
		Assert.isTrue(currentCurricula.getProfessionalData().contains(db));

		currentCurricula.getProfessionalData().remove(db);

		this.professionalDataRepository.delete(db);
	}

	//Finds
	public ProfessionalData findOne(final int dataId) {
		final ProfessionalData result = this.professionalDataRepository.findOne(dataId);

		return result;
	}

	public Collection<ProfessionalData> findAll() {
		final Collection<ProfessionalData> result = this.professionalDataRepository.findAll();

		return result;
	}

	public void flush() {
		this.professionalDataRepository.flush();
	}

	public void deletePosCritic(final ProfessionalData pd) {
		this.professionalDataRepository.delete(pd);
	}
	public ProfessionalData createCopy() {
		Actor principal;
		ProfessionalData result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "CRITIC"));

		result = new ProfessionalData();

		return result;
	}

	public void checkOwnerProfessionalData(final Integer id) {
		final Actor principal = this.actorService.findByPrincipal();
		final Curricula c = this.curriculaService.getCurriculaByProfessionalData(id);
		Assert.isTrue(this.criticRepository.findCriticByCurriculaId(c.getId()).equals(principal));
	}
}
