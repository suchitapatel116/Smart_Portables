var req;
var isIE;
var completeField;
var completeTable;
var new_autoRow;

function init() {
	completeField = document.getElementById("searchId");
	completeTable = document.getElementById("autoComplete_table");
	new_autoRow = document.getElementById("auto_searchRow");
}

function doCompletion() {
	var url = "autocomplete?action=complete&searchId=" +escape(searchId.value);
	req = initRequest();
	req.open("GET", url, true);
	req.onreadystatechange = callback;
	req.send(null);
}

function initRequest() {
	if (window.XMLHttpRequest) {
		if (navigator.userAgent.indexOf('MSIE') != -1) {
			isIE = true;
		}
		return new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		return new ActiveXObject("Microsoft.XMLHTTP");
	}
}

function appendProduct(prodName, prodId) {
	var row, cell, a_linkElement;

	if(isIE) {
		completeTable.style.display = 'block';
		row = completeTable.insertRow(completeTable.rows.length);
		cell = row.insertCell(0);
	}
	else {
		completeTable.style.display = 'table';
		row = document.createElement("tr");
		cell = document.createElement("td");
		row.appendChild(cell);
		completeTable.appendChild(row);
	}

	cell.className = "popupCell";
	a_linkElement= document.createElement("a");
	a_linkElement.className = "popupItem";
	a_linkElement.setAttribute("href", "autocomplete?action=lookup&searchId=" + prodId);
	a_linkElement.appendChild(document.createTextNode(prodName));
	cell.appendChild(a_linkElement);
}

function parseMessages(responseXML) {
	//no match returned
	if(responseXML == null)
		return false;
	else {
		var products = responseXML.getElementsByTagName("products")[0];
		if(products.childNodes.length > 0){
			//if(completeTable != null)
			//{
			completeTable.setAttribute("bordercolor","black");
			completeTable.setAttribute("border","1");
			completeTable.setAttribute("background-color","white");
			//}
			for(loop = 0; loop<products.childNodes.length; loop++)
			{
				var prod = products.childNodes[loop];
				var productNa = prod.getElementsByTagName("productName")[0];
				var productId = prod.getElementsByTagName("id")[0];
				appendProduct(productNa.childNodes[0].nodeValue, productId.childNodes[0].nodeValue);
			}
		}
	}
}

function callback() {
	clearTable();
	if (req.readyState == 4) {
		if (req.status == 200)
			parseMessages(req.responseXML);
	}
}

function clearTable() {
//if(completeTable != null)
//{
	if (completeTable.getElementsByTagName("tr").length > 0) {
		completeTable.style.display= 'none';
		for (loop = completeTable.childNodes.length -1; loop >= 0 ; loop--)
		{
			completeTable.removeChild(completeTable.childNodes[loop]);
		}
	}
//}
}