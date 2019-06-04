package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageBoxRepository;
import domain.Actor;
import domain.Message;
import domain.MessageBox;

@Service
@Transactional
public class MessageBoxService {

	// Repository

	@Autowired
	private MessageBoxRepository messageBoxRepository;

	// Services

	@Autowired
	private ActorService actorService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private Validator validator;

	// CRUD METHODS

	public MessageBox create() {
		MessageBox result = new MessageBox();

		result.setIsPredefined(false);
		result.setMessages(new ArrayList<Message>());

		return result;
	}

	public MessageBox save(final MessageBox messageBox) {
		MessageBox result;
		MessageBox boxBD = this.findOne(messageBox.getId());
		if (messageBox.getId() != 0) {
			Assert.isTrue(this.actorService.findByPrincipal().equals(
					messageBox.getOwner())
					|| this.actorService.findByPrincipal().equals(
							boxBD.getOwner()));
			if (boxBD.getIsPredefined() == false)
				boxBD.setName(messageBox.getName());
			Assert.isTrue(this.checkParentBox(boxBD, messageBox));
			boxBD.setParentMessageBox(messageBox.getParentMessageBox());
			boxBD.setMessages(messageBox.getMessages());
		} else
			Assert.notNull(messageBox.getName());
		result = this.messageBoxRepository.save(boxBD);

		return result;
	}

	public void delete(final MessageBox messageBox) {
		Assert.isTrue((this.actorService.findByPrincipal().equals(messageBox
				.getOwner())), "Box not belong to the logged actor");
		Assert.isTrue(!(messageBox.getIsPredefined()), "Box undeleteable");
		final Collection<MessageBox> childBoxes = this.messageBoxRepository
				.findByParent(messageBox.getId());
		if (!childBoxes.isEmpty())
			for (final MessageBox b : childBoxes)
				this.delete(b);
		if (messageBox.getMessages() != null)
			for (final Message m : messageBox.getMessages())

				this.messageService.deleteMessages(m, messageBox);
		this.messageBoxRepository.delete(messageBox);
	}

	// Other requirements
	public void initializeDefaultBoxes(Actor newActor) {

		final MessageBox in = this.create();
		in.setIsPredefined(true);
		in.setName("In box");
		in.setOwner(newActor);
		this.save(in);

		final MessageBox trash = this.create();
		trash.setIsPredefined(true);
		trash.setName("Trash box");
		trash.setOwner(newActor);
		this.save(trash);

		final MessageBox out = this.create();
		out.setIsPredefined(true);
		out.setName("Out box");
		out.setOwner(newActor);
		this.save(out);

		final MessageBox spam = this.create();
		spam.setIsPredefined(true);
		spam.setName("Spam box");
		spam.setOwner(newActor);
		this.save(spam);

		final MessageBox notification = this.create();
		notification.setIsPredefined(true);
		notification.setName("Notification box");
		notification.setOwner(newActor);
		this.save(notification);

	}

	public Collection<MessageBox> findByParent(final int idBox) {
		final Collection<MessageBox> boxes = this.messageBoxRepository
				.findByParent(idBox);
		return boxes;
	}

	public Collection<MessageBox> findAll() {
		return this.messageBoxRepository.findAll();
	}

	public MessageBox findOne(final int idBox) {
		final MessageBox box = this.messageBoxRepository.findOne(idBox);
		return box;
	}

	public Collection<MessageBox> findByOwner(final int actorId) {
		return this.messageBoxRepository.boxesByActor(actorId);
	}

	public Collection<MessageBox> findByOwnerFirst(final int actorId) {
		return this.messageBoxRepository.firstBoxesByActor(actorId);
	}

	public MessageBox findByName(final int actorId, final String name) {
		return this.messageBoxRepository.boxByName(actorId, name);
	}

	public boolean checkUniqueBox(final MessageBox box) {
		boolean bool = true;
		final Actor actor = this.actorService.findByPrincipal();
		final MessageBox mb = this.findByName(actor.getId(), box.getName());
		if (mb != null && mb.getId() != box.getId())
			bool = false;
		return bool;
	}

	public boolean checkParentBox(final MessageBox boxBD, final MessageBox box) {
		boolean bool = true;
		final Collection<MessageBox> boxes = this.messageBoxRepository
				.findByParent(boxBD.getId());
		if (boxes.contains(box.getParentMessageBox()))
			bool = false;
		return bool;

	}

	public MessageBox reconstruct(final MessageBox messageBox,
			final BindingResult binding) {
		final MessageBox result = this.create();
		if (messageBox.getId() == 0) {
			result.setName(messageBox.getName());
			result.setParentMessageBox(messageBox.getParentMessageBox());
			result.setOwner(this.actorService.findByPrincipal());
			this.validator.validate(result, binding);
		} else {
			final MessageBox bd = this.messageBoxRepository.findOne(messageBox
					.getId());
			Assert.notNull(bd);
			result.setName(messageBox.getName());
			result.setParentMessageBox(messageBox.getParentMessageBox());
			result.setOwner(messageBox.getOwner());
			this.validator.validate(result, binding);
			if (!binding.hasErrors()) {
				result.setId(bd.getId());
				result.setVersion(bd.getVersion());
				result.setIsPredefined(bd.getIsPredefined());
				result.setParentMessageBox(messageBox.getParentMessageBox());
			}
		}
		return result;
	}

	
	public void deleteBox(MessageBox mb ){
		
		this.messageBoxRepository.delete(mb);
		
		

	}
 
}
