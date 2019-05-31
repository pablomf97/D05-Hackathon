
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CriticRepository;
import repositories.CurriculaRepository;
import domain.Critic;
import domain.Curricula;
import domain.EducationData;
import domain.MiscellaneousData;
import domain.PersonalData;
import domain.ProfessionalData;

@Transactional
@Service
public class CurriculaService {

	//Repository

	@Autowired
	private CurriculaRepository			curriculaRepository;

	//Services

	@Autowired
	private ActorService				actorService;

	@Autowired
	private PersonalDataService			personalDataService;

	@Autowired
	private MiscellaneousDataService	miscellaneousDataService;

	@Autowired
	private ProfesionalDataService		professionalDataService;

	@Autowired
	private EducationDataService		educationDataService;

	@Autowired
	private CriticRepository			criticRepository;


	//Create
	public Curricula create() {
		Curricula result;
		Critic principal;

		principal = (Critic) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "CRITIC"));

		result = new Curricula();
		result.setEducationData(new ArrayList<EducationData>());
		result.setProfessionalData(new ArrayList<ProfessionalData>());
		result.setPersonalData(this.personalDataService.create());
		return result;
	}

	//Save
	public Curricula save(final Curricula curricula) {
		Curricula result, aux = null;
		Critic principal;

		//Checking curricula owner
		principal = (Critic) this.actorService.findByPrincipal();
		Assert.isTrue(this.criticRepository.findCriticByCurriculaId(curricula.getId()).getId() == principal.getId());

		//Checking persistence
		if (curricula.getId() == 0)
			Assert.notNull(curricula.getPersonalData());
		else {
			Assert.notNull(curricula.getPersonalData());
			Assert.notEmpty(curricula.getEducationData());
			Assert.notEmpty(curricula.getProfessionalData());
			aux = this.findOne(curricula.getId());

		}

		result = this.curriculaRepository.save(curricula);
		principal.setCurricula(result);

		if (aux != null)
			this.personalDataService.delete(aux.getPersonalData().getId());

		return result;
	}
	public Curricula saveNewCurricula(final PersonalData d) {
		final Curricula c = this.create();
		c.setPersonalData(d);
		final Critic actor = (Critic) this.actorService.findByPrincipal();
		Assert.isNull(actor.getCurricula());
		final Curricula cur = this.curriculaRepository.save(c);
		return cur;
	}

	//Finds
	public Curricula findOne(final int curriculaId) {

		final Curricula result = this.curriculaRepository.findOne(curriculaId);

		return result;
	}

	public Collection<Curricula> findAll() {

		final Collection<Curricula> result = this.curriculaRepository.findAll();

		return result;
	}

	public Curricula getCurriculaByMiscellaneousData(final int dataId) {
		final Curricula result = this.curriculaRepository.getCurriculaByMiscellaneousData(dataId);

		return result;
	}

	public Curricula getCurriculaByEducationData(final int dataId) {
		final Curricula result = this.curriculaRepository.getCurriculaByEducationData(dataId);

		return result;
	}

	public Curricula getCurriculaByProfessionalData(final int dataId) {
		final Curricula result = this.curriculaRepository.getCurriculaByProfessionalData(dataId);

		return result;
	}

	public Curricula getCurriculaByPersonalData(final int dataId) {
		final Curricula result = this.curriculaRepository.getCurriculaByPersonalData(dataId);

		return result;
	}

	public void deleteCV(final Curricula curricula) {
		Critic principal;
		principal = (Critic) this.actorService.findByPrincipal();
		Assert.isTrue(principal.getCurricula().equals(curricula));
		final Curricula cv = curricula;
		this.personalDataService.delete(cv.getPersonalData().getId());
		for (final MiscellaneousData md : cv.getMiscellaneousData())
			this.miscellaneousDataService.delete(md);
		for (final EducationData ed : cv.getEducationData())
			this.educationDataService.deleteEDCritic(ed);
		for (final ProfessionalData pd : cv.getProfessionalData())
			this.professionalDataService.deletePosCritic(pd);
		this.curriculaRepository.delete(cv);

	}

	public void flush() {
		this.curriculaRepository.flush();
	}

}
