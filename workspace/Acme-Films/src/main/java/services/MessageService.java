<<<<<<< HEAD
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import domain.Actor;
import domain.Message;
import domain.MessageBox;

@Service
@Transactional
public class MessageService {

	//Repository

	@Autowired
	private MessageRepository messageRepository;

	//Services

	@Autowired
	private ActorService actorService;

	@Autowired
	private Validator validator;

	@Autowired
	private MessageBoxService messageBoxService;

	//CRUD METHODS

	public Message create(){
		Message result;
		Actor principal = this.actorService.findByPrincipal;

		result.setSendMoment(new Date(System.currentTimeMillis()-1));
		result.setSender(principal);
		result.getMessageBoxes().add(this.messageBoxService.findOutBoxActorId(principal.getId()));
		result.setIsFilmCreated(false);
		result.setIsSpam(false);

		return result;
	}

	public Message save(final Message message){
		Message result;
		Actor principal = this.actorService.findByPrincipal();
		MessageBox inSpamBox;

		//Checking sender is the principal
		Assert.isTrue(message.getSender().getId() == principal.getId(), "Not your message");

		//Checking receiver is an actor from the system
		Assert.isTrue(this.actorService.checkAuthority(message.getReceiver(),"CRITIC") ||
				this.actorService.checkAuthority(message.getReceiver(),"MODERATOR")||
				this.actorService.checkAuthority(message.getReceiver(),"ADMIN"),
				this.actorService.checkAuthority(message.getReceiver(),"FILMENTHUSIAST") );

		//Checking message has got right boxes(inBox from sender, outBox or spamBox from receiver)
		Assert.isTrue((message.getMessageBoxes().contains(this.messageBoxService.findOutBoxActorId(principal.getId()))&&
				message.getMessageBoxes().contains(this.messageBoxService.findInBoxActorId(message.getReceiver().getId())))||
				(message.getMessageBoxes().contains(this.messageBoxService.findOutBoxActorId(principal.getId()))&&
						message.getMessageBoxes().contains(this.messageBoxService.findSpamBoxActorId(message.getReceiver().getId()))));

		//Checking spam
		boolean containsSpam = false;
		final String[] spamWords = this.systemConfigurationService.findMySystemConfiguration().getSpamWords().split(",");
		final String[] subject = message.getSubject().split("(¿¡,.-_/!?) ");
		for (final String word : spamWords) {
			for (final String titleWord : subject)
				if (titleWord.toLowerCase().contains(word.toLowerCase())) {
					containsSpam = true;
					break;
				}
			if (containsSpam) {
				message.setIsSpam(true);
				message.setTag("spam");
				principal.setIsSpammer(true);
				break;
			}
		}
		if (!containsSpam) {
			final String[] body = message.getBody().split("(¿¡,.-_/!?) ");
			for (final String word : spamWords) {
				for (final String titleWord : body)
					if (titleWord.toLowerCase().contains(word.toLowerCase())) {
						containsSpam = true;
						break;
					}
				if (containsSpam) {
					message.setIsSpam(true);
					message.setTag("spam");
					principal.setIsSpammer(true);
					break;
				}
			}

			//Update some values like send moment

			message.setSendMoment(new Date(System.currentTimeMillis()-1));

			//Checking spamBox

			if(message.getTag().equals("spam")){
				inSpamBox = this.messageBoxService.findSpamBoxActorId(message.getReceiver().getId());
				Assert.notNull(inSpamBox);
			}else{
				inSpamBox = this.messageBoxService.findInBoxActorId(message.getReceiver().getId());
				Assert.notNull(inSpamBox);
			}

			//Update boxes' messages
			message.getMessageBoxes().add(inSpamBox);

			result = this.messageRepository.save(message);

			this.messageBoxService.findOutBoxActorId(principal.getId()).getMessages().add(result);
			inSpamBox.getMessages().add(result);

			return result;
		}
	}

	public void delete(final Message message) {
		Actor principal;
		MessageBox trashBoxPrincipal;
		Collection<Message> messagesTrashBox;


		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		trashBoxPrincipal = this.messageBoxService.findTrashBoxActorId(principal.getId());
		Assert.notNull(trashBoxPrincipal);

		messagesTrashBox = trashBoxPrincipal.getMessages();
		Assert.notNull(messagesTrashBox);

		if(messagesTrashBox.contains(message)){
			if(this.findMessage(message)){
				messagesTrashBox.remove(message);
			}else{
				messagesTrashBox.remove(message);
				this.messageRepository.delete(message);
			}
		}else{
			this.move(message, trashBoxPrincipal);
		}
	}

	public boolean findMessage(final Message message){
		boolean result = false;
		Actor sender;
		Actor recipient;

		recipient = message.getReceiver();
		sender = message.getSender();

		Assert.notNull(sender);
		Assert.notNull(recipient);

		for(MessageBox mb : sender.getMessageBoxes()){
			if(!mb.getName().equals("Trash Box")){
				if(mb.getMessages().contains(message)){
					result = true;
				}
			}
		}

		for(MessageBox mb : recipient.getMessageBoxes()){
			if(!mb.getName().equals("Trash Box")){
				if(mb.getMessages().contains(message)){
					result = true;
				}
			}
		}
		return result;

	}

