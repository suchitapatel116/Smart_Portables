$(document).ready(function() {
	getTotalSalesData();
});

function getTotalSalesData() 
{
	$.ajax({
		//servlet name from where the products list in json format is to be obtained
		url: "ChartVisualizationData",				
		dataType: "JSON",

		//GET = download data from some url/somewhere
		//POST = send data to server
		type: "POST",
		data: "{}",
		success: function(data){
			drawChart(data);
		},
		error: function() {
			console.log("Error: during ajax call");
		}
	});
}

google.charts.load('current', {packages: ['corechart','bar','table','controls']});
//google.charts.setOnLoadCallback(drawChart);

function drawChart(jsonData) 
{
	//1. set options for drawing the bar chart
	var options = {
		height: 400,
		title: 'Total Sales Chart',
		bars:'vertical',
		fontSize: 12,
		hAxis: {
			title: 'Product Name',
		},
		vAxis: {
			title: 'Total Sales',
		},
		legend: {
			position: 'top',
			alignment: 'end',
		},
	};

	//2. set the visualization data to be displayed
	var data_arr = [];
	//var keys_pna = new Array();
	//keys_pna = Object.keys(jsonData);

	$.each(jsonData, function(key, value){
		data_arr.push([key, value]);
	});

	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Product Name');
	data.addColumn('number', 'Total Sales');
	data.addRows(data_arr);

	//3. draw the chart
	var chart = new google.visualization.ColumnChart(document.getElementById("totalSales_barChart"));
	chart.draw(data, options);

	//to redraw the chart on window resize
	$(window).resize(function () {
		chart.draw(data, options);
	});
}
