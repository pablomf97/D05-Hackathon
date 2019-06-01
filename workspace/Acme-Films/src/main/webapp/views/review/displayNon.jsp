<%--
 * action-1.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<jstl:if test="${!(review.isDraft)}">
	<table class="displayStyle">
		<tr>
			<td><strong> <spring:message code="review.title" /> :
			</strong></td>
			<td><jstl:out value="${review.title}" /></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="review.body" /> :
			</strong></td>
			<td><jstl:out value="${review.body}" /></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="review.rating" /> :
			</strong></td>
			<td><jstl:out value="${review.rating}" /></td>
		<tr>
			<td><strong> <spring:message code="review.creationDate" />
					:
			</strong></td>
			<td><jstl:out value="${review.creationDate}" /></td>
		<tr>
			<td><strong> <spring:message code="review.draft" /> :
			</strong></td>
			<td><jstl:out value="${review.isDraft}" /></td>
		<tr>
			<td><strong> <spring:message code="review.status" /> :
			</strong></td>
			<td><jstl:out value="${review.status}" /></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="review.rejectReason" />
					:
			</strong></td>
			<td><jstl:out value="${procession.rejectReason}" /></td>
		</tr>

		<jstl:if test="${review.moderator != null}">
			<tr>
				<td><strong> <spring:message code="review.moderator" />
						:
				</strong></td>
				<td><jstl:out value="${review.moderator.userAccount.username}" /></td>
			</tr>


		</jstl:if>

	</table>

	<acme:cancel url="review/critic/listAll.do" code="review.cancel" />


</jstl:if>