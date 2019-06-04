<%--
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<security:authorize access="isAuthenticated()">

	<form:form action="social/actor/edit.do"
		modelAttribute="socialProfile" methodParam="post">

		<form:hidden path="id" />
		<form:hidden path="version" />

		<form:label path="nick">
			<spring:message code="actor.socialprofile.nick" />:
		</form:label>
		<form:input path="nick" value="${socialProfile.nick}" />
		<form:errors cssClass="error" path="nick" />
		<br>

		<form:label path="socialNetwork">
			<spring:message code="actor.socialprofile.network" />:
		</form:label>
		<form:input path="socialNetwork"
			value="${socialProfile.socialNetwork}" />
		<form:errors cssClass="error" path="socialNetwork" />
		<br>

		<form:label path="link">
			<spring:message code="actor.socialprofile.link" />:
		</form:label>
		<form:input path="link" value="${socialProfile.link}" />
		<form:errors cssClass="error" path="link" />
		<br>

		<input type="submit" name="save" id="save"
			value='<spring:message code="system.save"/>' />
		<jstl:if test="${socialProfile.id != 0}">
			<input type="submit" name="delete" id="delete"
				value="<spring:message code="actor.delete.social" />"
				onclick="redirect: location.href = '/';" />
		</jstl:if>
		
		<input type="button" name="cancel"
			value="<spring:message code="actor.cancel" />"
			onclick="javascript: relativeRedir('/');" />

	</form:form>

</security:authorize>