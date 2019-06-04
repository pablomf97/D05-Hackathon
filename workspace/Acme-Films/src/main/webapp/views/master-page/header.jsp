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
					<li><a href="message/administrator/broadcast.do"><spring:message
								code="master.page.message.broadcast" /></a></li>
					<li><a href="/administrator/listSuspicious.do"><spring:message
								code="master.page.register.admin" /></a></li>

				</ul></li>

		</security:authorize>


		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv"><spring:message code="master.page.film" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="film/list.do"><spring:message
								code="master.page.film.list" /></a></li>
				</ul></li>
			<security:authorize access="hasAnyRole('MODERATOR','FILMENTHUSIAST')">
				<li><a class="fNiv"><spring:message
							code="master.page.group" /></a>
					<ul>
						<li class="arrow"></li>
						<li><a href="group/list.do"><spring:message
									code="master.page.mygroup" /></a></li>
						<security:authorize access="hasRole('MODERATOR')">
							<li><a href="group/moderator/listWithout.do"><spring:message
										code="master.page.deactiveGroups" /></a></li>
						</security:authorize>
					</ul></li>
			</security:authorize>

			<li><a class="fNiv"><spring:message code="master.page.saga" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="saga/list.do"><spring:message
								code="master.page.saga.list" /></a></li>
				</ul></li>

			<security:authorize access="hasRole('MODERATOR')">

				<li><a class="fNiv"><spring:message
							code="master.page.position" /></a>
					<ul>
						<li class="arrow"></li>
						<li><a href="position/moderator/list.do"><spring:message
									code="master.page.position.list" /></a></li>
					</ul></li>

				<li><a class="fNiv"><spring:message
							code="master.page.genre" /></a>
					<ul>
						<li class="arrow"></li>
						<li><a href="genre/moderator/list.do"><spring:message
									code="master.page.genre.list" /></a></li>
					</ul></li>

			</security:authorize>

			<li><a class="fNiv"><spring:message
						code="master.page.person" /></a>

				<ul>
					<li class="arrow"></li>
					<li><a href="person/list.do"><spring:message
								code="master.page.person.list" /></a></li>
				</ul></li>

			<security:authorize access="hasAnyRole('MODERATOR','SPONSOR')">

				<li><a class="fNiv"><spring:message
							code="master.page.sponsorship" /></a>
					<ul>
						<li class="arrow"></li>
						<li><a href="sponsorship/list.do"><spring:message
									code="master.page.sponsorship.list" /></a></li>
					</ul></li>

			</security:authorize>

		</security:authorize>

		<security:authorize access="hasRole('FILMENTHUSIAST')">

			<li><a class="fNiv"><spring:message
						code="master.page.comments" /></a>
				<ul>

					<li><a href="comment/filmEnthusiast/list.do"><spring:message
								code="master.page.comment.list" /></a></li>


					<li><a href="comment/filmEnthusiast/createFilm.do"><spring:message
								code="master.page.comment.create" /></a></li>

				</ul></li>


		</security:authorize>

		<security:authorize access="hasRole('CRITIC')">

			<li><a class="fNiv"><spring:message
						code="master.page.reviews" /></a>
				<ul>

					<li><a href="review/critic/listAll.do"><spring:message
								code="master.page.review.listAll" /></a></li>


					<li><a href="review/critic/create.do"><spring:message
								code="master.page.review.create" /></a></li>

				</ul></li>


		</security:authorize>

		<security:authorize access="hasRole('MODERATOR')">

			<li><a class="fNiv"><spring:message
						code="master.page.reviews" /></a>
				<ul>

					<li><a href="review/moderator/listToAssign.do"><spring:message
								code="master.page.review.listToAssign" /></a></li>

					<li><a href="review/moderator/listMyReviews.do"><spring:message
								code="master.page.review.listMyReviews" /></a></li>



				</ul></li>


		</security:authorize>


		<security:authorize access="isAnonymous()">
			<li><a class="fNiv"><spring:message code="master.page.film" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="film/list.do"><spring:message
								code="master.page.film.list" /></a></li>
				</ul></li>
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
						<li><a href="moderator/export.do"><spring:message
									code="export" /></a></li>
					</security:authorize>

					<security:authorize access="hasRole('SPONSOR')">
						<li><a href="sponsor/display.do"><spring:message
									code="actor.view" /></a></li>
						<li><a href="sponsor/sponsor/edit.do"><spring:message
									code="master.page.actor.edit" /></a></li>
						<li><a href="sponsor/export.do"><spring:message
									code="export" /></a></li>
					</security:authorize>

					<security:authorize access="hasRole('FILMENTHUSIAST')">
						<li><a href="finder/filmEnthusiast/search.do"><spring:message
									code="master.page.finder" /></a></li>
						<li><a href="filmEnthusiast/display.do"><spring:message
									code="actor.view" /></a></li>
						<li><a href="filmEnthusiast/filmEnthusiast/edit.do"><spring:message
									code="master.page.actor.edit" /></a></li>



						<li><a href="filmEnthusiast/export.do"><spring:message
									code="export" /></a></li>

					</security:authorize>

					<security:authorize access="hasRole('CRITIC')">
						<li><a href="critic/display.do"><spring:message
									code="actor.view" /></a></li>
						<li><a href="critic/critic/edit.do"><spring:message
									code="master.page.actor.edit" /></a></li>
						<li><a href="critic/export.do"><spring:message
									code="export" /></a></li>
					</security:authorize>


					<li><a href="messagebox/list.do"><spring:message
								code="master.page.profile.message.boxes" /></a></li>
					<li><a href="messagebox/create.do"><spring:message
								code="master.page.box.new" /></a></li>
					<li><a href="message/actor/create.do"><spring:message
								code="master.page.message.new" /></a></li>



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
		test="${pageContext.response.locale.language == 'es' && not empty breachNotification.get('Español')}">
		<h2>
			<strong style="color: red;"><jstl:out
					value="${breachNotification.get('Español')}">
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
