<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1 class="text-center text-info m-4">Thêm Lớp Sinh Viên</h1>
<c:url value="/class/add" var="action" />
<form:form method="post" action="${action}" modelAttribute="clazz" enctype="multipart/form-data">
    <!-- Form fields for Student Assistant -->
    <div class="form-floating mb-3 mt-3">
        <form:input class="form-control" path="name" placeholder="Tên Học Kỳ" />
        <label for="firstName">Tên Lớp</label>
        <form:errors path="name" cssClass="text text-danger" />
    </div>
    <div class="form-floating">
        <button class="btn btn-success mt-1" type="submit">
            <c:choose>
                <c:when test="${clazz.id > 0}">Cập nhật</c:when>
                <c:otherwise>Thêm</c:otherwise>
            </c:choose>
        </button>
        <form:hidden path="id" />
    </div>
</form:form>
