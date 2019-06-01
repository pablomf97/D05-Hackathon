
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.EventRepository;
import repositories.GroupRepository;
import domain.Actor;
import domain.Event;
import domain.FilmEnthusiast;
import domain.Forum;

@Transactional
@Service
public class EventService {

	@Autowired
	private ActorService	actorService;

	@Autowired
	private EventRepository	eventRepository;

	@Autowired
	private GroupRepository	groupRepository;

	@Autowired
	private Validator		validator;


	public Event create(final Forum group) {
		Actor principal;
		Event result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "FILMENTHUSIAST"), "not.allowed");

		result = new Event();
		result.setAttenders(null);
		result.setForum(group);
		final Collection<FilmEnthusiast> attenders = new ArrayList<>();
		result.setAttenders(attenders);
		return result;
	}

	public Collection<Event> findAll() {
		return this.eventRepository.findAll();
	}

	public Collection<Event> findAllByGroup(final int Id) {
		final Collection<Event> events = this.eventRepository.findAllByGroup(Id);
		final Forum group = this.groupRepository.findOne(Id);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(group.getCreator().equals(actor) || group.getGroupMembers().contains(actor));
		return events;
	}

	public Event findOne(final int eventId) {
		Event result;

		result = this.eventRepository.findOne(eventId);
		Assert.notNull(result);
		return result;
	}

	public Event save(final Event event) {
		Actor principal;
		principal = this.actorService.findByPrincipal();
		Event result = null;
		Assert.isTrue(this.actorService.checkAuthority(principal, "FILMENTHUSIAST"), "not.allowed");
		Assert.isTrue(event.getForum().getCreator().equals(principal));
		if (event.getId() == 0)
			result = event;
		Assert.notNull(result);
		result = this.eventRepository.save(result);

		return result;
	}

	public void delete(final Event event) {
		Actor principal;
		Assert.notNull(event);
		Assert.isTrue(event.getId() != 0, "wrong.id");
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "FILMENTHUSIAST"), "not.allowed");
		final Event orig = this.findOne(event.getId());
		Assert.isTrue(event.getForum().getCreator().getId() == principal.getId(), "not.allowed");
		Assert.isTrue(orig.getId() == event.getId());
		this.eventRepository.delete(orig.getId());
	}

	public Event reconstruct(final Event event, final BindingResult binding) {
		Event result = null;
		final Actor principal = this.actorService.findByPrincipal();
		result = this.create(event.getForum());
		if (event.getId() == 0) {
			Assert.isTrue(this.actorService.checkAuthority(principal, "FILMENTHUSIAST"), "not.allowed");
			result.setAddress(event.getAddress());
			result.setDescription(event.getDescription());
			result.setMaximumCapacity(event.getMaximumCapacity());
			result.setPrice(event.getPrice());
			result.setEventDate(event.getEventDate());
			result.setSigninDeadline(event.getSigninDeadline());
			result.setTitle(event.getTitle());
			Assert.isTrue(result.getForum().getCreator().equals(principal));
		}
		this.validator.validate(result, binding);
		return result;
	}

	public void requestEvent(final int Id) {
		final Event event = this.findOne(Id);
		Assert.isTrue(event.getAttenders().size() < event.getMaximumCapacity(), "capacity exceded");
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(!event.getAttenders().contains(actor) || event.getForum().getGroupMembers().equals(actor));
		event.getAttenders().add((FilmEnthusiast) actor);

	}

	public Collection<Event> EventsWithHigeshtMaximumCapacity() {
		return this.eventRepository.EventsWithHigeshtMaximumCapacity();
	}

	public Collection<Event> top3EventsWithMorePeople() {
		final List<Event> l = (List<Event>) this.eventRepository.top3EventsWithMorePeople();
		return l.subList(0, 3);
	}
}
