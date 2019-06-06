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

	<form:form modelAttribute="educationData"
		action="educationData/critic/edit.do?dataId=${educationData.id}&curriculaId=${currentCurricula.id}"
		id="form">

		<form:hidden path="id" />

		<acme:textbox code="educationData.degree" path="degree" />
		<br>
		<br>

		<acme:textbox code="educationData.institution" path="institution" />

		<br />
		<br />
		<acme:textbox code="educationData.mark" path="mark" />
		<br />
		<br />

		<acme:textbox code="educationData.startDate" path="startDate" />


		<br />
		<br />
		<acme:textbox code="educationData.endDate" path="endDate"/>
		<br />
		<br />


		<acme:submit code="educationData.save" name="save" />&nbsp;
		
		<acme:cancel
			url="educationData/critic/list.do?curriculaId=${currentCurricula.id}"
			code="educationData.cancel" />

		<jstl:if test="${educationData.id != 0 }">
			<input type="submit" name="delete"
				value="<spring:message code="educationData.delete"/>" />

		</jstl:if>


	</form:form>

</security:authorize>