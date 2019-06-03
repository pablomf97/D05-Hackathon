<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('FILMENTHUSIAST')">
	<jstl:if test="${possible}">

		<display:table pagesize="10" class="displaytag" name="comments"
			requestURI="comment/filmEnthusiast/list.do" id="row">

			<display:column titleKey="comment.body">
				<jstl:out value="${row.body}"></jstl:out>
			</display:column>

			<display:column titleKey="comment.publishedMoment">
				<jstl:out value="${row.publishedMoment}"></jstl:out>
			</display:column>
			<display:column titleKey="comment.rating">
				<jstl:out value="${row.rating}"></jstl:out>
			</display:column>

			<jstl:if test="${row.film != null }">
				<display:column titleKey="comment.film">
					<jstl:out value="${row.film.title}"></jstl:out>
				</display:column>


			</jstl:if>

			<jstl:if test="${row.film == null }">
				<display:column titleKey="comment.film">

				</display:column>


			</jstl:if>

			<jstl:if test="${row.forum != null }">
				<display:column titleKey="comment.forum">
					<jstl:out value="${row.forum.name}"></jstl:out>
				</display:column>


			</jstl:if>

			<jstl:if test="${row.forum == null }">
				<display:column titleKey="comment.forum">

				</display:column>


			</jstl:if>


			<display:column titleKey="comment.display">
				<a href="comment/filmEnthusiast/display.do?id=${row.id}"> <spring:message
						code="comment.display" />
				</a>
			</display:column>


		</display:table>
	</jstl:if>
	<jstl:if test="${!possible }">
		<spring:message code="comment.nopermission" var="permission"></spring:message>

		<strong><jstl:out value="${permission}">
			</jstl:out></strong>
	</jstl:if>

</security:authorize>

<jstl:if test="${!possible }">
		<spring:message code="comment.nopermission" var="permission"></spring:message>

		<strong><jstl:out value="${permission}">
			</jstl:out></strong>
	</jstl:if>
