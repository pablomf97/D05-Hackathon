package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Forum;

@Repository
public interface GroupRepository extends JpaRepository<Forum, Integer>{

}
