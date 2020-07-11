// https://stackoverflow.com/a/2901298/524588
function numberWithCommasFormatter(cell, formatterParams, onRendered) {
    return cell.getValue().toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

const table = new Tabulator("#table", {
    data: tabledata.countries,
    layout: "fitColumns",
    initialSort:[
        {column:"total_cases", dir:"desc"},
        {column:"new_cases", dir:"desc"},
    ],
    columns: [
        {title: "Country", field: "location", widthGrow: 5},
        // {title: "Date", field: "date"},
        {title: "Total", field: "total_cases", widthGrow: 4, formatter: numberWithCommasFormatter},
        {title: "New", field: "new_cases", widthGrow: 3, formatter: numberWithCommasFormatter},
        // {title: "Total/M", field: "total_cases_per_million"},
        // {title: "New/M", field: "new_cases_per_million"},
        // {title: "POP", field: "populationShort"},
    ],
});

document.getElementById("update-date").innerText = tabledata.date;