package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class Critic extends Actor{

	//Attributes
	
	private Curricula curricula;

	//Getters and setters
	
	@Valid
	@OneToOne(optional = true)
	public Curricula getCurricula() {
		return curricula;
	}

	public void setCurricula(Curricula curricula) {
		this.curricula = curricula;
	}
	
	
	
}
