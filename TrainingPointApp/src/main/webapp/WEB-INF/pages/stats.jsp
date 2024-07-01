<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<div class="container p-4" id="content">
    <c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
        <h3 class="text-info text-center m-4">THỐNG KÊ BÁO CÁO TOÀN TRƯỜNG</h3>
    </c:if>
    <c:if test="${pageContext.request.isUserInRole('ROLE_ASSISTANT')}">
        <h3 class="text-info text-center m-4">Thống kê báo cáo theo khoa ${user.student.facultyId.name}</h3>
    </c:if>
    <c:if test="${pageContext.request.userPrincipal != null}">
        <c:choose>
            <c:when test="${pageContext.request.isUserInRole('ROLE_ADMIN') || pageContext.request.isUserInRole('ROLE_ASSISTANT')}">
                <div class="mb-4 exclude-export">
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
                        <div class="col col-md-8 col-12">
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
                        <div class="col col-md-4 col-12">
                            <canvas id="facultyClassChart"></canvas>
                        </div>
                    </div>
                    <div class="row mb-4">
                        <div class="col col-md-8 col-12">
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
                        <div class="col col-md-4 col-12">
                            <canvas id="classificationChart"></canvas>
                        </div>
                    </div>
                </c:if>
                <c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
                    <h3 class="text-info text-center m-4">THỐNG KÊ BÁO CÁO THEO KHOA</h3>
                </c:if>
                <div class="exclude-export">
                    <c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
                        <label for="facultySelect">Chọn Khoa:</label>
                        <select class="form-select mb-4" onchange="updateFacultyCharts(this.value)" id="facultySelect">
                            <option selected value="0">Tất cả</option>
                            <c:forEach items="${faculties}" var="f">
                                <option value="${f.id}">${f.name}</option>
                            </c:forEach>
                        </select>
                    </c:if>
                </div>
                <c:if test="${pageContext.request.isUserInRole('ROLE_ASSISTANT')}">
                    <input type="hidden" id="assistantFacultyId" value="${user.student.facultyId.id}">
                </c:if>
                <div class="row mb-4">
                    <div class="col col-md-8 col-12">
                        <table class="table table-striped mt-1" id="classTable">
                            <thead>
                                <tr>
                                    <th>Lớp</th>
                                    <th>Điểm</th>
                                    <th>Tỷ lệ</th>
                                    <th class="exclude-export"></th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <div class="col col-md-4 col-12">
                        <canvas id="classChart"></canvas>
                    </div>
                </div>
                <div class="row mb-4">
                    <div class="col col-md-8 col-12">
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
                    <div class="col col-md-4 col-12">
                        <canvas id="facultyClassificationChart"></canvas>
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
<div class="d-flex">
    <button class="btn btn-primary mt-3 me-2" onclick="exportToPDF()">Xuất file PDF</button>
    <button class="btn btn-primary mt-3 me-2" onclick="exportPDFwithNoChart()">Xuất file PDF không biểu đồ</button>
    <button class="btn btn-secondary mt-3 me-2" onclick="downloadCSV()">Xuất CSV</button>
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
                    const semester = document.getElementById("semesterSelect").value;
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
                            <td>` + (isNaN(percentage) ? "0" : percentage) + `%</td>` +
                                (tableId === "#classTable" ? `<td class='d-flex justify-content-center exclude-export'>` + "<a class='btn btn-success exclude-export' href=" + contextPath + `/stats/?className=` + encodeURIComponent(label) + `&semesterId=` + semester + ">Chi tiết</a>" + `</td>` : "") +
                                `</tr>`;
                        tableBody.innerHTML += row;
                    });
                    if (chart)
                        chart.destroy();
                    return drawChart(canvasId, 'pie', labels, values, label);
                });
    }

    function updateFacultyCharts(facultyId) {
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
                            <td>` + (isNaN(percentage) ? "0" : percentage) + `%</td>
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
                            <td>` + (isNaN(percentage) ? "0" : percentage) + `%</td>
                            </tr>`;
                        tableBody.innerHTML += row;
                    });

                    if (facultyClassChart)
                        facultyClassChart.destroy();
                    facultyClassChart = drawChart('facultyClassChart', 'pie', labels, values, 'Biểu đồ tổng điểm rèn luyện các khoa toàn trường');
                });
        fetchAndDrawChart(contextPath + `/statistics/classification/0`, classificationChart, 'classificationChart', 'Biểu đồ xếp loại toàn trường', '#classificationTable')
                .then(newChart => classificationChart = newChart);

        fetchAndDrawChart(contextPath + `/statistics/classification/` + +
    <c:choose>
        <c:when  test="${pageContext.request.isUserInRole('ROLE_ASSISTANT')}">${user.student.facultyId.id}</c:when>
        <c:otherwise>'0'</c:otherwise>
    </c:choose>, facultyClassificationChart, 'facultyClassificationChart', 'Biểu đồ xếp loại của khoa', '#facultyClassificationTable')
                .then(newChart => facultyClassificationChart = newChart);

        fetchAndDrawChart(contextPath + `/statistics/faculty/` +
    <c:choose>
        <c:when  test="${pageContext.request.isUserInRole('ROLE_ASSISTANT')}">${user.student.facultyId.id}</c:when>
        <c:otherwise>'0'</c:otherwise>
    </c:choose>, classChart, 'classChart', 'Biểu đồ tổng điểm rèn luyện theo khoa', '#classTable')
                .then(newChart => classChart = newChart);
    });
    function checkImageLoaded() {
        console.log("Image loaded successfully.");
    }

    async function exportToPDF() {
        const eles = document.getElementsByClassName("exclude-export");
        for (let i = 0; i < eles.length; i++) {
            eles[i].style.display = "none";
        }
        const {jsPDF} = window.jspdf;
        const content = document.getElementById('content');
        html2canvas(content, {
            allowTaint: true,
            useCORS: true,
            scale: 1.5
        }).then(canvas => {
            const imgData = canvas.toDataURL('image/png');
            const pdf = new jsPDF('p', 'mm', 'a4');
            const pdfWidth = pdf.internal.pageSize.getWidth();
            const pdfHeight = (canvas.height * pdfWidth) / canvas.width;
            pdf.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight);
            pdf.save('class-achievements.pdf');
        }).then(() => {
            window.location.href = contextPath + "/stats";
        });
    }

    function downloadCSV() {
                    const semesterId = document.getElementById("semesterSelect").value;

        fetch(contextPath + `/statistics/csv?semesterId=` + semesterId)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.blob();
                })
                .then(blob => {
                    const url = window.URL.createObjectURL(blob);
                    const a = document.createElement('a');
                    a.style.display = 'none';
                    a.href = url;
                    a.download = 'university_achievements.csv';
                    document.body.appendChild(a);
                    a.click();
                    window.URL.revokeObjectURL(url);
                })
                .catch(error => console.error('There was a problem with the fetch operation:', error));
    }
    function exportPDFwithNoChart() {
                    const semesterId = document.getElementById("semesterSelect").value;

        fetch(contextPath + `/statistics/pdf?semesterId=` + semesterId)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.blob();
                })
                .then(blob => {
                    const url = window.URL.createObjectURL(blob);
                    const a = document.createElement('a');
                    a.style.display = 'none';
                    a.href = url;
                    a.download = 'university_achievements.pdf';
                    document.body.appendChild(a);
                    a.click();
                    window.URL.revokeObjectURL(url);
                })
                .catch(error => console.error('There was a problem with the fetch operation:', error));
    }
</script>
<script src="<c:url value="/javascript/script.js" />"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js"></script>