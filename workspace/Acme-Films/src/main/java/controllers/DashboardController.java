package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Critic;
import domain.Event;
import domain.Film;
import domain.SocialProfile;

import services.CriticService;
import services.EventService;
import services.FilmService;
import services.FinderService;
import services.ModeratorService;
import services.ReviewService;
import services.SocialProfileService;
import services.SponsorService;

@Controller
@RequestMapping(value = "statistics/administrator")
public class DashboardController extends AbstractController{
	
	
	@Autowired
	private FinderService finderService;
	
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private SocialProfileService socialProfile;
	@Autowired
	private SponsorService sponsorService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private FilmService filmService;
	
	@Autowired
	private CriticService criticService;
	
	@Autowired
	private ModeratorService moderatorService;
	
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;
		
		
		Double RatioFindersEmpty=this.finderService.RatioFindersEmpty();
		Double[] StatsFinder=this.finderService.StatsFinder();
		Double [] statsCommentsPerFilm=this.filmService.statsCommentsPerFilm();
		Double[] statsPersonsPerFilm=this.filmService.statsPersonsPerFilm();
		Collection<Film> filmsWithHighestRating=this.filmService.filmsWithHighestRating();
		Double ratioFinalModeFilms=this.filmService.ratioFinalModeFilms();
		Collection<Film> top5FilmsWithMoreRunTime=this.filmService.top5FilmsWithMoreRunTime();
		Double[] statsPointsVisualizationPerFilm=this.filmService.statsPointsVisualizationPerFilm();
		
		Collection<Event> EventsWithHigeshtMaximumCapacity=this.eventService.EventsWithHigeshtMaximumCapacity();
		Collection<Critic> top3CriticsMoreProfessional=this.criticService.top3CriticsMoreProfessional();
		Collection<Critic> criticsWithHighestRatingReview=this.criticService.criticsWithHighestRatingReview();
		Integer socialProfilesInSystem=this.socialProfile.socialProfilesInSystem();
		Double[] statsSponsorshipsPerSponsor=this.sponsorService.statsSponsorshipsPerSponsor();
		Double[] statsReviewsPerModerator=this.moderatorService.statsReviewsPerModerator();
		Collection<Event> top3EventsWithMorePeople=this.eventService.top3EventsWithMorePeople();
		result = new ModelAndView("administrator/statistics");
		

		result.addObject("requestURI", "statistics/administrator/display.do");
		result.addObject("top3EventsWithMorePeople",top3EventsWithMorePeople);
		result.addObject("statsReviewsPerModerator",statsReviewsPerModerator);
		result.addObject("statsSponsorshipsPerSponsor",statsSponsorshipsPerSponsor);
		result.addObject("socialProfilesInSystem",socialProfilesInSystem);
		result.addObject("criticsWithHighestRatingReview",criticsWithHighestRatingReview);
		result.addObject("top3CriticsMoreProfessional",top3CriticsMoreProfessional);
		result.addObject("EventsWithHigeshtMaximumCapacity",EventsWithHigeshtMaximumCapacity);
		result.addObject("statsPointsVisualizationPerFilm",statsPointsVisualizationPerFilm);
		result.addObject("top5FilmsWithMoreRunTime",top5FilmsWithMoreRunTime);
		result.addObject("ratioFinalModeFilms",ratioFinalModeFilms);
		result.addObject("filmsWithHighestRating",filmsWithHighestRating);
		result.addObject("statsPersonsPerFilm",statsPersonsPerFilm);
		result.addObject("statsCommentsPerFilm",statsCommentsPerFilm);
		result.addObject("finder",StatsFinder);
		result.addObject("RatioFindersEmpty",RatioFindersEmpty);
	
		
		return result;
	}
	

}

