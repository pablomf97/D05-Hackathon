package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.MessageBoxRepository;
import domain.MessageBox;

@Service
@Transactional
public class MessageBoxService {

	//Repository

	@Autowired
	private MessageBoxRepository messageBoxRepository;

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

}
