<%-- 
    Document   : newjsp
    Created on : Feb 8, 2017, 12:17:19 PM
    Author     : cjedwards1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" type="text/css" href="css/style.css">
	<link rel="stylesheet" type="text/css" href="bootstrap-3.3.7-dist/css/bootstrap.min.css">
	<!--<script type="text/javascript" src="bootstrap-3.3.7-dist/js/bootstrap.min.js">-->
	<title>Avian data retreival</title>
	<style>
		/*do nothing*/
	</style>
</head>
  <body>
    <script>
	//This top 2 variables are global which means iFrames can use them by calling parent.[variable], and this allows them to change
	//these 2 which we can then keep track of.
	
	//Variables for the climate division iFrame.
	var cdFrameResults = "";	//The results from clicking in the climate division iFrame.
	var startLoad = false;		//Boolean to tell the iFrame to start loading the massive GeoJSON file for the map.
	var loadDone = false;		//Boolean for the iFrame to set to true when the map is fully loaded.
	var loadComplete;			//A set interval object that will be started when the user clicks on the link, and cancelled when the map is done loading.
	
	//The results from clicking in the state iFrame.
	var stFrameResults = "";
	
	//Booleans to say whether or not the warning text inside of the date div should be shown or not.
	var yearBoolean = false;
	var monthBoolean = false;
	var dayBoolean = false;
	
	//When you click on a tab.
	function openTab(evt, tabName) 
	{
		// Declare all variables
		var i, tabcontent, tablinks;

		// Get all elements with class="tabcontent" and hide them
		tabcontent = document.getElementsByClassName("tabcontent");
		for (i = 0; i < tabcontent.length; i++) 
		{
			tabcontent[i].style.display = "none";
		}

		// Get all elements with class="tablinks" and remove the class "active"
		tablinks = document.getElementsByClassName("tablinks");
		for (i = 0; i < tablinks.length; i++) 
		{
			tablinks[i].className = tablinks[i].className.replace(" active", "");
		}

		// Show the current tab, and add an "active" class to the link that opened the tab
		document.getElementById(tabName).style.display = "block";
		evt.currentTarget.className += " active";

		//To make the about section look a little bit better by getting rid of unwanted objects.
		if(tabName == "aboutView" || tabName == "downloadView")
		{
			document.getElementById('tableResults').style = "display:none";
			document.getElementById('filler').style = "display:none";
			document.getElementById('buttons').style = "display:none";
		}
		else
		{
			document.getElementById('tableResults').style = "display:";
			document.getElementById('filler').style = "display:";
			document.getElementById('buttons').style = "display:";
		}
	}
	
	//When you click on a checkbox for options to be seen.
	function inputOptionChange(evt)
	{
		//Get the object clicked.
		var node = evt.target || evt.srcElement;
		
		if(node.checked)
		{
			//Check whether the year, month, or day boxes were clicked.
			if(node.value == "year")
			{
				yearBoolean = true;
			}
			
			if(node.value == "month")
			{
				monthBoolean = true;
			}
			
			if(node.value == "day")
			{
				dayBoolean = true;
			}
			
			//If any of them were clicked show the warning text.
			if(yearBoolean || monthBoolean || dayBoolean)
			{
				document.getElementById('warningText').style= "display:";
			}
			
			//Display the corresponding div to the check box by making the display option be its default.
			document.getElementById(node.value).style= "display:";
		}
		else
		{
			if(node.value == "year")
			{
				yearBoolean = false;
			}
			
			if(node.value == "month")
			{
				monthBoolean = false;
			}
			
			if(node.value == "day")
			{
				dayBoolean = false;
			}
			
			if(!yearBoolean && !monthBoolean && !dayBoolean)
			{
				document.getElementById('warningText').style= "display:none";
			}
			
			//If a checbox is unchecked then hide its div.
			document.getElementById(node.value).style= "display:none";			
		}
	}

	//If a radio button is pressed anywhere on the page.
	function inputRadioChange(evt)
	{
		var node = evt.target || evt.srcElement;
		
		//Get the parent that is holding all of the radio buttons.
		node = node.parentElement;
		
		//Go over every button to see which one is selected.
		for(var i = 0; i < node.childNodes.length; i++)
		{
			if(node.childNodes[i].type == "radio" && node.childNodes[i].checked)
			{
				//Make sure the radio button is valid.
				if(document.getElementById(node.childNodes[i].value))
				{
					//Set it to the default display and tab over 1.
					document.getElementById(node.childNodes[i].value).style="display:;padding-left: 4em";
				}
			}
			else if(node.childNodes[i].type == "radio")
			{
				if(document.getElementById(node.childNodes[i].value))
				{
					document.getElementById(node.childNodes[i].value).style="display:none";			
				}
			}
		}
	}

	//Get the correct data for whichever option on the page is currently being checked.
	function getOptionString(option)
	{
		//Will be returned
		var result = "=";
		switch(option)
		{
			//Climate division selection.
			case "cd":
				result += cdFrameResults;
				break;
				
			//Lat Long selection.
			case "ll":
				result += document.getElementById('latLongFinalInput').innerHTML;
				break;
				
			//State final selection
			case "st":
				result += stFrameResults;
				break;
				
			//Bird taxonomy 
			case "ty":
				result += document.getElementById('taxonomyInput').value;
				break;
				
			//bird names
			case "bn":
				var parent = document.getElementById('bn');
				for(var i = 0; i < parent.childNodes.length; i++)
				{
					var child = parent.childNodes[i];
					if(child.type == "radio")
					{
						if(child.checked)
						{
							result += child.value + "/";
						}
					}
				}
				
				var tempString = document.getElementById('birdNameInput').value;
				tempString = tempString.replace(/\s,/g, ",");
				tempString = tempString.replace(/,\s/g, ",");
				result += tempString.replace(/\s/g, "_");
				break;
				
			//pivot bird stored procedure 
			case "bs":
				var parent = document.getElementById('bs');
				for(var i = 0; i < parent.childNodes.length; i++)
				{
					var child = parent.childNodes[i];
					if(child.type == "radio")
					{
						if(child.checked)
						{
							result += child.value + "/";
						}
					}
				}
				result += document.getElementById('birdNameCalcInput').value;
				break;
			
			//long or short migration
			case "ls":
				var parent = document.getElementById('ls');
				for(var i = 0; i < parent.childNodes.length; i++)
				{
					var child = parent.childNodes[i];
					if(child.type == "radio")
					{
						if(child.checked)
						{
							result += child.value;
						}
					}
				}
				break;
				
			//feeding guild selection
			case "fg":
				result += document.getElementById('feedingGuild').value;
				break;
				
			//habitat in the east, west, or both
			case "ew":
				var parent = document.getElementById('ew');
				for(var i = 0; i < parent.childNodes.length; i++)
				{
					var child = parent.childNodes[i];
					if(child.type == "radio")
					{
						if(child.checked)
						{
							result += child.value;
						}
					}
				}
				break;
				
			//observer name selection
			case "on":
				result += document.getElementById('oName').value;
				break;
				
			//observer id selection
			case "od":
				result += document.getElementById('oid').value;
				break;
				
			case "ct":
				result += document.getElementById('observerComparisonSelection').selectedIndex + "/" + document.getElementById('ochN').value;
				break;
				
			//Check which observers have certain number of checklists submitted.
			case "cs":
				var parent = document.getElementById('cs');
				for(var i = 0; i < parent.childNodes.length; i++)
				{
					var child = parent.childNodes[i];
					if(child.type == "radio")
					{
						if(child.checked)
						{
							result += child.value + "/";
						}
					}
				}
				result += document.getElementById('checklistNumber').value;
				break;
				
			//Calculate the number of a certain variable.
			case "cl":
				var parent = document.getElementById('variableOptions');
				for(var i = 0; i < parent.childNodes.length; i++)
				{
					var child = parent.childNodes[i];
					
					if(child.type == "radio")
					{
						if(child.checked)
						{
							result += child.value;
						}
					}
				}
				break;
			default:
				result = "";
		}
		return result;
	}
	
	//When the user clicks on submit.
	function submit()
	{
		finishSubmit("");
	}

	//Will finish the submit process based off of what button calls it.
	function finishSubmit(application)
	{
		if(!application)
		{
			//Activate the loading icon to show the user something is happening.
			document.getElementById('load').className = "loader";
		}
		
		//Prepare a request.
		var xmlhttp = new XMLHttpRequest();
		
		//Set it to the submit_jobs servlet.
		var url = "/avianMigration/submit_job";
		
		//Prepare the parameters.
		var params = application;
		
		console.log(!params);
		
		//Figure out which tab is currently the selected one. Only one tab can be selected at a time.
		tablinks = document.getElementsByClassName("tablinks");
		var id;
		for (i = 0; i < tablinks.length; i++) 
		{
			if(tablinks[i].className == "tablinks active")
				id = tablinks[i].id;
		}
		
		if(id == "birdTab")
		{
			var options = document.getElementById('birdOptions');
			for(var i = 0; i < options.childNodes.length; i++)
			{
				if(options.childNodes[i].checked)
				{
					//If the parameters variable has data in it already, then you need to add a "&" because there is a variable there already.
					if(!params)
					{
						params = options.childNodes[i].value + getOptionString(options.childNodes[i].value);
					}
					else
					{
						params += "&" + options.childNodes[i].value + getOptionString(options.childNodes[i].value);
					}
				}
			}
		}
		else if(id== "mainTab")
		{
			var options = document.getElementById('options');

			for(var i = 0; i < options.childNodes.length; i++)
			{
				if(options.childNodes[i].checked)
				{
					//Check whether the date checkbox is selected. The date option overrides the year, month, and day options, so we do not 
					//want those showing up if date is checked.
					if(!document.getElementById('dateCheckbox').checked)
					{
						if(options.childNodes[i].value == "year")
						{
							if(!params)
							{
								params = "yr=" + document.getElementById('beginYearText').value + "/" + document.getElementById('endYearText').value;
							}
							else
							{
								params += "&yr=" + document.getElementById('beginYearText').value + "/" + document.getElementById('endYearText').value;
							}
						}
						else if(options.childNodes[i].value == "month")
						{
							if(!params)
							{
								params = "mh=" + document.getElementById('beginMonthText').value + "/" + document.getElementById('endMonthText').value;
							}
							else
							{
								params += "&mh=" + document.getElementById('beginMonthText').value + "/" + document.getElementById('endMonthText').value;
							}
						}
						else if(options.childNodes[i].value == "day")
						{
							if(!params)
							{
								params = "dy=" + document.getElementById('beginDayText').value + "/" + document.getElementById('endDayText').value;
							}
							else
							{
								params += "&dy=" + document.getElementById('beginDayText').value + "/" + document.getElementById('endDayText').value;
							}
						}
					}
					
					//This is a loop so we cannot put an else after the last if to add date because it will just show up later and
					//be added again. So just have an if prepared for when it does.
					if(options.childNodes[i].value == "date")
					{
						var beginDate = document.getElementById('beginDateText').value;
						
						var endDate = document.getElementById('endDateText').value;
						
						if(!params)
						{
							params = "dt=" + beginDate + "/" + endDate;
						}
						else
						{
							params += "&dt=" + beginDate + "/" + endDate;
						}
					}
					
					//If it is any other variable then go out and get the correct data for it and return.
					var sections = document.getElementById(options.childNodes[i].value);
					for(var x = 0; x < sections.childNodes.length; x++)
					{
                                            console.log(sections.childNodes[x].type);
						if(sections.childNodes[x].type == "radio")
						{
							if(sections.childNodes[x].checked)
							{
								if(!params)
								{
									params = sections.childNodes[x].value + getOptionString(sections.childNodes[x].value);
								}
								else
								{
									params += "&" + sections.childNodes[x].value + getOptionString(sections.childNodes[x].value);
								}
							}
						}
					}
				}
			}
			
			//Find which calculation option is selected.
			options = document.getElementById('calcOptions');

			for(var i = 0; i < options.childNodes.length; i++)
			{
				if(options.childNodes[i].checked)
				{
					if(!params)
					{
						params = options.childNodes[i].value + getOptionString(options.childNodes[i].value);
					}
					else
					{
						params += "&" + options.childNodes[i].value + getOptionString(options.childNodes[i].value);
					}
				}
			}
		}
		
		//For debugging what will be going over to the server.
		console.log(params);
		
		//Open the connection using the url and set it to a Post request.
		xmlhttp.open("Post", url, true);
		
		//Set that we will be sending url encoded strings to the server.
		xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		
		//Will be called when a state of the connection has changed.
		xmlhttp.onreadystatechange = function() 
		{
			//When a response has been returned and it set to 200 "ok".
			if (this.readyState == 4 && this.status == 200)
			{
				//myArr is a standard variable I started to use to show you are working with a returned json object. It is used all over every website as of right now.
				//It can be changed if wanted, but make sure to do ctrl + h to replace all
				var myArr = JSON.parse(this.responseText);
				if(myArr)
				{
					//Get the results table.
					var table = document.getElementById('results');
					table.innerHTML = "<caption>Sample of Query Results</caption>";
					
					//Insert the variables' names into the headers. Do so in order using the "order" object returned with the json object.
					var header = table.insertRow(0);
					var i = 0;
					for(var key in myArr["order"])
					{
						header.insertCell(i).outerHTML = "<th>" + myArr["order"][key] + "</th>";
						i++;
					}
					
					for(var num = 1; num <= myArr[myArr["order"][0]].length; num++)
					{
						table.insertRow(table.rows.length);
					}
					
					var cellNum = 0;
					for(var key in myArr["order"])
					{	
						var value = myArr[myArr["order"][key]];
						i = 1;
						for(var x in value)
						{
							var row = table.rows[i];
							
							if(value[x])
								row.insertCell(cellNum).innerHTML = value[x];
							else
								row.insertCell(cellNum).innerHTML = "NULL";
							i++;
						}
						cellNum++;
					}
				}
   			} 
			//If there was something recieved, and that it was a 500 "internal server error" which mainly means a try catch failure, or faiure we set up for.
   			else if(this.readyState == this.HEADERS_RECEIVED && this.status == 500)
   			{
        		alert(xmlhttp.getResponseHeader("error"));
    		}
			
			//Turn the loader object off since the request is finished. Whether it ended with a failure or success.
			document.getElementById('load').className = "temp";
		};

		//Send the request over the connection. For "Post" requests, the parameters are put inside the .send() call. 
		//For a "Get" request they are added to the url at the start when the connection is created. --> url + params.
		xmlhttp.send(params);
	}
	
	//Used when the user requests the excel file that was made during the last request.
	function exportFile()
	{
	
		//OLD FUNCTION OF EXPORT BUTTON, SAVE FOR LATER TIME IF NEEDED.
		//Set url
		//var url = "/avianMigration/submit_job";
		
		//Set the only parameter to tell the server we are looking for the excel file.
		//var params = "?application=true";
		
		//Create an 'a' element. This is used to hold the reference to the url.
		//var dl = document.createElement('a');
		
		//Set it to download a document when clicked. This can be arbitrary.
		//dl.download = "results.docx";
		
		//Set the url reference.
		//dl.href = url + params;
		
		//For firefox, the element must be added to the body before clicking for the clicking function to work, the remove it.
		//document.body.appendChild(dl);
		
		//Artificially click the 'a' element to activate it to send the request to download the file.
		//dl.click();
		
		//document.body.removeChild(dl);
		
		finishSubmit("application=true");
	}
	
	//When the user wants to add a Lat Long square area to the list.
	function addLatLongRow()
	{
		//Create and insert a new row into the lat long table.
		var row = document.getElementById('latLongInputTable').insertRow(document.getElementById('latLongInputTable').rows.length);
		
		//Insert the data into the new row.
		row.insertCell(0).innerHTML = document.getElementById('latStart').value;
		row.insertCell(1).innerHTML = document.getElementById('latStop').value;
		row.insertCell(2).innerHTML = document.getElementById('longStart').value;
		row.insertCell(3).innerHTML = document.getElementById('longStop').value;
		row.insertCell(4).innerHTML = "<input id='edit1' type='submit' name='delete' value='Delete'>";
		
		//Access the div holder that is holding the final string for the lat long option.
		var tempHolder = document.getElementById('latLongFinalInput').innerHTML;
		
		//If there is more than one option in the holder already.
		if(tempHolder.includes("/"))
		{
			tempHolder += "," + document.getElementById('latStart').value + "/" + document.getElementById('latStop').value + "/" + document.getElementById('longStart').value + "/" + document.getElementById('longStop').value;
		}
		else
		{
			tempHolder = document.getElementById('latStart').value + "/" + document.getElementById('latStop').value + "/" + document.getElementById('longStart').value + "/" + document.getElementById('longStop').value;
		}
		
		//Put back the holder's new inner html.
		document.getElementById('latLongFinalInput').innerHTML = tempHolder;
		
		//Reset the text boxes.
		document.getElementById('latStart').value = "";
		document.getElementById('latStop').value = "";
		document.getElementById('longStart').value = "";
		document.getElementById('longStop').value = "";
	}
	
	//To delete a Lat Long option.
	function deleteLatLongRow(evt)
	{
		var node = evt.target || evt.srcElement;
		var cells = node.parentElement.parentElement.cells;
		var checkString = cells[0].innerHTML + "/" + cells[1].innerHTML + "/" + cells[2].innerHTML + "/" + cells[3].innerHTML;
		
		var tempHolder = document.getElementById('latLongFinalInput').innerHTML;
		if(tempHolder.includes("," + checkString))
		{
			tempHolder = tempHolder.replace("," + checkString, "");
		}
		else
		{
			tempHolder = tempHolder.replace(checkString, "");
			if(tempHolder.startsWith(","))
			{
				tempHolder = tempHolder.substring(1, tempHolder.length);
			}
		}
		document.getElementById('latLongFinalInput').innerHTML = tempHolder;
		
		document.getElementById(node.parentElement.parentElement.parentElement.parentElement.id).deleteRow(node.parentElement.parentElement.rowIndex);
	}
	
	//Called when the user clicks on the link to load the climate division map.
	function openCdMap()
	{
		document.getElementById('cdIframeLink').style = "display:none;";
		document.getElementById('cdIframe').style = "border:none;display:;";
		
		//Set the start load boolean to true to tell the climate division iFrame to load the map.
		startLoad = true;
		
		//Start the loading GIF.
		document.getElementById('load').className = "loader";
		
		//Check every 100 seconds to see if the map is done loading.
		loadComplete = window.setInterval(checkLoadDone, 100);
	}
	
	function checkLoadDone()
	{
		if(loadDone)
		{
			window.clearInterval(loadComplete);
			document.getElementById('load').className = "temp";
		}
	}
    </script>
	<div class="container">
		<!--The div that holds the loading element.-->
		<div id="load" class="temp "></div>
		
		<!--The invisible list that keeps track of the tabs.-->
		<div class="row">
			<ul class="tab col-xs-12" id="navbar">
				<li class="col-xs-12 col-sm-3"><a class="col-xs-12 tablinks" href="javascript:void(0)" onclick="openTab(event, 'mainView')" id="mainTab">Main</a></li>
				<li class="col-xs-12 col-sm-3"><a class="col-xs-12 tablinks" href="javascript:void(0)" onclick="openTab(event, 'birdView')" id="birdTab">Birds</a></li>
				<li class="col-xs-12 col-sm-3"><a class="col-xs-12 tablinks" href="javascript:void(0)" onclick="openTab(event, 'downloadView')" id="downloadTab">Downloads</a></li>
				<li class="col-xs-12 col-sm-3"><a class="col-xs-12 tablinks" href="javascript:void(0)" onclick="openTab(event, 'aboutView')" id="aboutTab">About</a></li>
				<!--<li><a href="javascript:void(0)" class="tablinks" onclick="openTab(event, 'storedProcedure')" id="storeTab">Calculations</a></li>-->
				<!--<li><a href="javascript:void(0)" class="tablinks" onclick="openTab(event, 'mapView')" id="mapTab">Build map</a></li>-->
			</ul>
		</div>
		<div class="row">
		<div id="mainContent" class="col-xs-12">
			<!--Main tab-->
			<div id="mainView" class="col-xs-12 col-md-4 tabcontent"><!--what do we use this ID for? Is JS referencing it at all?"
													CORY: Yes, you can link the id back to the onclick event for the <a> of its tab when it calls openTab in the javascript section.-->
			
				<!--Put the 2 main divs of this tab into 1 row of a table so as to make sure that they stay side by side.-->
				<table>
					<tr>
						<td style="border: 1px solid #ccc;vertical-align:top;white-space:nowrap;">
							<!--The filters side of the main tab.-->
							<div id="filterSide">
								<h1>Filters</h1>
								<div id="options" name="classify" onChange="inputOptionChange(event)">
									<input type="checkbox" value="location">location
									<input type="checkbox" value="year">year
									<input type="checkbox" value="month">month
									<input type="checkbox" value="day">day
									<input id="dateCheckbox" type="checkbox" value="date">date select
									<input type="checkbox" value="ampm">AM / PM
									<input type="checkbox" value="observer">observer
								</div>
								
								<!--This divs are laced everywhere as a section break. They are not used by anything else and they will always have the id of "filler".-->
								<div id="filler"><br><br></div>
								
								<div id="location" style="display:none" onChange="inputRadioChange(event)">
									<b>Pick a location type to search for.</b><br>
									<input type="radio" name="location" value="cd">Climate Division<br>
									<div id="cd" style="display:none">
										<a href="#" id="cdIframeLink" onclick="openCdMap()">Load climate divison map. This may take a few minutes...</a>
										<iframe id="cdIframe" src="climDiv.html" height="320" width="520" style="border:none;display:none"></iframe>                                                                                                                                                                                            
									</div>
									<input type="radio" name="location" value="ll">Latitude, Longitude<br>
									<div id="ll" style="display:none">
										<b>Plug in values for the latitude and longitude then press Add.</b><br>
										Latitude start:  <input type="text" id="latStart" style="margin-bottom:1em"> 
										<div id="tab" style="padding-left:2em;display:inline-block"></div> 
										Latitude stop:   <input type="text" id="latStop"><br>
										Longitude start: <input type="text" id="longStart">
										<div id="tab" style="padding-left:2em;display:inline-block"></div>
										Longitude stop:  <input type="text" id="longStop">
										<div id="tab" style="padding-left:2em;display:inline-block"></div>
										<button type="button" onclick="addLatLongRow()">Add</button><br><br>
										<table id="latLongInputTable" class="showData" style="width:30%" onclick="deleteLatLongRow(event)">
											<caption>Lat Long inputs</caption>
											<tr>
												<th>Latitude start</th>
												<th>Latitude stop</th>
												<th>Longitude start</th>
												<th>Longitude stop</th>
											</tr>
										</table>
										<div id="latLongFinalInput" style="display:none"></div>
									</div>
									<input type="radio" name="location" value="st">States<br>
									<div id="st" style="display:none">
										<iframe id="stIframe" src="states.html" height="320" width="520" style="border:none;"></iframe>
									</div><br>
								</div>
								
								<div id="year" style="display:none">
									<b>Enter a year range, or to search for 1 year, have both fields equal.</b><br>
									<div id="beginYear" style="display:inline-block">
										Beginning year:<br>
										<input type="text" id="beginYearText">	
									</div>
									<div id="endYear" style="display:inline-block">
										Ending year:<br>
										<input type="text" id="endYearText">
									</div><br><br>
								</div>
								
								<div id="month" style="display:none">
									<b>Enter a month range, or to search for 1 month, have both fields equal.</b><br>
									<div id="beginMonth" style="display:inline-block">
										Beginning month:<br>
										<input type="text" id="beginMonthText">	
									</div>
									<div id="endMonth" style="display:inline-block">
										Ending month:<br>
										<input type="text" id="endMonthText">
									</div><br><br>
								</div>
								
								<div id="day" style="display:none">
									<b>Enter a day range, or to search for 1 day, have both fields equal.</b><br>
									<!--<input type="radio" name="day" value="month" checked>Day of month  EX: 15, 23, 1<br>
									<input type="radio" name="day" value="week">Day of week EX: M, T, W, H, F, Sa, Su<br><br>-->
									<div id="beginDay" style="display:inline-block">
										Beginning day:<br>
										<input type="text" id="beginDayText">	
									</div>
									<div id="endDay" style="display:inline-block">
										Ending day:<br>
										<input type="text" id="endDayText">
									</div><br><br>
								</div>
								
								<div id="date" style="display:none">
									<b>Select start and end date.</b><br>
									<font id="warningText" color="red" style="display:none"><b>The date select option overrides year, month, and day options.</b><br></font>
									<div id="beginDate" style="display:inline-block">
										Beginning date:<br>
										<input type="date" id="beginDateText">	
									</div>
									<div id="endDate" style="display:inline-block">
										Ending date:<br>
										<input type="date" id="endDateText">
									</div><br><br>
								</div>
								
								<div id="ampm" style="display:none">
									<b>Pick a time of day type to search for.</b><br>
									<input type="radio" name="ampm" value="ap=AM" checked>AM<br>
									<input type="radio" name="ampm" value="ap=PM">PM<br>
								</div><br>
								
								<div id="observer" style="display:none" onChange="inputRadioChange(event)">
									<b>Pick an observer variable to search for.</b><br>
									<input type="radio" name="observer" value="on">Name<br>
										<div id="on" style="display:none">
											<b>Enter an observer's name. Seperate with ",".</b><br> EX: Firstname Lastname,Firstname2 Lastname2<br>
											<input type="text" list="oNameList" id="oName">
										</div>
									<input type="radio" name="observer" value="od">Observer id<br>
										<div id="od" style="display:none">
											<b>Enter an id. Seperate numbers with ",".</b><br> EX: 967,1015<br>
											<input type="text" id="oid">
										</div>
									<input type="radio" name="observer" value="ct">Observers with # of submitted checklists<br>
										<div id="ct" style="display:none">
											Observers with number of submitted checklists
											<select id="observerComparisonSelection">
												<option>>=</option>
												<option>></option>
												<option><=</option>
												<option><</option>
												<option>=</option>
											</select>
											<input type="text" id="ochN">
										</div>
								</div>
							</div>
						</td>
						<td style="border: 1px solid #ccc;vertical-align:top;white-space:nowrap;">
							<!--The calculation side of the main tab.-->
							<div id="calcSide">
								<h1>Calculations</h1>
								<b>NOTE:</b>The filters will be applied to the calculation.<br><br>
								<div id="calcOptions" name="classify" onChange="inputRadioChange(event)">
									<input type="radio" name="calc" value="nn" checked>None
									<input type="radio" name="calc" value="cs">Observers with # of checklists
									<input type="radio" name="calc" value="bs">Number of certain birds seen per checklist
									<input type="radio" name="calc" value="cl">Calculate number of variable
								</div>
								
								<div id="filler"><br><br></div>
								
								<div id="nn">No calculations will be done, and the raw data from the filters will be returned</div>
								
								<div id="cs" style="display:none">
									<b>Chose whether or not to add the number of submitted group checklists to the total or not. 
										If include is selected, it will only return people who have both group and none group checklists.</b><br>
									<input type="radio" name="groupCheck" value="yes" checked>Include group checklists<br>
									<input type="radio" name="groupCheck" value="no">Don't include group checklists<br><br>
									<b>Chose an operator then enter the number of checklists you want to search for. Seperate with ",".</b><br> EX: 55,21,15<br>
									<input type="radio" name="operator" value="ge" checked>>=<br>
									<input type="radio" name="operator" value="g">><br>
									<input type="radio" name="operator" value="le"><=<br>
									<input type="radio" name="operator" value="l"><<br>
									<input type="radio" name="operator" value="e">=<br>
									# of checklists: <input type="text" id="checklistNumber">
								</div>
								
								<div id="bs" style="display:none">
									<b>Chose a type of name then enter the correct name(s) for the choise. Seperate with ",".</b><br> EX: Highland_Tinamou,White-throated_Tinamou {for science name}<br>
									<input type="radio" name="birdNameCalc" value="s" checked>Scientific name<br>
									<input type="radio" name="birdNameCalc" value="p">Common name<br>
									<input type="radio" name="birdNameCalc" value="t">Taxonomy<br>
									NAME: <input type="text" id="birdNameCalcInput"><br><br>
								</div>
								
								<div id="cl" style="display:none">
									<b>Chose a variable that you would like the count of.</b><br>
									<div id="variableOptions" style="display:inline-block;padding-right:2em;height:120px;border:1px solid #ccc;overflow:auto;" 
										onChange="var radios = document.getElementsByName('variables'); var selected; for(var i = 0; i < radios.length; i++) {if (radios[i].checked){selected = radios[i].value; break;}
													}document.getElementById('variableOptionsSelectedOption').innerHTML = &quot <b>Selected variable:</b> &quot + selected">
									</div>
									<div id="variableOptionsSelectedOption" style="display:inline-block">
										<b>Selected variable:</b>
									</div>
								</div>
							</div>
						</td>
					</tr>
				</table>
			</div>   
			
				<!--The bird tab.-->
				<div id="birdView" class="tabcontent">
					<h1>Pick which options to filter birds by.</h1>
					<div id="birdOptions" name="classify" onChange="inputOptionChange(event)">
						<input type="checkbox" value="ty">Taxonomy
						<input type="checkbox" value="bn">Name
						<input type="checkbox" value="ls">Migration type
						<input type="checkbox" value="fg">Feeding guild
						<input type="checkbox" value="ew">Habitat region
					</div>
					
					<div id="filler"><br><br></div>
					
					<div id="ty" style="display:none">
						<b>Enter a birds taxonomy number. Seperate numbers with ",".</b><br> EX: 84,102,40<br>
						<input type="text" id="taxonomyInput"><br><br>
					</div>
					
					<div id="bn" style="display:none">
						<b>Chose a type of name then enter the correct name for the choise. Seperate with ",".</b><br> EX: Highland_Tinamou,White-throated_Tinamou {for science name}<br>
						<input type="radio" name="birdNameDecision" value="s" checked>Scientific name<br>
						<input type="radio" name="birdNameDecision" value="p">Common name<br>
						<input type="radio" name="birdNameDecision" value="f">Family name<br>
						<input type="radio" name="birdNameDecision" value="o">Order name<br>
						NAME: <input type="text" id="birdNameInput"><br><br>
					</div>
					
					<div id="ls" style="display:none">
						<b>Chose a type of bird migration.</b><br>
						<input type="radio" name="migration" value="l" checked>Long migration<br>
						<input type="radio" name="migration" value="s">Short migration<br><br>
					</div>
					
					<div id="fg" style="display:none">
						<b>Enter a birds feeding guild. Seperate numbers with ",".</b><br> EX: ?<br>
						<input type="text" id="feedingGuild"><br><br>
					</div>
					
					<div id="ew" style="display:none">
						<b>Chose side of the US for the birds to be stationed at.</b><br>
						<input type="radio" name="home" value="e" checked>East<br>
						<input type="radio" name="home" value="w">West<br>
						<input type="radio" name="home" value="b">Both<br><br>
					</div>
				</div>
				
				<!--This will have all of the user specific files that a user wants to download.-->
				<div id="downloadView" class="tabcontent">
					Will have files that the user has requested to download.
				</div>
				
				<!--This will be the section that will have all of the references and who did what.-->
				<div id="aboutView" class="tabcontent">
					References to where the data came from, how we got it, and who gave what data will be here.<br>
					Along with that, whoever works on the site and/or data or backend will also be mentioned here and what all they did as well.
				</div>
				
				<!--This was going to be the tab that holds a timeline style map of retreived data, but during its production, it got cancelled. So, for now the start of its 
					code will sit here and not be used. Maybe later it will be added, but for now it won't.-->
				<div id="mapView" class="tabcontent" style="display:none">
					<h1>Map will be shown here.</h1>
					<b>NOTE:</b>The filters that are on the "Main" tab will be used to make the map.<br><br>
					<div id="mapVariableOptionsCheck" style="display:inline-block;padding-right:2em;height:120px;border:1px solid #ccc;overflow:auto;">
					</div>
					<div id="tabbed" style="padding-left:2em;display:inline-block"></div>
					<div id="mapVariableOptionsRadio" style="display:inline-block;padding-right:2em;height:120px;border:1px solid #ccc;overflow:auto;"
						onChange="var radios = document.getElementsByName('mapRadioVariables'); var selected; for(var i = 0; i < radios.length; i++) {if (radios[i].checked){selected = radios[i].value; break;}
										}document.getElementById('mapVariableOptionsSelectedOption').innerHTML = &quot <b>Selected variable:</b> &quot + selected">
					</div>
					<div id="mapVariableOptionsSelectedOption" style="display:inline-block">
						<b>Selected variable:</b>
					</div>
				</div>
		</div>
		</div>
		<!--Div to hold the submit and export buttons.-->
		<div class="row">
		<div id="buttons" class="col-xs-12">
			<div id="submitButtonWrapper" class="buttonWrapper"><button class="button" type="button" onclick="submit()">Submit request</button></div>
			<!--<div id="tab" style="padding-left:8em;display:inline-block"></div>-->
			<div id="exportButtonWrapper" class="buttonWrapper"><button class="button" type="button" onclick="exportFile()">Export requested data</button></div>
		</div>
		</div>
		
		<!--Table that will show resulting data.-->
		<div class="row">
		<div id="tableResults" class="col-xs-12">
			<table id="results" class="showData" style="width:30%">
				<caption>Sample of Query Results</caption>
			</table>
		</div>
		</div>
	</div>
  </body>

  <script>
	//called upon website being opened.
	
	//Set the main tab as selected.
	document.getElementById('mainTab').click();
	
	
	//Do a request to get all of the possible variables that can be used.
	var xmlhttp = new XMLHttpRequest();
	var url = "/avianMigration/submit_job";
	var params = "?vars=true";
	xmlhttp.open("Get", url + params, true);
	xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xmlhttp.onreadystatechange = function() 
	{
		if (this.readyState == 4 && this.status == 200)
		{
                    console.log(this.responseText);
			var myArr = JSON.parse(this.responseText);
			if(myArr)
			{
				for(var i in myArr["names"])
				{
					//Names will be put in a select list for the calculating variable section.
					document.getElementById('variableOptions').innerHTML += "<input type=\"radio\" name=\"variables\" value=\"" + myArr["names"][i] + "\">" + myArr["names"][i] + "<br>";
					
					//This part would have been used for the map tab.
					if(myArr["names"][i].toLowerCase().includes("precipitation") || myArr["names"][i].toLowerCase().includes("average") || myArr["names"][i].toLowerCase().includes("maximum") || myArr["names"][i].toLowerCase().includes("minimum") || myArr["names"][i].toLowerCase().includes("palmer"))
					{
						document.getElementById('mapVariableOptionsRadio').innerHTML += "<input type=\"radio\" name=\"mapRadioVariables\" value=\"" + myArr["names"][i] + "\">" + myArr["names"][i] + "<br>";
					}
					else
					{
						document.getElementById('mapVariableOptionsCheck').innerHTML += "<input type=\"checkbox\" name=\"mapCheckVariables\" value=\"" + myArr["names"][i] + "\">" + myArr["names"][i] + "<br>";
					}
				}
			}
		}
	}
	xmlhttp.send(params);
  </script>
</html>
