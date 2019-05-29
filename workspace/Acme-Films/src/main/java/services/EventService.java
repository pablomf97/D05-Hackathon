package services;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.Event;

import repositories.EventRepository;

@Transactional
@Service
public class EventService {
		
	
	@Autowired
	private EventRepository eventRepository;
	
	public  Collection<Event> EventsWithHigeshtMaximumCapacity(){
		return this.eventRepository.EventsWithHigeshtMaximumCapacity();
	}
	public Collection<Event> top3EventsWithMorePeople(){
		List<Event> l=(List<Event>)this.eventRepository.top3EventsWithMorePeople();
		return l.subList(0, 3);
	}
}
