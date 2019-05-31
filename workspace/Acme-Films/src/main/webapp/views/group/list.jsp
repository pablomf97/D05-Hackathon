<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasAnyRole('FILMENTHUSIAST','MODERATOR')">

	<display:table pagesize="10" class="displaytag" name="groups"
		requestURI="${requestURI }" id="group">

		<display:column titleKey="group.name">
			<jstl:out value="${group.name}"></jstl:out>
		</display:column>

		<display:column titleKey="group.creationDate">
			<jstl:out value="${group.creationDate}"></jstl:out>
		</display:column>
		<display:column titleKey="group.creator">
			<jstl:out value="${group.creator}"></jstl:out>
		</display:column>
		<display:column titleKey="group.about">
			<jstl:if test="${not empty group.sagaAbout }">
				<jstl:out value="${group.sagaAbout}"></jstl:out>
			</jstl:if>
			<jstl:if test="${not empty group.filmAbout }">
				<jstl:out value="${group.filmAbout}"></jstl:out>
			</jstl:if>
		</display:column>

		<display:column titleKey="group.display">
			<a href="group/display.do?Id=${group.id}"> <spring:message
					code="group.display" />
			</a>
		</display:column>
	</display:table>
	<jstl:if test="${group.creator eq actor }">
		<input type="button" value="<spring:message code="group.create"	/>"
			onclick="redirect: location.href = 'group/create.do?Id=${group.id}';" />
	</jstl:if>
	<acme:cancel
		url="curricula/critic/display.do?curriculaId=${currentCurricula.id}"
		code="miscellaneousData.cancel" />
</security:authorize>
