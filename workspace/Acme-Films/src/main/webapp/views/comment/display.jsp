<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('FILMENTHUSIAST')" />

<jstl:if test="${possible && comment.id != 0}">
	<jstl:if test="${comment.film != null}">
		<table class="displayStyle">
			<tr>

				<td><strong><spring:message code="comment.body" /> :
				</strong></td>

				<td><jstl:out value="${comment.body}"></jstl:out></td>
			</tr>

			<tr>

				<td><strong><spring:message code="comment.rating" />
						: </strong></td>

				<td><jstl:out value="${comment.rating}"></jstl:out></td>
			</tr>

			<tr>

				<td><strong><spring:message
							code="comment.publishedMoment" /> : </strong></td>

				<td><jstl:out value="${comment.publishedMoment}"></jstl:out></td>
			</tr>


		</table>

	</jstl:if>

	<jstl:if test="${comment.film == null && comment.id != 0}">
		<table class="displayStyle">
			<tr>

				<td><strong><spring:message code="comment.body" /> :
				</strong></td>

				<td><jstl:out value="${comment.body}"></jstl:out></td>
			</tr>

			<tr>

				<td><strong><spring:message
							code="comment.publishedMoment" /> : </strong></td>

				<td><jstl:out value="${comment.publishedMoment}"></jstl:out></td>
			</tr>


		</table>

	</jstl:if>

</jstl:if>

<jstl:if test="${!possible}">
	<spring:message code="comment.permission"/>
</jstl:if>
