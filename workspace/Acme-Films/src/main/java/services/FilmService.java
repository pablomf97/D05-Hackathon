package services;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.Film;

import repositories.FilmRepository;

@Transactional
@Service
public class FilmService {


	@Autowired
	private FilmRepository filmRepository;


	public Double [] statsCommentsPerFilm(){

		return this.filmRepository.statsCommentsPerFilm();

	}
	public Double[] statsPersonsPerFilm(){
		return this.filmRepository.statsPersonsPerFilm();
	}
	public Collection<Film> filmsWithHighestRating(){
		return this.filmRepository.filmsWithHighestRating();
	}

	public Double ratioFinalModeFilms(){

		return this.filmRepository.ratioFinalModeFilms();
	}
	public Collection<Film> top5FilmsWithMoreRunTime(){
		List<Film> col =(List<Film>) this.filmRepository.top5FilmsWithMoreRunTime();
		return col.subList(0,5);
	}
	public Double[] statsPointsVisualizationPerFilm(){
		return this.filmRepository.statsPointsVisualizationPerFilm();
	}

}
