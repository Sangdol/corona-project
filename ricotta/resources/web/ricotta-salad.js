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

tabledata.forEach((country) => {
  country.populationShort = abbreviateNumber(country.population);
});

const table = new Tabulator("#container", {
    data: tabledata,
    layout: "fitColumns",
    columns: [
        {title: "Country", field: "location"},
        // {title: "Date", field: "date"},
        {title: "Total", field: "total_cases"},
        {title: "New", field: "new_cases"},
        // {title: "Total/M", field: "total_cases_per_million"},
        // {title: "New/M", field: "new_cases_per_million"},
        {title: "POP", field: "populationShort"},
    ],
});
