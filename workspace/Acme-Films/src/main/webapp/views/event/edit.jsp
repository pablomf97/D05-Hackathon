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
	<jstl:if test="${event.forum.creator eq actor }">

		<form:form modelAttribute="event"
			action="event/critic/edit.do?Id=${event.id}" id="form">

			<form:hidden path="id" />
			<form:hidden path="forum" />

			<form:label path="title">
				<spring:message code="event.title" /> :
		</form:label>
			<form:textarea path="title" />
			<form:errors cssClass="error" path="title" />

			<br>
			<br>

			<form:label path="price">
				<spring:message code="event.price" /> :
		</form:label>
			<form:textarea path="price" />
			<form:errors cssClass="error" path="price" />

			<br>
			<br>

			<form:label path="eventDate">
				<spring:message code="event.eventDate" /> :
		</form:label>
			<form:textarea path="eventDate" />
			<form:errors cssClass="error" path="eventDate" />

			<br>
			<br>

			<form:label path="signinDeadline">
				<spring:message code="event.signinDeadline" /> :
		</form:label>
			<form:textarea path="signinDeadline" />
			<form:errors cssClass="error" path="signinDeadline" />

			<br>
			<br>

			<form:label path="maximumCapacity">
				<spring:message code="event.maximumCapacity" /> :
		</form:label>
			<form:textarea path="maximumCapacity" />
			<form:errors cssClass="error" path="maximumCapacity" />

			<br>
			<br>

			<form:label path="description">
				<spring:message code="event.description" /> :
		</form:label>
			<form:textarea path="description" />
			<form:errors cssClass="error" path="description" />

			<br>
			<br>

			<acme:submit code="event.save" name="save" />&nbsp;
		
		<acme:cancel url="group/list.do?Id=${event.forum.id}"
				code="event.cancel" />

			<br />
			<br />

		</form:form>
	</jstl:if>

</security:authorize>