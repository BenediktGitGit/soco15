var donut = c3.generate({
    data: {
        columns: [
            ['data1', 30],
            ['data2', 120]
        ],
        type : 'donut',
        onclick: function (d, i) { console.log("onclick", d, i); },
        onmouseover: function (d, i) { console.log("onmouseover", d, i); },
        onmouseout: function (d, i) { console.log("onmouseout", d, i); }
    },
    donut: {
        title: "Iris Petal Width"
    }
});

var chart = c3.generate({
    data: {
        xs: {
            data1: 'date1'
        },
        // iris data from R
        columns: [
            ['date1', 1425921671000, 1425921672000, 1425921673000, 1425921674000],
            ['data1', 1, 2, 3, 4],
        ],
        type: 'spline'
    },
    axis: {
        x: {
            type: 'timeseries',
            tick: {
                max: 5,
                format: '%m/%d'
            }
        }
    }
});

setTimeout(function (chart) {
    chart.flow({
        columns: [
            ["date1", 1425921675000],
            ["data1", 2],
        ],
        length: 1
    });
}, 1000);