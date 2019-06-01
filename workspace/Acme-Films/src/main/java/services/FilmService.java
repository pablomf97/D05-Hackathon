package services;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FilmRepository;
import domain.Actor;
import domain.Film;
import domain.Genre;
import domain.Moderator;
import domain.Person;
import domain.Saga;

@Transactional
@Service
public class FilmService {


	@Autowired
	private FilmRepository filmRepository;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private Validator validator;
	
	public Film create() {
		Actor principal;
		Film result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MODERATOR"),
				"not.allowed");

		result = new Film();
		result.setGenres(new ArrayList<Genre>());
		result.setPersons(new ArrayList<Person>());
		result.setSagas(new ArrayList<Saga>());
		result.setModerator((Moderator) principal);
		
		return result;
	}

	public Collection<Film> findAll() {
		Collection<Film> result;
		result = this.filmRepository.findAll();

		return result;
	}

	public Film findOne(final int filmId) {
		Film result;
		result = this.filmRepository.findOne(filmId);

		return result;
	}
	
	public Film save(final Film film) {
		Actor principal;
		Film result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MODERATOR"), "not.allowed");
		
		Assert.notNull(film.getTitle());
		Assert.notNull(film.getSynopsis());
		Assert.notNull(film.getReleaseDate());
		
		result = this.filmRepository.save(film);

		return result;
	}
	
	public void delete(final Film film) {
		Actor principal;

		Assert.notNull(film);
		Assert.isTrue(film.getId() != 0, "wrong.id");

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MODERATOR"),
				"not.allowed");

		this.filmRepository.delete(film.getId());
	}
	
	// Other business methods -------------------------------
	
	public Film reconstruct(final Film film,
			 BindingResult binding) {
		Film result;
		Actor principal;
		
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "MODERATOR"),
				"not.allowed");
		
		if (film.getId() == 0) {
			
			Assert.notNull(film.getTitle());
			Assert.notNull(film.getSynopsis());
			Assert.notNull(film.getReleaseDate());
			
			result = this.create();
			film.setTicker(this.generateTicker(film.getReleaseDate()));
		} else {
			result = this.findOne(film.getId());
		}
		
		if(film.getIsDraft()) {
			result.setTitle(film.getTitle());
			result.setSynopsis(film.getSynopsis());
			result.setPoster(film.getPoster());
			result.setReleaseDate(film.getReleaseDate());
			result.setRunTime(film.getRunTime());
			result.setGenres(film.getGenres());
		}
		
		result.setPersons(film.getPersons());
		result.setSagas(film.getSagas());
		
		this.validator.validate(result, binding);
		
		return result;
	}
	

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

	public String generateTicker(Date releaseDate) {
		String uniqueTicker = null;
		Calendar date;
		String year, month, day, alphaNum, todayDate;
		boolean unique = false;

		date = Calendar.getInstance();
		date.setTime(releaseDate);
		year = String.valueOf(date.get(Calendar.YEAR));
		year = year.substring(year.length() - 2, year.length());
		month = String.valueOf(date.get(Calendar.MONTH) + 1);
		if (month.length() == 1) {
			month = "0" + month;
		}
		day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
		if (day.length() == 1) {
			day = "0" + day;
		}

		while (unique == false) {
			alphaNum = this.randomString();
			todayDate = year + month + day;
			uniqueTicker = todayDate + "-" + alphaNum;
			for (final Film film : this.findAll())
				if (film.getTicker().equals(uniqueTicker))
					continue;
			unique = true;
		}
		return uniqueTicker;
	}
	
	public String randomString() {

		final String possibleChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final SecureRandom rnd = new SecureRandom();
		final int length = 6;

		final StringBuilder stringBuilder = new StringBuilder(length);

		for (int i = 0; i < length; i++)
			stringBuilder.append(possibleChars.charAt(rnd.nextInt(possibleChars
					.length())));
		return stringBuilder.toString();

	}
	
	public Collection<Film> filmsOfSaga (int sagaId) {
		Collection<Film> result;
		
		result = this.filmRepository.filmsOfSaga(sagaId);
		
		return result;
	}
	
	public Collection<Film> filmsOfPerson (int personId) {
		Collection<Film> result;
		
		result = this.filmRepository.filmsOfPerson(personId);
		
		return result;
	}
	
	public Collection<Film> filmsWithGenre(int genreId) {
		Collection<Film> result;
		
		result = this.filmRepository.filmsWithGenre(genreId);
		
		return result;
	}

}
