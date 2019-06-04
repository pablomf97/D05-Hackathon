<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<security:authorize access="hasAnyRole('FILMENTHUSIAST','MODERATOR')">
	<table class="displayStyle">
		<tr>

			<td><strong><spring:message code="group.name" /> : </strong></td>
			<td><jstl:out value="${group.name}">
				</jstl:out></td>
		</tr>
		<tr>

			<td><strong><spring:message code="group.creationDate" />
					: </strong></td>
			<td><jstl:out value="${group.creationDate}">
				</jstl:out></td>
		</tr>
		<tr>

			<td><strong><spring:message code="group.creator" /> :
			</strong></td>
			<td><jstl:out value="${group.creator.userAccount.username}">
				</jstl:out></td>
		</tr>
		<tr>
			<td><strong><spring:message code="group.about" /> : </strong></td>

			<jstl:if test="${not empty group.sagaAbout }">
				<td><jstl:out value="${group.sagaAbout.title}"></jstl:out></td>
			</jstl:if>
			<jstl:if test="${not empty group.filmAbout }">
				<td><jstl:out value="${group.filmAbout.title}"></jstl:out></td>
			</jstl:if>

		</tr>
		<tr>

			<td><strong><spring:message code="group.description" />
					: </strong></td>
			<td><jstl:out value="${group.description}">
				</jstl:out></td>
		</tr>

	</table>
	<jstl:set var="contains2" value="false" />
		<jstl:forEach var="item2" items="${group.groupMembers}">
			<jstl:if
				test="${item2.userAccount.username eq actor.userAccount.username}">
				<jstl:set var="contains2" value="true" />
			</jstl:if>
		</jstl:forEach>
	<!--  Lista de comentarios -->
	  <jstl:if test="${group.creator eq actor or contains2}">
		<input type="button" name="list"
			value="<spring:message code="group.list.comments"/>"
			onclick="redirect: location.href = 'group/comments.do?Id=${group.id}';" />
	</jstl:if>
	<!-- Delete -->
	<jstl:if
		test="${group.creator.userAccount.username eq actor.userAccount.username and not group.isActive}">
		<input type="button" name="list"
			value="<spring:message code="group.delete"	/>"
			onclick="redirect: location.href = 'group/delete.do?Id=${group.id}';" />
				</jstl:if>
			
		<!-- edit -->
		<jstl:if
		test="${group.creator.userAccount.username eq actor.userAccount.username and group.isActive}">
		<input type="button" name="list"
			value="<spring:message code="group.edit"/>"
			onclick="redirect: location.href = 'group/edit.do?Id=${group.id}';" />
	</jstl:if>
	<!-- moderator -->
	<jstl:if
		test="${empty group.moderator and not group.isActive}">
		<input type="button" name="list"
			value="<spring:message code="group.active"	/>"
			onclick="redirect: location.href = 'group/moderator/activate.do?Id=${group.id}';" />
	</jstl:if>
	<!-- Cancelar -->
	<acme:cancel url="group/list.do" code="group.cancel" />
	<!-- Request -->
	<security:authorize access="hasRole('FILMENTHUSIAST')">
		
		<jstl:if test="${contains2}">
			<a href="group/filmenthusiast/request.do?Id=${group.id}"> <spring:message
					code="group.request" />
			</a>
		</jstl:if>
	</security:authorize>
	<!-- Request -->
	<security:authorize access="hasRole('FILMENTHUSIAST')">
		<jstl:set var="contains2" value="false" />
		<jstl:forEach var="item2" items="${group.groupMembers}">
			<jstl:if
				test="${item2.userAccount.username eq actor.userAccount.username}">
				<jstl:set var="contains2" value="true" />
			</jstl:if>
		</jstl:forEach>
		<jstl:if
			test="${contains2 or group.creator.userAccount.username eq actor.userAccount.username}">
			<a href="event/list.do?Id=${group.id}"> <spring:message
					code="group.event.list" />
			</a>
		</jstl:if>
	</security:authorize>
</security:authorize>
