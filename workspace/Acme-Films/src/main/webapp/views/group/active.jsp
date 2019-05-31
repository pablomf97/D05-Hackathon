<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasAnyRole('FIMLENTHUSIAST','MODERATOR')">
	<table class="displayStyle">
		<tr>

			<td><strong><spring:message code="event.title" /> : </strong></td>
			<td><jstl:out value="${event.title}">
				</jstl:out></td>
		</tr>
		<tr>

			<td><strong><spring:message code="event.address" /> :
			</strong></td>
			<td><jstl:out value="${event.address}">
				</jstl:out></td>
		</tr>
		<tr>

			<td><strong><spring:message code="event.price" /> : </strong></td>
			<td><jstl:out value="${event.price}">
				</jstl:out></td>
		</tr>
		<tr>

			<td><strong><spring:message code="event.eventDate" />
					: </strong></td>
			<td><jstl:out value="${event.eventDate}">
				</jstl:out></td>
		</tr>
		<tr>

			<td><strong><spring:message code="event.signinDeadline" />
					: </strong></td>
			<td><jstl:out value="${event.signinDeadline}">
				</jstl:out></td>
		</tr>
		<tr>

			<td><strong><spring:message
						code="event.maximumCapacity" /> : </strong></td>
			<td><jstl:out value="${event.maximumCapacity}">
				</jstl:out></td>
		</tr>
		<tr>

			<td><strong><spring:message code="event.forum.name" />
					: </strong></td>
			<td><jstl:out value="${event.forum.name}">
				</jstl:out></td>
		</tr>
		
				<tr>

			<td><strong><spring:message code="event.description" />
					: </strong></td>
			<td><jstl:out value="${event.description}">
				</jstl:out></td>
		</tr>

	</table>
	<jstl:if test="${event.forum.creator eq actor }">
		<input type="button" name="list"
			value="<spring:message code="event.list.members"	/>"
			onclick="redirect: location.href = 'event/filmenthusiast/listMembers.do?Id=${event.id}';" />
			
			<input type="button" name="list"
			value="<spring:message code="event.delete"	/>"
			onclick="redirect: location.href = 'event/delete.do?Id=${event.id}';" />
	</jstl:if>
	<jstl:if test="${event.forum.creator != actor }">
		<jstl:set var="contains" value="false" />

		<jstl:forEach var="item" items="${event.attenders}">
			<jstl:if test="${item eq actor}">
				<jstl:set var="contains" value="true" />
			</jstl:if>
		</jstl:forEach>
	</jstl:if>
	<jstl:if test="${contains and event.maximumCapacity le fn:length(event.attenders)}">
		<input type="button" name="list"
			value="<spring:message code="event.request.members"	/>"
			onclick="redirect: location.href = 'event/filmenthusiast/request.do?Id=${event.id}';" />
	</jstl:if>
	<jstl:if test="${contains}">
		<input type="button" name="list"
			value="<spring:message code="event.list.members"/>"
			onclick="redirect: location.href = 'event/filmenthusiast/listMembers.do?Id=${event.id}';" />
	</jstl:if>

	<acme:cancel
		url="miscellaneousData/critic/list.do?curriculaId=${curriculaId}"
		code="event.cancel" />
</security:authorize>
