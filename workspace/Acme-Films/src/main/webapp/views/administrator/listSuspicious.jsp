<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasAnyRole('ADMIN')">

	<display:table pagesize="5" class="displaytag" name="suspiciousActors"
		requestURI="/administrator/listSuspicious.do" id="actor">

		<display:column titleKey="actor.type">
			<jstl:out value="${actor.userAccount.authorities[0]}" />
		</display:column>

		<display:column titleKey="actor.status">
			<jstl:choose>
				<jstl:when test="${actor.userAccount.isBanned}">
					<spring:message code="actor.is.banned" />
				</jstl:when>
				<jstl:otherwise>
					<spring:message code="actor.not.banned" />
				</jstl:otherwise>
			</jstl:choose>
		</display:column>

		<display:column titleKey="enthusiast.username">
			<jstl:out value="${actor.userAccount.username}" />
		</display:column>

		<display:column titleKey="enthusiast.name">
			<jstl:out value="${actor.name} ${actor.surname}" />
		</display:column>

		<display:column titleKey="enthusiast.email">
			<jstl:out value="${actor.email}"></jstl:out>
		</display:column>

		<display:column titleKey="actor.status">
			<jstl:choose>
				<jstl:when test="${actor.userAccount.isBanned}">
					<button
						onclick="window.location.href='administrator/unban.do?id=${actor.id}'">
						<spring:message code="actor.unban" />
					</button>
				</jstl:when>
				<jstl:otherwise>
					<button
						onclick="window.location.href='administrator/ban.do?id=${actor.id}'">
						<spring:message code="actor.ban" />
					</button>
				</jstl:otherwise>
			</jstl:choose>
		</display:column>

	</display:table>

	<acme:cancel url="/" code="group.cancel" />

</security:authorize>
