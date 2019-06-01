<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<h1><spring:message	code="film.list" />
<jstl:out value="${film.film.title}" /></h1>
	<jstl:choose>
		<jstl:when test="${isPrincipal}">

			<display:table class="displaytag" name="films" pagesize="5" 
				requestURI="film/list.do" id="film">

				<display:column titleKey="film.title" sortable="true">
					<jstl:out value="${film.title}" />
				</display:column>

				<display:column titleKey="film.releaseDate" sortable="true">
					<jstl:out value="${film.releaseDate}" />
				</display:column>
				
				<display:column>
					<a href="film/display.do?filmId=${film.id}"> <spring:message
							code="film.display" />
					</a>
				</display:column>
				
				<display:column>
					<a href="film/edit.do?filmId=${film.id}"> <spring:message
							code="film.edit" />
					</a>
				</display:column>
				
				<display:column>
					<a href="film/delete.do?filmId=${film.id}"> <spring:message
							code="film.delete" />
					</a>
				</display:column>
				
			</display:table>
			
			<a href="film/create.do"> <spring:message
				code="film.create" /></a>
				
		</jstl:when>
		<jstl:otherwise>
			<display:table class="displaytag" name="films" pagesize="5" 
				requestURI="film/list.do" id="film">

				<display:column titleKey="film.title" sortable="true">
					<jstl:out value="${film.title}" />
				</display:column>

				<display:column titleKey="film.releaseDate" sortable="true">
					<jstl:out value="${film.releaseDate}" />
				</display:column>
				
				<display:column>
					<a href="film/display.do?filmId=${film.id}"> <spring:message
							code="film.display" />
					</a>
				</display:column>
			</display:table>
		</jstl:otherwise>
	</jstl:choose>