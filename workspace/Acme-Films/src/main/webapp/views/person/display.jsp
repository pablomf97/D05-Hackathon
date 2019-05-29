<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<h1><spring:message code="person.title.display" /></h1>
<table class="displayStyle">
	<tr>
		<td><strong> <spring:message code="person.name" />	: </strong></td>
		<td><jstl:out value="${person.name}"/></td>
		
		<td><strong> <spring:message code="person.surname" />	: </strong></td>
		<td><jstl:out value="${person.surname}"/></td>
		
		<td><strong> <spring:message code="person.gender" />	: </strong></td>
		<td><jstl:out value="${person.gender}"/></td>
		
		<td><strong> <spring:message code="person.nationality" />	: </strong></td>
		<td><jstl:out value="${person.nationality}"/></td>
		
		<td><strong> <spring:message code="person.birthDate" />	: </strong></td>
		<td><jstl:out value="${person.birthDate}"/></td>
		
		<td><strong> <spring:message code="person.photo" />	: </strong></td>
		<td><jstl:out value="${person.photo}"/></td>
		
		<td><strong> <spring:message code="person.positions" />	: </strong></td>
		<td><jstl:out value="${person.positions}"/></td>
		
		<jstl:choose>
			<jstl:when test="${pageContext.response.locale.language == 'es'}">
				<td><strong> <spring:message code="person.positions" />	: </strong></td>
				<jstl:forEach items="${person.positions}" var="name">
					<td><jstl:out value="${name.get('English')}"/></td>	
				</jstl:forEach>
			</jstl:when>
			<jstl:otherwise>
				<td><strong> <spring:message code="person.positions" />	: </strong></td>
				<jstl:forEach items="${person.positions}" var="name">
					<td><jstl:out value="${name.get('English')}"/></td>	
				</jstl:forEach>
			</jstl:otherwise>
		</jstl:choose>
	</tr>
</table>
		
<display:table class="displaytag" name="films" pagesize="5" 
		requestURI="person/display.do" id="film">

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
