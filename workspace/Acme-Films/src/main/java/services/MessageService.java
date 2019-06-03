package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

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

	// Repository

	@Autowired
	private MessageRepository messageRepository;

	// Services

	@Autowired
	private ActorService actorService;

	@Autowired
	private Validator validator;

	@Autowired
	private MessageBoxService messageBoxService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	// CRUD METHODS

	public Message create() {
		Message result = new Message();
		Actor principal = this.actorService.findByPrincipal();

		result.setSendMoment(new Date(System.currentTimeMillis() - 1));
		result.setSender(principal);
		result.setMessageBoxes(new ArrayList<MessageBox>());
		result.setIsFilmCreated(false);
		result.setIsSpam(false);

		return result;
	}

	public Message save(final Message message) {
		Message result = new Message();
		Actor principal = this.actorService.findByPrincipal();
		MessageBox inSpamBox = null;

		Assert.notNull(message.getBody());
		Assert.notNull(message.getSubject());
		Assert.notNull(message.getBody());
		Assert.notNull(message.getPriority());
		Assert.notNull(message.getReceiver());
		
		// Checking sender is the principal
		Assert.isTrue(message.getSender().getId() == principal.getId(),
				"Not your message");

		// Checking receiver is an actor from the system
		Assert.isTrue(this.actorService.checkAuthority(message.getReceiver(),
				"CRITIC")
				|| this.actorService.checkAuthority(message.getReceiver(),
						"MODERATOR")
						|| this.actorService.checkAuthority(message.getReceiver(),
								"ADMIN")
								|| this.actorService.checkAuthority(message.getReceiver(),
										"FILMENTHUSIAST")
										|| this.actorService.checkAuthority(message.getReceiver(),
												"SPONSOR"));


		// Checking spam
		boolean containsSpam = false;
		final String[] spamWords = this.systemConfigurationService
				.findMySystemConfiguration().getSpamWords().split(",");
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


		}

		// Update some values like send moment

		message.setSendMoment(new Date(System.currentTimeMillis() - 1));

		// Checking spamBox
		if(message.getTag()!= null){
			if (message.getTag().equals("spam")) {
				inSpamBox = this.messageBoxService.findByName(
						message.getReceiver().getId(), "Spam box");
				Assert.notNull(inSpamBox);
			} 
		}
		if(!message.getIsSpam()){
			inSpamBox = this.messageBoxService.findByName(message.getReceiver().getId(), "In box");

			Assert.notNull(inSpamBox);

		}


		// Update boxes' messages
		message.getMessageBoxes().add(inSpamBox);
		message.getMessageBoxes().add(this.messageBoxService.findByName(principal.getId(), "Out box"));

		result = this.messageRepository.save(message);

		this.messageBoxService.findByName(principal.getId(), "Out box")
		.getMessages().add(result);
		inSpamBox.getMessages().add(result);
		return result;
	}

	public void delete(final Message message) {
		Actor principal;
		MessageBox trashBoxPrincipal;
		Collection<Message> messagesTrashBox;
		
		
		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		trashBoxPrincipal = this.messageBoxService.findByName(
				principal.getId(), "Trash box");
		Assert.notNull(trashBoxPrincipal);

		messagesTrashBox = trashBoxPrincipal.getMessages();
		Assert.notNull(messagesTrashBox);
		
		if (messagesTrashBox.contains(message)) {
			if (this.findMessage(message)) {
				messagesTrashBox.remove(message);
			} else {
				messagesTrashBox.remove(message);
				
				this.messageRepository.delete(message);
			}
		} else {
			this.move(message, trashBoxPrincipal);
		}
	}

	public boolean findMessage(final Message message) {
		boolean result = false;
		Actor sender;
		Actor recipient;

		recipient = message.getReceiver();
		sender = message.getSender();

		Assert.notNull(sender);
		Assert.notNull(recipient);

		for (MessageBox mb : this.messageBoxService.findByOwner(sender.getId())) {
			if (!mb.getName().equals("Trash box")) {
				if (mb.getMessages().contains(message)) {
					result = true;
					break;
				}
			}
		}

		for (MessageBox mb : this.messageBoxService.findByOwner(recipient.getId())) {
			if (!mb.getName().equals("Trash box")) {
				if (mb.getMessages().contains(message)) {
					result = true;
					break;
				}
			}
		}
		return result;

	}

	public void move(final Message message, final MessageBox destination) {
		Actor principal = this.actorService.findByPrincipal();
		MessageBox origin = null;
		Collection<Message> updatedOriginBox, updatedDestinationBox, messages;
		Collection<MessageBox> messageBoxes;

		final Message bd = this.findOne(message.getId());

		messages = new ArrayList<Message>();
		messageBoxes = new ArrayList<MessageBox>();
		Assert.notNull(message);
		Assert.notNull(destination);

		Assert.isTrue(message.getId() != 0);
		Assert.isTrue(destination.getId() != 0);

		Assert.isTrue(principal.getId() == message.getSender().getId() || principal.getId() == message.getReceiver().getId());

		messageBoxes = message.getMessageBoxes();
		Assert.notNull(messageBoxes);


		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);




		for (MessageBox principalBox : this.messageBoxService.findByOwner(principal.getId())) {
			if (principalBox.getMessages().contains(message)) {
				messages.addAll(principalBox.getMessages());
				origin = principalBox;
				messageBoxes.remove(origin);
				break;
			}

		}

		for(MessageBox otherMb : bd.getMessageBoxes()){
			if(!otherMb.equals(origin)){
				messageBoxes.add(otherMb);
			}
		}


		Assert.isTrue(messages.contains(message));

		messageBoxes.add(destination);

		Assert.isTrue(this.messageBoxService.findByOwner(principal.getId()).contains(origin));
		Assert.isTrue(this.messageBoxService.findByOwner(principal.getId()).contains(destination));

		message.setMessageBoxes(messageBoxes);

		message.setVersion(bd.getVersion());
		
		this.messageRepository.save(message);
		updatedOriginBox = origin.getMessages();
		updatedDestinationBox = destination.getMessages();

		updatedOriginBox.remove(message);
		updatedDestinationBox.add(message);

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

	public void broadcast(final Message m) {
		Actor principal;
		String subject, priority, body, tags;
		Date sentMoment;
		boolean isSpam;
		MessageBox outBoxAdmin;
		Collection<MessageBox> allBoxes, boxes, notificationBoxes;
		Message saved;
		Collection<Actor> recipients;

		allBoxes = new ArrayList<MessageBox>();
		notificationBoxes = new ArrayList<MessageBox>();
		boxes = new ArrayList<MessageBox>();
		recipients = new ArrayList<Actor>();

		recipients = this.actorService.findAll();

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(this.actorService.checkAuthority(principal,
				"ADMIN"));

		Assert.notNull(m);

		subject = m.getSubject();
		body = m.getBody();
		priority = m.getPriority();
		tags = m.getTag();
		isSpam = false;
		sentMoment = new Date(System.currentTimeMillis() - 1);

		for (Actor a : recipients) {
			if (!(this.actorService.checkAuthority(a, "ADMIN"))) {
				notificationBoxes.add(this.messageBoxService.findByName(
						a.getId(), "Notification box"));
			}

		}

		outBoxAdmin = this.messageBoxService.findByName(principal.getId(),
				"Out box");
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

		for (MessageBox notBox : notificationBoxes) {
			notBox.getMessages().add(saved);
		}

		outBoxAdmin.getMessages().add(saved);

	}

	public void deleteMessages(final Message m, final MessageBox mb) {
		Collection<Message> messages = new ArrayList<Message>();

		mb.setMessages(messages);

	}

	public Message reconstruct(final Message message,
			final BindingResult binding) {
		Message result = this.create();

		if (message.getId() == 0) {
			result.setSubject(message.getSubject());
			result.setBody(message.getBody());
			result.setPriority(message.getPriority());
			result.setTag(message.getTag());
			result.setReceiver(message.getReceiver());
			result.setSender(this.actorService.findByPrincipal());
			result.setSendMoment((new Date(System.currentTimeMillis()-1)));


			this.validator.validate(result, binding);

		} else {
			final Message m = this.messageRepository.findOne(message.getId());
			message.setSubject(m.getSubject());
			message.setBody(m.getBody());
			message.setPriority(m.getPriority());
			message.setSendMoment(m.getSendMoment());
			message.setTag(m.getTag());
			message.setReceiver(m.getReceiver());
			message.setSender(m.getSender());
			message.setIsSpam(m.getIsSpam());
			
			if(!(message.getMessageBoxes().size() ==1)){
				message.setMessageBoxes(m.getMessageBoxes());
			}
			result = message;

			this.validator.validate(result, binding);

		}
		
		if(binding.hasErrors()){
			throw new ValidationException();
		}
		return result;
	}

	public Message reconstructBroadcast(final Message message,
			final BindingResult binding) {

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

		this.validator.validate(result, binding);

		return result;

	}
	public Collection<Message> messagesSenderByActor(int id){
		return this.messageRepository.messagesSenderByActor(id);
	}
	public Collection<Message> messagesInvolved(int id){
		return this.messageRepository.messagesInvolved(id);
	}
	
	public void deleteMessagesInvolved(){
		
		this.messageRepository.deleteInBatch(this.findAll());
	}
	
	public Collection<Message> messagesReceiverByActor(int id){
		
		return this.messageRepository.messagesReceiverByActor(id);
	}
	public void deleteMessage(Message m){
		this.messageRepository.delete(m);
	}
}