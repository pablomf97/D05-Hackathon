
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
import services.FilmService;
import services.GroupService;
import services.SagaService;
import domain.Actor;
import domain.Film;
import domain.Forum;
import domain.Saga;

@Controller
@RequestMapping("/group")
public class GroupController extends AbstractController {

	@Autowired
	private GroupService	groupService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private FilmService		filmService;

	@Autowired
	private SagaService		sagaService;


	public GroupController() {
		super();
	}

	//Groups without moderator
	@RequestMapping(value = "/moderator/listWithout", method = RequestMethod.GET)
	public ModelAndView listAllWithoutAsigne() {
		ModelAndView result;
		try {
			final Collection<Forum> groups = this.groupService.findAllWithoutModerator();
			result = new ModelAndView("group/list");
			result.addObject("requestURI", "/group/listWithout.do");
			result.addObject("groups", groups);
			final Actor actor = this.actorService.findByPrincipal();
			result.addObject("actor", actor);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	@RequestMapping(value = "/listByFilm", method = RequestMethod.GET)
	public ModelAndView listAllByFilm(@RequestParam final int Id) {
		ModelAndView result;
		try {
			final Collection<Forum> groups = this.groupService.findAllByFilm(Id);
			result = new ModelAndView("group/list");
			result.addObject("requestURI", "/group/listByFilm.do");
			result.addObject("groups", groups);
			final Actor actor = this.actorService.findByPrincipal();
			result.addObject("actor", actor);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	@RequestMapping(value = "/listBySaga", method = RequestMethod.GET)
	public ModelAndView listAllBySaga(@RequestParam final int Id) {
		ModelAndView result;
		try {
			final Actor actor = this.actorService.findByPrincipal();
			final Collection<Forum> groups = this.groupService.findAllBySaga(Id);
			result = new ModelAndView("group/list");
			result.addObject("requestURI", "/group/listBySaga.do");
			result.addObject("groups", groups);
			result.addObject("actor", actor);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listLoged() {
		ModelAndView result;
		try {
			Collection<Forum> groups = null;
			result = new ModelAndView("group/list");
			final Actor actor = this.actorService.findByPrincipal();
			result.addObject("actor", actor);
			if (this.actorService.checkAuthority(actor, "MODERATOR"))
				groups = this.groupService.findByModerator();
			else
				groups = this.groupService.findAllByFilmEnthusiast(actor.getId());
			result.addObject("requestURI", "/group/list.do");
			result.addObject("groups", groups);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "group.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteGroup(@RequestParam final int Id) {
		ModelAndView result;
		try {
			final Forum forum = this.groupService.findOne(Id);
			this.groupService.delete(forum);
			if (forum.getFilmAbout() != null)
				result = new ModelAndView("redirect:listByFilm.do?Id=" + forum.getFilmAbout().getId());
			else
				result = new ModelAndView("redirect:listBySaga.do?Id=" + forum.getSagaAbout().getId());
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "group.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView save(final Forum group, final BindingResult binding) {
		ModelAndView result;
		Forum res = null;
		try {
			res = this.groupService.reconstruct(group, binding);
			if (binding.hasErrors()) {
				result = new ModelAndView("group/edit");
				result.addObject("group", group);
			} else
				try {
					this.groupService.save(res);
					result = new ModelAndView("redirect:list.do");
				} catch (final Throwable opps) {
					result = new ModelAndView("redirect:../welcome/index.do");
					result.addObject("messageCode", "group.commit.error");
				}
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "group.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editGroup(@RequestParam final int Id) {
		ModelAndView result;
		Forum group;
		try {
			group = this.groupService.findOne(Id);
			result = new ModelAndView("group/edit");
			result.addObject("group", group);
			final Actor actor = this.actorService.findByPrincipal();
			Assert.isTrue(group.getCreator().equals(actor));
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "group.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayGroup(@RequestParam final int Id) {
		ModelAndView result;
		final Forum group;
		Actor actor = null;
		final Boolean b = true;
		try {
			result = new ModelAndView("group/display");
			group = this.groupService.findOne(Id);
			actor = this.actorService.findByPrincipal();
			if ((((group.getModerator() != actor) && group.getCreator() != actor)) && (group.getIsActive() == false))
				Assert.isTrue(false);
			result.addObject("actor", actor);
			result.addObject("group", group);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "group.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int Id) {
		ModelAndView result;
		try {
			final Film film = this.filmService.findOne(Id);
			final Saga saga = this.sagaService.findOne(Id);
			result = new ModelAndView("group/edit");
			Assert.isTrue(film != null || saga != null);
			Forum group = null;
			if (film != null)
				group = this.groupService.createForFilm(film);
			else if (saga != null)
				group = this.groupService.createForSaga(saga);
			result.addObject("group", group);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "group.commit.error");
		}
		return result;

	}

	// Moderator sctive
	@RequestMapping(value = "/moderator/edit", method = RequestMethod.GET)
	public ModelAndView editModerator(@RequestParam final int Id) {
		ModelAndView result;
		Forum group;
		try {
			group = this.groupService.findOne(Id);
			result = new ModelAndView("group/active");
			result.addObject("group", group);
			Assert.isTrue(group.getModerator() == null);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../../welcome/index.do");
			result.addObject("messageCode", "group.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/moderator/save", params = "active", method = RequestMethod.POST)
	public ModelAndView activeGroup(final Forum group, final BindingResult binding) {
		ModelAndView result;
		try {
			this.groupService.activateGroup(group);
			result = new ModelAndView("redirect:../display.do?Id=" + group.getId());
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../../welcome/index.do");
			result.addObject("messageCode", "group.commit.error");
		}
		return result;
	}
	@RequestMapping(value = "/moderator/save", params = "deactive", method = RequestMethod.POST)
	public ModelAndView deactiveGroup(final Forum group, final BindingResult binding) {
		ModelAndView result;
		try {
			this.groupService.deactivateGroup(group);
			result = new ModelAndView("redirect:../display.do?Id=" + group.getId());
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../../welcome/index.do");
			result.addObject("messageCode", "group.commit.error");
		}
		return result;
	}

	// Film enthusiast request
	@RequestMapping(value = "/filmenthusiast/request", method = RequestMethod.GET)
	public ModelAndView requestGroup(@RequestParam final int Id) {
		ModelAndView result;
		try {
			final Forum group = this.groupService.findOne(Id);
			this.groupService.requestGroup(Id);
			result = new ModelAndView("redirect:../display.do?Id=" + group.getId());
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../../welcome/index.do");
			result.addObject("messageCode", "group.commit.error");
		}
		return result;
	}

	// Film enthusiast group list
	@RequestMapping(value = "/filmenthusiast/listMembers", method = RequestMethod.GET)
	public ModelAndView requestList(@RequestParam final int Id) {
		ModelAndView result;
		Forum group;
		try {
			group = this.groupService.findOne(Id);
			result = new ModelAndView("filmenthusiast/list");
			result.addObject("filmenthusiasts", group.getGroupMembers());
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "group.commit.error");
		}
		return result;
	}

	// Film enthusiast delete member by owner
	@RequestMapping(value = "/filmenthusiast/delete", method = RequestMethod.GET)
	public ModelAndView requesStatus(@RequestParam final int memberId, @RequestParam final int groupId) {
		ModelAndView result;
		try {
			this.groupService.deleteMember(memberId, groupId);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "group.commit.error");
		}
		return result;
	}

}
