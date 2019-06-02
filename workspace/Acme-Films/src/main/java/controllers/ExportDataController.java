package controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;


import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import domain.Administrator;
import domain.Critic;
import domain.EducationData;
import domain.Film;
import domain.FilmEnthusiast;
import domain.Forum;
import domain.Message;
import domain.MiscellaneousData;
import domain.Moderator;
import domain.ProfessionalData;
import domain.Review;
import domain.SocialProfile;
import domain.Sponsor;
import domain.Sponsorship;

import repositories.MessageRepository;
import services.ActorService;

import services.FilmService;
import services.GroupService;
import services.ReviewService;
import services.SponsorshipService;


@Controller
public class ExportDataController extends AbstractController {

	@Autowired
	private ActorService actorService;

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private FilmService filmService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private SponsorshipService sponsorshipService;
	
	@Autowired
	private MessageRepository messageRepository;
	
	@RequestMapping(value = "administrator/export.do", method = RequestMethod.GET)
	public @ResponseBody
	void downloadFileAdministrator(HttpServletResponse resp) {
		String downloadFileName = "dataUser";
		String res;

		Administrator actor = (Administrator) this.actorService
				.findByPrincipal();

		res = "Data of your user account:";
		res += "\r\n\r\n";
		res += "Name: " + actor.getName() + " \r\n" + "Surname: "
				+ actor.getSurname() + " \r\n" 
				+ " \r\n" + "Photo: " + actor.getPhoto() + " \r\n" + "Email: "
				+ actor.getEmail() + " \r\n" + "Phone Number: "
				+ actor.getPhoneNumber() + " \r\n" + "Address: "
				+ actor.getAddress() + " \r\n" + " \r\n" + "\r\n";
		
		
		
		
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		
		res+="\r\n\r\n";
		
		
		res+="Messages Sender: ";
		res+="\r\n\r\n";
		for(Message s :this.messageRepository.messagesSenderByActor(actor.getId())){
			

			res+="Receiver: "+s.getReceiver().getName() +"\r\n"+"Subject: "+s.getSubject()+ "\r\n"+
					"Body: "+s.getBody()+"\r\n"+"Tag: "+s.getTag() +"\r\n"+
					"Priority: "+s.getPriority() +"\r\n" ;
					;
			
			res+="\r\n"+"----------------------"+"\r\n";
		}
		res += "\r\n\r\n";
		res += "----------------------------------------";
		
		res+="\r\n\r\n";
		res+="Social profiles: \r\n\r\n";
		
		for(SocialProfile s: actor.getSocialProfile()){
			
			res+="Nick: "+s.getNick() +"\r\n"+"Social network: "+s.getSocialNetwork()+ "\r\n" + "Link: "+s.getLink() ;

			res+="\r\n"+"----------------------"+"\r\n";
			
		}
		
		
		
		
		String downloadStringContent = res; // implement this
		
		try {
			OutputStream out = resp.getOutputStream();
			resp.setContentType("text/plain; charset=utf-8");
			resp.addHeader("Content-Disposition", "attachment; filename=\""
					+ downloadFileName + "\"");
			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
			out.flush();
			out.close();
		} catch (IOException e) {
		}

	}
	@RequestMapping(value = "critic/export.do", method = RequestMethod.GET)
	public @ResponseBody
	void downloadFileCritic(HttpServletResponse resp) {
		String downloadFileName = "dataUser";
		String res;

		Critic actor = (Critic) this.actorService
				.findByPrincipal();

		res = "Data of your user account:";
		res += "\r\n\r\n";
		res += "Name: " + actor.getName() + " \r\n" + "Surname: "
				+ actor.getSurname() + " \r\n" 
				+ " \r\n" + "Photo: " + actor.getPhoto() + " \r\n" + "Email: "
				+ actor.getEmail() + " \r\n" + "Phone Number: "
				+ actor.getPhoneNumber() + " \r\n" + "Address: "
				+ actor.getAddress() + " \r\n" + " \r\n" + "\r\n";
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		
		res+="\r\n\r\n";
		
		
		res+="Messages Sender: ";
		res+="\r\n\r\n";
		for(Message s :this.messageRepository.messagesSenderByActor(actor.getId())){
			

			res+="Receiver: "+s.getReceiver().getName() +"\r\n"+"Subject: "+s.getSubject()+ "\r\n"+
					"Body: "+s.getBody()+"\r\n"+"Tag: "+s.getTag() +"\r\n"+
					"Priority: "+s.getPriority() +"\r\n" ;
					;
			
			res+="\r\n"+"----------------------"+"\r\n";
		}
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		
		res+="\r\n\r\n";
		res+="Social profiles: \r\n\r\n";
		
		for(SocialProfile s: actor.getSocialProfile()){
			
			res+="Nick: "+s.getNick() +"\r\n"+"Social network: "+s.getSocialNetwork()+ "\r\n" + "Link: "+s.getLink() ;
			res+="\r\n"+"----------------------"+"\r\n";
			
		}
		if(actor.getCurricula()!=null){
			
			res += "\r\n\r\n";
			res += "----------------------------------------";
			
			res+="\r\n\r\n";
			res+="Curricula: "+"\r\n\r\n";
			res+="Personal Data "+"\r\n\r\n";
			res+="Full name:"+actor.getCurricula().getPersonalData().getFullName()+"\r\n"+"linkedIn: "+actor.getCurricula().getPersonalData().getLinkedIn()+"\r\n"+
					"Statement: "+actor.getCurricula().getPersonalData().getStatement()+"\r\n"+
					"Phone Number: "+actor.getCurricula().getPersonalData().getPhoneNumber()+"\r\n"+
					"Github profile: "+actor.getCurricula().getPersonalData().getGithubProfile()+"\r\n";
			
			
			for(MiscellaneousData m : actor.getCurricula().getMiscellaneousData()){
				res+="Miscellanous Data "+"\r\n\r\n";
				res+="Text: "+m.getText()+"\r\n"+"Attachements: "+m.getAttachements()+"\r\n";
				res+="\r\n\r\n";
			}for(EducationData e : actor.getCurricula().getEducationData()){
				res+="Education Data "+"\r\n\r\n";
				res+="Degree: "+e.getDegree()+"\r\n"+"Institution: "+e.getInstitution()+"\r\n";
				res+="Mark: "+e.getMark()+"\r\n"+"Start Date : "+e.getStartDate()+"\r\n";
				res+="End date: "+e.getEndDate();
				res+="\r\n\r\n";
				
			}
			for(ProfessionalData e :actor.getCurricula().getProfessionalData()){
				res+="Education Data "+"\r\n\r\n";
				res+="Title: "+e.getTitle()+"\r\n"+"Description: "+e.getDescription()+"\r\n";
				res+="Start Date : "+e.getStartDate()+"\r\n";
				res+="End date: "+e.getEndDate();
				res+="\r\n\r\n";
			}
			
		}
		res += "\r\n\r\n";
		res += "----------------------------------------";
		if(this.reviewService.getReviewsByCritic(actor.getId()).size()!=0){
			
			for(Review e:this.reviewService.getReviewsByCritic(actor.getId())){
				
				res+="Review: "+"\r\n\r\n";
				res+="Title: "+e.getTitle()+"\r\n"+"Body: "+e.getBody()+"\r\n";
				res+="Rating: "+e.getRating()+"\r\n"+"Creation date : "+e.getCreationDate()+"\r\n";

				res+="\r\n\r\n";
				
			}
			
		}
		
		
		
		String downloadStringContent = res; // implement this
		
		try {
			OutputStream out = resp.getOutputStream();
			resp.setContentType("text/plain; charset=utf-8");
			resp.addHeader("Content-Disposition", "attachment; filename=\""
					+ downloadFileName + "\"");
			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
			out.flush();
			out.close();
		} catch (IOException e) {
		}

	}
	
