package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import domain.Comment;
@Controller
@RequestMapping(value="comment/filmEnthusiast")
public class CommentController extends AbstractController{
	
	//Services
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private ActorService actorService;
	
	//Create
	
	@RequestMapping(value="/create" , method = RequestMethod.GET)
	public ModelAndView create(){
		ModelAndView result;
		Comment comment = this.commentService.create();
		
		result = new ModelAndView("comment/create");
		
		return result;
		
	}
	
	//POST METHODS
	
	@RequestMapping(value="/create", method = RequestMethod.POST, params="save")
	public ModelAndView save(final Comment comment, final BindingResult binding){
		ModelAndView result;
		Comment validComment;
		
		validComment = this.commentService.reconstruct(validComment, comment.getFilm(), comment.getForum(), binding);
		
		if(binding.hasErrors()){
			result = new ModelAndView("comment/create");
			
			result.addObject("error",comment.binding.error);
		}else{
			try{
				this.commentService.save(validComment);
				result = new ModelAndView("redirec:/welcome.index");
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
				result = new ModelAndView("redirec:/welcome.index");
			}catch(Throwable oops){
				result = new ModelAndView("comment/create");
				
				result.addObject("error",comment.commit.error);
			}
		}
		
		return result;
	}
	
}
