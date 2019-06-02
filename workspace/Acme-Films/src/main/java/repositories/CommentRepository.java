package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Comment;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{
<<<<<<< HEAD

	@Query("select c from Comment c where c.filmEnthusiast.id = ?1")
	public Collection<Comment> commentsByOwner(int id);
=======
	
	
		@Query("select v from Comment v where v.film.id= ?1")
		Collection<Comment> vPerFilm(int id);
>>>>>>> 352761bd927b6c29aa0296214608145ceb1d9286
}
