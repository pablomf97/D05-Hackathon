<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<security:authorize access="hasRole('MODERATOR')">

	<jstl:choose>
		<jstl:when test="${isPrincipal}">
		<h1><spring:message	code="film.title.edit" /></h1>
		<form:form modelAttribute="film" action="film/edit.do"
			id="form">
	
			<form:hidden path="id" />
			
			<jstl:if test="${film.isDraft || film.id == 0}">
			
				<acme:textbox code="film.title" path="title" size="100px" /><br/> <br/>
				<acme:textbox code="film.synopsis" path="synopsis" size="100px" /><br/> <br/>
				<acme:textbox code="film.poster" path="poster" size="100px" /><br/> <br/>
				<acme:textbox code="film.releaseDate" path="releaseDate" size="100px" /><br/> <br/>
				<acme:textbox code="film.runTime" path="runTime" size="100px" /><br/> <br/>
				<acme:textbox code="film.rating" path="rating" size="100px" /><br/> 
				<form:label path="genres"><spring:message code="film.genres" /></form:label><br>
				<jstl:if test="${!position.isDraft || film.id == 0}">
					<jstl:choose>
						<jstl:when test="${pageContext.response.locale.language == 'es'}">
							<select multiple="multiple" name="genresArray" style="width:200px;">
								<jstl:forEach var="genre" items="${genres}">
									<option value="${genre.id}" >
										<jstl:out value="${genre.name.get('Español')}" />
									</option>
								</jstl:forEach>
							</select>
						</jstl:when>
						<jstl:otherwise>
							<select multiple="multiple" name="genresArray" style="width:200px;">
								<jstl:forEach var="genre" items="${genres}">
									<option value="${genre.id}" >
										<jstl:out value="${genre.name.get('English')}" />
									</option>
								</jstl:forEach>
							</select><br><br>
						</jstl:otherwise>
					</jstl:choose>
				</jstl:if>
			</jstl:if>
			
			<form:label path="persons"><spring:message code="film.persons" /></form:label><br>
			<select multiple="multiple" name="personsArray" style="width:200px;">
				<jstl:forEach var="person" items="${persons}">
					<option value="${person.id}" >
						<jstl:out value="${person.name} ${person.surname}" />
					</option>
				</jstl:forEach>
			</select><br><br>
			
			<acme:multipleSelect items="${sagas}" itemLabel="title" code="film.sagas" path="sagas"/><br>
			
			<acme:submit code="film.save" name="save" />&nbsp;
			<acme:cancel url="film/list.do" code="film.cancel" />
			<br />
	
		</form:form>
		</jstl:when>		
	<jstl:otherwise>
		<p>
			<spring:message	code="film.not.allowed" /><br>
		</p>
	</jstl:otherwise>
	</jstl:choose>
</security:authorize>
