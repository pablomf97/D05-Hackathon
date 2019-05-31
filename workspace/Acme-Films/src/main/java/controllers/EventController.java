
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.EventService;
import services.GroupService;
import domain.Actor;
import domain.Event;
import domain.Forum;

@Controller
@RequestMapping("/event")
public class EventController extends AbstractController {

	@Autowired
	private EventService	eventService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private GroupService	groupService;


	public EventController() {
		super();
	}

	//Film enthusiast owner
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listEvents(@RequestParam final int Id) {
		ModelAndView result;
		try {
			final Collection<Event> events = this.eventService.findAllByGroup(Id);
			result = new ModelAndView("event/list");
			result.addObject("requestURI", "/event/list.do");
			final Actor actor = this.actorService.findByPrincipal();
			result.addObject("actor", actor);
			result.addObject("groups", events);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "event.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteEvent(@RequestParam final int Id) {
		ModelAndView result;
		try {
			final Event event = this.eventService.findOne(Id);
			this.eventService.delete(event);
			result = new ModelAndView("redirect:list.do?Id=" + event.getForum().getId());
			final Actor actor = this.actorService.findByPrincipal();
			Assert.isTrue(event.getForum().getCreator().equals(actor));
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "event.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView saveEvent(final Event event, final BindingResult binding) {
		ModelAndView result;
		Event res = null;
		try {
			res = this.eventService.reconstruct(event, binding);
			if (binding.hasErrors()) {
				result = new ModelAndView("event/edit");
				result.addObject("event", event);
				final Actor actor = this.actorService.findByPrincipal();

				result.addObject("actor", actor);
			} else
				try {
					final Event ev = this.eventService.save(res);
					result = new ModelAndView("redirect:list.do?Id=" + ev.getForum().getId());
				} catch (final Throwable opps) {
					result = new ModelAndView("redirect:../welcome/index.do");
					result.addObject("messageCode", "event.commit.error");
				}
		} catch (final Throwable opps) {
			//TODO: pantalla de error
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "event.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editEvent(@RequestParam final int Id) {
		ModelAndView result;
		Event event;
		try {
			event = this.eventService.findOne(Id);
			result = new ModelAndView("event/edit");
			result.addObject("event", event);
			final Actor actor = this.actorService.findByPrincipal();
			Assert.isTrue(event.getForum().getCreator().equals(actor));
			result.addObject("actor", actor);

		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "event.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayEvent(@RequestParam final int Id) {
		ModelAndView result;
		Event event;
		try {
			final Actor actor = this.actorService.findByPrincipal();
			result = new ModelAndView("event/display");
			event = this.eventService.findOne(Id);
			Assert.isTrue(event.getAttenders().contains(actor) || event.getForum().getCreator().equals(actor));

			result.addObject(event);
			result.addObject(actor);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "event.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int Id) {
		ModelAndView result;
		try {
			result = new ModelAndView("event/edit");
			final Forum group = this.groupService.findOne(Id);
			final Event event = this.eventService.create(group);
			result.addObject("event", event);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "event.commit.error");
		}
		return result;
	}

	//Film enthusiast request
	@RequestMapping(value = "/filmenthusiast/request", method = RequestMethod.GET)
	public ModelAndView requestEvent(@RequestParam final int Id) {
		ModelAndView result;
		try {
			this.eventService.requestEvent(Id);
			result = new ModelAndView("redirect:display.do?Id=" + Id);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "event.commit.error");
		}
		return result;
	}

	//Film enthusiast group list
	@RequestMapping(value = "/filmenthusiast/listMembers", method = RequestMethod.GET)
	public ModelAndView requestList(@RequestParam final int Id) {
		ModelAndView result;
		Event event;
		try {
			event = this.eventService.findOne(Id);
			result = new ModelAndView("filmenthusiast/list");
			result.addObject("filmenthusiasts", event.getAttenders());
			final Actor actor = this.actorService.findByPrincipal();
			Assert.isTrue(event.getAttenders().contains(actor) || event.getForum().getCreator().equals(actor));
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "event.commit.error");
		}
		return result;
	}
}