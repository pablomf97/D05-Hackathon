<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<script>
	function YNconfirm(uri, message1) {
		if (window.confirm(message1)) {
			window.location.href = uri;
		}
	}
</script>

<security:authorize access="hasAnyRole('FILMENTHUSIAST')">
	<jstl:choose>
		<jstl:when test="${isMember or isCreator}">
			<display:table pagesize="10" class="displaytag"
				name="filmenthusiasts" requestURI="${requestURI}" id="enthusiast">

				<display:column titleKey="enthusiast.username">
					<jstl:out value="${enthusiast.userAccount.username}" />
				</display:column>

				<display:column titleKey="enthusiast.name">
					<jstl:out value="${enthusiast.name} ${enthusiast.surname}" />
				</display:column>

				<display:column titleKey="enthusiast.email">
					<jstl:out value="${enthusiast.email}"></jstl:out>
				</display:column>

				<display:column>
					<button
						onclick="location.href='filmEnthusiast/display.do?id=${enthusiast.id}'">
						<spring:message code="enthusiast.profile" />
					</button>
				</display:column>

				<display:column>
					<spring:message code="delete.confirm" var="confirmation" />
					<jstl:if
						test="${not(groupId  eq 0) and eventId eq 0 and group.isActive}">
						<button
							onclick="YNconfirm('group/filmenthusiast/delete.do?memberId=${enthusiast.id}&groupId=${groupId}','${confirmation}')">

							<spring:message code="enthusiast.delete" />
						</button>
					</jstl:if>
				</display:column>

			</display:table>
			<acme:cancel url="/" code="group.cancel" />
		</jstl:when>
		<jstl:otherwise>

		</jstl:otherwise>

	</jstl:choose>

</security:authorize>