	@RequestMapping(value = "moderator/export.do", method = RequestMethod.GET)
	public @ResponseBody
	void downloadFileModerator(HttpServletResponse resp) {
		String downloadFileName = "dataUser";
		String res;

		Moderator actor = (Moderator) this.actorService
				.findByPrincipal();

		res = "Data of your user account:";
		res += "\r\n\r\n";
		res += "Name: " + actor.getName() + " \r\n" + "Surname: "
				+ actor.getSurname() + " \r\n" 
				+ " \r\n" + "Photo: " + actor.getPhoto() + " \r\n" + "Email: "
				+ actor.getEmail() + " \r\n" + "Phone Number: "
				+ actor.getPhoneNumber() + " \r\n" + "Address: "
				+ actor.getAddress() + " \r\n" + " \r\n" + "\r\n";
		
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		
		res+="\r\n\r\n";
		
		
		res+="Messages Sender: ";
		res+="\r\n\r\n";
		for(Message s :this.messageRepository.messagesSenderByActor(actor.getId())){
			

			res+="Receiver: "+s.getReceiver().getName() +"\r\n"+"Subject: "+s.getSubject()+ "\r\n"+
					"Body: "+s.getBody()+"\r\n"+"Tag: "+s.getTag() +"\r\n"+
					"Priority: "+s.getPriority() +"\r\n" ;
					;
			
			res+="\r\n"+"----------------------"+"\r\n";
		}
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		
		res+="\r\n\r\n";
		res+="Social profiles: \r\n\r\n";
		
		for(SocialProfile s: actor.getSocialProfile()){
			
			res+="Nick: "+s.getNick() +"\r\n"+"Social network: "+s.getSocialNetwork()+ "\r\n" + "Link: "+s.getLink() ;

			res+="\r\n"+"----------------------"+"\r\n";
			
		}
		res+="Films created: \r\n\r\n";
		
		for(Film s: this.filmService.filmsByModerator(actor.getId())){
			
			res+="Title: "+s.getTitle() +"\r\n"+"Synopsis: "+s.getSynopsis()
					+ "\r\n" + "Poster: "+s.getPoster() 
					+ "\r\n" + "Release date: "+s.getReleaseDate()
					+ "\r\n" + "Run time: "+s.getRunTime()
					+ "\r\n" + "Rating: "+s.getRating();

			res+="\r\n"+"----------------------"+"\r\n";
		}
		
		
		
		String downloadStringContent = res; // implement this
		
		try {
			OutputStream out = resp.getOutputStream();
			resp.setContentType("text/plain; charset=utf-8");
			resp.addHeader("Content-Disposition", "attachment; filename=\""
					+ downloadFileName + "\"");
			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
			out.flush();
			out.close();
		} catch (IOException e) {
		}

	}

