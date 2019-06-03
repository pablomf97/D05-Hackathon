<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<security:authorize access="hasRole('CRITIC')">

	<form:form modelAttribute="professionalData"
		action="professionalData/critic/edit.do?dataId=${professionalData.id}&curriculaId=${currentCurricula.id}"
		id="form">

		<form:hidden path="id" />

		<acme:textbox code="professionalData.title" path="title" />
		<br>
		<br>

		<spring:message code="professionalData.description" />
		<br>
		<form:textarea code="professionalData.description"
			path="description" />
		<form:errors path="description" cssClass="error" />

		<br>
		<br>
		
		<form:label path="startDate">
			<spring:message code="professionalData.startDate" />:
		</form:label>
		<form:input path="startDate" placeholder="dd/MM/yyyy HH:mm" />
		<form:errors cssClass="error" path="startDate" />
		
		<br />
		<br />
		
		
			<form:label path="endDate">
			<spring:message code="professionalData.endDate" />:
		</form:label>
		<form:input path="endDate" placeholder="dd/MM/yyyy HH:mm" />
		<form:errors cssClass="error" path="endDate" />
		
		<br />
		<br />
		

		<acme:submit code="professionalData.save" name="save" />&nbsp;
		
		<acme:cancel
			url="professionalData/critic/list.do?curriculaId=${currentCurricula.id}"
			code="professionalData.cancel" />

		<jstl:if test="${professionalData.id != 0 }">
			<input type="submit" name="delete"
				value="<spring:message code="professionalData.delete"/>" />

		</jstl:if>

		
	</form:form>

</security:authorize>