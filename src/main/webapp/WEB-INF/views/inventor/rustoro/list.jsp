<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="urn:jsptagdir:/WEB-INF/tags"%>

<acme:list>
	<acme:list-column code="inventor.rustoro.list.label.code" path="code" />	
	<acme:list-column code="inventor.rustoro.list.label.name" path="name" />
	<acme:list-column code="inventor.rustoro.list.label.quota" path="quota" />
	<acme:list-column code="inventor.rustoro.list.label.itemName" path="itemName" />
</acme:list>