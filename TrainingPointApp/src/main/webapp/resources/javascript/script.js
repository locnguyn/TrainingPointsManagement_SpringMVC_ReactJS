function formatDate(inputDate) {
    // Split the input date string
    var dateParts = inputDate.split(" ")[0].split("-");
    var year = dateParts[0];
    var month = dateParts[1];
    var day = dateParts[2];

    // Format the date
    var formattedDate = day + "/" + month + "/" + year;

    return formattedDate;
}

function openModal(imageSrc) {
    var modal = document.getElementById("myModal");
    var modalImg = document.getElementById("img01");
    modal.style.display = "block";
    modalImg.src = imageSrc;
}

function closeModal() {
    var modal = document.getElementById("myModal");
    modal.style.display = "none";
}

function drawChart(chartId, type, labels, data, title, chart) {
    if(chart)
        chart.destroy();
    const ctx = document.getElementById(chartId).getContext('2d');
    const bgs = labels.map(() => {
        return getRandomColor();
    });
    const datasets = [{
//                        label: [labels],
            data: data,
            backgroundColor: bgs
        }];


    chart = new Chart(ctx, {
        type: type,
        data: {
            labels: labels,
            datasets: datasets
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: title
                }
            }
        }
    });

    return chart;
}

function getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

function sumArr(arr) {
    return arr.reduce((a, b) => a + b, 0);
}