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

<display:table pagesize="10" class="displaytag" name="events"
	requestURI="${requestURI }" id="event">

	<display:column titleKey="event.title">
		<jstl:out value="${event.title}"></jstl:out>
	</display:column>

	<display:column titleKey="event.eventDate">
		<jstl:out value="${event.eventDate}"></jstl:out>
	</display:column>
	<display:column titleKey="event.maximumCapacity">
		<jstl:out value="${event.maximumCapacity}"></jstl:out>
	</display:column>
	<display:column titleKey="event.price">
		<jstl:out value="${event.price}"></jstl:out>
	</display:column>
	<display:column titleKey="event.display">
		<a
			href="event/display.do?Id=${event.id}">
			<spring:message code="event.display" />
		</a>
	</display:column>
</display:table>
	<jstl:if test="${event.forum.creator eq actor }">
		<input type="button"
			value="<spring:message code="event.create"	/>"
			onclick="redirect: location.href = 'event/create.do?Id=${group.id}';" />
	</jstl:if>
<acme:cancel
	url="group/display.do?Id=${event.forum.id}"
	code="event.cancel" />
</security:authorize>
	