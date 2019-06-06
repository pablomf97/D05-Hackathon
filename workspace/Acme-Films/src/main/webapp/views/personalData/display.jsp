<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>



<table class="displayStyle">

	<tr>

		<td><strong><spring:message code="personalData.linkedIn" />
				: </strong></td>
		<td><jstl:out value="${data.linkedIn}">

			</jstl:out></td>

	</tr>

	<tr>

		<td><strong><spring:message code="personalData.fullName" />
				: </strong></td>
		<td><jstl:out value="${data.fullName}">

			</jstl:out></td>

	</tr>

	<tr>

		<td><strong><spring:message
					code="personalData.statement" /> : </strong></td>
		<td><jstl:out value="${data.statement}">

			</jstl:out></td>

	</tr>

	<tr>

		<td><strong><spring:message
					code="personalData.phoneNumber" /> : </strong></td>
		<td><jstl:out value="${data.phoneNumber}">

			</jstl:out></td>

	</tr>

</table>
<security:authorize access="hasRole('CRITIC')">
	<jstl:if
		test="${not empty principal and principal.curricula.id == curriculaId}">

		<input type="button" name="edit"
			value="<spring:message code="personalData.edit"	/>"
			onclick="redirect: location.href = 'personalData/critic/edit.do?dataId=${data.id}&curriculaId=${curriculaId}';" />
	</jstl:if>
</security:authorize>
<acme:cancel url="/" code="personalData.cancel" />

