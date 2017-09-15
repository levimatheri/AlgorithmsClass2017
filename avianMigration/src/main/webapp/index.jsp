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
        <title>Avian data retrieval</title>   
        <link rel="stylesheet" type="text/css" href="/avianMigration/css/jquery-ui 1.12.1.css">
        <link rel="stylesheet" type="text/css" href="/Core/bootstrap-3.3.7-dist/css/bootstrap.min.css">	    
        <link rel="stylesheet" type="text/css" href="/avianMigration/css/style.css"> 
        <link rel="stylesheet" type="text/css" href="/Core/css/options.css">
        <script type="text/javascript" src="/avianMigration/script/iframe.js"></script>
        <script type="text/javascript" src="/avianMigration/script/jquery-3.1.1.min.js"></script>   
        <script type="text/javascript" src="/avianMigration/script/jquery-ui 1.12.1.js"></script>
        <script type="text/javascript" src="/Core/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>       
        <script type="text/javascript" src="/avianMigration/script/submit.js"></script>
        <script type="text/javascript" src="/avianMigration/script/change option.js"></script>
        <script type="text/javascript" src="/avianMigration/script/extra.js"></script> 
        <script type="text/javascript" src="/Core/script/options.js"></script>
</head>
  <body onload="start()">
	<div class="container">
		<!--The div that holds the loading element.-->
		<div id="load" class="temp "></div>
		
		<!--The invisible list that keeps track of the tabs.-->
		<div class="row" id="my_tabs">
			<ul class="tab col-xs-12" id="navbar">
				<li class="col-xs-12 col-sm-3"><a class="col-xs-12 tablinks" href="javascript:void(0)" onclick="openTab(event, 'mainView')" id="mainTab">Observations</a></li>
				<!--<li class="col-xs-12 col-sm-3"><a class="col-xs-12 tablinks" href="javascript:void(0)" onclick="openTab(event, 'birdView')" id="birdTab">Birds</a></li>-->
				<li class="col-xs-12 col-sm-3"><a class="col-xs-12 tablinks" href="javascript:void(0)" onclick="openTab(event, 'downloadView')" id="downloadTab">Downloads</a></li>
				<li class="col-xs-12 col-sm-2"><a class="col-xs-12 tablinks" href="javascript:void(0)" onclick="openTab(event, 'creditView')" id="creditTab">Credits</a></li>
				<li class="col-xs-12 col-sm-2"><a class="col-xs-12 tablinks" href="javascript:void(0)" onclick="openTab(event, 'aboutView')" id="aboutTab">About</a></li>
                                <li class="dropdown" id="dropOpt">
                                    
                                </li>
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
                                    <div id="obs_options" onChange="onHistRadioClick()">
                                        <input id="gen_rd" type="radio" name="obs_option"  checked > General
                                        <input id="hist_rd" type="radio" name="obs_option"> Historical
                                    </div>
                                    <br><br>
                                                                
                                    <div id="options" onChange="inputOptionChange(event)">
                                        <p style="display: none" id="mainChbxCheck"><font color="red"><b>Check one or more options!</b></font></p>
                                        <div style="display:inline"><input id="loc_chbx" type="checkbox" value="location"> location</div>
                                        <div style="display:inline"><input type="checkbox" value="year"> year</div>
                                        <div style="display:inline"><input type="checkbox" value="month"> month</div>
                                        <div id="firstArrive" style="display:none"><input id="checkFirstArrival" type="checkbox" value="firstArrival"> first arrival date</div>                                                           									
                                        <div id="dateSelect" style="display:inline"><input id="checkDate" type="checkbox" value="date"> date</div>
                                        <div id="day_chbx" style="display:inline"><input id="checkDay" type="checkbox" value="day"> day</div>                                                                     
                                        <div id="ap_chbx" style="display:inline"><input id="checkAmpm" type="checkbox" value="ampm"> AM / PM</div>
                                        <div id="obs_chbx" style="display:inline"><input id="checkObserver" type="checkbox" value="observer"> observer</div><br>
                                        
                                    </div>
								
							        <br><br>
								
							        <div id="location" onChange="inputRadioChange(event)">
                                                                        <p style="display: none" id="locationCheck"><font color="red"><b>Please select an option!</b></font></p>
								        <b>Pick a location type to search for.</b><br>
								        <input type="radio" name="location" value="cd"> Climate Division<br>
								        <!--<div id="cd" style="display:none">
									        <a href="#" id="cdIframeLink" onclick="openCdMap()">Load climate divison map. This may take a few minutes...</a>
									        <iframe id="cdIframe" src="climDiv.html" height="320" width="520" style="border:none;display:none"></iframe>                                                                                                                                                                                            
								        </div>-->
                                                                          <div id="cd" style="display:none">
									        <b>Plug in values for the state and climate division then press Add.</b><br>
                                                                                State:  <div class="ui-front" style="display: inline; margin-bottom: 10px;"><input type="text" id="state_cd" style="margin-bottom:1em" onchange="getClimateDiv()"></div>                                                                                                                                                
									        <div class="tabbed"></div> 
                                                                                Climate Division:   <div class="ui-front" style="display: inline"><input type="text" class="ui-autocomplete-input" id="cdiv"><br></div>									        
									        <div class="tabbed"></div>
									        <button type="button" onclick="addStateClimateDivRow()">Add</button>
									        <table id="stateClimateDivInputTable" class="showData" style="width:30%" onclick="deleteStateClimateDivRow(event)">
										        <caption>State Climate_Div inputs table</caption>
										        <tr id="stClimateHeader">
											        <th>State</th>
											        <th>Climate Division</th>						        
										        </tr>
									        </table>
									        <div id="stateClimateDivFinalInput" style="display:none"></div>
								        </div>
                                                                        
								        <input type="radio" name="location" value="ll"> Latitude, Longitude<br>
								        <div id="ll" style="display:none">
									        <b>Plug in values for the latitude and longitude then press Add.</b><br>
									        Latitude start:  <input type="text" id="latStart" style="margin-bottom:1em"> 
									        <div class="tabbed"></div> 
									        Latitude stop:   <input type="text" id="latStop"><br>
									        Longitude start: <input type="text" id="longStart">
									        <div class="tabbed"></div>
									        Longitude stop:  <input type="text" id="longStop">
									        <div class="tabbed"></div>
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
								        <input type="radio" name="location" value="st"> States<br>
								        <div id="st" style="display:none">
									        <iframe id="stIframe" src="states.html" height="320" width="520" style="border:none;"></iframe>
								        </div><br>
							        </div>
								
							        <div id="year" style="display:none">
								        <b>Enter a year range, or to search for 1 year, have both fields equal.</b><br>
								        <div id="beginYear" style="display:inline-block">
									        Beginning year:<br>
                                                                                <select id="beginYearText" class="year" onchange="selectOption(event, this)">
                                                                                </select> 	
								        </div>&nbsp;&nbsp;&nbsp;&nbsp;
								        <div id="endYear" style="display:inline-block">
									        Ending year:<br>
									        <select class="year" id="endYearText">
                                                                                </select>     
								        </div><br><br>
							        </div>
								
							        <div id="month" style="display:none">
								        <b>Enter a month range, or to search for 1 month, have both fields equal.</b><br>
								        <div id="beginMonth" style="display:inline-block">
									        Beginning month:<br>
                                                                                <select id="beginMonthText" class="month" onchange="selectOption(event, this)">
                                                                                    <option value="1">1</option>
                                                                                    <option value="2">2</option>
                                                                                    <option value="3">3</option>
                                                                                    <option value="4">4</option>
                                                                                    <option value="5">5</option>
                                                                                    <option value="6">6</option>
                                                                                    <option value="7">7</option>
                                                                                    <option value="8">8</option>
                                                                                    <option value="9">9</option>
                                                                                    <option value="10">10</option>
                                                                                    <option value="11">11</option>
                                                                                    <option value="12">12</option>
                                                                                </select>
								        </div>&nbsp;&nbsp;&nbsp;&nbsp;
								        <div id="endMonth" style="display:inline-block">
									        Ending month:<br>
									        <select id="endMonthText" class="month">
                                                                                    <option value="1">1</option>
                                                                                    <option value="2">2</option>
                                                                                    <option value="3">3</option>
                                                                                    <option value="4">4</option>
                                                                                    <option value="5">5</option>
                                                                                    <option value="6">6</option>
                                                                                    <option value="7">7</option>
                                                                                    <option value="8">8</option>
                                                                                    <option value="9">9</option>
                                                                                    <option value="10">10</option>
                                                                                    <option value="11">11</option>
                                                                                    <option value="12">12</option>
                                                                                </select>
								        </div><br><br>
							        </div>
								
							        <div id="day" style="display:none">
								        <b>Enter a day range, or to search for 1 day, have both fields equal.</b><br>
								        <!--<input type="radio" name="day" value="month" checked>Day of month  EX: 15, 23, 1<br>
								        <input type="radio" name="day" value="week">Day of week EX: M, T, W, H, F, Sa, Su<br><br>-->
								        <div id="beginDay" style="display:inline-block">
									        Beginning day:<br>
									        <select id="beginDayText" class="dayOpt" onchange="selectOption(event, this)">
                                                                                    <option value="2">M</option>
                                                                                    <option value="3">T</option>
                                                                                    <option value="4">W</option>
                                                                                    <option value="5">H</option>
                                                                                    <option value="6">F</option>
                                                                                    <option value="7">Sa</option>
                                                                                    <option value="1">Su</option>                                                                                    
                                                                                </select>	
								        </div>&nbsp;&nbsp;&nbsp;&nbsp;
								        <div id="endDay" style="display:inline-block">
									        Ending day:<br>
									        <select id="endDayText" class="dayOpt">
                                                                                    <option value="2">M</option>
                                                                                    <option value="3">T</option>
                                                                                    <option value="4">W</option>
                                                                                    <option value="5">H</option>
                                                                                    <option value="6">F</option>
                                                                                    <option value="7">Sa</option>
                                                                                    <option value="1">Su</option>                                       
                                                                                </select>	
								        </div><br><br>
							        </div>
                                                                
                                                                <div id="firstArrival" style="display:none">   
                                                                    <b>Select start and end date (YYYY-MM-DD)</b><br>
                                                                    <span id="firstWarningText" style="display:none; color: red"><b>The date select option overrides year and month options.</b><br></span>
                                                                    <div id="beginDate" style="display:inline-block">
                                                                            Beginning date:<br>
                                                                            <input type="date" id="beginDateText_fArrival">	
                                                                    </div>
                                                                    <div id="endDate" style="display:inline-block">
                                                                            Ending date:<br>
                                                                            <input type="date" id="endDateText_fArrival">
                                                                    </div><br><br>
							        </div>
								
							        <div id="date" style="display:none">
                                                                    <p style="display: none" id="dateCheck"><font color="red"><b>Please check for missing inputs!</b></font></p><br>
                                                                    <b>Select start and end date</b><br>
                                                                    <span id="warningText" style="display:none; color: red"><b>The date select option overrides year, month, and day options.</b><br></span>
                                                                    <div id="beginDate" style="display:inline-block">
                                                                            Beginning date:<br>
                                                                            <input id="beginDateText">	
                                                                    </div>&nbsp;&nbsp;
                                                                    <div id="endDate" style="display:inline-block">
                                                                            Ending date:<br>
                                                                            <input  id="endDateText">
                                                                    </div><br><br>
							        </div>
								
							        <div id="ampm" style="display:none">
                                                                    <b>Pick a time of day type to search for.</b><br>
                                                                    <input type="radio" name="ampm" value="ap=AM" checked> AM<br>
                                                                    <input type="radio" name="ampm" value="ap=PM"> PM<br>
							        </div><br>
								
							        <div id="observer" style="display:none" onChange="inputRadioChange(event)">
                                                                    <p style="display: none" id="observerCheck"><font color="red"><b>Please select an option!</b></font></p><br>
                                                                    <b>Pick an observer variable to search for.</b><br>
                                                                    <input type="radio" name="observer" value="on"> Name<br>
                                                                        <div id="on" style="display:none">
                                                                                <b>Enter an observer's name. Separate with ",".</b><br> EX: Firstname Lastname,Firstname2 Lastname2<br>
                                                                                <input type="text" list="oNameList" id="oName">
                                                                        </div>
                                                                    <input type="radio" name="observer" value="od"> Observer id<br>
                                                                        <div id="od" style="display:none">
                                                                                <b>Enter an id. Separate numbers with ",".</b><br> EX: 967,1015<br>
                                                                                <input type="text" id="oid">
                                                                        </div>
                                                                    <input type="radio" name="observer" value="ct"> Observers with # of submitted checklists<br>
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
								    <b>NOTE:</b> The filters will be applied to the calculation.<br><br>
								    <div id="calcOptions" onChange="inputRadioChange(event)">
									    <input type="radio" name="calc" value="nn" id="noneChbx" checked>None
									    <input type="radio" name="calc" value="cs" id="obschlt"> Observers with # of checklists
									    <input type="radio" name="calc" value="bs" id="bpchlt"> Number of certain birds seen per checklist
									    <input type="radio" name="calc" value="cl" id="novar"> Calculate number of variable
								    </div>
								
								    <br><br>
								
								    <div id="nn">No calculations will be done, and the raw data from the filters will be returned</div>
								
                                                                    <div id="cs" style="display:none" onchange="inputRadioChange(event)">
									    <!--<b>Chose whether or not to add the number of submitted group checklists to the total or not. 
										    If include is selected, it will only return people who have both group and none group checklists.</b><br>
									    <input type="radio" name="groupCheck" value="yes" checked> Include group checklists<br>
									    <input type="radio" name="groupCheck" value="no"> Don't include group checklists<br><br>-->
                                                                            <b>Chose an operator then enter the number of checklists you want to search for.</b><br>
									    <input type="radio" name="operator" value="ge" checked> >=<br>
									    <input type="radio" name="operator" value="g"> ><br>
									    <input type="radio" name="operator" value="le"> <=<br>
									    <input type="radio" name="operator" value="l"> <<br>
									    <input id="equals_sign" type="radio" name="operator" value="e"> =<br>
									    # of checklists: <input type="text" id="checklistNumber">
                                                                            <button id="addChkList" onclick="addCheckList()">Add</button><br><br>
                                                                            <table id="checkListInputTable" class="showData" style="width:30%" onclick="deleteCheckList(event)">
										        <tr id="chkLstTableHeader">
                                                                                            <th>Number of checklists</th>   
										        </tr>
                                                                            </table>
                                                                            <div id="checkListFinalInput" style="display:none"></div>
								    </div>
								
                                                                    <div id="bs" style="display:none" onchange="inputRadioChange(event)">
									    <b>Choose a type of name then start typing correct name(s) for the choice. Press add to insert to table</b><br><br>
									    <input id="sci_radio" type="radio" name="birdNameCalc" value="s" checked> Scientific name<br>
									    <input id="comm_radio" type="radio" name="birdNameCalc" value="p"> Common name<br>
									    <input id="tax_radio" type="radio" name="birdNameCalc" value="t"> Taxonomy<br>
                                                                            
								    </div>
                                                                    <div id="noOfBirdsDiv" style="display:none; margin-left:4em">
                                                                        <br>NAME: <input type="text" id="birdNameCalcInput">
                                                                        <button type="button" onclick="addBird()">Add</button><br>
                                                                        <table id="birdInputTable" class="showData" style="width:30%" onclick="deleteBirdRow(event)">
                                                                                <caption>Bird input</caption>
                                                                                <tr id="birdHeader">
                                                                                        <th>Bird Name</th>                                                                                       
                                                                                </tr>
                                                                        </table><br><br>                                                                          
                                                                        <div id="birdFinalInput" style="display:none"></div>
                                                                    </div>
								
								    <div id="cl" style="display:none">
									    <b>Choose a variable that you would like the count of.</b><br>
									    <div id="variableOptions" style="display:inline-block;padding-right:2em;height:120px;border:1px solid #ccc;overflow:auto;" 
                                                                                 onChange="var radios = document.getElementById('variableOptions').children; var selected; 
                                                                                     for(var i = 0; i &#60; radios.length; i++) 
                                                                                     {
                                                                                         if(radios[i].checked)
                                                                                         {
                                                                                            selected = radios[i].value; break;
                                                                                         }
                                                                                     }
                                                                                     document.getElementById('variableOptionsSelectedOption').innerHTML = &quot <b>Selected variable:</b> &quot + selected">
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
				    <div id="birdOptions" onChange="inputOptionChange(event)">
					    <input type="checkbox" value="ty"> Taxonomy
					    <input type="checkbox" value="bn"> Name
					    <input type="checkbox" value="ls"> Migration type
					    <input type="checkbox" value="fg"> Feeding guild
					    <input type="checkbox" value="ew"> Habitat region
				    </div>
					
				    <br><br>
					
				    <div id="ty" style="display:none">
					    <b>Enter a birds taxonomy number. Separate numbers with ",".</b><br> EX: 84,102,40<br>
					    <input type="text" id="taxonomyInput"><br><br>
				    </div>
					
				    <div id="bn" style="display:none">
					    <b>Chose a type of name then enter the correct name for the choice. Separate with ",".</b><br> EX: Highland_Tinamou,White-throated_Tinamou {for science name}<br>
					    <input type="radio" name="birdNameDecision" value="s" checked> Scientific name<br>
					    <input type="radio" name="birdNameDecision" value="p"> Common name<br>
					    <input type="radio" name="birdNameDecision" value="f"> Family name<br>
					    <input type="radio" name="birdNameDecision" value="o"> Order name<br>
                                            NAME: <input type="text" id="birdNameInput" style="width: 300px"><br><br>
				    </div>
					
				    <div id="ls" style="display:none">
					    <b>Chose a type of bird migration.</b><br>
					    <input type="radio" name="migration" value="l" checked> Long migration<br>
					    <input type="radio" name="migration" value="s"> Short migration<br><br>
				    </div>
					
				    <div id="fg" style="display:none">
					    <b>Enter a birds feeding guild. Separate numbers with ",".</b><br> EX: ?<br>
					    <input type="text" id="feedingGuild"><br><br>
				    </div>
					
				    <div id="ew" style="display:none">
					    <b>Chose side of the US for the birds to be stationed at.</b><br>
					    <input type="radio" name="home" value="e" checked> East<br>
					    <input type="radio" name="home" value="w"> West<br>
					    <input type="radio" name="home" value="b"> Both<br><br>
				    </div>
			    </div>
				
			    <!--This will have all of the user specific files that a user wants to download.-->
			    <div id="creditView" class="tabcontent">
				    Credits will go here.
			    </div>
				
			    <!--This will have all of the user specific files that a user wants to download.-->

			    <div id="downloadView" class="tabcontent"> 
                                <button style="float: right; height: 30px" onclick="refreshDownloads()">Refresh Page</button><br><br>
                                    <table id="table" class='showData'>
                                        <tr id="headerRow">
                                            <th class='showData'>#</th>
                                            <th class='showData'>Date</th>
                                            <th class='showData'>Name</th>
                                            <th class='showData'>Time left</th>
                                            <th class='showData'>Size</th>
                                            <th class='hiddenCell' colspan="3"></th>                                           
                                        </tr>                                   
                                    </table>
                                    <br><br>
                                    <p id="totalSize"></p>
                                    <p id="totalLeft"></p>                                 

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
					    onChange="var radios = document.getElementsByName('mapRadioVariables'); var selected; for(var i = 0; i &#60; radios.length; i++) {if (radios[i].checked){selected = radios[i].value; break;}document.getElementById('mapVariableOptionsSelectedOption').innerHTML = &quot <b>Selected variable:</b> &quot + selected">
				    </div>
				    <div id="mapVariableOptionsSelectedOption" style="display:inline-block">
					    <b>Selected variable:</b>
				    </div>
			    </div>
		    </div>
		</div> 
                <div id="lowerDiv">
                    <p style="margin-left: 3.5em">Return Variables:</p>
                    <p style="display: none" id="warningCheck"><font color="red"><b>Check one or more!</b></font></p>
                    <input id="checkAll" type="checkbox" style="margin-left: 3.8em" onclick="toggle(this)"> <i>Check/Uncheck All</i>
                    <div class="returnDiv">                                                                           
                        <div class="returnSide" id="returnVariables" onchange="returnSideChange()">

                        </div>
                        
                        <div class="returnSide" id="histReturnVariables">

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
</html>

