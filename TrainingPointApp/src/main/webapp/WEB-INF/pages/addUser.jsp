<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1 class="text-center text-info m-4">Thêm trợ lý sinh viên</h1>
<c:url value="/assistant/add" var="action" />
<form:form method="post" action="${action}" modelAttribute="studentUserForm" enctype="multipart/form-data">
    <!-- Form fields for Student Assistant -->
    <div class="form-floating mb-3 mt-3">
        <form:input class="form-control" path="firstName" placeholder="Tên sinh viên" />
        <label for="firstName">Tên Sinh viên</label>
        <form:errors path="firstName" cssClass="text text-danger" />
    </div>

    <div class="form-floating mb-3 mt-3">
        <form:input class="form-control" path="lastName" placeholder="Họ sinh viên" />
        <label for="lastName">Họ Sinh viên</label>
        <form:errors path="lastName" cssClass="text text-danger" />
    </div>

    <div class="form-floating mb-3 mt-3">
        <form:input class="form-control" path="studentCode" placeholder="Mã số sinh viên" />
        <label for="studentCode">Mã số Sinh viên</label>
        <form:errors path="studentCode" cssClass="text text-danger" />
    </div>

    <div class="form-floating mb-3 mt-3">
        <form:select class="form-select" id="facultyId" path="facultyId">
            <c:forEach items="${faculties}" var="f">
                <form:option value="${f.id}">${f.name}</form:option>
            </c:forEach>
        </form:select>
        <label for="facultyId" class="form-label">Chọn khoa</label>
        <form:errors path="facultyId" element="div" cssClass="text text-danger" />
    </div>
    <div class="form-floating mb-3 mt-3">
        <form:select class="form-select" id="role" path="role">
            <form:option value="ROLE_USER">Sinh viên</form:option>
            <form:option value="ROLE_ASSISTANT">Trợ lý sinh viên</form:option>
        </form:select>
        <label for="role" class="form-label">Chọn vai trò</label>
    </div>S
    <div class="form-floating mb-3 mt-3">
        <form:select class="form-select" id="classId" path="classId">
            <c:forEach items="${classes}" var="c">
                <form:option value="${c.id}">${c.name}</form:option>
            </c:forEach>
        </form:select>
        <label for="classId" class="form-label">Chọn lớp</label>
        <form:errors path="classId" element="div" cssClass="text text-danger" />
    </div>

    <div class="form-floating mb-3 mt-3">
        <form:input class="form-control" path="email" type="email" placeholder="Email sinh viên" />
        <label for="email">Email Sinh viên</label>
        <form:errors path="email" cssClass="text text-danger" />
    </div>

    <div class="form-floating mb-3 mt-3">
        <form:input class="form-control" path="phoneNumber" placeholder="Số điện thoại sinh viên" />
        <label for="phoneNumber">Số điện thoại Sinh viên</label>
        <form:errors path="phoneNumber" cssClass="text text-danger" />
    </div>

    <div class="form-floating mb-3 mt-3">
        <form:input class="form-control" path="username" placeholder="Tên đăng nhập" />
        <label for="username">Tên đăng nhập</label>
        <form:errors path="username" cssClass="text text-danger" />
    </div>

    <div class="form-floating mb-3 mt-3">
        <form:password class="form-control" path="password" placeholder="Mật khẩu" />
        <label for="password">Mật khẩu</label>
        <form:errors path="password" cssClass="text text-danger" />
    </div>

    <div class="form-floating mb-3 mt-3">
        <form:password class="form-control" path="confirmPassword" placeholder="Xác nhận mật khẩu" />
        <label for="confirmPassword">Xác nhận mật khẩu</label>
        <form:errors path="confirmPassword" cssClass="text text-danger" />
    </div>

    <div class="form-floating mb-3 mt-3">
        <form:input type="file" class="form-control" path="avatar" placeholder="Ảnh đại diện" accept="image/png, image/jpeg" />
        <label for="avatar">Ảnh đại diện</label>
        <form:errors path="avatar" cssClass="text text-danger" />
    </div>

    <!-- Submit Button -->
    <button class="btn btn-success mt-3" type="submit">Lưu</button>
</form:form>
