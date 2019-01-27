google.charts.load('current', {packages: ['corechart', 'bar']});
//google.charts.setOnLoadCallback(drawChart);

//Binding on click event to make ajax call to onPost() in DV.java which will return a json obj having top 3 review for each city
$("#dataVizChartBtn").click(function(){
	$("#dataVizChartBtn").hide();
	$.ajax({
		//url = url to get/send data to, here DV.java
		url: "DataVisualization",
		//GET = download data from some url/somewhere
		//POST = send data to server
		type: "POST",
		data: "{}",
		success: function(str)
		{
			createDataTableFromJson(str);
		},
		error: function()
		{
		}
	});
});

//parse json to populate datatable which will be used by google API for plotting the bar chart
function createDataTableFromJson(jsonStr)
{
	//parsing the json string to be interpreted as json object
	var parsed_data = $.parseJSON(jsonStr);
	//or var parsed_data = JSON.parse(jsonStr);

	var data_arr = new Array();
	var prod_arr = new Array();
	var zip_arr = new Array();

	//Populate pname and zip array from json data
	for(var i=0; i<parsed_data.length; i++)
	{
		var pname = parsed_data[i]["pname"];
		var zip = parsed_data[i]["retZip"];
		if(!prod_arr.includes(pname))
			prod_arr.push(pname);
		if(!zip_arr.includes(zip))
			zip_arr.push(zip);
	}

	//Google API header generation
	var j = 0;
	var heading_arr = new Array(prod_arr.length + 1);
	heading_arr[0] = "Zip Code";
	for(var i=1; i<=prod_arr.length; i++){
		heading_arr[i] = prod_arr[j];
		j++;
	}

	data_arr[0] = heading_arr;

	//create an arr of arr from json data for plotting the chart
	var cnt = 1;
	for(var i=0; i<zip_arr.length; i++)
	{
		var arr = new Array(heading_arr.length);
		arr[0] = zip_arr[i];

		for(var j=0; j<prod_arr.length; j++)
		{
			for(var l=0; l<parsed_data.length; l++)
			{
				if(parsed_data[l]["retZip"] === zip_arr[i] && parsed_data[l]["pname"] === prod_arr[j])
					arr[j+1] = parseInt(parsed_data[l]["reviewRating"]);
			}
		}
		//Fill empty cell with zero
		for(var i=0; i<heading_arr.length; i++)
		{
			if(!(arr[i]>0))
				arr[i] = 0;
		}
		data_arr[cnt] = arr;
		cnt++;
	}
	drawChart(data_arr, prod_arr);
}

//Job of the function to add html in the div tag
function drawChart(data_arr, prod_arr)
{
	var pnames = "";
	for(var i=0; i<prod_arr.length; i++)
		pnames += prod_arr[i]+",";

	//Invoke google's built in method to get data table object required by google
	var chart_data = new google.visualization.arrayToDataTable(data_arr);
	var options = {
		'width': 600,
		'height': 650,
		chart: {
			title: 'Chart for Trending Products',
			subtitle: pnames,
		},
		bars: 'horizontal'
	};
	var chart = new google.visualization.BarChart(document.getElementById('chart_div'));
	chart.draw(chart_data, options);
}