	@RequestMapping(value = "sponsor/export.do", method = RequestMethod.GET)
	public @ResponseBody
	void downloadFileSponsor(HttpServletResponse resp) {
		String downloadFileName = "dataUser";
		String res;

		Sponsor actor = (Sponsor) this.actorService
				.findByPrincipal();
		
		res = "Data of your user account:";
		res += "\r\n\r\n";
		res += "Name: " + actor.getName() + " \r\n" + "Surname: "
				+ actor.getSurname() + " \r\n" 
				+ " \r\n" + "Photo: " + actor.getPhoto() + " \r\n" + "Email: "
				+ actor.getEmail() + " \r\n" + "Phone Number: "
				+ actor.getPhoneNumber() + " \r\n" + "Address: "
				+ actor.getAddress() + " \r\n" + " \r\n" + "\r\n";
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		
		res+="\r\n\r\n";
		
		
		res+="Messages Sender: ";
		res+="\r\n\r\n";
		for(Message s :this.messageRepository.messagesSenderByActor(actor.getId())){
			

			res+="Receiver: "+s.getReceiver().getName() +"\r\n"+"Subject: "+s.getSubject()+ "\r\n"+
					"Body: "+s.getBody()+"\r\n"+"Tag: "+s.getTag() +"\r\n"+
					"Priority: "+s.getPriority() +"\r\n" ;
					;
			
			res+="\r\n"+"----------------------"+"\r\n";
		}
		
		
		
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		
		res+="\r\n\r\n";
		res+="Social profiles: \r\n\r\n";
		
		for(SocialProfile s: actor.getSocialProfile()){
			
			res+="Nick: "+s.getNick() +"\r\n"+"Social network: "+s.getSocialNetwork()+ "\r\n" + "Link: "+s.getLink() ;

			res+="\r\n"+"----------------------"+"\r\n";
			
		}
		res += "\r\n\r\n";
		res += "----------------------------------------";
		
		res+="\r\n\r\n";
		res+="Sponsorships created: \r\n\r\n";
		
		for(Sponsorship s :  this.sponsorshipService.sponsorshipPerSponsor(actor.getId())){
			
			res+="Title: "+s.getTitle() +"\r\n"+"banner: "+s.getBanner()+ "\r\n" + "Target page: "+s.getTargetPage() ;
			

			res+="\r\n"+"----------------------"+"\r\n";
			
		}
		
		
		
		
		
		String downloadStringContent = res; // implement this
		
		try {
			OutputStream out = resp.getOutputStream();
			resp.setContentType("text/plain; charset=utf-8");
			resp.addHeader("Content-Disposition", "attachment; filename=\""
					+ downloadFileName + "\"");
			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
			out.flush();
			out.close();
		} catch (IOException e) {
		}

	}

