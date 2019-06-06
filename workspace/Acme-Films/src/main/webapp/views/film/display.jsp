<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>


<h1><jstl:out value="${film.title}"/></h1>

<div>
<jstl:if test="${film.poster != null}">
<img style="height: 400px;  margin-left:33% ;margin-bottom: 20px;" src="${film.poster}"
					alt="<spring:message code="film.poster" /> ">
	</jstl:if>
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
		<td><spring:message code="date.dateFormat" var="format" /> 
				<span><fmt:formatDate pattern="${format }" value="${film.releaseDate}" /></span></td>
	</tr>
	
	<tr>
		<td><strong> <spring:message code="film.runTime" /> : </strong></td>
		<td><jstl:out value="${film.runTime}"></jstl:out> min</td>
	</tr>
	
	<tr>
		<td><strong> <spring:message code="film.rating" /> : </strong></td>
		<td><jstl:out value="${film.rating}"></jstl:out> / 10 </td>
	</tr>
	
	<tr>
		<td><strong> <spring:message code="film.ticker" /> : </strong></td>
		<td><jstl:out value="${film.ticker}"></jstl:out></td>
	</tr>
	
	<security:authorize access="isAuthenticated()">
	<jstl:choose>
		<jstl:when test="${pageContext.response.locale.language == 'es'}">
			<tr>
				<td><strong> <spring:message code="film.genres" /> : </strong></td>
				<td>
				-
				<jstl:forEach items="${film.genres}" var="genre">
						<jstl:out value="${genre.name.get('Español')}" />	
					-
				</jstl:forEach>
				</td>
			</tr>
		</jstl:when>
		<jstl:otherwise>
			<tr>
				<td><strong> <spring:message code="film.genres" /> : </strong></td>
				<td>
				-
				<jstl:forEach items="${film.genres}" var="genre">
					<jstl:out value="${genre.name.get('English')}"/>	
					-
				</jstl:forEach>
				</td>
			</tr>
		</jstl:otherwise>
	</jstl:choose>
	</security:authorize>
	
	<jstl:if test="${film.isDraft eq true}">
		<spring:message var="status" code='film.draft.true' />
	</jstl:if>
	<jstl:if test="${film.isDraft eq false}">
		<spring:message var="status" code='film.draft.false' />
	</jstl:if>

	<jstl:if test="${isPrincipal }">
		<tr>
			<td><strong> <spring:message code="film.isDraft" /> : </strong></td>
			<td>${status}</td>
		</tr>
	
		<tr>
			<td><strong> <spring:message code="film.moderator" /> : </strong></td>
			<td><jstl:out value="${film.moderator.name} ${film.moderator.surname}"></jstl:out></td>
		</tr>
	</jstl:if>
	
</table>
</div>
	
<h2><spring:message code="film.saga.persons" /></h2>
<display:table class="displaytag" name="${film.persons}" pagesize="5" 
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

<h2><spring:message code="film.saga.sagas" /></h2>
<display:table class="displaytag" name="${film.sagas}" pagesize="5" 
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

<p><a href="visualization/list.do?filmId=${film.id}"> <spring:message
		code="film.visualization.list" />
</a></p>

	<security:authorize access="hasAnyRole('MODERATOR','FILMENTHUSIAST')">
	<p>
		<a
			href="group/listByFilm.do?Id=${film.id}">
			<spring:message code="group.list" />
		</a>
	</p>
	</security:authorize>
	
	<security:authorize access="hasRole('FILMENTHUSIAST')">
	<p>
		<a href="group/create.do?Id=${film.id}"> <spring:message
				code="group.newcreate" />
		</a>
	</p>
	</security:authorize>
	
<jstl:if test="${spoBanner.targetPage ne null}">
	<div>
		<h3>
			<spring:message code="film.sponsored" />
		</h3>
		<a href="${spoBanner.targetPage }" target="_blank">
			<img style="height: 100px" src="${spoBanner.banner}" 
				alt="Banner" >
		</a><br><br>
	</div>
</jstl:if>

	<input type="button" name="back"
		value="<spring:message code="sponsorship.back" />"
		onclick="window.history.back()" />


