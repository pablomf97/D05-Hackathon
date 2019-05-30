<%--
 * header.jsp
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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<div>
	<a href="#"><img src="${banner}" alt="Acme-Films Co., Inc."
		style="margin-bottom: 0.5em;" /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMIN')">
			<!-- Register admin -->
			<li><a class="fNiv"><spring:message
						code="master.page.register" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="administrator/administrator/register.do"><spring:message
								code="master.page.register.admin" /></a></li>
					<li><a href="administrator/statistics.do"><spring:message
								code="master.page.dashboard" /></a></li>

				</ul></li>

		</security:authorize>


		<security:authorize access="isAuthenticated()">

			<li><a class="fNiv"><spring:message
						code="master.page.visualization" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="visualization/list.do"><spring:message
								code="master.page.visualization.list" /></a></li>
				</ul></li>
				
			<li><a class="fNiv"><spring:message
						code="master.page.saga" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="saga/list.do"><spring:message
								code="master.page.saga.list" /></a></li>
				</ul></li>
		</security:authorize>

		<security:authorize access="isAnonymous()">
			<!-- Sign up -->
			<li><a class="fNiv"><spring:message
						code="master.page.singup" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="sponsor/register.do"><spring:message
								code="master.page.register.sponsor" /></a></li>
					<li><a href="filmEnthusiast/register.do"><spring:message
								code="master.page.register.filme" /></a></li>
					<li><a href="critic/register.do"><spring:message
								code="master.page.register.critic" /></a></li>
				</ul></li>

			<li><a class="fNiv" href="security/login.do"><spring:message
						code="master.page.login" /></a></li>
		</security:authorize>

		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv"> <spring:message
						code="master.page.profile" /> (<security:authentication
						property="principal.username" />)
			</a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="statistics/administrator/display.do"><spring:message
									code="master.page.dashboard" /></a></li>

						<li><a href="sysconfig/administrator/display.do"><spring:message
									code="master.page.system" /></a></li>

						<li><a href="administrator/display.do"><spring:message
									code="actor.view" /></a></li>
						<li><a href="administrator/export.do"><spring:message
									code="export" /></a></li>
						<li><a href="administrator/administrator/edit.do"><spring:message
									code="master.page.actor.edit" /></a></li>

					</security:authorize>

					<security:authorize access="hasRole('MODERATOR')">
						<li><a href="moderator/display.do"><spring:message
									code="actor.view" /></a></li>
						<li><a href="moderator/moderator/edit.do"><spring:message
									code="master.page.actor.edit" /></a></li>
					</security:authorize>

					<security:authorize access="hasRole('SPONSOR')">
						<li><a href="sponsor/display.do"><spring:message
									code="actor.view" /></a></li>
						<li><a href="sponsor/sponsor/edit.do"><spring:message
									code="master.page.actor.edit" /></a></li>
					</security:authorize>

					<security:authorize access="hasRole('FILMENTHUSIAST')">
						<li><a href="finder/filmEnthusiast/search.do"><spring:message
									code="master.page.finder" /></a></li>
						<li><a href="filmEnthusiast/display.do"><spring:message
									code="actor.view" /></a></li>
						<li><a href="filmEnthusiast/filmEnthusiast/edit.do"><spring:message
									code="master.page.actor.edit" /></a></li>
					</security:authorize>

					<security:authorize access="hasRole('CRITIC')">
						<li><a href="critic/display.do"><spring:message
									code="actor.view" /></a></li>
						<li><a href="critic/critic/edit.do"><spring:message
									code="master.page.actor.edit" /></a></li>
					</security:authorize>

					<li><a href="j_spring_security_logout"><spring:message
								code="master.page.logout" /> </a></li>
				</ul>
		</security:authorize>

	</ul>

</div>

<div style="float: right;">

	<a href="?language=en"><img style="width: 20px; height: 15px"
		src="https://upload.wikimedia.org/wikipedia/en/thumb/a/ae/Flag_of_the_United_Kingdom.svg/1280px-Flag_of_the_United_Kingdom.svg.png"
		alt="EN"></a> <span>|</span> <a href="?language=es"><img
		style="width: 20px; height: 15px;"
		src="http://www.ahb.es/m/100150RES.jpg" alt="ES"></a>
</div>

<security:authorize access="isAuthenticated()">
	<jstl:if
		test="${pageContext.response.locale.language == 'es' && not empty breachNotification.get('Espa�ol')}">
		<h2>
			<strong style="color: red;"><jstl:out
					value="${breachNotification.get('Espa�ol')}">
					<br>
				</jstl:out></strong>
		</h2>
	</jstl:if>
	<jstl:if
		test="${pageContext.response.locale.language == 'en' && not empty breachNotification.get('English')}">
		<h2>
			<strong style="color: red;"> <jstl:out
					value="${breachNotification.get('English')}">
					<br>
				</jstl:out>
			</strong>
		</h2>
	</jstl:if>
</security:authorize>
