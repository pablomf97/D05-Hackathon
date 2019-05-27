package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CommentRepository;
import domain.Comment;
import domain.Film;
import domain.FilmEnthusiast;
import domain.Forum;

@Service
@Transactional
public class CommentService {

	//Repository
	@Autowired
	private CommentRepository commentRepository;

	//Services
	@Autowired
	private ActorService actorService;

	@Autowired
	private Validator validator;

	//CRUD METHODS

	public Comment create(){
		Comment result = new Comment();
		FilmEnthusiast principal = (FilmEnthusiast) this.actorService.findByPrincipal();

		result.setPublishedMoment(new Date(System.currentTimeMillis()-1));
		result.setFilmEnthusiast(principal);

		return result;
	}

	public Comment save(final Comment comment){
		Comment result;
		FilmEnthusiast principal = this.actorService.findByPrncipal();

		Assert.isTrue(principal.getId() == comment.getFilmEnthusiast().getId());
		Assert.isTrue(comment.getId()==0);

		if(comment.getForum() != null){
			Assert.isTrue(comment.getForum().getGroupMembers().contains(comment.getFilmEnthusiast()));
		}

		comment.setPublishedMoment(new Date(System.currentTimeMillis()-1));


		result = this.commentRepository.save(comment);

		return result;

	}

	public void delete(final Comment comment){

		FilmEnthusiast principal = this.actorService.findByPrncipal();

		Assert.isTrue(principal.getId() == comment.getFilmEnthusiast().getId());
		Assert.isTrue(comment.getId()!=0);

		this.commentRepository.delete(comment);
	}

	public Comment reconstruct(Comment comment,Film film,Forum forum,BindingResult binding){

		Assert.isTrue(comment.getId() == 0);

		if(film != null){
			comment.setFilm(film);
		}else if(forum != null){
			comment.setForum(forum);
		}

		this.validator.validate(comment, binding);
	}

	public Comment findOne(int commentId){
		Comment result = this.commentRepository.findOne(commentId);

		return result;
	}
	
	public Collection<Comment> findAll(){
		Collection<Comment> result = this.commentRepository.findAll();
		
		return result;
	}
}