package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Comment;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{
	
	
		@Query("select v from Comment v where v.film.id= ?1")
		Collection<Comment> vPerFilm(int id);
		
		@Query("select v from Comment v where v.forum.id= ?1")
		Collection<Comment> CommentsPerForum(int id);
		
}
