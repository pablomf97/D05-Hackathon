<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<h1><spring:message code="film.title.display" /></h1>
<table class="displayStyle">
	<tr>
		<td><strong> <spring:message code="film.title" /> : </strong></td>
		<td><jstl:out value="${film.title}"></jstl:out></td>
	</tr>
	
	<tr>
		<td><strong> <spring:message code="film.synopsis" /> : </strong></td>
		<td><jstl:out value="${film.synopsis}"></jstl:out></td>
	</tr>
	
	<tr>
		<td><strong> <spring:message code="film.releaseDate" /> : </strong></td>
		<td><jstl:out value="${film.releaseDate}"></jstl:out></td>
	</tr>
	
	<tr>
		<td><strong> <spring:message code="film.runTime" /> : </strong></td>
		<td><jstl:out value="${film.runTime}"></jstl:out>min</td>
	</tr>
	
	<tr>
		<td><strong> <spring:message code="film.rating" /> : </strong></td>
		<td><jstl:out value="${film.rating}"></jstl:out></td>
	</tr>
	
	<tr>
		<td><strong> <spring:message code="film.ticker" /> : </strong></td>
		<td><jstl:out value="${film.ticker}"></jstl:out></td>
	</tr>
	
	<tr>
		<td><strong> <spring:message code="film.genres" /> : </strong></td>
		<td><jstl:out value="${film.genres}"></jstl:out></td>
	</tr>
</table>
	
<h1><spring:message code="film.saga.persons" /></h1>
<display:table class="displaytag" name="persons" pagesize="5" 
		requestURI="film/display.do" id="person">

	<display:column titleKey="film.person.name" sortable="true">
		<jstl:out value="${person.name}" />
	</display:column>
	
	<display:column titleKey="film.person.surname" sortable="true">
		<jstl:out value="${person.surname}" />
	</display:column>
	
	<display:column >
		<a href="person/display.do?personId=${person.id}"> <spring:message
				code="film.person.display" />
		</a>
	</display:column>
</display:table>

<h1><spring:message code="film.saga.sagas" /></h1>
<display:table class="displaytag" name="sagas" pagesize="5" 
		requestURI="film/display.do" id="saga">

	<display:column titleKey="film.saga.title" sortable="true">
		<jstl:out value="${saga.title}" />
	</display:column>
	
	<display:column >
		<a href="saga/display.do?sagaId=${saga.id}"> <spring:message
				code="saga.display" />
		</a>
	</display:column>
</display:table>



