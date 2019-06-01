package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{

	@Query("select m from Message m where m.sender.id= ?1")
	Collection<Message> messagesSenderByActor(int id);
	@Query("select m from Message m where m.sender.id= ?1 or m.receiver.id=?1")
	Collection<Message> messagesInvolved(int id);
	@Query("select m from Message m where  m.receiver.id=?1")
	Collection<Message> messagesReceiverByActor(int id);
	
	
	
}
