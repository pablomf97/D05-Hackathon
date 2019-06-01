package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.GenreRepository;
import domain.Actor;
import domain.Film;
import domain.Genre;

@Service
@Transactional
public class GenreService {

	// Managed Repository

	@Autowired
	private GenreRepository genreRepository;
	// Supporting Services

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private FilmService filmService;

	@Autowired
	private Validator validator;

	// Simple CRUD methods

	public Genre create() {
		Actor principal;
		Genre res;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "MODERATOR"),
				"not.allowed");
		
		res = new Genre();
		return res;
	}

	public Genre findOne(final int genreId) {
		Genre res;

		res = this.genreRepository.findOne(genreId);

		return res;
	}

	public Collection<Genre> findAll() {
		Collection<Genre> res;

		res = this.genreRepository.findAll();
		Assert.notNull(res, "no.genres");

		return res;
	}

	public Genre save(Genre genre) {
		Actor principal;
		Genre res;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "MODERATOR"),
				"not.allowed");

		Assert.notNull(genre, "null.genre");
		Assert.isTrue(!(genre.getName().get("Español").isEmpty() || genre
				.getName().get("English").isEmpty()), "genre.name.empty");

		res = this.genreRepository.save(genre);

		return res;
	}

	public void delete(Genre genre) {
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "MODERATOR"),
				"not.allowed");

		Assert.notNull(genre, "null.genre");

		this.genreRepository.delete(genre);
	}

	// Other business methods

	/* Reconstruct */
	public Genre reconstruct(Genre genre, String nameES,
			String nameEN, BindingResult binding) {
		Genre res;

		if (genre.getId() == 0) {
			genre.setName(new HashMap<String, String>());

			genre.getName().put("Español", nameES);
			genre.getName().put("English", nameEN);
			res = genre;
		} else {
			res = this.genreRepository.findOne(genre.getId());

			genre.setName(new HashMap<String, String>());

			genre.getName().put("Español", nameES);
			genre.getName().put("English", nameEN);

			res.setName(genre.getName());
		}
		this.validator.validate(res, binding);

		return res;
	}

	public Genre findByName(String name) {
		Genre res;
		Collection<Genre> genres;

		res = null;
		genres = this.findAll();
		for (Genre genre : genres) {
			if (genre.getName().containsValue(name)) {
				res = genre;
				break;
			}
		}
		return res;
	}
	
	public Collection<String> nameEsGenres(){
		Collection<String> result = new ArrayList<String>();
		Collection<Genre> genres;
		genres = this.findAll();
		
		for(Genre genre : genres){
			result.add("'"+ genre.getName().get("Español")+"'");
		}
		return result;
	}
	
	public Collection<String> nameEnGenres(){
		Collection<String> result = new ArrayList<String>();
		Collection<Genre> genres;
		genres = this.findAll();
		
		for(Genre genre : genres){
			result.add("'"+ genre.getName().get("English")+"'");
		}
		return result;		
	}
	
	public void deleteGenreFromFilms (Genre genre) {
		Collection<Film> films;
		
		films = this.filmService.filmsWithGenre(genre.getId());
		
		for(Film film : films) {
			Collection<Genre> genres = film.getGenres();
			genres.remove(genre);
			film.setGenres(genres);
		}
	}
	
	public Collection<Genre> parseGenres (String [] array) {
		Collection<Genre> result = new ArrayList<>();
		String a = null;
		Integer n = 0;
		Genre genre = null;
		
		for (int i = 0; i <= array.length - 1; i++) {
			a = array[i];
			n = Integer.parseInt(a);
			genre = this.findOne(n);
			result.add(genre);
		}
		return result;
	}
}
