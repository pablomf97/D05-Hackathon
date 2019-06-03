package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

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

	// Repository
	@Autowired
	private CommentRepository commentRepository;

	// Services
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private Validator validator;

	
	// CRUD METHODS
	public Comment create() {
		Comment result = new Comment();
		FilmEnthusiast principal = (FilmEnthusiast) this.actorService
				.findByPrincipal();

		result.setPublishedMoment(new Date(System.currentTimeMillis() - 1));
		result.setFilmEnthusiast(principal);

		return result;
	}

	public Comment save(final Comment comment) {
		Comment result;
		FilmEnthusiast principal = (FilmEnthusiast) this.actorService
				.findByPrincipal();

		Assert.isTrue(principal.getId() == comment.getFilmEnthusiast().getId());
		Assert.isTrue(comment.getId() == 0);
	
		Assert.isTrue((comment.getFilm() != null) || (comment.getForum()!= null), "Need select one at least.");
		comment.setPublishedMoment(new Date(System.currentTimeMillis() - 1));


		result = this.commentRepository.save(comment);

		return result;

	}

	public void delete(final Comment comment) {

		FilmEnthusiast principal = (FilmEnthusiast) this.actorService
				.findByPrincipal();

		Assert.isTrue(principal.getId() == comment.getFilmEnthusiast().getId());
		Assert.isTrue(comment.getId() != 0);

		this.commentRepository.delete(comment);
	}
	
	public Comment reconstruct(final Comment comment , BindingResult binding){
		
		
		if(comment.getId()!=0){
			Comment bd = this.findOne(comment.getId());
			
			comment.setForum(bd.getForum());
			comment.setFilm(bd.getFilm());
			comment.setRating(bd.getRating());
			comment.setBody(bd.getBody());
			comment.setFilmEnthusiast((FilmEnthusiast)this.actorService.findByPrincipal());
			comment.setPublishedMoment(new Date(System.currentTimeMillis()-1));
			
		}else{
			comment.setFilmEnthusiast((FilmEnthusiast)this.actorService.findByPrincipal());
			comment.setPublishedMoment(new Date(System.currentTimeMillis()-1));
			
		}
		
		
		this.validator.validate(comment, binding);
		
		if(binding.hasErrors()){
			throw new ValidationException();
		}
		
		return comment;
	}

	public Comment findOne(int commentId) {
		Comment result = this.commentRepository.findOne(commentId);

		return result;
	}

	public Collection<Comment> findAll() {
		Collection<Comment> result = this.commentRepository.findAll();

		return result;
	}
	

	public Collection<Comment> getCommentsByOwner(int id){
		
		Collection<Comment> result = this.commentRepository.commentsByOwner(id);
		
		return result;
	}

	public void deleteCommentsPerFilms(int filmId){
		this.commentRepository.deleteInBatch(this.commentRepository.vPerFilm(filmId));

	}
}