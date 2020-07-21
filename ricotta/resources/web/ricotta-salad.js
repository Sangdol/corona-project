// https://stackoverflow.com/a/2901298/524588
function numberWithCommasFormatter(cell, formatterParams, onRendered) {
    return cell.getValue().toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

tabledata.countries.forEach((country) => {
  if (country.date == tabledata.date) {
    country.name_and_date = country.location;
  } else {
    country.name_and_date = `${country.location}<br><span class="update-date">${country.date}</span>`;
  }
});

const table = new Tabulator("#table", {
    data: tabledata.countries,
    layout: "fitColumns",
    initialSort:[
        {column:"total_cases", dir:"desc"},
        {column:"new_cases", dir:"desc"},
    ],
    columns: [
        {title: "Country", field: "name_and_date", widthGrow: 5, formatter: "html", resizable: false},
        {title: "New", field: "new_cases", widthGrow: 3, formatter: numberWithCommasFormatter, resizable: false},
        {title: "Total", field: "total_cases", widthGrow: 4, formatter: numberWithCommasFormatter, resizable: false},
    ],
});

document.getElementById("update-date").innerText = tabledata.date;