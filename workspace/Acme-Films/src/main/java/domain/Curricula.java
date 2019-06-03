
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class Curricula extends DomainEntity {

	//Attributes
	private Collection<MiscellaneousData>	miscellaneousData;
	private Collection<EducationData>		educationData;
	private Collection<ProfessionalData>	professionalData;
	private PersonalData					personalData;


	//Getters and setters

	@Valid
	@ElementCollection
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public Collection<MiscellaneousData> getMiscellaneousData() {
		return this.miscellaneousData;
	}
	public void setMiscellaneousData(final Collection<MiscellaneousData> miscellaneousData) {
		this.miscellaneousData = miscellaneousData;
	}

	@Valid
	@ElementCollection
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public Collection<EducationData> getEducationData() {
		return this.educationData;
	}
	public void setEducationData(final Collection<EducationData> educationData) {
		this.educationData = educationData;
	}

	@Valid
	@ElementCollection
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public Collection<ProfessionalData> getProfessionalData() {
		return this.professionalData;
	}
	public void setProfessionalData(final Collection<ProfessionalData> professionalData) {
		this.professionalData = professionalData;
	}

	@Valid
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	public PersonalData getPersonalData() {
		return this.personalData;
	}
	public void setPersonalData(final PersonalData personalData) {
		this.personalData = personalData;
	}

}
