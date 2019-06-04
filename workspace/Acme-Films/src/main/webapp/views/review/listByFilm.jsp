<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>



<display:table pagesize="5" class="displaytag" name="reviews"
	requestURI="review/lisByFilm.do" id="row">

	<!-- Attributes-->

	<display:column titleKey="review.title" sortable="true">
		<jstl:out value="${row.title }"></jstl:out>
	</display:column>

	<display:column titleKey="review.body" sortable="true">
		<jstl:out value="${row.body}"></jstl:out>
	</display:column>

	<display:column titleKey="review.rating">
		<jstl:out value="${row.rating}"></jstl:out>
	</display:column>


	<display:column titleKey="review.status" sortable="">
		<jstl:out value="${row.status}"></jstl:out>
	</display:column>


	<display:column titleKey="review.creationDate" sortable="true">
		<jstl:out value="${row.creationDate}"></jstl:out>
	</display:column>

	<display:column titleKey="review.draft">
		<jstl:out value="${row.isDraft}"></jstl:out>
	</display:column>

	<jstl:if test="${row.moderator != null}">

		<display:column titleKey="review.moderator" sortable="true">
			<jstl:out value="${row.moderator.userAccount.username}"></jstl:out>
		</display:column>
	</jstl:if>
	<display:column>
		<a href="review/displayNon.do?reviewId=${row.id}"> <spring:message
				code="review.display" />
		</a>
	</display:column>


</display:table>
