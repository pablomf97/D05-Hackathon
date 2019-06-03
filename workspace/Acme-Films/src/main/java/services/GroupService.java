
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
	private EventService eventService;
	
	@Autowired
	private CommentService commentService;


	public Forum createForFilm(final Film film) {
		Actor principal;
		Forum result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "FILMENTHUSIAST"), "not.allowed");

		result = new Forum();
		final Date d = new Date();
		result.setCreationDate(d);
		result.setCreator((FilmEnthusiast) principal);
		result.setFilmAbout(film);
		final Collection<FilmEnthusiast> groupMembers = new ArrayList<>();
		result.setGroupMembers(groupMembers);

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
		return result;
	}

	public Collection<Forum> findAllByFilm(final Integer id) {
		Assert.notNull(id);
		return this.groupRepository.forumsPerFilm(id);
	}
	public Collection<Forum> findAllBySaga(final Integer id) {
		Assert.notNull(id);
		return this.groupRepository.forumsPerSaga(id);
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
		if (forum.getFilmAbout() != null)
			result = this.createForFilm(forum.getFilmAbout());
		else
			result = this.createForSaga(forum.getSagaAbout());
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
		Assert.notNull(result);

		result = this.groupRepository.save(result);

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
			Assert.isTrue(forum.getFilmAbout() != null || forum.getSagaAbout() != null);
			if (forum.getFilmAbout() != null) {
				result = this.createForFilm(forum.getFilmAbout());
				if (forum.getId() == 0) {
					final Date moment = new Date();
					result.setCreationDate(moment);
					result.setFilmAbout(forum.getFilmAbout());
				} else {
					orig = this.findOne(forum.getId());
					Assert.notNull(orig);
					Assert.isTrue(orig.getCreator().getId() == principal.getId());
					Assert.isTrue(orig.getIsActive());
					result.setId(orig.getId());
					result.setGroupMembers(orig.getGroupMembers());
					result.setRejectReason(orig.getRejectReason());
					result.setModerator(orig.getModerator());
					result.setIsActive(orig.getIsActive());
				}
				result.setDescription(forum.getDescription());
				result.setName(forum.getName());
			} else if (forum.getSagaAbout() != null) {
				result = this.createForSaga(forum.getSagaAbout());
				if (forum.getId() == 0) {
					final Date moment = new Date();
					result.setCreationDate(moment);
					result.setSagaAbout(forum.getSagaAbout());
				} else {
					orig = this.findOne(forum.getId());
					Assert.notNull(orig);
					Assert.isTrue(orig.getCreator().getId() == principal.getId());
					result.setId(orig.getId());
					result.setGroupMembers(orig.getGroupMembers());
					result.setRejectReason(orig.getRejectReason());
					result.setModerator(orig.getModerator());
					result.setIsActive(orig.getIsActive());
				}
				result.setDescription(forum.getDescription());
				result.setName(forum.getName());
			}
		} catch (final Throwable oops) {
			binding.rejectValue("filmAbout", "film.error.not");
			binding.rejectValue("sagaAbout", "saga.error.not");
		}
		this.validator.validate(result, binding);
		return result;
	}
	public void activateGroup(final Forum group) {
		final Forum res = this.findOne(group.getId());
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(actor, "MODERATOR"), "not.allowed");
		Assert.isNull(group.getIsActive());
		res.setModerator((Moderator) actor);
		res.setIsActive(true);
		this.groupRepository.save(res);
	}
	public void deactivateGroup(final Forum group) {
		final Forum res = this.findOne(group.getId());
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(actor, "MODERATOR"), "not.allowed");
		Assert.isNull(group.getIsActive());
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

	public void deleteGroupModerator(int id) {
		
		this.groupRepository.deleteInBatch(this.groupRepository.forumPerModerator(id));
		
	
		
	}

	public void deleteGroupPerFilm(int id) {
		
		for(Forum g :this.groupRepository.forumPerFilmDefault(id)){
			
			this.eventService.deleteEventPerForum(g.getId());
			this.groupRepository.delete(g);
		}
		
	}

	public void deleteGroupPerFilmEnthusiast(FilmEnthusiast f) {
		
		for(Forum g: this.findAll()){
			if(g.getGroupMembers().contains(f)||g.getCreator().getId()==f.getId()){
				this.commentService.deleteCommentsPerForum(g);
				//this.groupRepository.delete(g);
			}
		}
		
		
	}

}
