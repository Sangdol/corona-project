const table = new Tabulator("#container", {
    height: 500, // set height of table (in CSS or here), this enables the Virtual DOM and improves render speed dramatically (can be any valid css height value)
    data: tabledata, //assign data to table
    layout: "fitColumns", //fit columns to width of table (optional)
    columns: [ //Define Table Columns
        {title: "Country", field: "location", width:150},
        //{title: "Age", field: "age", hozAlign: "left", formatter: "progress"},
        {title: "Date", field: "date"},
        {title: "Total Cases", field: "total_cases"},
        {title: "New Cases", field: "new_cases"},
        {title: "Total Cases per Milion", field: "total_cases_per_million"},
        {title: "New Cases per Milion", field: "new_cases_per_million"},
        {title: "Population", field: "population"},
    ],
});
