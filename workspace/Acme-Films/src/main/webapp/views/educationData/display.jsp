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

		<td><strong><spring:message code="educationData.degree" />
				: </strong></td>
		<td><jstl:out value="${data.degree}">

			</jstl:out></td>


	</tr>


	<tr>

		<td><strong><spring:message
					code="educationData.institution" /> : </strong></td>
		<td><jstl:out value="${data.institution}">

			</jstl:out></td>

	</tr>

	<tr>

		<td><strong><spring:message code="educationData.mark" />
				: </strong></td>
		<td><jstl:out value="${data.mark}">

			</jstl:out></td>

	</tr>

	<tr>

		<td><strong><spring:message
					code="educationData.startDate" /> : </strong></td>
		<td><jstl:out value="${data.startDate}">

			</jstl:out></td>

	</tr>

	<tr>

		<td><strong><spring:message code="educationData.endDate" />
				: </strong></td>
		<td><jstl:out value="${data.endDate}">

			</jstl:out></td>

	</tr>

</table>
<security:authorize access="hasRole('CRITIC')">
<jstl:if test="${not empty principal and principal.curricula.id == currentCurricula.id}">

	<input type="button" name="edit"
		value="<spring:message code="educationData.edit"	/>"
		onclick="redirect: location.href = 'educationData/critic/edit.do?dataId=${data.id}&curriculaId=${curriculaId}';" />
		</jstl:if>
</security:authorize>

<acme:cancel
	url="educationData/critic/list.do?curriculaId=${curriculaId}"
	code="educationData.cancel" />
