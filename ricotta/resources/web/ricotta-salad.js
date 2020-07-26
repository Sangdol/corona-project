// https://stackoverflow.com/a/2901298/524588
function numberWithCommasFormatter(cell, formatterParams, onRendered) {
    return cell.getValue().toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

tabledata.countries.forEach((country) => {
  if (country.date == tabledata.date) {
    country.name_and_date = country.location;
  } else {
    country.name_and_date = `${country.location}<br><span class="sub-text">${formatDateShort(country.date)}</span>`;
  }
});

const table = new Tabulator("#table", {
    selectable: false,
    data: tabledata.countries,
    layout: "fitColumns",
    initialSort:[
        {column:"total_cases", dir:"desc"},
        {column:"new_cases", dir:"desc"},
    ],
    columns: [
        {title: "Country", field: "name_and_date", widthGrow: 6, formatter: "html", resizable: false},
        {
          title: `New<br><span class="sub-text">Daily</span>`,
          titleFormatter: "html",
          field: "new_cases",
          widthGrow: 4,
          formatter: numberWithCommasFormatter,
          resizable: false
        },
        {title: "Total", field: "total_cases", widthGrow: 5, formatter: numberWithCommasFormatter, resizable: false},
    ],
});

function formatDateShort(dateStr) {
  const date = new Date(dateStr);
  const dateTimeFormat = new Intl.DateTimeFormat('en', { year: 'numeric', month: 'long', day: '2-digit' });
  const [{ value: month },,{ value: day },,{ value: year }] = dateTimeFormat .formatToParts(date);

  return `${month} ${day}`;
}

// https://stackoverflow.com/a/3552493/524588
// dateStr: e.g., 2020-07-21
function formatDate(dateStr) {
  const date = new Date(dateStr);
  const dateTimeFormat = new Intl.DateTimeFormat('en', { year: 'numeric', month: 'long', day: '2-digit' });
  const [{ value: month },,{ value: day },,{ value: year }] = dateTimeFormat .formatToParts(date);

  return `${month} ${day}, ${year}`
}

document.getElementById("update-date").innerText = formatDate(tabledata.date);