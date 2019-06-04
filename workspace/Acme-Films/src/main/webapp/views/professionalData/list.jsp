<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<display:table pagesize="10" class="displaytag" name="professionalData"
	requestURI="professionalData/critic/list.do?curriculaId=${currentCurricula.id}"
	id="row">

	<display:column titleKey="professionalData.title">
		<jstl:out value="${row.title}"></jstl:out>
	</display:column>

	<display:column titleKey="professionalData.description">
		<jstl:out value="${row.description}"></jstl:out>
	</display:column>

	<display:column titleKey="professionalData.display">
		<a
			href="professionalData/critic/display.do?dataId=${row.id}&curriculaId=${currentCurricula.id}">
			<spring:message code="professionalData.display" />
		</a>
	</display:column>
</display:table>
<security:authorize access="hasRole('CRITIC')">
<jstl:if test="${not empty principal and principal.curricula.id == currentCurricula.id}">
	<a
		href="professionalData/critic/create.do?curriculaId=${currentCurricula.id}">
		<spring:message code="professionalData.create" />

	</a>&nbsp;&nbsp;&nbsp;
	</jstl:if>
</security:authorize>
<acme:cancel
	url="curricula/critic/display.do?curriculaId=${currentCurricula.id}"
	code="professionalData.cancel" />

