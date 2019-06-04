<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>



<security:authorize access="hasRole('MODERATOR')">
		<form:form modelAttribute="group"
			action="group/moderator/save.do" id="form">

			<form:hidden path="id" />
			<form:hidden path="sagaAbout" />
			<form:hidden path="filmAbout" />

			<form:label path="rejectReason">
				<spring:message code="group.rejectReason" /> :
		</form:label>
			<form:textarea path="rejectReason" />
			<form:errors cssClass="error" path="rejectReason" />

			<br>
			<br>

			<acme:submit code="group.save.active" name="active" />&nbsp;
			<acme:submit code="group.save.deactive" name="deactive" />&nbsp;
		
		<acme:cancel url="group/list.do" code="group.cancel" />

			<br />
			<br />

		</form:form>
</security:authorize>