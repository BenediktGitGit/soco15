//var chartgen = 0;
//var donut = 0;
//var mse_chart = 0;
//
//var i = 1;
//var c = 0;
//
//
//var interval = 1500;
//var real_output_list = Create2DArray(4);
//var predicted_output_list = Create2DArray(4);
//var dist = Create2DArray(3);
//var dist_helper = Create2DArray(3);
//
//var dps = '[{"x": 1, "y": 10}, {"x": 2, "y": 13}, {"x": 2, "y": 11}, {"x": 21, "y": 24}]';
//var dps_json = JSON.parse(dps);
//var dps_iter = [];
//dps_iter.push(dps_json[0]);
//
//initDonutProcessing();
//initChartProcessing();
//initMseChart();
//loadCharts(i, predicted_output_list.length);
////var length = getDistLength(dist[0]);
//
//// GET predicted & real Value List
//
//real_output_list[0] = ['real_output', 30, 200, 100, 400, 150];
//real_output_list[1] = ['real_output', 200, 100, 400, 400, 300];
//real_output_list[2] = ['real_output', 100, 400, 400, 300, 270];
//real_output_list[3] = ['real_output', 400, 400, 300, 270, 290];
//
//predicted_output_list[0] = ['predicted output', 50, 20, 10, 40, 15];
//predicted_output_list[1] = ['predicted output', 20, 10, 40, 15, 70];
//predicted_output_list[2] = ['predicted output', 10, 40, 15, 70, 120];
//predicted_output_list[3] = ['predicted output', 40, 15, 70, 120, 79];
//
//
//// GET Distribution List of Value List
//dist[0].push(['under', 1]);
//dist[1].push(['equal', 0]);
//dist[2].push(['over', 0]);
//dist[0].push(['under', 0]);
//dist[1].push(['equal', 1]);
//dist[2].push(['over', 0]);
//dist[0].push(['under', 0]);
//dist[1].push(['equal', 0]);
//dist[2].push(['over', 1]);
//dist[0].push(['under', 0]);
//dist[1].push(['equal', 0]);
//dist[2].push(['over', 1]);
//
//dist_helper[0] = dist[0][0];
//dist_helper[1] = dist[1][0];
//dist_helper[2] = dist[2][0];
//
//function Create2DArray(rows) {
//    var arr = [];
//
//    for (var i=0;i<rows;i++) {
//        arr[i] = [];
//    }
//
//    return arr;
//}
//
////function getDistLength(obj) {
////    for (var key in obj) {
////        if (obj.hasOwnProperty(key)) ++c;
////    }
////    return c;
////}
//
//var donut = initDonutProcessing();
//var chartgen = initChartProcessing();
//var i = 1;
//loadCharts(i, predicted_output_list.length);
//var length = getDistLength(dist[0]);
//
//function initChartProcessing() {
//
//    chartgen =   c3.generate({
//        x: 'real_output',
//        bindto: '#chart',
//        data: {
//            columns: [
//                real_output_list[0],
//                predicted_output_list[0]
//            ],
//            axes: {
//                show: true,
//                data2: 'DAX',
//                label: {
//                    text: 'Value',
//                    position: 'outer-middle'
//                }
//            },
//            axis: {
//                x: {
//                    tick: {
//                        count: 5,
//                        format: d3.format('$,')
//                    }
//                }
//            }
//        },
//        bindto: '#chart'
//    });
//}
//
//function initDonutProcessing() {
//    donut = c3.generate({
//        data: {
//            columns: [
//                dist_helper[0],
//                dist_helper[1],
//                dist_helper[2]
//            ],
//            type: 'donut',
//            onclick: function (d, i) {
//                console.log("onclick", d, i);
//            },
//            onmouseover: function (d, i) {
//                console.log("onmouseover", d, i);
//            },
//            onmouseout: function (d, i) {
//                console.log("onmouseout", d, i);
//            }
//        },
//        donut: {
//            title: "Distribution"
//        },
//        bindto: "#donut"
//    });
//
//    return donut;
//}
//
