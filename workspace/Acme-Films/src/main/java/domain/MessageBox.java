package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class MessageBox extends DomainEntity{

	//Attributes
	private String name;
	private boolean isPredefined;
	private Collection<Message> messages;
	private Collection<MessageBox> childMessageBoxes;
	private MessageBox parentMessageBox;
	
	//Getters and setters
	
	@NotBlank
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean getIsPredefined() {
		return isPredefined;
	}
	public void setIsPredefined(boolean isPredefined) {
		this.isPredefined = isPredefined;
	}
	
	@Valid
	@ManyToMany
	public Collection<Message> getMessages() {
		return messages;
	}
	public void setMessages(Collection<Message> messages) {
		this.messages = messages;
	}
	
	@Valid
	@ManyToMany
	public Collection<MessageBox> getChildMessageBoxes() {
		return childMessageBoxes;
	}
	public void setChildMessageBoxes(Collection<MessageBox> childMessageBoxes) {
		this.childMessageBoxes = childMessageBoxes;
	}
	
	@Valid
	@ManyToOne(optional = true)
	public MessageBox getParentMessageBox() {
		return parentMessageBox;
	}
	public void setParentMessageBox(MessageBox parentMessageBox) {
		this.parentMessageBox = parentMessageBox;
	}
	
	
	
	
	
}
