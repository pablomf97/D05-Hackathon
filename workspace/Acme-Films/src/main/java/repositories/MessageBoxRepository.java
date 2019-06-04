package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.MessageBox;

@Repository
public interface MessageBoxRepository extends
		JpaRepository<MessageBox, Integer> {

	@Query("select b from MessageBox b join b.owner o where o.id = ?1")
	public Collection<MessageBox> boxesByActor(int actorid);

	@Query("select b from MessageBox b join b.owner o where o.id = ?1 and b.parentMessageBox.id = null")
	public Collection<MessageBox> firstBoxesByActor(int actorid);

	@Query("select b from MessageBox b where b.parentMessageBox.id = ?1")
	public Collection<MessageBox> findByParent(int boxId);

	@Query("select b from MessageBox b where b.owner.id = ?1 and b.name = ?2")
	public MessageBox boxByName(int actorid, String name);
}
