package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Saga;

@Repository
public interface SagaRepository extends JpaRepository<Saga, Integer>{
	
}
