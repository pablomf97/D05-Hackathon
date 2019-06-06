
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

	@Query("select e from Event e where e.forum.id=?1")
	Collection<Event> findAllByGroup(int Id);

	@Query("select r from Event r where r.maximumCapacity= (select max(r.maximumCapacity)from Event r)")
	Collection<Event> EventsWithHigeshtMaximumCapacity();
	
	@Query("select e From Event e order by e.attenders.size desc")
	Collection<Event> top3EventsWithMorePeople();
	
	
	


}
