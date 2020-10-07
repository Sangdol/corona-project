// https://stackoverflow.com/a/2901298/524588
function addCommas(n) {
  return n.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
}

// tabledata is from ricotta-milk.js
tabledata.countries.forEach((country) => {
  if (country.date == tabledata.date) {
    country.name_and_date = country.location;
  } else {
    country.name_and_date =
      `${country.location}<br>
      <span class="sub-text">${formatDateShort(country.date)}</span>`;
  }
});

const table = new Tabulator("#table", {
    selectable: false,
    data: tabledata.countries,
    layout: "fitColumns",
    initialSort:[
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
          title: `New<br><span class="sub-text">Daily</span>`,
          titleFormatter: "html",
          field: "new_cases",
          widthGrow: 5,
          formatter: function(cell) {
            return addCommas(cell.getValue());
          },
          resizable: false,
          headerSortStartingDir:"desc",
        },
        {
          title: `Density<br><span class="sub-text">Per Million</span>`,
          titleFormatter: "html",
          field: "new_cases_per_million",
          widthGrow: 5,
          formatter: function(cell) {
            return addCommas(cell.getValue().toFixed(2));
          },
          resizable: false,
          headerSortStartingDir: "desc",
        },
    ],
});

const tableTotal = new Tabulator("#table-total", {
    selectable: false,
    data: tabledata.countries,
    layout: "fitColumns",
    initialSort:[
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
          headerSortStartingDir: "desc",
        },
        {
          title: `Density<br><span class="sub-text">Per Million</span>`,
          titleFormatter: "html",
          field: "total_cases_per_million",
          widthGrow: 5,
          formatter: function(cell) {
            return addCommas(cell.getValue().toFixed(1));
          },
          resizable: false,
          headerSortStartingDir: "desc",
        },
    ],
});

function trendFormatter(weeks) {
  return function(cell, formatterParams, onRendered){
    onRendered(function(){
      // https://omnipotent.net/jquery.sparkline/#s-about
      $(cell.getElement()).sparkline(cell.getValue().slice(-weeks),
        {
          width:"100%",
          type:"line",
          disableTooltips:false,
          lineColor: "#353B3C",
          fillColor: "#EEF0F2",
          highlightLineColor: "#333",
          spotRadius: 0,
        });
    });
  }
}

// http://tabulator.info/docs/4.7/sort
function chartSorter(a, b, aRow, bRow, column, dir, sorterParams) {
  function rangeSum(list) {
    return list.slice(-sorterParams.range).reduce((x, y) => x + y);
  }

  return rangeSum(a) - rangeSum(b);
}

const trendShortWeekSize = 8;
const trendLongWeekSize = tabledata['weekly-trend-size'];

const tableTrend = new Tabulator("#table-trend", {
    selectable: false,
    data: tabledata.countries,
    layout: "fitColumns",
    initialSort:[
        {column:"weekly-trend", dir:"desc"},
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
          title: `${trendShortWeekSize} Weeks`,
          titleFormatter: "html",
          field: "weekly-trend",
          widthGrow: 5,
          formatter: trendFormatter(trendShortWeekSize),
          sorter: chartSorter,
          sorterParams: {range:trendShortWeekSize},
          resizable: false,
          headerSortStartingDir: "desc",
        },
        {
          title: `${trendLongWeekSize} Weeks`,
          titleFormatter: "html",
          field: "weekly-trend",
          widthGrow: 5,
          formatter: trendFormatter(trendLongWeekSize),
          sorter: chartSorter,
          sorterParams: {range:trendLongWeekSize},
          resizable: false,
          headerSortStartingDir: "desc",
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

  gtag('event', 'button_click', {
    button_name: name
  });
});

$('.tabulator-sortable').click(function() {
  const tableName = $(this).closest('.tabulator.table').data('name');
  const buttonName = $(this).attr('tabulator-field');

  gtag('event', 'sort_click', {
    table_name: tableName,
    button_name: buttonName
  });
});
