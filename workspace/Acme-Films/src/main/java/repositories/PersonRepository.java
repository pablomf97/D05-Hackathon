package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>{

}
