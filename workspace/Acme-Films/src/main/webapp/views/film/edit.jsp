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


<security:authorize access="hasRole('PROVIDER')">

	<jstl:choose>
		<jstl:when test="${isPrincipal}">
		<h1><spring:message	code="film.title.edit" /></h1>
		<form:form modelAttribute="film" action="film/edit.do"
			id="form">
	
			<form:hidden path="id" />
			
			<acme:textbox code="film.title" path="title" size="100px" /><br> <br>
			<acme:textbox code="film.synopsis" path="synopsis" size="100px" /><br> <br>
			<acme:textbox code="film.poster" path="poster" size="100px" /><br> <br>
			<acme:textbox code="film.releaseDate" path="releaseDate" size="100px" /><br> <br>
			<acme:textbox code="film.runTime" path="runTime" size="100px" /><br> <br>
			<acme:textbox code="film.rating" path="rating" size="100px" /><br> <br>
			
			<jstl:choose>
				<jstl:when test="${pageContext.response.locale.language == 'es'}">
					<acme:multipleSelect items="${genres}" itemLabel="${genre.name.get('Español')}" code="film.genre" path="genres"/>
				</jstl:when>
				<jstl:otherwise>
					<acme:multipleSelect items="${genres}" itemLabel="${genre.name.get('English')}" code="film.genre" path="genres"/>
				</jstl:otherwise>
			</jstl:choose>
			
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
