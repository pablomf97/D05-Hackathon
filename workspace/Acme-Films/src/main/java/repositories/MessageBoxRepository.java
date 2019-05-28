package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.MessageBox;

@Repository
public interface MessageBoxRepository extends JpaRepository<MessageBox, Integer>{

	@Query("select m from MessageBox m, Actor a where(m member of a.messageBoxes and m.name='Out box' and a.id=?1)")
	MessageBox findOutBoxActorId(int actorId);

	@Query("select m from MessageBox m, Actor a where(m member of a.messageBoxes and m.name='In box' and a.id=?1)")
	MessageBox findInBoxActorId(int actorId);

	@Query("select m from MessageBox m, Actor a where(m member of a.messageBoxes and m.name='Spam box' and a.id=?1)")
	MessageBox findSpamBoxActorId(int actorId);

	@Query("select m from MessageBox m, Actor a where(m member of a.messageBoxes and m.name='Trash box' and a.id=?1)")
	MessageBox findTrashBoxActorId(int actorId);
	
	@Query("select m from MessageBox m, Actor a where(m member of a.messageBoxes and m.name='Notification box' and a.id=?1)")
	MessageBox findNotificationBoxActorId(int actorId);
}
