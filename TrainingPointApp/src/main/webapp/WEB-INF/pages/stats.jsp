<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<div class="container">
    <c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
        <h1 class="text-info text-center m-4">THỐNG KÊ BÁO CÁO TOÀN TRƯỜNG</h1>
    </c:if>
    <c:if test="${pageContext.request.isUserInRole('ROLE_ASSISTANT')}">
        <h1 class="text-info text-center m-4">Thống kê báo cáo theo khoa ${user.student.facultyId.name}</h1>
    </c:if>
    <c:if test="${pageContext.request.userPrincipal != null}">
        <c:choose>
            <c:when test="${pageContext.request.isUserInRole('ROLE_ADMIN') || pageContext.request.isUserInRole('ROLE_ASSISTANT')}">
                <div class="mb-4">
                    <label for="semesterSelect">Chọn Học Kỳ:</label>
                    <select id="semesterSelect" class="form-select" onchange="updateCharts(this.value)">
                        <option selected value="0">Tất cả</option>
                        <c:forEach items="${semesters}" var="semester">
                            <option value="${semester.id}">Học Kỳ ${semester.semesterName}</option>
                        </c:forEach>
                    </select>
                </div>
                <c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
                    <div class="row mb-4">
                        <div class="col col-md-7 col-12">
                            <table class="table table-striped mt-1" id="facultyClassTable">
                                <thead>
                                    <tr>
                                        <th>Khoa</th>
                                        <th>Điểm</th>
                                        <th>Tỷ lệ</th>
                                    </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                        <div class="col col-md-5 col-12">
                            <canvas id="facultyClassChart"></canvas>
                        </div>
                    </div>
                    <div class="row mb-4">
                        <div class="col col-md-7 col-12">
                            <table class="table table-striped mt-1" id="classificationTable">
                                <thead>
                                    <tr>
                                        <th>Xếp loại</th>
                                        <th>Số lượng</th>
                                        <th>Tỷ lệ</th>
                                    </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                        <div class="col col-md-5 col-12">
                            <canvas id="classificationChart"></canvas>
                        </div>
                    </div>
                </c:if>
                <c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
                    <h1 class="text-info text-center m-4">THỐNG KÊ BÁO CÁO THEO KHOA</h1>
                </c:if>
                <c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
                    <label for="facultySelect">Chọn Khoa:</label>
                    <select class="form-select mb-4" onchange="updateFacultyCharts(this.value)" id="facultySelect">
                        <option selected value="0">Tất cả</option>
                        <c:forEach items="${faculties}" var="f">
                            <option value="${f.id}">${f.name}</option>
                        </c:forEach>
                    </select>
                </c:if>
                <c:if test="${pageContext.request.isUserInRole('ROLE_ASSISTANT')}">
                    <input type="hidden" id="assistantFacultyId" value="${user.student.facultyId.id}">
                </c:if>
                <div class="row mb-4">
                    <div class="col col-md-7 col-12">
                        <table class="table table-striped mt-1" id="facultyClassificationTable">
                            <thead>
                                <tr>
                                    <th>Xếp loại</th>
                                    <th>Số lượng</th>
                                    <th>Tỷ lệ</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <div class="col col-md-5 col-12">
                        <canvas id="facultyClassificationChart"></canvas>
                    </div>
                </div>
                <div class="row mb-4">
                    <div class="col col-md-7 col-12">
                        <table class="table table-striped mt-1" id="classTable">
                            <thead>
                                <tr>
                                    <th>Lớp</th>
                                    <th>Điểm</th>
                                    <th>Tỷ lệ</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <div class="col col-md-5 col-12">
                        <canvas id="classChart"></canvas>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <!-- If the role is not recognized, or the user is 'ROLE_USER' -->
                <h2 class="text-danger text-center m-4">Không có quyền truy cập</h2>
            </c:otherwise>
        </c:choose>
    </c:if>
</div>

