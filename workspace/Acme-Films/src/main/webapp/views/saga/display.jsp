<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<h1><spring:message code="saga.title.display" /></h1>
<table class="displayStyle">
	<tr>
		<td><strong> <spring:message code="saga.title" />
				:
		</strong></td>
		<td><jstl:out value="${saga.title}">
			</jstl:out></td>
	</tr>
</table>
		
<display:table class="displaytag" name="films" pagesize="5" 
		requestURI="saga/display.do" id="film">

	<display:column titleKey="film.title" sortable="true">
		<jstl:out value="${film.title}" />
	</display:column>
	
	<display:column titleKey="film.releaseDate" sortable="true">
		<jstl:out value="${film.releaseDate}" />
	</display:column>
	
	<display:column titleKey="film.rating" sortable="true">
		<jstl:out value="${film.rating}" />
	</display:column>
	
	<display:column >
		<a href="film/display.do?filmId=${film.id}"> <spring:message
				code="film.display" />
		</a>
	</display:column>
</display:table>
