<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<h1><spring:message	code="sponsorship.title.list" />
<jstl:out value="${(film.title)}" /></h1>
	<jstl:choose>
		<jstl:when test="${isPrincipal == 'MODERATOR'}">

			<display:table class="displaytag" name="sponsorships" pagesize="5" 
				requestURI="sponsorship/list.do" id="sponsorship">

				<display:column titleKey="sponsorship.title" sortable="true">
					<jstl:out value="${sponsorship.title}" />
				</display:column>

				<display:column titleKey="sponsorship.banner" sortable="true">
					<jstl:out value="${sponsorship.banner}" /> &#8364;
				</display:column>
				
				<display:column titleKey="sponsorship.title" sortable="true">
					<jstl:out value="${sponsorship.title}" />
				</display:column>

				<display:column titleKey="sponsorship.targetPage" sortable="true">
					<jstl:out value="${sponsorship.targetPage}" /> &#8364;
				</display:column>
				
				<display:column titleKey="sponsorship.sponsor" sortable="true">
					<jstl:out value="${sponsorship.sponsor.name} ${sponsorship.sponsor.surname}" />
				</display:column>
				
				<display:column>
					<a id="edit"
						href="sponsorship/action.do?action=accept&sponsorshipId=${sponsorship.id}">
						<spring:message code="sponsorship.accept" />
					</a>
				</display:column>
				
				<display:column>
					<a id="edit"
						href="sponsorship/action.do?action=reject&sponsorshipId=${sponsorship.id}">
						<spring:message code="sponsorship.reject" />
					</a>
				</display:column>
				
			</display:table>
			
		</jstl:when>
		<jstl:otherwise>
			<display:table class="displaytag" name="sponsorships" pagesize="5" 
				requestURI="sponsorship/list.do" id="sponsorship">

				<display:column titleKey="sponsorship.siteName" sortable="true">
					<jstl:out value="${sponsorship.siteName}" />
				</display:column>

				<display:column titleKey="sponsorship.price" sortable="true">
					<jstl:out value="${sponsorship.price}" />
				</display:column>
				
				<display:column titleKey="sponsorship.link" sortable="true">
					<a href="${sponsorship.link}"> 
						<spring:message
							code="sponsorship.link.goto" />
					</a>
				</display:column>
				
				<display:column>
					<a href="sponsorship/edit.do?sponsorshipId=${sponsorship.id}"> <spring:message
							code="sponsorship.edit" />
					</a>
				</display:column>
				
				<display:column>
					<a href="sponsorship/delete.do?sponsorshipId=${sponsorship.id}"> <spring:message
							code="sponsorship.delete" />
					</a>
				</display:column>
			</display:table>
			<br><a href="sponsorship/create.do?filmId=${film.id }"> <spring:message
								code="sponsorship.create" /></a>
		</jstl:otherwise>
	</jstl:choose>