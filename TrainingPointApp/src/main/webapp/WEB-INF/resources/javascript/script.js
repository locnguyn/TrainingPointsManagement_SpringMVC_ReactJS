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