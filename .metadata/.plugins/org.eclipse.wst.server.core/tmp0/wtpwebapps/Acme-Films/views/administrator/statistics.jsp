
<%--
 * login.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>


<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<security:authorize access="hasRole('ADMIN')">
	<table class="displayStyle" >
			<tr>
				<th colspan="2"><spring:message
						code="administrator.statistics" /></th>
			</tr>
			
			
			<tr>
				<td><spring:message code="administrator.emptyFinder" /></td>
				<td style="text-align: right">${RatioFindersEmpty}</td>
			</tr>
			<tr>
				<td><spring:message code="administrator.statsCommentsPerFilm" /></td>
				<td style="text-align: right"> ${statsCommentsPerFilm[0]}|${statsCommentsPerFilm[1]}|${statsCommentsPerFilm[2]}|${statsCommentsPerFilm[3]}</td>
			</tr>
				<tr>
				<td><spring:message code="administrator.statsPersonsPerFilm" /></td>
				<td style="text-align: right"> ${statsPersonsPerFilm[0]}|${statsPersonsPerFilm[1]}|${statsPersonsPerFilm[2]}|${statsPersonsPerFilm[3]}</td>
			</tr>
		
				<tr>
				<td><spring:message code="administrator.filmsWithHighestRating" /></td>
			
				<jstl:forEach var="c" items="${filmsWithHighestRating}">
					<td style="text-align: right"><jstl:out
							value="${c.title}" /></td>
					<br />
				</jstl:forEach>
			</tr>
				<tr>
				<td><spring:message code="administrator.ratioFinalModeFilms" /></td>
				<td style="text-align: right">${ratioFinalModeFilms}</td>
			</tr>
						<tr>
				<td><spring:message code="administrator.top5FilmsWithMoreRunTime" /></td>
	
				
				<jstl:forEach var="c" items="${top5FilmsWithMoreRunTime}">
					<td style="text-align: right"><jstl:out
							value="${c.title}" /></td>
					<br />
				</jstl:forEach>
			</tr>
						<tr>
				<td><spring:message code="administrator.statsPointsVisualizationPerFilm" /></td>
				<td style="text-align: right">${statsPointsVisualizationPerFilm[0]}|${statsPointsVisualizationPerFilm[1]}|${statsPointsVisualizationPerFilm[2]}|${statsPointsVisualizationPerFilm[3]}</td>
			</tr>
					<tr>
				<td><spring:message code="administrator.EventsWithHigeshtMaximumCapacity" /></td>
		
				<jstl:forEach var="c" items="${EventsWithHigeshtMaximumCapacity}">
					<td style="text-align: right"><jstl:out
							value="${c.title}" /></td>
					<br />
				</jstl:forEach>
				
			</tr>
					<tr>
				<td><spring:message code="administrator.top3CriticsMoreProfessional" /></td>
				
					<jstl:forEach var="c" items="${top3CriticsMoreProfessional}">
					<td style="text-align: right"><jstl:out
							value="${c.name}" /></td>
					<br />
				</jstl:forEach>
			</tr>
				<tr>
				<td><spring:message code="administrator.criticsWithHighestRatingReview" /></td>
			
					<jstl:forEach var="c" items="${criticsWithHighestRatingReview}">
					<td style="text-align: right"><jstl:out
							value="${c.name}" /></td>
					<br />
				</jstl:forEach>
			</tr>
			
					<tr>
				<td><spring:message code="administrator.socialProfilesInSystem" /></td>
				<td style="text-align: right">${socialProfilesInSystem}</td>
			</tr>
			
							<tr>
				<td><spring:message code="administrator.statsSponsorshipsPerSponsor" /></td>
				<td style="text-align: right">${statsSponsorshipsPerSponsor[0]}|${statsSponsorshipsPerSponsor[1]}|${statsSponsorshipsPerSponsor[2]}|${statsSponsorshipsPerSponsor[3]}</td>
			</tr>
						<tr>
				<td><spring:message code="administrator.statsReviewsPerModerator" /></td>
				<td style="text-align: right">${statsReviewsPerModerator[0]}|${statsReviewsPerModerator[1]}|${statsReviewsPerModerator[2]}|${statsReviewsPerModerator[3]}</td>
			</tr>
						<tr>
				<td><spring:message code="administrator.top3EventsWithMorePeople" /></td>
			
					<jstl:forEach var="event" items="${top3EventsWithMorePeople}">
					<td style="text-align: right"><jstl:out
							value="${event.title}" /></td>
					<br />
				</jstl:forEach>
			</tr>
			<tr>
				<td><spring:message code="administrator.resultsFinder" /></td>
				<td style="text-align: right">${finder[0]}</td>
			</tr>
			<tr>
				<td><spring:message code="administrator.resultsFinder.min" /></td>
				<td style="text-align: right">${finder[1]}</td>
			</tr>
			<tr>
				<td><spring:message code="administrator.resultsFinder.avg" /></td>
				<td style="text-align: right">${finder[2]}</td>
			</tr>
			<tr>
				<td><spring:message code="administrator.resultsFinder.stdev" /></td>
				<td style="text-align: right">${finder[3]}</td>
			</tr>
		</table>
</security:authorize>