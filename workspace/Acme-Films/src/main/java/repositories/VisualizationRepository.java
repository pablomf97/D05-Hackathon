package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Visualization;

@Repository
public interface VisualizationRepository extends JpaRepository<Visualization, Integer>{

}
