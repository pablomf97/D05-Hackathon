package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>{

	@Query("select r from Event r where r.maximumCapacity= (select max(r.maximumCapacity)from Event r)")
	Collection<Event> EventsWithHigeshtMaximumCapacity();
}