	@RequestMapping(value = "filmEnthusiast/export.do", method = RequestMethod.GET)
	public @ResponseBody
	void downloadFileFilmEnthusiast(HttpServletResponse resp) {
		String downloadFileName = "dataUser";
		String res;

		FilmEnthusiast actor = (FilmEnthusiast) this.actorService
				.findByPrincipal();

		res = "Data of your user account:";
		res += "\r\n\r\n";
		res += "Name: " + actor.getName() + " \r\n" + "Surname: "
				+ actor.getSurname() + " \r\n" 
				+ " \r\n" + "Photo: " + actor.getPhoto() + " \r\n" + "Email: "
				+ actor.getEmail() + " \r\n" + "Phone Number: "
				+ actor.getPhoneNumber() + " \r\n" + "Address: "
				+ actor.getAddress() + " \r\n" + " \r\n" + "\r\n";
		
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		
		res+="\r\n\r\n";
		
		
		res+="Messages Sender: ";
		res+="\r\n\r\n";
		for(Message s :this.messageRepository.messagesSenderByActor(actor.getId())){
			

			res+="Receiver: "+s.getReceiver().getName() +"\r\n"+"Subject: "+s.getSubject()+ "\r\n"+
					"Body: "+s.getBody()+"\r\n"+"Tag: "+s.getTag() +"\r\n"+
					"Priority: "+s.getPriority() +"\r\n" ;
					;
			
			res+="\r\n"+"----------------------"+"\r\n";
		}
		
		
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		
		res+="\r\n\r\n";
		res+="Social profiles: \r\n\r\n";
		
		for(SocialProfile s: actor.getSocialProfile()){
			
			res+="Nick: "+s.getNick() +"\r\n"+"Social network: "+s.getSocialNetwork()+ "\r\n" + "Link: "+s.getLink() ;

			res+="\r\n"+"----------------------"+"\r\n";
			
		}
		res+="\r\n\r\n";
		res+="Groups created: \r\n\r\n";
		
		for(Forum s :this.groupService.findAllByFilmEnthusiast(actor.getId())){
			
			res+="Name: "+s.getName() +"\r\n"+"Description: "+s.getDescription()+ "\r\n" + "Creation date: "+s.getCreationDate()
					+ "\r\n" + "Reject reason: "+s.getRejectReason();
			

			res+="\r\n"+"----------------------"+"\r\n";
			
		}
		
		
		
		
		
		String downloadStringContent = res; // implement this
		
		try {
			OutputStream out = resp.getOutputStream();
			resp.setContentType("text/plain; charset=utf-8");
			resp.addHeader("Content-Disposition", "attachment; filename=\""
					+ downloadFileName + "\"");
			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
			out.flush();
			out.close();
		} catch (IOException e) {
		}

	}




	}
