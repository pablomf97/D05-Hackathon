<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<h1><spring:message	code="visualization.title.list" />
<jstl:out value="${visualization.film.title}" /></h1>
	<jstl:choose>
		<jstl:when test="${isPrincipal}">

			<display:table class="displaytag" name="visualizations" pagesize="5" 
				requestURI="visualization/list.do" id="visualization">

				<display:column titleKey="visualization.siteName" sortable="true">
					<jstl:out value="${visualization.siteName}" />
				</display:column>

				<display:column titleKey="visualization.price" sortable="true">
					<jstl:out value="${visualization.price}" />
				</display:column>
				
				<display:column titleKey="visualization.link" sortable="true">
					<a href="${visualization.link}"> 
						<spring:message
							code="visualization.link.goto" />
					</a>
				</display:column>
				
				<display:column>
					<a href="visualization/edit.do?visualizationId=${visualization.id}"> <spring:message
							code="visualization.edit" />
					</a>
				</display:column>
				
				<display:column>
					<a href="visualization/delete.do?visualizationId=${visualization.id}"> <spring:message
							code="visualization.delete" />
					</a>
				</display:column>
				
			</display:table>
		</jstl:when>
		<jstl:otherwise>
			<display:table class="displaytag" name="visualizations" pagesize="5" 
				requestURI="visualization/list.do" id="visualization">

				<display:column titleKey="visualization.siteName" sortable="true">
					<jstl:out value="${visualization.siteName}" />
				</display:column>

				<display:column titleKey="visualization.price" sortable="true">
					<jstl:out value="${visualization.price}" />
				</display:column>
				
				<display:column titleKey="visualization.link" sortable="true">
					<a href="${visualization.link}"> 
						<spring:message
							code="visualization.link.goto" />
					</a>
				</display:column>
			</display:table>
		</jstl:otherwise>
	</jstl:choose>