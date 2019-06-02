<%--
 * login.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
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

<security:authorize access="hasRole('FILMENTHUSIAST')">

	<form:form action="finder/filmEnthusiast/search.do" modelAttribute="finder">

		<form:hidden path="id" />
		<form:hidden path="version" />


		<form:label path="keyWord">
			<spring:message code="finder.keyWord" />:
		</form:label>
		<form:input path="keyWord" />
		<form:errors cssClass="error" path="keyWord" />
		<br />
		<br />



		<form:label path="minimumRating">
			<spring:message code="finder.minimumRating" />:
		</form:label>
		<form:input path="minimumRating"  />
		<form:errors cssClass="error" path="minimumRating" />


		<form:label path="maximumRating">
			<spring:message code="finder.maximumRating" />:
		</form:label>
		<form:input path="maximumRating"  />
		<form:errors cssClass="error" path="maximumRating" />
		<br />
		<br />
		
		
				<form:label path="maximumDuration">
			<spring:message code="finder.maximumDuration" />:
		</form:label>
		<form:input path="maximumDuration"  />
		<form:errors cssClass="error" path="maximumDuration" />
<br/>
<br/>
		
		<input type="submit" name="save" id="save"
			value="<spring:message code="finder.showResults" />" />
		
	&#160;
		<jstl:if test="${finder.id!=0}">
			<input type="submit" name="delete" id="delete"
				value='<spring:message code="finder.delete"/>' />
		</jstl:if>

	</form:form>

	<jstl:if test="${not empty films}">
		<display:table name="films" id="row"
			requestURI="${requestURI}" pagesize="10" class="displaytag">

			<!-- Attributes-->
			
			<display:column titleKey="film.title" sortable="true">
				<jstl:out value="${row.title}" />
			</display:column>
			<display:column titleKey="film.rating" sortable="true">
				<jstl:out value="${row.rating}" />
			</display:column>
	
			<display:column titleKey="film.synopsis" sortable="true">
				<jstl:out value="${row.synopsis}" />
			</display:column>
			<display:column titleKey="film.runTime" sortable="true">
				<jstl:out value="${row.runTime}" />
			</display:column>
			

			<!-- Action links -->

			<display:column>
				<a href="film/display.do?Id=${row.id}"> <spring:message
						code="film.display" />
				</a>
			</display:column>
		</display:table>
	</jstl:if>
</security:authorize>