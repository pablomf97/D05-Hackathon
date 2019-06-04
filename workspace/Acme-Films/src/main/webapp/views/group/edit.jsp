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
	<jstl:if test="${forum.creator eq actor }">

		<form:form modelAttribute="group"
			action="group/edit.do" id="form">

			<form:hidden path="id" />
			<form:hidden path="sagaAbout" />
			<form:hidden path="filmAbout" />

			<form:label path="name">
				<spring:message code="group.name" /> :
		</form:label>
			<form:textarea path="name" />
			<form:errors cssClass="error" path="name" />

			<br>
			<br>

			<form:label path="description">
				<spring:message code="group.description" /> :
		</form:label>
			<form:textarea path="description" />
			<form:errors cssClass="error" path="description" />

			<br>
			<br>



			<acme:submit code="group.save" name="save" />&nbsp;
		
		<acme:cancel url="group/list.do" code="group.cancel" />

			<br />
			<br />

		</form:form>
	</jstl:if>

</security:authorize>