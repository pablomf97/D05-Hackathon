package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MessageBoxService;
import services.MessageService;
import services.SystemConfigurationService;
import domain.Actor;
import domain.Message;
import domain.MessageBox;

@Controller
@RequestMapping("message/actor")
public class MessageController extends AbstractController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private MessageBoxService messageBoxService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	// Create
	// ----------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		Message mensaje;

		mensaje = this.messageService.create();

		result = this.createEditModelAndView(mensaje);

		return result;
	}

	// Edition

	@RequestMapping(value = "display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int messageId) {
		ModelAndView result;
		Message message;

		message = this.messageService.findOne(messageId);

		result = new ModelAndView("message/display");
		result.addObject("message0", message);
		
		return result;
	}

	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int messageId) {

		ModelAndView result;
		Message message;

		message = this.messageService.findOne(messageId);

		result = this.createEditModelAndView(message);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Message mensaje, final BindingResult binding) {
		ModelAndView result;
		Message m;

		m = this.messageService.reconstruct(mensaje, binding);

		if(binding.hasErrors()){
			result = this.createEditModelAndView(m);
		}else{
			try {
				this.messageService.save(m);
				result = new ModelAndView("redirect:/messagebox/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(m,
						"message.commit.error");
			}
		}

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "move")
	public ModelAndView move(final Message mensaje, final BindingResult binding) {
		ModelAndView result;
		MessageBox destination;

		Message message;

		message = this.messageService.reconstruct(mensaje, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(message);
		else
			try {

				destination = message.getMessageBoxes().iterator().next();
				this.messageService.move(message, destination);
				result = new ModelAndView("redirect:/messagebox/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(message,
						"message.commit.error");

			}

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Message mensaje,
			final BindingResult binding) {
		ModelAndView result;

		Message message;

		message = this.messageService.reconstruct(mensaje, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(message);
		else
			try {
				this.messageService.delete(message);
				result = new ModelAndView("redirect:/messagebox/list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(message,
						"message.commit.error");

			}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Message mensaje) {
		ModelAndView result;

		result = this.createEditModelAndView(mensaje, null);

		return result;

	}

	protected ModelAndView createEditModelAndView(final Message mensaje,
			final String messageError) {
		ModelAndView result;
		Collection<MessageBox> boxes, messageBoxes;
		Collection<Message> messages;
		Actor sender;
		boolean possible = false;
		Actor principal;
		Date sentMoment;
		Collection<Actor> recipients;

		String[] priorities;
		String priority;
		Collection<String> splitPriorities = new ArrayList<String>();

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		messages = new ArrayList<Message>();
		recipients = new ArrayList<Actor>();

		boxes = this.messageBoxService.findByOwner(principal.getId());

		if (mensaje.getId() != 0) {

			for (final MessageBox mb : boxes) {

				if (mb.getMessages().contains(mensaje)
						&& mensaje.getSender().equals(principal))
					possible = true;
			}
		} else {
			if (mensaje.getSender().equals(principal)) {
				possible = true;
			}
		}

		sentMoment = mensaje.getSendMoment();
		messageBoxes = mensaje.getMessageBoxes();
		sender = mensaje.getSender();

		recipients = this.actorService.findAllExceptPrincipal();

		//		priority = this.systemConfigurationService.findMySystemConfiguration()
		//				.getMessagePriority();
		//
		//		priorities = priority.split(",");

		//		for (String p : priorities) {
		//			splitPriorities.add(p);
		//		}
		Collection<String> priorities2 = new ArrayList<String>();

		priorities2.add("HIGH");
		priorities2.add("NEUTRAL");
		priorities2.add("LOW");

		result = new ModelAndView("message/edit");
		result.addObject("sentMoment", sentMoment);
		result.addObject("messageBoxes", messageBoxes);
		result.addObject("sender", sender);
		result.addObject("mensaje", mensaje);
		result.addObject("boxes", boxes);
		result.addObject("requestURI", "message/actor/edit.do");
		result.addObject("possible", possible);
		result.addObject("broadcast", false);
		result.addObject("message", messageError);
		result.addObject("recipients", recipients);

		result.addObject("priorities", priorities2);

		return result;
	}
}
