package domain;

import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Position extends DomainEntity{
	
	//Attributes
	
	private Map<String,String> name;
	private Position parentPosition;
	
	//Getters and setters
	
	@NotNull
	@ElementCollection
	public Map<String, String> getName() {
		return name;
	}

	public void setName(Map<String, String> name) {
		this.name = name;
	}
	
	@Valid
	@ManyToOne(optional = true)
	public Position getParentPosition() {
		return parentPosition;
	}

	public void setParentPosition(Position parentPosition) {
		this.parentPosition = parentPosition;
	}

}
