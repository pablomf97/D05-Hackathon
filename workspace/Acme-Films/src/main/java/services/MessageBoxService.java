<<<<<<< HEAD
package services;

import java.util.ArrayList;

import javax.transaction.Transactional;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MessageBoxRepository;
import domain.Actor;
import domain.Message;
import domain.MessageBox;

@Service
@Transactional
public class MessageBoxService {

	//Repository

	@Autowired
	private MessageBoxRepository messageBoxRepository;

	//Services

	@Autowired
	private ActorService actorService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private Validator validator;


	//CRUD METHODS

	public MessageBox create(){
		MessageBox result = new MessageBox();
		
		Actor principal = this.actorService.findByPrincipal();
		

		result.setIsPredefined(false);
		result.setMessages(new ArrayList<Message>());
		principal.getMessageBoxes().add(result);


		return result;
	}

	public MessageBox save(final MessageBox messageBox, final MessageBox parentBox){
		MessageBox result;
		Actor principal = this.actorService.findByPrincipal();

		Assert.isTrue(principal.getMessageBoxes().contains(messageBox) ||
				principal.getMessageBoxes().contains(parentBox));


		if(parentBox == null){

			result = this.messageBoxRepository.save(messageBox);
			
			principal.getMessageBoxes().add(result);


		}else{

			messageBox.setParentMessageBox(parentBox);

			result = this.messageBoxRepository.save(messageBox);

			parentBox.getChildMessageBoxes().add(result);
			principal.getMessageBoxes().add(result);

		}

		return result;

	}

	public void delete(final MessageBox messageBox){

		Actor principal = this.actorService.findByPrincipal();

		Assert.isTrue(principal.getMessageBoxes().contains(messageBox));

		Assert.isTrue(messageBox.getIsPredefined() == false);

		if(!(messageBox.getChildMessageBoxes().isEmpty())){
			for(MessageBox mb : messageBox.getChildMessageBoxes()){

				if(!mb.getMessages().isEmpty()){
					mb.setMessages(new ArrayList<Message>());
					for(Message m : mb.getMessages()){
						m.getMessageBoxes().remove(mb);
					}
				}

				this.delete(mb);

			}

		}

		if(messageBox.getParentMessageBox()!= null && messageBox.getParentMessageBox().getIsPredefined() == false){
			messageBox.getParentMessageBox().getChildMessageBoxes().remove(messageBox);


		}
		
		principal.getMessageBoxes().remove(messageBox);
		
		this.messageBoxRepository.delete(messageBox);
	}


	public MessageBox findOutBoxActorId(int actorId){

		MessageBox result = this.messageBoxRepository.findOutBoxActorId(actorId);

		return result;
	}

	public MessageBox findInBoxActorId(int actorId){

		MessageBox result = this.messageBoxRepository.findInBoxActorId(actorId);

		return result;
	}
	public MessageBox findSpamBoxActorId(int actorId){

		MessageBox result = this.messageBoxRepository.findSpamBoxActorId(actorId);

		return result;
	}

	public MessageBox findTrashBoxActorId(int actorId){

		MessageBox result = this.messageBoxRepository.findTrashBoxActorId(actorId);

		return result;
	}

	public MessageBox findNotificationBoxActorId(int actorId){

		MessageBox result = this.messageBoxRepository.findNotificationBoxActorId(actorId);

		return result;
	}

	//Other requirements
	public void initializeDefaultBoxes() {
		
		Actor principal = this.actorService.findByPrincipal();
		
		final MessageBox in = this.create();
		in.setIsPredefined(true);
		in.setName("In box");
		this.save(in,null);
		principal.getMessageBoxes().add(in);

		final MessageBox trash = this.create();
		trash.setIsPredefined(true);
		trash.setName("Trash box");
		this.save(trash,null);
		principal.getMessageBoxes().add(trash);

		final MessageBox out = this.create();
		out.setIsPredefined(true);
		out.setName("Out box");
		this.save(out,null);
		principal.getMessageBoxes().add(out);


		final MessageBox spam = this.create();
		spam.setIsPredefined(true);
		spam.setName("Spam box");
		this.save(spam,null);
		principal.getMessageBoxes().add(spam);


		final MessageBox notification = this.create();
		notification.setIsPredefined(true);
		notification.setName("Notification box");
		this.save(notification,null);
		principal.getMessageBoxes().add(notification);


	}

}