<script>
    const contextPath = '<%= request.getContextPath()%>';
    let classChart = null, facultyClassChart = null, classificationChart = null, facultyClassificationChart = null;
    function fetchAndDrawChart(url, chart, canvasId, label, tableId) {
        return fetch(url, {
            credentials: 'include'
        })
                .then(response => response.json())
                .then(data => {
                    const labels = Object.keys(data);
                    const values = Object.values(data);
                    const tableBody = document.querySelector(tableId + ' > tbody');
                    console.log(tableId);
                    tableBody.innerHTML = '';
                    const total = sumArr(values);
                    labels.forEach((label, index) => {
                        const percentage = (values[index] / total * 100).toFixed(2);
                        const row = `<tr>
                            <td>` + label + `</td>
                            <td>` + values[index] + `</td>
                            <td>` + (isNaN(percentage)?"0":percentage) + `%</td>
                            </tr>`;
                        tableBody.innerHTML += row;
                    });
                    if (chart)
                        chart.destroy();
                    return drawChart(canvasId, 'pie', labels, values, label);
                });
    }

    function updateFacultyCharts(facultyId) {
        if (facultyId != 0)
            fetchAndDrawChart(contextPath + `/statistics/faculty/` + facultyId, classChart, 'classChart', 'Biểu đồ tổng điểm rèn luyện theo khoa', '#classTable')
                    .then(newChart => classChart = newChart);

        fetchAndDrawChart(contextPath + `/statistics/classification/` + facultyId, facultyClassificationChart, 'facultyClassificationChart', 'Biểu đồ xếp loại của khoa', '#facultyClassificationTable')
                .then(newChart => facultyClassificationChart = newChart);
    }

    function updateCharts(semesterId) {
        const facultySelect = document.getElementById("facultySelect");
        const assistantFacultyId = document.getElementById("assistantFacultyId");
        const facId = facultySelect ? facultySelect.value : (assistantFacultyId ? assistantFacultyId.value : '0');
        fetchAndDrawChart(contextPath + `/statistics/faculty/` + facId + `?semesterId=` + semesterId, classChart, 'classChart', 'Biểu đồ tổng điểm rèn luyện theo khoa', '#classTable')
                .then(newChart => classChart = newChart);

        fetchAndDrawChart(contextPath + `/statistics/classification/` + facId + `?semesterId=` + semesterId, facultyClassificationChart, 'facultyClassificationChart', 'Biểu đồ xếp loại của khoa', '#facultyClassificationTable')
                .then(newChart => facultyClassificationChart = newChart);

        fetchAndDrawChart(contextPath + `/statistics/classification/0` + `?semesterId=` + semesterId, classificationChart, 'classificationChart', 'Biểu đồ xếp loại toàn trường', '#classificationTable')
                .then(newChart => classificationChart = newChart);

        fetch(contextPath + `/statistics/faculty-class?semesterId=` + semesterId, {
            credentials: 'include'
        })
                .then(response => response.json())
                .then(data => {
                    const labels = Object.keys(data.facultyClassStats);
                    const values = labels.map(faculty => sumArr(Object.values(data.facultyClassStats[faculty])));

                    const tableBody = document.querySelector('#facultyClassTable > tbody');
                    tableBody.innerHTML = '';
                    const total = sumArr(values);
                    labels.forEach((label, index) => {
                        const percentage = (values[index] / total * 100).toFixed(2);
                        const row = `<tr>
                            <td>` + label + `</td>
                            <td>` + values[index] + `</td>
                            <td>` + (isNaN(percentage)?"0":percentage) + `%</td>
                            </tr>`;
                        tableBody.innerHTML += row;
                    });

                    if (facultyClassChart)
                        facultyClassChart.destroy();
                    facultyClassChart = drawChart('facultyClassChart', 'pie', labels, values, 'Biểu đồ tổng điểm rèn luyện các khoa toàn trường');
                });
    }
    document.addEventListener('DOMContentLoaded', () => {
        fetch(contextPath + `/statistics/faculty-class`, {
            credentials: 'include'
        })
                .then(response => response.json())
                .then(data => {
                    const labels = Object.keys(data.facultyClassStats);
                    const values = labels.map(faculty => sumArr(Object.values(data.facultyClassStats[faculty])));

                    const tableBody = document.querySelector('#facultyClassTable > tbody');
                    tableBody.innerHTML = '';
                    const total = sumArr(values);
                    labels.forEach((label, index) => {
                        const percentage = (values[index] / total * 100).toFixed(2);
                        const row = `<tr>
                            <td>` + label + `</td>
                            <td>` + values[index] + `</td>
                            <td>` + (isNaN(percentage)?"0":percentage) + `%</td>
                            </tr>`;
                        tableBody.innerHTML += row;
                    });

                    if (facultyClassChart)
                        facultyClassChart.destroy();
                    facultyClassChart = drawChart('facultyClassChart', 'pie', labels, values, 'Biểu đồ tổng điểm rèn luyện các khoa toàn trường');
                });
        fetchAndDrawChart(contextPath + `/statistics/classification/0`, classificationChart, 'classificationChart', 'Biểu đồ xếp loại toàn trường', '#classificationTable')
                .then(newChart => classificationChart = newChart);
    });
</script>
<script src="<c:url value="/javascript/script.js" />"></script>