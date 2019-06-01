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
			<jstl:out value="${group.creator.userAccount.username}"></jstl:out>
		</display:column>
		<display:column titleKey="group.about">
			<jstl:if test="${not empty group.sagaAbout }">
				<jstl:out value="${group.sagaAbout.title}"></jstl:out>
			</jstl:if>
			<jstl:if test="${not empty group.filmAbout }">
				<jstl:out value="${group.filmAbout.title}"></jstl:out>
			</jstl:if>
		</display:column>

		<display:column titleKey="group.display">
			<jstl:if test="${group.isActive or group.creator eq actor }">
				<a href="group/display.do?Id=${group.id}"> <spring:message
						code="group.display" />
				</a>
			</jstl:if>
		</display:column>
		<!-- Solicitar membresía -->
		<display:column titleKey="group.request">
			<security:authorize access="hasRole('FILMENTHUSIAST')">
				<jstl:set var="contains" value="false" />

				<jstl:forEach var="item2" items="${event.forum.groupMembers}">
					<jstl:if test="${item2 eq actor}">
						<jstl:set var="contains" value="true" />
					</jstl:if>
				</jstl:forEach>
				<jstl:if test="${true}">
					<a href="group/filmenthusiast/request.do?Id=${group.id}"> <spring:message
							code="group.request" />
					</a>
				</jstl:if>
			</security:authorize>
		</display:column>
		<!-- Editar por moderador -->
		<display:column titleKey="group.active">
			<security:authorize access="hasRole('MODERATOR')">
				<jstl:if
					test="${group.moderator eq actor or empty group.moderator }">
					<a href="group/moderator/edit.do?Id=${group.id}"> <spring:message
							code="group.active" />
					</a>
				</jstl:if>
			</security:authorize>
		</display:column>
	</display:table>
	<acme:cancel url="/" code="group.cancel" />
</security:authorize>
