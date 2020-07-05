// https://stackoverflow.com/a/10601315/524588
function abbreviateNumber(value) {
    var newValue = value;
    if (value >= 1000) {
        var suffixes = ["", "K", "M", "B", "T"];
        var suffixNum = Math.floor( (""+value).length/3 );
        var shortValue = '';
        for (var precision = 2; precision >= 1; precision--) {
            shortValue = parseFloat( (suffixNum != 0 ? (value / Math.pow(1000,suffixNum) ) : value).toPrecision(precision));
            var dotLessShortValue = (shortValue + '').replace(/[^a-zA-Z 0-9]+/g,'');
            if (dotLessShortValue.length <= 2) { break; }
        }
        if (shortValue % 1 != 0)  shortValue = shortValue.toFixed(1);
        newValue = shortValue+suffixes[suffixNum];
    }
    return newValue;
}

// https://stackoverflow.com/a/2901298/524588
function numberWithCommasFormatter(cell, formatterParams, onRendered) {
    return cell.getValue().toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

tabledata.forEach((country) => {
  country.populationShort = abbreviateNumber(country.population);
});

const table = new Tabulator("#container", {
    data: tabledata,
    layout: "fitColumns",
    initialSort:[
        {column:"total_cases", dir:"desc"}, //then sort by this second
        {column:"new_cases", dir:"desc"}, //sort by this first
    ],
    columns: [
        {title: "Country", field: "location", widthGrow: 4},
        // {title: "Date", field: "date"},
        {title: "Total", field: "total_cases", widthGrow: 3, formatter: numberWithCommasFormatter},
        {title: "New", field: "new_cases", widthGrow: 2, formatter: numberWithCommasFormatter},
        // {title: "Total/M", field: "total_cases_per_million"},
        // {title: "New/M", field: "new_cases_per_million"},
        // {title: "POP", field: "populationShort"},
    ],
});
