<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<h1 class="text-center text-info m-4">Danh sách học kỳ</h1>
<table class="table table-striped mt-1">
    <tr>
        <th>Id</th>
        <th>Tên học kỳ</th>
        <th>Ngày bắt đầu học kỳ</th>
        <th>Ngày kết thúc học kỳ</th>
            <c:if test="${pageContext.request.userPrincipal.name != null}">
            <th></th>
            <th></th>
            </c:if>
    </tr>
    <c:forEach items="${semesters}" var="s">
        <tr>
            <td>${s.id}</td>
            <td>${s.semesterName}</td>
            <td><fmt:formatDate value="${s.startDate}" pattern="dd-MM-yyyy"/></td>
            <td><fmt:formatDate value="${s.endDate}" pattern="dd-MM-yyyy"/></td>
            <c:if test="${pageContext.request.userPrincipal.name != null}">
                <td>
                    <a class="" href="<c:url value="/semester/update/${s.id}" />">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-pencil-square" viewBox="0 0 16 16">
                        <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z"/>
                        <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"/>
                        </svg>
                    </a>
                </td>
                <td>
                    <a onclick="return confirm('Xác nhận xóa học kỳ này?');" class="text-danger" href="<c:url value="/semester/remove/${s.id}" />">
                        <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-x" viewBox="0 0 16 16">
                        <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
                        </svg>
                    </a>
                </td>
            </c:if>
        </tr>
    </c:forEach>
</table>