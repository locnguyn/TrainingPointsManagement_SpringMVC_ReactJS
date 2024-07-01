<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1 class="text-center text-info m-4">Thêm đối tượng tham gia</h1>
<c:url value="/participant/add" var="action" />
<form:form method="post" action="${action}" modelAttribute="participant" enctype="multipart/form-data">
    <form:errors path="*" cssClass="text text-danger" />
    <div class="form-floating mb-3 mt-3">
        <form:input class="form-control" path="name" placeholder="Tên đối tượng" />
        <label for="firstName">Tên đối tượng</label>
        <form:errors path="name" cssClass="text text-danger" />
    </div>
    <div class="form-floating">
        <button class="btn btn-success mt-1" type="submit">
            <c:choose>
                <c:when test="${participant.id > 0}">Cập nhật</c:when>
                <c:otherwise>Thêm</c:otherwise>
            </c:choose>
        </button>
        <form:hidden path="id" />
    </div>
</form:form>
