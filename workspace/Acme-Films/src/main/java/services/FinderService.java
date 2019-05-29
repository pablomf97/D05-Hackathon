package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import domain.Actor;
import domain.Film;
import domain.FilmEnthusiast;
import domain.Finder;

import repositories.FinderRepository;

@Transactional
@Service
public class FinderService {
	

	// Managed repository ------------------------------
	@Autowired
	private FinderRepository finderRepository;

	// Supporting services -----------------------
	@Autowired
	private ActorService actorService;

/*
	@Autowired
	private SystemConfigurationService systemConfigurationService;

*/
	
	// Constructors
	public FinderService() {
		super();
	}

	public Finder create(){
		Finder result;
		Actor principal;


		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "FILMENTHUSIAST"),
				"not.allowed");
		result=new Finder();
		result.setResults(new ArrayList<Film>());
		return result;
	}

	// /FINDONE
	public Finder findOne(final int finderId) {
		Finder result;

		result = this.finderRepository.findOne(finderId);


		return result;
	}

	// FINDALL
	public Collection<Finder> findAll() {
		Collection<Finder> result;
		result = this.finderRepository.findAll();


		return result;

	}
	
	public Finder save(Finder finder){
		Finder result;

		FilmEnthusiast principal;
		Date currentMoment;
		currentMoment = new Date(System.currentTimeMillis() - 1);

		principal = (FilmEnthusiast)this.actorService.findByPrincipal();
		Assert.isTrue(
		this.actorService.checkAuthority(principal, "FILMENTHUSIAST"),
			"not.allowed");
		Assert.isTrue(principal.getFinder().equals(finder),"not.allowed");
		Assert.notNull(finder, "not.allowed");

		finder.setSearchMoment(currentMoment);
		result = this.finderRepository.save(finder);
		Assert.notNull(result, "not.null");

		return result;
	}
	
	public void delete(Finder finder){
		FilmEnthusiast principal;


		principal = (FilmEnthusiast)this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "FILMENTHUSIAST"),
				"not.allowed");
		Assert.isTrue(finder.getId()!=0);
		Assert.isTrue(principal.getFinder().equals(finder),"not.allowed");
		finder.setResults(null);
		finder.setKeyWord(null);
		finder.setMaximumDuration(null);
		finder.setMinimumRating(null);
		finder.setSearchMoment(null);//Watch out!
		finder.setMaximumRating(null);

		this.finderRepository.save(finder);
	}

	//Ancillary methods

		public void deleteExpiredFinder(Finder finder){
			Date maxLivedMoment = new Date();
			int timeChachedFind;
			Date currentMoment;
			currentMoment = new Date(System.currentTimeMillis() - 1);

/*			timeChachedFind = this.systemConfigurationService
					.findMySystemConfiguration().getTimeResultsCached();
			maxLivedMoment = DateUtils.addHours(currentMoment, -timeChachedFind);
			*/if (finder.getSearchMoment().before(maxLivedMoment)) {

				finder.setResults(null);
				finder.setKeyWord(null);
				finder.setMaximumDuration(null);
				finder.setMinimumRating(null);
				finder.setSearchMoment(null);//Watch out!
				finder.setMaximumRating(null);

				this.finderRepository.save(finder);


			}
		}
		public Collection<Film> search(Finder finder){

			Collection<Film> results=new ArrayList<Film>();
			String keyWord;
			Double maximumDuration;
			Double minimumRating;
			Double maximumRating;
			int nResults;
			

			Collection<Film> resultsPageables = new ArrayList<Film>();

			nResults =10; //this.systemConfigurationService.findMySystemConfiguration()
				//	.getMaxResults();
			keyWord = (finder.getKeyWord() == null || finder.getKeyWord().isEmpty()) ? ""
					: finder.getKeyWord();

			minimumRating=(finder.getMinimumRating() == null ) ? 0.0
					: finder.getMinimumRating();
			maximumRating=(finder.getMaximumRating() == null ) ? 10.0
					: finder.getMaximumRating();
			maximumDuration=(finder.getMaximumDuration() == null ) ? 1000.0
					: finder.getMaximumDuration();


		

			if((finder.getKeyWord()==null||finder.getKeyWord().isEmpty())&& 
					finder.getMinimumRating()==null&&
					finder.getMaximumRating()==null&& 
					finder.getMaximumDuration()==null){
				results=this.allFilmsFinal();
			}else{
				
					
					results=this.finderRepository.search(keyWord, maximumDuration, minimumRating, maximumRating);
					List<Film> r = new ArrayList<Film>();
					r.addAll(this.finderRepository.searchV(keyWord, maximumDuration, minimumRating, maximumRating));
					
					Set <Film> s=new HashSet<Film>(r);
					for(Film f :s){
						results.add(f);
					}
				
				
			}
			

			int count=0;

			for(Film p : results){
				resultsPageables.add(p);
				count++;
				if(count>=nResults){
					break;
				}
			}
			finder.setResults(resultsPageables);


			this.save(finder);


			return resultsPageables;
			
		}

		public Collection<Film> allFilmsFinal(){
			return this.finderRepository.allFilmsFinal();
		}
		
		public Double RatioFindersEmpty(){
			return this.finderRepository.RatioFindersEmpty();
		}
	public Double[] StatsFinder(){
		return this.finderRepository.StatsFinder();
	}
}
