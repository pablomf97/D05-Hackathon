package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>{
	
	@Query("select p from Person p join p.positions pos where pos.id = ?1")
	Collection<Person> personsWithPosition(int positionId);

}
