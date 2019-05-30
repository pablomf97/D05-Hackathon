package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import services.FilmService;
import domain.Comment;
import domain.Film;
import domain.FilmEnthusiast;
import domain.Forum;
@Controller
@RequestMapping(value="comment/filmEnthusiast")
public class CommentController extends AbstractController{
	
	//Services
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private FilmService filmService;
	
	@Autowired
	private ForumService forumService;
	
	//Create
	
	@RequestMapping(value="/createFilm" , method = RequestMethod.GET)
	public ModelAndView createFilm(){
		ModelAndView result;
		FilmEnthusiast principal = (FilmEnthusiast)this.actorService.findByPrincipal();
		Collection<Film> films = this.filmService.findAll();

		
		Comment comment = this.commentService.create();
		
		boolean possible = false;
		if(comment.getFilmEnthusiast().equals(principal)){
			possible = true;
		}
		
		result = new ModelAndView("comment/createFilm");
		result.addObject("possible", possible);
		result.addObject("films", films);

		return result;
		
	}
	
	@RequestMapping(value="/createForum" , method = RequestMethod.GET)
	public ModelAndView createForum(){
		ModelAndView result;
		FilmEnthusiast principal = (FilmEnthusiast)this.actorService.findByPrincipal();
		Collection<Film> forums = this.forumService.findAll();

		
		Comment comment = this.commentService.create();
		
		boolean possible = false;
		if(comment.getFilmEnthusiast().equals(principal)){
			possible = true;
		}
		
		result = new ModelAndView("comment/createForum");
		result.addObject("possible", possible);
		result.addObject("forums", forums);

		return result;
		
	}
	
	
	//POST METHODS
	
	@RequestMapping(value="/createFilm", method = RequestMethod.POST, params="save")
	public ModelAndView saveFilm(@Valid final Comment comment, final BindingResult binding){
		ModelAndView result;
		Comment validComment;
		
		if(binding.hasErrors()){
			result = new ModelAndView("comment/create");
			
			result.addObject("error",comment.binding.error);
		}else{
			try{
				Film film = comment.getFilm();
				Forum forum = comment.getForum();
				
				this.commentService.save(validComment,film,forum);
				result = new ModelAndView("redirect:/welcome.index");
			}catch(Throwable oops){
				result = new ModelAndView("comment/create");
				
				result.addObject("error",comment.commit.error);
			}
		}
		
		return result;
	}
	
	
	@RequestMapping(value="/createForum", method = RequestMethod.POST, params="save")
	public ModelAndView saveForum(@Valid final Comment comment, final BindingResult binding){
		ModelAndView result;
		Comment validComment;
		
		if(binding.hasErrors()){
			result = new ModelAndView("comment/create");
			
			result.addObject("error",comment.binding.error);
		}else{
			try{
				Film film = comment.getFilm();
				Forum forum = comment.getForum();
				
				this.commentService.save(validComment,film,forum);
				result = new ModelAndView("redirect:/welcome.index");
			}catch(Throwable oops){
				result = new ModelAndView("comment/create");
				
				result.addObject("error",comment.commit.error);
			}
		}
		
		return result;
	}
	
	@RequestMapping(value="/create", method = RequestMethod.POST, params="delete")
	public ModelAndView delete(@Valid final Comment comment, final BindingResult binding){
		ModelAndView result;
		
		
		if(binding.hasErrors()){
			result = new ModelAndView("comment/create");
			
			result.addObject("error",comment.binding.error);
		}else{
			try{
				this.commentService.delete(comment);
				result = new ModelAndView("redirect:/welcome.index");
			}catch(Throwable oops){
				result = new ModelAndView("comment/create");
				
				result.addObject("error",comment.commit.error);
			}
		}
		
		return result;
	}
	
	public ModelAndView display(@RequestParam final int id){
		ModelAndView result;
		Comment comment;
		FilmEnthusiast principal = (FilmEnthusiast)this.actorService.findByPrincipal();
		comment = this.commentService.findOne(id);
		boolean possible = false;
		if(comment.getFilmEnthusiast().equals(principal)){
			possible = true;
		}
		
		result = new ModelAndView("comment/display");
		result.addObject("comment", comment);
		result.addObject("possible", possible);
	}
	
}
