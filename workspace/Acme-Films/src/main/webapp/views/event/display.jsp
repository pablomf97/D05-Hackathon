<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<security:authorize access="hasAnyRole('FILMENTHUSIAST','MODERATOR')">
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
			<td><spring:message code="date.dateFormat" var="format" /> <span><fmt:formatDate
						pattern="${format }" value="${event.eventDate}" /></span></td>
		</tr>
		<tr>

			<td><strong><spring:message code="event.signinDeadline" />
					: </strong></td>

			<td><span><fmt:formatDate pattern="${format }"
						value="${event.signinDeadline}" /></span>
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
	<jstl:set var="contains2" value="no" />

	<jstl:forEach var="item2" items="${event.forum.groupMembers}">
		<jstl:if
			test="${item2.userAccount.username eq actor.userAccount.username}">
			<jstl:set var="contains2" value="yes" />
		</jstl:if>
	</jstl:forEach>
	<jstl:if
		test="${event.forum.creator.userAccount.username eq actor.userAccount.username or contains2}">
		<input type="button" name="list"
			value="<spring:message code="event.list.members"	/>"
			onclick="redirect: location.href = 'event/filmenthusiast/listMembers.do?Id=${event.id}';" />
	</jstl:if>

	<jstl:if
		test="${event.forum.creator.userAccount.username eq actor.userAccount.username and eventDate}">


		<input type="button" name="list"
			value="<spring:message code="event.delete"	/>"
			onclick="redirect: location.href = 'event/delete.do?Id=${event.id}';" />
	</jstl:if>
	<jstl:if
		test="${not(event.forum.creator.userAccount.username eq actor.userAccount.username) }">
		<jstl:set var="contains" value="no" />

		<jstl:forEach var="item" items="${event.attenders}">
			<jstl:if
				test="${item.userAccount.username eq actor.userAccount.username}">
				<jstl:set var="contains" value="yes" />
			</jstl:if>
		</jstl:forEach>

	</jstl:if>
	<jstl:if
		test="${contains2 eq 'yes' and contains eq 'no' and event.maximumCapacity gt fn:length(event.attenders) and eventDate}">
		<input type="button" name="list"
			value="<spring:message code="event.request.members"	/>"
			onclick="redirect: location.href = 'event/filmenthusiast/request.do?Id=${event.id}';" />
	</jstl:if>
	<jstl:if test="${contains2 eq 'yes'}">
		<input type="button" name="list"
			value="<spring:message code="event.list.members"/>"
			onclick="redirect: location.href = 'event/filmenthusiast/listMembers.do?Id=${event.id}';" />
	</jstl:if>

	<acme:cancel url="event/list.do?Id=${event.forum.id}"
		code="event.cancel" />
</security:authorize>
