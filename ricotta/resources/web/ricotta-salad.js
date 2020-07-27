// https://stackoverflow.com/a/2901298/524588
function addCommas(n) {
  return n.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
}

// https://stackoverflow.com/a/19722641/524588
Number.prototype.round = function(places) {
  return +(Math.round(this + "e+" + places)  + "e-" + places);
}

tabledata.countries.forEach((country) => {
  if (country.date == tabledata.date) {
    country.name_and_date = country.location;
  } else {
    country.name_and_date =
      `${country.location}<br>
      <span class="sub-text">${formatDateShort(country.date)}</span>`;
  }

  country.new_and_total_cases =
    `${addCommas(country.new_cases)}<br>
    <span class="sub-text">Total: ${addCommas(country.total_cases)}</span>`;

  country.new_and_total_per_million =
    `${addCommas(country.new_cases_per_million.round(2))}`
});

// http://tabulator.info/docs/4.7/sort#func-custom
// takes number from "number + sub-text" html
function multilineSorter(a, b) {
    function cases(text) {
      return text.match(/[\d,]+/)[0].replace(/,/g, "");
    }

    return cases(a) - cases(b);
}

function numberWithCommasFormatter(cell, formatterParams, onRendered) {
    return cell.getValue().toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

const table = new Tabulator("#table", {
    selectable: false,
    data: tabledata.countries,
    layout: "fitColumns",
    initialSort:[
        {column:"new_cases_per_million", dir:"desc"},
        {column:"new_cases", dir:"desc"},
    ],
    columns: [
        {
          title: "Country",
          field: "name_and_date",
          widthGrow: 6,
          formatter: "html",
          resizable: false,
        },
        {
          title: `New`,
          titleFormatter: "html",
          field: "new_cases",
          widthGrow: 5,
          formatter: function(cell) {
            return addCommas(cell.getValue());
          },
          resizable: false
        },
        {
          title: `Density<br><span class="sub-text">Per Million</span>`,
          titleFormatter: "html",
          field: "new_cases_per_million",
          widthGrow: 4,
          formatter: function(cell) {
            return addCommas(cell.getValue().round(2));
          },
          resizable: false,
        },
    ],
});

const tableTotal = new Tabulator("#table-total", {
    selectable: false,
    data: tabledata.countries,
    layout: "fitColumns",
    initialSort:[
        {column:"total_cases_per_million", dir:"desc"},
        {column:"total_cases", dir:"desc"},
    ],
    columns: [
        {
          title: "Country",
          field: "name_and_date",
          widthGrow: 6,
          formatter: "html",
          resizable: false
        },
        {
          title: `Total`,
          titleFormatter: "html",
          field: "total_cases",
          widthGrow: 5,
          formatter: function(cell) {
            return addCommas(cell.getValue().toString());
          },
          resizable: false,
        },
        {
          title: `Density<br><span class="sub-text">Per Million</span>`,
          titleFormatter: "html",
          field: "total_cases_per_million",
          widthGrow: 5,
          formatter: function(cell) {
            return addCommas(cell.getValue().round(0));
          },
          resizable: false,
        },
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

$('.button-wrap button').click(function() {
  const name = $(this).data('name');

  $('.table').hide();
  $(`.${name}`).show();
});