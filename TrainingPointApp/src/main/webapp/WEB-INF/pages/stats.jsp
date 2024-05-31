<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<div class="container">
    <h1 class="text-info text-center m-4">THỐNG KÊ BÁO CÁO TOÀN TRƯỜNG</h1>
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

    <h1 class="text-info text-center m-4">THỐNG KÊ BÁO CÁO THEO KHOA</h1>
    <select class="form-select mb-4" onchange="updateFacultyCharts(this.value)">
        <option selected value="0">Tất cả</option>
        <c:forEach items="${faculties}" var="f">
            <option value="${f.id}">${f.name}</option>
        </c:forEach>
    </select>
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


</div>

<script>
    const contextPath = '<%= request.getContextPath()%>';
    let classChart = null, facultyClassChart = null, classificationChart = null, facultyClassificationChart = null;
    function fetchAndDrawChart(url, chart, canvasId, label, tableId) {
        return fetch(url)
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
                            <td>` + percentage + `%</td>
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
            fetchAndDrawChart(contextPath + `/api/statistics/faculty/` + facultyId, classChart, 'classChart', 'Biểu đồ tổng điểm rèn luyện theo khoa', '#classTable')
                    .then(newChart => classChart = newChart);

        fetchAndDrawChart(contextPath + `/api/statistics/classification/` + facultyId, facultyClassificationChart, 'facultyClassificationChart', 'Biểu đồ xếp loại của khoa', '#facultyClassificationTable')
                .then(newChart => facultyClassificationChart = newChart);
    }
    document.addEventListener('DOMContentLoaded', () => {
        fetch(contextPath + `/api/statistics/faculty-class`)
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
                            <td>` + percentage + `%</td>
                            </tr>`;
                        tableBody.innerHTML += row;
                    });

                    if (facultyClassChart)
                        facultyClassChart.destroy();
                    facultyClassChart = drawChart('facultyClassChart', 'pie', labels, values, 'Biểu đồ tổng điểm rèn luyện các khoa toàn trường');
                });
        fetchAndDrawChart(contextPath + `/api/statistics/classification/0`, classificationChart, 'classificationChart', 'Biểu đồ xếp loại toàn trường', '#classificationTable')
                .then(newChart => classificationChart = newChart);
    });
</script>
<script src="<c:url value="/javascript/script.js" />"></script>