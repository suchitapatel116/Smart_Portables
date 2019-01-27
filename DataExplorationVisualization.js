(function() {
	var margin = { top: 0, left: 0, right: 0, bottom:0 },
		height = 400 - margin.top - margin.bottom,
		width = 720 - margin.left - margin.right;

		var svg = d3.select("#map")
					.append("svg")
					.attr("height", height + margin.top + margin.bottom)
					.attr("width", width + margin.left + margin.right)
					.append("g")
					.attr("transform", "translate(" + margin.left + "," + margin.top + ")");

		d3.queue()
			.defer(d3.json, "state.topo.json")	//states data for graph visualization fromjson from internet
			.defer(d3.json, "data_file.json")	//our data from mySQL
			.await(ready)

		var projection = d3.geoAlbersUsa()	//geoMercator()
							.translate([width/2, height/2])
							.scale(800)

		var path = d3.geoPath()
					.projection(projection)

		function ready(error, data, data_file) 
		{
			console.log(data)
			
			//get the states from the json file
			var states = topojson.feature(data, data.objects.state).features
			console.log(states)

			//draw the lines/paths
			console.log(data_file)
			
			//for the color bar on top
			color = d3.scaleQuantize()
					.domain([1, 10])
					.range(d3.schemeBlues[9])
			const x = d3.scaleLinear()
						.domain(d3.extent(color.domain()))
						.rangeRound([400, 700]);
			svg.selectAll("rect")
				.data(color.range().map(d => color.invertExtent(d)))
				.enter().append("rect")
				.attr("height", 8)
				.attr("x", d => x(d[0]))
				.attr("width", d => x(d[1]) - x(d[0]))
				.attr("fill", d => color(d[0]));
			

			//setup for coloring the states		
			var arr = ["#FAFEFF","#F7FBFF","#DEEBF7", "#C6DBEF","#9ECAE1","#6BAED6","#4292C6","#2171B5","#08519C","#08306B"];
			var max = 0;
			for(var i=0; i<data_file.length; i++){
				if(data_file[i].total_price > max)
					max = data_file[i].total_price;
			}
			var interval = max/9;

			//for drawing the states and coloring them		
			svg.selectAll(".state")
				.data(states)
				.enter().append("path")
				.attr("class", "state")
				.attr("d", path)
				.style("fill", function (d) 
				{
					var index = 0;
					var na2 = String(d.properties.NAME10).toLowerCase();
					//console.log(na2)
					for(var i=0; i<data_file.length; i++)
					{
						var na1 = String(data_file[i].state_name).toLowerCase();
						if(na1 == na2)
						{
							index = data_file[i].total_price/interval;
							break;
						}
					}
					return String(arr[index]);
				})
		}
})();