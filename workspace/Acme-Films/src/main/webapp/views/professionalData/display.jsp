<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<table class="displayStyle">
	<tr>

		<td><strong><spring:message
					code="professionalData.title" /> : </strong></td>
		<td><jstl:out value="${data.title}">

			</jstl:out></td>


	</tr>


	<tr>

		<td><strong><spring:message
					code="professionalData.description" /> : </strong></td>
		<td><jstl:out value="${data.description}">

			</jstl:out></td>

	</tr>

	<tr>

		<td><strong><spring:message
					code="professionalData.startDate" /> : </strong></td>
		<td><spring:message code="date.dateFormat" var="format" /> <span><fmt:formatDate
					pattern="${format }" value="${data.startDate}" /></span></td>

	</tr>

	<tr>

		<td><strong><spring:message
					code="professionalData.endDate" /> : </strong></td>
		<td><spring:message code="date.dateFormat" var="format" /> <span><fmt:formatDate
					pattern="${format }" value="${data.endDate}" /></span></td>

	</tr>

</table>
<security:authorize access="hasRole('CRITIC')">
	<jstl:if
		test="${not empty principal and principal.curricula.id == curriculaId}">

		<input type="button" name="edit"
			value="<spring:message code="professionalData.edit"	/>"
			onclick="redirect: location.href = 'professionalData/critic/edit.do?dataId=${data.id}&curriculaId=${curriculaId}';" />
	</jstl:if>
</security:authorize>
<acme:cancel
	url="professionalData/critic/list.do?curriculaId=${curriculaId}"
	code="professionalData.cancel" />
