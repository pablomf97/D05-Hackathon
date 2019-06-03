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
	<form:form action="comment/filmEnthusiast/display.do?id=${comment.id}"
		modelAttribute="comment" id="form">
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

			<jstl:if test="${comment.film != null}">
				<tr>

					<td><strong><spring:message code="comment.film" /> :
					</strong></td>

					<td><jstl:out value="${comment.film.title}"></jstl:out></td>
				</tr>

			</jstl:if>

			<jstl:if test="${comment.forum != null}">
				<tr>

					<td><strong><spring:message code="comment.forum" />
							: </strong></td>

					<td><jstl:out value="${comment.forum.name}"></jstl:out></td>
				</tr>

			</jstl:if>



		</table>

		<acme:cancel url="comment/filmEnthusiast/list.do" code="comment.back" />
		
		<acme:delete name="delete" confirmation="confirm.delete" code="comment.delete"/>
	</form:form>
</jstl:if>



<jstl:if test="${!possible}">
	<spring:message code="comment.permission" />
</jstl:if>
