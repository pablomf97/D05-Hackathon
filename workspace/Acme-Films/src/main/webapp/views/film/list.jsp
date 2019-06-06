<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

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
				<spring:message code="date.dateFormat" var="format" /> 
				<span><fmt:formatDate pattern="${format }" value="${film.releaseDate}" /></span>
				</display:column>
				
				<display:column titleKey="film.rating" sortable="true">
					<jstl:out value="${film.rating}" />
				</display:column>
				
				<jstl:if test="${film.isDraft eq true}">
					<spring:message var="status" code='film.draft.true' />
				</jstl:if>
				<jstl:if test="${film.isDraft eq false}">
					<spring:message var="status" code='film.draft.false' />
				</jstl:if>
				
				<display:column titleKey="film.isDraft" sortable="true">
					<jstl:out value="${status}" />
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
					<jstl:if test="${film.isDraft }">
						<a href="film/delete.do?filmId=${film.id}"> <spring:message
								code="film.delete" />
						</a>
					</jstl:if>
				</display:column>
			</display:table>
			
			<input type="button"
				onclick="redirect: location.href = 'film/create.do';"
				value="<spring:message code='film.create' />" />
				
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