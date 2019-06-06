
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

import repositories.GroupRepository;
import domain.Actor;
import domain.Comment;
import domain.Event;
import domain.Film;
import domain.FilmEnthusiast;
import domain.Forum;
import domain.Moderator;
import domain.Saga;

@Transactional
@Service
public class GroupService {

	@Autowired
	private ActorService	actorService;

	@Autowired
	private GroupRepository	groupRepository;
	@Autowired
	private Validator		validator;
	@Autowired
	private FilmService		filmService;
	@Autowired
	private SagaService		sagaService;
	@Autowired
	private CommentService	commentService;

	@Autowired
	private EventService	eventService;


	public Forum createForFilm(final Film film) {
		Actor principal;
		Forum result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "FILMENTHUSIAST"), "not.allowed");
		Assert.isTrue(film.getIsDraft() == false);
		result = new Forum();
		final Date d = new Date();
		result.setCreationDate(d);
		result.setCreator((FilmEnthusiast) principal);
		result.setFilmAbout(film);
		final Collection<FilmEnthusiast> groupMembers = new ArrayList<>();
		result.setGroupMembers(groupMembers);
		result.setIsActive(false);
		return result;
	}

	public Forum createForSaga(final Saga saga) {
		Actor principal;
		Forum result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "FILMENTHUSIAST"), "not.allowed");

		result = new Forum();
		final Date d = new Date();
		result.setCreationDate(d);
		result.setCreator((FilmEnthusiast) principal);
		result.setSagaAbout(saga);
		final Collection<FilmEnthusiast> groupMembers = new ArrayList<>();
		result.setGroupMembers(groupMembers);
		result.setIsActive(false);

		return result;
	}

	public Collection<Forum> findAllWithoutModerator() {
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(actor, "MODERATOR"));
		return this.groupRepository.forumsWithoutActive();
	}

	public Collection<Forum> findByModerator() {
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(actor, "MODERATOR"));
		final Moderator mod = (Moderator) actor;
		return this.groupRepository.forumsByModerator(mod.getId());
	}

	public Collection<Forum> findAllByFilm(final Integer id) {
		Assert.notNull(id);
		final Film film = this.filmService.findOne(id);
		Assert.notNull(film);
		final Collection<Forum> forums = this.groupRepository.forumsPerFilm(film.getId());
		return forums;
	}

	public Collection<Forum> findAllBySaga(final Integer id) {
		Assert.notNull(id);
		final Saga saga = this.sagaService.findOne(id);
		Assert.notNull(saga);
		final Collection<Forum> forums = this.groupRepository.forumsPerSaga(saga.getId());
		return forums;
	}
	public Collection<Forum> findAllByFilmEnthusiast(final Integer id) {
		Assert.notNull(id);
		return this.groupRepository.forumsPerFilmEnthusiast(id);
	}

	public Collection<Forum> findAll() {
		return this.groupRepository.findAll();
	}

	public Forum findOne(final int forumId) {
		Forum result;

		result = this.groupRepository.findOne(forumId);
		Assert.notNull(result);
		return result;
	}

	public Forum save(final Forum forum) {
		Actor principal;
		principal = this.actorService.findByPrincipal();
		Forum result = null;
		Assert.isTrue(forum.getFilmAbout() != null || forum.getSagaAbout() != null);
		if (forum.getFilmAbout() != null) {
			Assert.isNull(forum.getSagaAbout());
			result = this.createForFilm(forum.getFilmAbout());

		} else {
			Assert.isNull(forum.getFilmAbout());
			result = this.createForSaga(forum.getSagaAbout());
		}
		Assert.isTrue(this.actorService.checkAuthority(principal, "FILMENTHUSIAST"), "not.allowed");
		Assert.isTrue(forum.getCreator().equals(principal));
		if (forum.getId() == 0) {
			result = forum;
			final Date moment = new Date();
			result.setCreationDate(moment);
		} else {
			result = this.findOne(forum.getId());
			Assert.isTrue(result.getIsActive());
			result.setDescription(forum.getDescription());
			result.setName(forum.getName());
		}
		result.setDescription(forum.getDescription());
		result.setName(forum.getName());
		result = this.groupRepository.save(result);
		Assert.notNull(result);
		Assert.isTrue(result.getId() != 0);

		return result;
	}

	public void delete(final Forum forum) {
		Actor principal;
		Assert.notNull(forum);
		Assert.isTrue(!forum.getIsActive(), "Only can delete in deactive mode");
		Assert.isTrue(forum.getId() != 0, "wrong.id");
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "FILMENTHUSIAST"), "not.allowed");
		final Forum orig = this.findOne(forum.getId());
		Assert.isTrue(forum.getCreator().getId() == principal.getId(), "not.allowed");
		Assert.isTrue(orig.getId() == forum.getId());
		this.groupRepository.delete(orig.getId());
	}

	public Forum reconstruct(final Forum forum, final BindingResult binding) {
		Forum orig;
		Forum result = null;
		final Actor principal = this.actorService.findByPrincipal();

		try {

			if (forum.getId() == 0) {
				if (forum.getFilmAbout() != null)
					result = this.createForFilm(forum.getFilmAbout());
				else if (forum.getSagaAbout() != null)
					result = this.createForSaga(forum.getSagaAbout());
				final Date moment = new Date();
				result.setCreationDate(moment);

			} else {
				orig = this.findOne(forum.getId());
				if (orig.getFilmAbout() != null)
					result = this.createForFilm(orig.getFilmAbout());
				else if (orig.getSagaAbout() != null)
					result = this.createForSaga(orig.getSagaAbout());
				Assert.notNull(orig);
				Assert.isTrue(orig.getCreator().getId() == principal.getId());
				result.setId(orig.getId());
				result.setGroupMembers(orig.getGroupMembers());
				result.setRejectReason(orig.getRejectReason());
				result.setModerator(orig.getModerator());
				result.setIsActive(orig.getIsActive());
				result.setCreationDate(orig.getCreationDate());
			}
			result.setDescription(forum.getDescription());
			result.setName(forum.getName());

			Assert.isTrue(result.getFilmAbout() != null || result.getSagaAbout() != null);

			this.validator.validate(result, binding);
		} catch (final Throwable oops) {
			binding.rejectValue("filmAbout", "film.error.not");
			binding.rejectValue("sagaAbout", "saga.error.not");
		}

		return result;
	}

	public void activateGroup(final Forum group) {
		final Forum res = this.findOne(group.getId());
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(actor, "MODERATOR"), "not.allowed");
		Assert.isTrue(group.getIsActive() == false);
		res.setModerator((Moderator) actor);
		res.setIsActive(true);
		this.groupRepository.save(res);
	}
	public void deactivateGroup(final Forum group) {
		final Forum res = this.findOne(group.getId());
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(actor, "MODERATOR"), "not.allowed");
		Assert.isTrue(group.getIsActive() == false);
		res.setModerator((Moderator) actor);
		res.setRejectReason(group.getRejectReason());
		res.setIsActive(false);
		this.groupRepository.save(res);

	}

	public void requestGroup(final int id) {
		final Forum res = this.findOne(id);
		Assert.isTrue(res.getIsActive());
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(actor, "FILMENTHUSIAST"), "not.allowed");
		Assert.isTrue(!res.getGroupMembers().contains(actor) && !res.getCreator().equals(actor));
		res.getGroupMembers().add((FilmEnthusiast) actor);
		this.groupRepository.save(res);
	}

	public void deleteMember(final int memberId, final int groupId) {
		final Forum res = this.findOne(groupId);
		Assert.isTrue(res.getIsActive());
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(actor, "FILMENTHUSIAST"), "not.allowed");
		Assert.isTrue(actor.equals(res.getCreator()), "not.allowed");
		final Actor member = this.actorService.findOne(memberId);
		Assert.isTrue(res.getGroupMembers().contains(member));
		res.getGroupMembers().remove(member);
		this.groupRepository.save(res);
	}

	public void flush() {
		this.groupRepository.flush();
	}
	public void deleteGroupModerator(final int id) {

		this.groupRepository.deleteInBatch(this.groupRepository.forumPerModerator(id));

	}

	public void deleteGroupPerFilm(final int id) {

		for (final Film f : this.filmService.findAll())
			for (final Forum g : this.findAll())
				if (g.getModerator() == this.actorService.findByPrincipal() || this.filmService.filmsByModerator(this.actorService.findByPrincipal().getId()).contains(f)) {

					this.commentService.deleteCommentsPerForum(g.getId());
					for (final Event e : this.eventService.findAll())
						if (g == e.getForum()) {
							this.eventService.deleteEventPerForum(g.getId());
							this.groupRepository.delete(g);
						}
				}

		/*
		 * for (final Forum g : this.groupRepository.forumPerFilmDefault(id)) {
		 * 
		 * this.commentService.deleteCommentsPerForum(g.getId());
		 * for(Event e :this.eventService.findAll()){
		 * if(g==e.getForum()){
		 * this.eventService.deleteEventPerForum(g.getId());
		 * }
		 * }
		 * 
		 * this.groupRepository.delete(g);
		 * }
		 */
	}

	public void deleteGroupPerFilmEnthusiast(final FilmEnthusiast f) {

		for (final Forum g : this.findAll())
			if (g.getGroupMembers().contains(f))
				g.getGroupMembers().remove(f);
		for (final Event e : this.eventService.findAll())
			if (e.getAttenders().contains(f))
				e.getAttenders().remove(f);
		for (final Forum g : this.groupRepository.forumsPerFilmEnthusiast(f.getId()))
			for (final Comment c : this.commentService.findAll())
				if (c.getFilmEnthusiast() == f || c.getForum() == g)
					this.commentService.deleteComment(c);

		for (final Forum g : this.findAll())
			if (g.getCreator().getId() == f.getId()) {

				this.eventService.deleteEventPerForum(g.getId());
				this.groupRepository.delete(g);
			}
	}

	public void deleteGroup(final Forum f) {
		// TODO Auto-generated method stub
		this.groupRepository.delete(f);

	}

	public Collection<Forum> getForumsToComment(final int id) {
		final Collection<Forum> result = this.groupRepository.getForumsToComment(id);

		return result;
	}

	//	public Collection<Forum> groupsWithSaga(int sagaId) {
	//		Collection<Forum> result;
	//
	//		result = this.groupRepository.groupsWithSaga(sagaId);
	//
	//		return result;
	//	}

}