	public void move(final Message message, final MessageBox destination) {
		Actor principal;
		MessageBox origin = null;
		Collection<Message> updatedOriginBox,updatedDestinationBox,messages;
		Collection<MessageBox> messageBoxes;

		Message m;

		messages = new ArrayList<Message>();
		messageBoxes = new ArrayList<MessageBox>();
		Assert.notNull(message);
		Assert.notNull(destination);

		Assert.isTrue(message.getId() != 0);
		Assert.isTrue(destination.getId() != 0);

		messageBoxes = message.getMessageBoxes();
		Assert.notNull(messageBoxes);


		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);


		for(MessageBox box: principal.getMessageBoxes()){
			messages.addAll(box.getMessages());
		}

		Assert.isTrue(messages.contains(message));

		for(MessageBox principalBox : principal.getMessageBoxes()){
			if(principalBox.getMessages().contains(message)){
				origin = principalBox;
				messageBoxes.remove(origin);
				break;
			}



		}

		messageBoxes.add(destination);


		Assert.isTrue(principal.getMessageBoxes().contains(origin));
		Assert.isTrue(principal.getMessageBoxes().contains(destination));

		message.setMessageBoxes(messageBoxes);
		m = this.messageRepository.save(message);

		updatedOriginBox = origin.getMessages();
		updatedDestinationBox = destination.getMessages();

		updatedOriginBox.remove(m);
		updatedDestinationBox.add(m);

		origin.setMessages(updatedOriginBox);
		destination.setMessages(updatedDestinationBox);


	}

	public Message findOne(final int messageId) {
		Message result;

		result = this.messageRepository.findOne(messageId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Message> findAll() {
		Collection<Message> result;

		result = this.messageRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public void broadcast(final Message m){
		Actor principal;
		String subject,priority,body,tags;
		Date sentMoment;
		boolean isSpam;
		MessageBox outBoxAdmin;
		Collection<MessageBox> allBoxes ,boxes,notificationBoxes;
		Message saved;
		Collection<Actor>recipients;

		allBoxes = new ArrayList<MessageBox>();
		notificationBoxes = new ArrayList<MessageBox>();
		boxes = new ArrayList<MessageBox>();
		recipients = new ArrayList<Actor>();

		recipients = this.actorService.findAll();

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMINISTRATOR"));

		Assert.notNull(m);

		subject = m.getSubject();
		body = m.getBody();
		priority = m.getPriority();
		tags = m.getTag();
		isSpam = false;
		sentMoment = new Date(System.currentTimeMillis()-1);

		for(Actor a : recipients){
			if(!(this.actorService.checkAuthority(a, "ADMIN"))){
				notificationBoxes.add(this.messageBoxService.findNotificationBoxActorId(a.getId()));
			}

		}




		outBoxAdmin = this.messageBoxService.findOutBoxActorId(principal.getId());
		Assert.notNull(outBoxAdmin);



		final Message message = new Message();
		message.setSubject(subject);
		message.setBody(body);
		message.setSendMoment(sentMoment);
		message.setPriority(priority);
		message.setTag(tags);
		message.setIsSpam(isSpam);
		message.setReceiver(principal);
		message.setSender(principal);

		boxes.add(outBoxAdmin);
		boxes.addAll(notificationBoxes);

		message.setMessageBoxes(boxes);

		saved = this.messageRepository.save(message);

		for(MessageBox notBox : notificationBoxes){
			notBox.getMessages().add(saved);
		}

		outBoxAdmin.getMessages().add(saved);

	}


	public void deleteMessages(final Message m, final MessageBox mb){
		Collection<Message> messages= new ArrayList<Message>();

		mb.setMessages(messages);


	}

	public Message reconstruct(final Message message, final BindingResult binding){
		Message result = this.create();

		if(message.getId()==0){
			result.setSubject(message.getSubject());
			result.setBody(message.getBody());
			result.setPriority(message.getPriority());
			result.setTag(message.getTag());
			result.setReceiver(message.getReceiver());

			this.validator.validate(result,binding);

		}else{
			final Message m = this.messageRepository.findOne(message.getId());
			message.setSubject(m.getSubject());
			message.setBody(m.getBody());
			message.setPriority(m.getPriority());
			message.setSendMoment(m.getSendMoment());
			message.setTag(m.getTag());
			message.setReceiver(m.getReceiver());
			message.setSender(m.getSender());

			result = message;

			this.validator.validate(result,binding);


		}
		return result;
	}

	public Message reconstructBroadcast(final Message message, final BindingResult binding){

		Message result = this.create();
		Actor principal;

		principal = this.actorService.findByPrincipal();

		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"));
		Assert.isTrue(message.getId() == 0);

		result.setSubject(message.getSubject());
		result.setBody(message.getBody());
		result.setPriority(message.getPriority());
		result.setTag(message.getTag());
		result.setReceiver(principal);

		this.validator.validate(result,binding);

		return result;


	}


}
