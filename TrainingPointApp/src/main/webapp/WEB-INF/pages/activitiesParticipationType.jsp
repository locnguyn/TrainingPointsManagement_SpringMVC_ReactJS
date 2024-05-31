<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<h1 class="text-center text-info m-4">QUẢN LÝ HOẠT ĐỘNG NGOẠI KHÓA</h1>
<c:if test="${not empty message}">
    <div class="alert ${alertClass} alert-dismissible fade show" role="alert">
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>
<c:choose>
    <c:when test="${activityId != null}">
        <a class="btn btn-success" href="<c:url value="/activity-participation-type/add?activityId=${activityId}" />">Thêm hình thức tham gia cho hoạt động này</a>
    </c:when>
</c:choose>
<table class="table table-striped mt-1">
    <tr>
        <th>Id</th>
        <th>Hoạt động</th>
        <th>Hình thức tham gia</th>
        <th>Điểm</th>
            <c:choose>
                <c:when test="${pageContext.request.userPrincipal.name != null}">
                <th>Nạp danh sách điểm danh</th>
                <th></th>
                <th></th>
                </c:when>
            </c:choose>
    </tr>
    <c:forEach items="${activitiesParticipationType}" var="a">
        <tr>
            <td>${a.id}</td>
            <td>${a.activityId.name}</td>
            <td>${a.participationTypeId.name}</td>
            <td>${a.point}</td>
            <c:choose>
                <c:when test="${pageContext.request.userPrincipal.name != null}">
                    <td>
                        <form method="post" action="<c:url value="/activity-participation-type/update/${a.id}/upload-csv" />" enctype="multipart/form-data" class="d-flex">
                            <input type="file" class="form-control me-2" name="file" accept=".csv" required>
                            <button type="submit" class="btn btn-primary">
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-upload" viewBox="0 0 16 16">
                                <path d="M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5"/>
                                <path d="M7.646 1.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1-.708.708L8.5 2.707V11.5a.5.5 0 0 1-1 0V2.707L5.354 4.854a.5.5 0 1 1-.708-.708z"/>
                                </svg>
                            </button>
                        </form>
                    </td>
                    <td>
                        <a class="" href="<c:url value="/activity-participation-type/update/${a.id}" />">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-pencil-square" viewBox="0 0 16 16">
                            <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z"/>
                            <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"/>
                            </svg>
                        </a>
                    </td>
                    <td>
                        <c:url value="/activity-participation-type/delete/${a.id}" var="url" />
                        <a href="${url}" class="text-danger" data-bs-toggle="tooltip" data-bs-placement="top" title="Tooltip on top" onclick="return confirm('Bạn có chắc chắn muốn xóa?');">
                            <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-x" viewBox="0 0 16 16">
                            <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
                            </svg>
                        </a>
                    </td>
                </c:when>
            </c:choose>
        </tr>
    </c:forEach>
</table>