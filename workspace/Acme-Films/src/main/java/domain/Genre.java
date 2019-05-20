package domain;

import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Genre extends DomainEntity{
	
	//Attributes
	
		private Map<String,String> name;
		
		//Getters and setters
		
		@NotBlank
		public Map<String, String> getName() {
			return name;
		}

		public void setName(Map<String, String> name) {
			this.name = name;
		}
}
