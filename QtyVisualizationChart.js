$(document).ready(function() {
	getProductQtyData();
});

function getProductQtyData() 
{
	$.ajax({
		//servlet name from where the products list in json format is to be obtained
		url: "ChartVisualizationData",				
		dataType: "JSON",

		//GET = download data from some url/somewhere
		//POST = send data to server
		//type: "POST",
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
		title: 'Products Quantity Chart',
		bars:'vertical',
		fontSize: 12,
		hAxis: {
			title: 'Product Name',
		},
		vAxis: {
			title: 'Quantity',
		},
		legend: {
			position: 'top',
			alignment: 'end',
		},
	};

	//2. set the visualization data to be displayed
	var data_arr = [];
	$.each(jsonData, function(i, object){
		data_arr.push([object.name, parseInt(object.quantity)]);
	});

	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Product Name');
	data.addColumn('number', 'Quantity');
	data.addRows(data_arr);

	//3. draw the chart
	var chart = new google.visualization.ColumnChart(document.getElementById("productQty_barChart"));
	chart.draw(data, options);

	//to redraw the chart on window resize
	$(window).resize(function () {
		chart.draw(data, options);
	});
}





/* simple chart
google.charts.load('current', {packages: ['bar']});
google.charts.setOnLoadCallback(drawChart);

function drawChart() 
{
	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Product Name');
	data.addColumn('number', 'Quantity');
	data.addRows([
		['2000',2],
		['2001',4],
	]);
	var option = {
		'width': 600,
		'height': 650,
		chart: {
			title: 'Chart for Products Quantity',
		},
		bars:'vertical',
	};
	var chart = new google.charts.Bar(document.getElementById("productQty_barChart"));
	chart.draw(data, option);
}*/