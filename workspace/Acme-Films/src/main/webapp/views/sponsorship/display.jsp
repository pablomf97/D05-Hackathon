<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<h1><spring:message code="sponsorship.title.display" /></h1>
<table class="displayStyle">
	<tr>
		<td><strong> <spring:message code="sponsorship.title" /> : </strong></td>
		<td><jstl:out value="${sponsorship.title}"></jstl:out></td>
	</tr>
	
	<tr>
		<td><strong> <spring:message code="sponsorship.banner" /> : </strong></td>
		<td><jstl:out value="${sponsorship.banner}"></jstl:out></td>
	</tr>
	
	<tr>
		<td><strong> <spring:message code="sponsorship.targetPage" /> : </strong></td>
		<td><jstl:out value="${sponsorship.targetPage}"></jstl:out></td>
	</tr>
	
	<tr>
		<td><strong> <spring:message code="sponsorship.isActive" /> : </strong></td>
		<td><jstl:out value="${sponsorship.isActive}"></jstl:out></td>
	</tr>
	
</table>
	
<h2><spring:message code="sponsorship.display.films" /></h2>
<display:table class="displaytag" name="${sponsorship.films}" pagesize="5" 
		requestURI="sponsorship/display.do" id="film">

	<display:column titleKey="sponsorship.film.name" sortable="true">
		<jstl:out value="${film.title}" />
	</display:column>
	
	<display:column >
		<a href="film/display.do?filmId=${film.id}"> <spring:message
				code="sponsorship.display" />
		</a>
	</display:column>
</display:table>
