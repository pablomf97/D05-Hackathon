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

		<td><strong><spring:message
					code="miscellaneousData.text" /> : </strong></td>
		<td><jstl:out value="${data.text}">

			</jstl:out></td>


	</tr>


	<tr>

		<td><strong><spring:message
					code="miscellaneousData.attachements" /> : </strong></td>
		<td><jstl:out value="${data.attachements}">

			</jstl:out></td>

	</tr>

</table>
<security:authorize access="hasRole('CRITIC')">
<jstl:if test="${not empty principal and principal.curricula.id == currentCurricula.id}">

	<input type="button" name="edit"
		value="<spring:message code="miscellaneousData.edit"	/>"
		onclick="redirect: location.href = 'miscellaneousData/critic/edit.do?dataId=${data.id}&curriculaId=${curriculaId}';" />

</jstl:if>
	<acme:cancel
		url="miscellaneousData/critic/list.do?curriculaId=${curriculaId}"
		code="miscellaneousData.cancel" />
</security:authorize>
