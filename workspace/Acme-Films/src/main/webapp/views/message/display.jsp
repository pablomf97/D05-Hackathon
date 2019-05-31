<%@page language="java" contentType="text/html; charset=ISO-8859-1"
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
<td><strong><spring:message code="message.subject" />:</strong> </td>
<td><jstl:out value="${message0.subject}" /></td>
</tr>

<tr>
<td><strong><spring:message code="message.actor.sender" />:</strong> </td>
<td><jstl:out value="${message0.sender.userAccount.username}" /></td>
</tr>

<tr>
<td><strong><spring:message code="message.actor.recipient" />:</strong> </td>
<td><jstl:out value="${message0.receiver.userAccount.username}" /></td>
</tr>

<tr>
<td><strong><spring:message code="message.sendTime" />:</strong> </td>
<td><jstl:out value="${message0.sendMoment}" /></td>
</tr>

<tr>
<td><strong><spring:message code="message.priority" />:</strong> </td>
<td><jstl:out value="${message0.priority}" /></td>
</tr>

<tr>
<td><strong><spring:message code="message.tags" />:</strong> </td>
<td><jstl:out value="${message0.tag}" /></td>
</tr>

<tr>
<td><strong><spring:message code="message.body" />:</strong> </td>
<td><jstl:out value="${message0.body}" ></jstl:out></td>
</tr>
	
<jstl:if test="${not empty messageCode}">
	<h4>
		<spring:message code="${messageCode}" />
	</h4>
</jstl:if>


</table>
<button onClick="window.location.href='messagebox/list.do'">
	<spring:message code="message.cancel" />
</button>