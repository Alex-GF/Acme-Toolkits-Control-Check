<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="urn:jsptagdir:/WEB-INF/tags"%>

<acme:list>
	<acme:list-column code="inventor.rustoro.list.label.code" path="code" />	
	<acme:list-column code="inventor.rustoro.list.label.title" path="title" />
	<acme:list-column code="inventor.rustoro.list.label.budget" path="budget" />
	<acme:list-column code="inventor.rustoro.list.label.itemName" path="itemName" />
</acme:list>