
<%--
 * action-1.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>


<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('MODERATOR')">

	<jstl:choose>
		<jstl:when test="${err}">
			<p>
				<jstl:out value="${errMsg.getMessage()}" />
			</p>
		</jstl:when>
		<jstl:otherwise>
			<h1><spring:message	code="genre.title.list" /></h1>
			<display:table style="width: 40%" class="displaytag" name="genres"
				requestURI="genre/moderator/list.do" id="genre">

				<jstl:choose>
					<jstl:when test="${pageContext.response.locale.language == 'es'}">
						<display:column titleKey="genre.name" sortable="true">
							<jstl:out value="${genre.name.get('Español')}" />
						</display:column>
					</jstl:when>
					<jstl:otherwise>
						<display:column titleKey="genre.name" sortable="true">
							<jstl:out value="${genre.name.get('English')}" />
						</display:column>
					</jstl:otherwise>
				</jstl:choose>
				<display:column style="width: 15%">
					<a href="genre/moderator/edit.do?genreId=${genre.id}"><spring:message
							code="genre.edit" /></a>
				</display:column>
				
				<display:column>
					<a href="genre/moderator/delete.do?genreId=${genre.id}"> <spring:message
							code="genre.delete" />
					</a>
				</display:column>
				
			</display:table>
			<input type="button"
				onclick="redirect: location.href = 'genre/moderator/create.do';"
				value="<spring:message code='genre.create' />" />
		</jstl:otherwise>
	</jstl:choose>

</security:authorize>

<security:authorize access="!hasRole('MODERATOR')">
		<p>
			<spring:message	code="sponsorship.not.allowed" /><br>
		</p>
</security:authorize>