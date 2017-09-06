//When you click on a tab.
function openTab(evt, tabName) {
    // Declare all variables
    var i, tabcontent, tablinks;

    // Get all elements with class="tabcontent" and hide them
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    // Get all elements with class="tablinks" and remove the class "active"
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    // Show the current tab, and add an "active" class to the link that opened the tab
    document.getElementById(tabName).style.display = "block";
    evt.currentTarget.className += " active";

    //To make the about section look a little bit better by getting rid of unwanted objects.
    if (tabName == "aboutView" || tabName == "downloadView" || tabName == "creditView") {
        document.getElementById('tableResults').style = "display:none";
        document.getElementById('buttons').style = "display:none";
        document.getElementById('lowerDiv').style = "display:none";
    }
    else {
        document.getElementById('tableResults').style = "display:";
        document.getElementById('buttons').style = "display:";
        if(document.getElementById('noneChbx').checked)
            document.getElementById('lowerDiv').style = "display:";
    }
}


function addStateClimateDivRow()
{
    var response = false;
    $('#stateClimateDivInputTable tr').each(function() {
        if($(this).attr('id') !== 'stClimateHeader')
        {
            if(($(this).find("td").eq(0).html() === $('#state_cd').val()) && ($(this).find("td").eq(1).html() === $('#cdiv').val()))
            {
                response = true;
            }
        }  
    });
    
    if(response)
        return;
    
    //Create and insert a new row into the State Climate Div table.
    
    //if state and climDiv textboxes are not empty
    if((document.getElementById('state_cd').value !== "") && (document.getElementById('cdiv').value !== "")){
        //Insert the data into the new row.
        var row = document.getElementById('stateClimateDivInputTable').insertRow(document.getElementById('stateClimateDivInputTable').rows.length);
        row.insertCell(0).innerHTML = document.getElementById('state_cd').value;
        row.insertCell(1).innerHTML = document.getElementById('cdiv').value;    
        row.insertCell(2).innerHTML = "<input id='edit1' type='submit' name='delete' value='Delete'>";

        //Access the div holder that is holding the final string for the state climDiv option.
        var tempHolder = document.getElementById('stateClimateDivFinalInput').innerHTML;

        //If there is more than one option in the holder already.
        if (tempHolder.includes("/")) {
            tempHolder += "," + document.getElementById('state_cd').value + "/" + document.getElementById('cdiv').value;
        }
        else {
            tempHolder = document.getElementById('state_cd').value + "/" + document.getElementById('cdiv').value;
        }

        //Put back the holder's new inner html.
        document.getElementById('stateClimateDivFinalInput').innerHTML = tempHolder;

        //Reset the text boxes.
        document.getElementById('state_cd').value = "";
        document.getElementById('cdiv').value = "";
    }      
}

//To delete a State Climate Div option.
function deleteStateClimateDivRow(evt) {
    var node = evt.target || evt.srcElement;
    var cells = node.parentElement.parentElement.cells;
    var checkString = cells[0].innerHTML + "/" + cells[1].innerHTML;

    var tempHolder = document.getElementById('stateClimateDivFinalInput').innerHTML;
    if (tempHolder.includes("," + checkString)) {
        tempHolder = tempHolder.replace("," + checkString, "");
    }
    else {
        tempHolder = tempHolder.replace(checkString, "");
        if (tempHolder.startsWith(",")) {
            tempHolder = tempHolder.substring(1, tempHolder.length);
        }
    }
    document.getElementById('stateClimateDivFinalInput').innerHTML = tempHolder;

    document.getElementById(node.parentElement.parentElement.parentElement.parentElement.id).deleteRow(node.parentElement.parentElement.rowIndex);
}

//when either general or historical tab is clicked
function onHistRadioClick() {
    
    
    var hist_checkButtons = document.getElementById('histReturnVariables').childNodes;
        
    var gen_checkButtons = document.getElementById('returnVariables').childNodes;
    //if historical radio button is clicked hide day, am/pm, observation and calculations div
    if (document.getElementById('hist_rd').checked) {
        $("#beginYearText").empty();
        $("#endYearText").empty();

        $.getJSON("/avianMigration/GetOrderedYears", {yearText: true, hist_clicked: true}, function(data) {
            var arr = data;

            for(var j in arr["years"]){
                //append option(text, value)
               $("#beginYearText").append(new Option((arr["years"][j]), arr["years"][j]));
               $("#endYearText").append(new Option((arr["years"][j]), arr["years"][j]));
            } 
        });
        
        
        document.getElementById('day_chbx').style.display = 'none';
        document.getElementById('firstArrive').style.display = 'inline';
        document.getElementById('ap_chbx').style.display = 'none';
        document.getElementById('dateSelect').style.display = 'none';
        document.getElementById('obs_chbx').style.display = 'none';
        document.getElementById('calcSide').style.display = 'none';

        //Check in case these divs were selected while in General.
        document.getElementById('date').style.display = 'none';
        document.getElementById('day').style.display = 'none';
        document.getElementById('ampm').style.display = 'none';
        document.getElementById('observer').style.display = 'none';

        //Also make sure that they are unchecked since we are making them disapear.
        document.getElementById('checkDate').checked = false;
        document.getElementById('checkDay').checked = false;
        document.getElementById('checkAmpm').checked = false;
        document.getElementById('checkObserver').checked = false;
        
        dayBoolean = false;
        
        if(!yearBoolean && !monthBoolean){
            document.getElementById('firstWarningText').style = "display:none; color:red";
            document.getElementById('warningText').style = "display:none; color:red";
        }
        
        document.getElementById('returnVariables').style.display = 'none';
        document.getElementById('histReturnVariables').style = 'display:';
        
        //make sure to uncheck the toggle button 
        document.getElementById('checkAll').checked = false;
        
        for(var k = 0; k < hist_checkButtons.length; k++)
        {
            hist_checkButtons[k].checked = false;
        }     
    }

    else if(document.getElementById('gen_rd').checked) {
        $("#beginYearText").empty();
        $("#endYearText").empty();

        console.log("Here");
        $.getJSON("/avianMigration/GetOrderedYears", {yearText: true}, function(data) {
            
            var arr = data;

            for(var j in arr["years"]) {
                //append option(text, value)
               $("#beginYearText").append(new Option((arr["years"][j]), arr["years"][j]));
               $("#endYearText").append(new Option((arr["years"][j]), arr["years"][j]));
            } 
        });
        
        
        //if general radio button is clicked, go back to original state
        document.getElementById('day_chbx').style.display = 'inline';
        document.getElementById('firstArrive').style.display = 'none';
        document.getElementById('ap_chbx').style.display = 'inline';
        document.getElementById('dateSelect').style.display = 'inline';
        document.getElementById('obs_chbx').style.display = 'inline';
        document.getElementById('calcSide').style.display = 'inline';

        document.getElementById('firstArrival').style.display = 'none';
        document.getElementById('checkFirstArrival').checked = false;

        document.getElementById('histReturnVariables').style.display = 'none';
        document.getElementById('returnVariables').style = 'display:';
        
        //make sure to uncheck the toggle button 
        document.getElementById('checkAll').checked = false;
        
        for(var k = 0; k < gen_checkButtons.length; k++)
        {
            gen_checkButtons[k].checked = false;
        }
    }
}

//When the user wants to add a Lat Long square area to the list.
function addLatLongRow() {
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
    if (tempHolder.includes("/")) {
        tempHolder += "," + document.getElementById('latStart').value + "/" + document.getElementById('latStop').value + "/" + document.getElementById('longStart').value + "/" + document.getElementById('longStop').value;
    }
    else {
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
function deleteLatLongRow(evt) {
    var node = evt.target || evt.srcElement;
    var cells = node.parentElement.parentElement.cells;
    var checkString = cells[0].innerHTML + "/" + cells[1].innerHTML + "/" + cells[2].innerHTML + "/" + cells[3].innerHTML;

    var tempHolder = document.getElementById('latLongFinalInput').innerHTML;
    if (tempHolder.includes("," + checkString)) {
        tempHolder = tempHolder.replace("," + checkString, "");
    }
    else {
        tempHolder = tempHolder.replace(checkString, "");
        if (tempHolder.startsWith(",")) {
            tempHolder = tempHolder.substring(1, tempHolder.length);
        }
    }
    document.getElementById('latLongFinalInput').innerHTML = tempHolder;

    document.getElementById(node.parentElement.parentElement.parentElement.parentElement.id).deleteRow(node.parentElement.parentElement.rowIndex);
}

function addBird() {
    
    var response = false;
    $('#birdInputTable tr').each(function() {
        if($(this).attr('id') !== 'birdHeader')
        {
            if($(this).find("td").eq(0).html() === $('#birdNameCalcInput').val())
            {
                response = true;
            }
        }  
    });
    
    if(response)
        return;
    
    if(document.getElementById('birdNameCalcInput').value !== "")
    {
        //Create and insert a new row into the bird table.
        var row = document.getElementById('birdInputTable').insertRow(document.getElementById('birdInputTable').rows.length);

        //Insert the data into the new row.
        row.insertCell(0).innerHTML = document.getElementById('birdNameCalcInput').value;
        row.insertCell(1).innerHTML = "<input id='edit1' type='submit' name='delete' value='Delete'>";

        //Access the div holder that is holding the final string for the lat long option.
        var tempHolder = document.getElementById('birdFinalInput').innerHTML;

        //If there is more than one option in the holder already.
        if (tempHolder !== "") {
            tempHolder += "," + document.getElementById('birdNameCalcInput').value;
        }
        else {
            tempHolder = document.getElementById('birdNameCalcInput').value;
        }

        //Put back the holder's new inner html.
        document.getElementById('birdFinalInput').innerHTML = tempHolder;

        //Reset the text boxes.
        document.getElementById('birdNameCalcInput').value = "";   
    }    
}

function deleteBirdRow(evt) {
    var node = evt.target || evt.srcElement;
    var cells = node.parentElement.parentElement.cells;
    var checkString = cells[0].innerHTML;

    var tempHolder = document.getElementById('birdFinalInput').innerHTML;
    if (tempHolder.includes("," + checkString)) {
        tempHolder = tempHolder.replace("," + checkString, "");
    }
    else {
        tempHolder = tempHolder.replace(checkString, "");
        if (tempHolder.startsWith(",")) {
            tempHolder = tempHolder.substring(1, tempHolder.length);
        }
    }
    document.getElementById('birdFinalInput').innerHTML = tempHolder;

    document.getElementById(node.parentElement.parentElement.parentElement.parentElement.id).deleteRow(node.parentElement.parentElement.rowIndex);
}

function addCheckList()
{
    
    var response = false;
    $('#checkListInputTable tr').each(function() {
        if($(this).attr('id') !== 'chkLstTableHeader')
        {
            if($(this).find("td").eq(0).html() === $('#checklistNumber').val())
            {
                response = true;
            }
        }  
    });
    
    if(response)
        return;
    
    //Create and insert a new row into the lat long table.
    var row = document.getElementById('checkListInputTable').insertRow(document.getElementById('checkListInputTable').rows.length);

    //Insert the data into the new row.
    row.insertCell(0).innerHTML = document.getElementById('checklistNumber').value;
    row.insertCell(1).innerHTML = "<input id='edit1' type='submit' name='delete' value='Delete'>";

    //Access the div holder that is holding the final string for the lat long option.
    var tempHolder = document.getElementById('checkListFinalInput').innerHTML;

    //If there is more than one option in the holder already.
    if (tempHolder !== "") {
        tempHolder += "," + document.getElementById('checklistNumber').value;
    }
    else {
        tempHolder = document.getElementById('checklistNumber').value;
    }

    //Put back the holder's new inner html.
    document.getElementById('checkListFinalInput').innerHTML = tempHolder;

    //Reset the text boxes.
    document.getElementById('checklistNumber').value = "";   
    
    //console.log(tempHolder);
}

function deleteCheckList(evt)
{
    var node = evt.target || evt.srcElement;
    var cells = node.parentElement.parentElement.cells;
    var checkString = cells[0].innerHTML;

    var tempHolder = document.getElementById('checkListFinalInput').innerHTML;
    if (tempHolder.includes("," + checkString)) {
        tempHolder = tempHolder.replace("," + checkString, "");
    }
    else {
        tempHolder = tempHolder.replace(checkString, "");
        if (tempHolder.startsWith(",")) {
            tempHolder = tempHolder.substring(1, tempHolder.length);
        }
    }
    document.getElementById('checkListFinalInput').innerHTML = tempHolder;

    document.getElementById(node.parentElement.parentElement.parentElement.parentElement.id).deleteRow(node.parentElement.parentElement.rowIndex);
}


function getClimateDiv(){
    var climDivs = [];
    
    var key = document.getElementById('state_cd').value;
    
    var climDivsArray = actual_JSON_ClimDiv[key];
    
    //console.log(actual_JSON[key]);
    for(var climDiv in climDivsArray){
        //console.log(countyArray[county]);
        climDivs.push(climDivsArray[climDiv]);
    }
    
//    $("#cdiv").autocomplete({
//        source: climDivs                               
//    });   
    $("#cdiv").autocomplete({
        source: function(request, response) {
            //get all values whose character match the current input
            var matches = $.map(climDivs, function(item) {
                if(item.toUpperCase().indexOf(request.term.toUpperCase()) === 0){
                    return item;
                }
            });
            //put all those matching values into the dropdown list
            response(matches);
        }                              
    }); 
}

function putUser()
{
    var name = "user=";
    var userName;
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length;) 
    {
        var c = ca[i];
        while (c.charAt(0) === ' ') 
        {
            c = c.substring(1);
        }
        
        if (c.indexOf(name) === 0) 
        {
            userName = c.substring(name.length, c.length);
            break;
        }
        i++;
    }
    
    if(userName)
    {
        document.getElementById("userName").innerHTML = userName;
    }
    else
        document.getElementById("userName").innerHTML = "guest";
}

function addDateFunction()
{
    $( "#beginDateText" ).datepicker({ 
        minDate: new Date(2005,0,1),
        yearRange: "2005:+nn",
        maxDate: new Date(),
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true,
        showOn: "button",
        buttonImage: "css/images/calendar.gif",
        buttonImageOnly: true,
        buttonText: "Select date"
    }); 
    $( "#beginDateText" ).datepicker ("option", "dateFormat", "yy-mm-dd"); 

    $( "#endDateText" ).datepicker({ 
        minDate: new Date(2005,0,1),
        yearRange: "2005:+nn",
        maxDate: new Date(),
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true,
        showOn: "button",
        buttonImage: "css/images/calendar.gif",
        buttonImageOnly: true,
        buttonText: "Select date"
    }); 
    $( "#endDateText" ).datepicker ("option", "dateFormat", "yy-mm-dd"); 
    
    
    $( "#beginDateText_fArrival" ).datepicker({ 
        minDate: new Date(1895,0,1),
        yearRange: "1895:+nn",
        maxDate: new Date(1969,12,0),
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true,
        showOn: "button",
        buttonImage: "css/images/calendar.gif",
        buttonImageOnly: true,
        buttonText: "Select date"
    }); 
    $( "#beginDateText_fArrival" ).datepicker ("option", "dateFormat", "yy-mm-dd"); 

    $( "#endDateText_fArrival" ).datepicker({ 
        minDate: new Date(1895,0,1),
        yearRange: "1895:+nn",
        maxDate: new Date(1969,12,0),
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true,
        showOn: "button",
        buttonImage: "css/images/calendar.gif",
        buttonImageOnly: true,
        buttonText: "Select date"
    }); 
    $( "#endDateText_fArrival" ).datepicker ("option", "dateFormat", "yy-mm-dd");
}

function start()
{  
     
    //Set the main tab as selected.
    document.getElementById('mainTab').click();
    
    document.getElementById('load').className = "loader";
   
    var a = function() {
        var defer = $.Deferred();
        addDateFunction();
        defer.resolve();
        return defer;
    };
    
    var b = function() {
        var defer = $.Deferred();
        
        $.getJSON("/avianMigration/GetOrderedYears", {yearText: true}, function(data) {
            var arr = data;

            for(var j in arr["years"]){
                //append option(text, value)
               $("#beginYearText").append(new Option((arr["years"][j]), arr["years"][j]));
               $("#endYearText").append(new Option((arr["years"][j]), arr["years"][j]));
            } 
        });
        defer.resolve();
        return defer;
    };
    
    var c = function() {
        var defer = $.Deferred();
        
        $.getJSON("/avianMigration/GetVariableNames", {vars: true}, function( data ) {
            var myArr = data;
            for (var i in myArr["names"]) {
                //Names will be put in a select list for the calculating variable section.
                document.getElementById('variableOptions').innerHTML += "<input type=\"radio\" name=\"variables\" value=\"" + myArr["names"][i] + "\">" + myArr["names"][i] + "<br>";


                document.getElementById('returnVariables').innerHTML += "<input type=\"checkbox\" id=\"" + myArr["names"][i] + "\"" + "name=\"myReturnVars\" value=\"" + myArr["names"][i] + "\">" + "&nbsp;" + myArr["names"][i] + "<br>";               
                //This part would have been used for the map tab.
                if (myArr["names"][i].toLowerCase().includes("precipitation") || myArr["names"][i].toLowerCase().includes("average") || myArr["names"][i].toLowerCase().includes("maximum") || myArr["names"][i].toLowerCase().includes("minimum") || myArr["names"][i].toLowerCase().includes("palmer")) {
                    document.getElementById('mapVariableOptionsRadio').innerHTML += "<input type=\"radio\" name=\"mapRadioVariables\" value=\"" + myArr["names"][i] + "\">" + myArr["names"][i] + "<br>";
                }
                else {
                    document.getElementById('mapVariableOptionsCheck').innerHTML += "<input type=\"checkbox\" name=\"mapCheckVariables\" value=\"" + myArr["names"][i] + "\">" + myArr["names"][i] + "<br>";
                }
            }
        });
        defer.resolve();
        return defer;
    };
    
    var d = function() {
        var defer = $.Deferred();
        
        $.getJSON("/avianMigration/GetHistVariableNames", {hist_vars: true}, function( data ) {
            var myArr = data;
            for (var i in myArr["hist_names"]) 
                document.getElementById('histReturnVariables').innerHTML += "<input type=\"checkbox\" id=\"" + myArr["hist_names"][i] + "\"" + "name=\"myHistReturnVars\" value=\"" + myArr["hist_names"][i] + "\">" + "&nbsp;" + myArr["hist_names"][i] + "<br>"; 
        });
        defer.resolve();
        return defer;
    };
     
    
    var e = function() {
        var defer = $.Deferred();
        $.getJSON("statesToClimDiv.json", function( data ) {            
            actual_JSON_ClimDiv = data;
            
            var states_cd = [];           
            
            $.each(actual_JSON_ClimDiv, function( key, val ) {
                //console.log(key);
                states_cd.push(key);
            });
            
            $("#state_cd").autocomplete({
                source: function(request, response) {
                    //get all values whose character match the current input
                    var matches = $.map(states_cd, function(item) {
                        if(item.toUpperCase().indexOf(request.term.toUpperCase()) === 0){
                            return item;
                        }
                    });
                    //put all those matching values into the dropdown list
                    response(matches);
                }                              
            });
        });
        
        defer.resolve();
        return defer;
    };
    
    a().then(b).then(c).then(d).then(e);
    
    document.getElementById('histReturnVariables').style.display = 'none';
    
    refreshDownloads();
    
    putUser();    
    document.getElementById("requestUrl").value = window.location.href;
    
    
    document.getElementById('addChkList').style = "display:none";
    document.getElementById('chkLstTableHeader').style = "display:none";
    //document.getElementById('checkListInputTable').style = "display:none";
    getBirdJSON("sci_radio");
   
    console.log("Req url is now: " + document.getElementById("requestUrl").value);
    
    document.getElementById('load').className = "temp";
    
    document.getElementById('location').style.display = 'none';
}

var totalSize = 0;

function refreshDownloads()
{
    $.get( "/avianMigration/files", {files: true, user: "jcourter"}, function( data ) {
        //Old way of showing files.
//        var myArr = JSON.parse(data);
//        if (myArr) {
//            var div = document.getElementById("downloadView");
//            for (var i in myArr["files"]) {
//                var a = document.createElement('a');
//                a.href = "/avianMigration/query_files/" + myArr["files"][i];
//                a.download = myArr["files"][i];
//                a.innerHTML = myArr["files"][i];
//                div.appendChild(document.createElement("br"));
//                div.appendChild(a);
//            }
//            //console.log(div);
//        }
        
        
        var myArr = JSON.parse(data);
        if (myArr) 
        {
            //Change this to be the id of the table.
            var table = document.getElementById("table");
            
            //console.log(table.rows.length);
            
            //Clear the table.
            
            $('#table tr').each(function() {
                if($(this).attr('id') !== 'headerRow')
                {
                    $(this).remove();
                }
            });
            
            totalSize = 0;
           
            for(var index in myArr)
            {               
                var row = table.insertRow(table.rows.length);
                
                //Row number
                var cell = row.insertCell(0);
                cell.innerHTML = table.rows.length - 1;
                
                //Date file was created or refreshed.
                cell = row.insertCell(1);
                cell.innerHTML = myArr[index]["date"];
                
                //Name of file (might think about changing from onchange to button press to change name?).
                cell = row.insertCell(2);
                var text = document.createElement("input");
                var buttonChangeName = document.createElement("input");
                buttonChangeName.setAttribute("type", "button");
                text.setAttribute("type", "text");
                text.setAttribute("size", "50");
                text.setAttribute("style", "margin-right: 10px");
                text.setAttribute("value", myArr[index]["name"]);
                buttonChangeName.setAttribute("value", "change name");
                buttonChangeName.setAttribute("onclick", "changeFileName(event, '" + myArr[index]["id"] + "')"); //This will be the method to update the name of the file in the database.
                cell.appendChild(text);
                cell.appendChild(buttonChangeName);
             
                //TTL of file.
                cell = row.insertCell(3);
                cell.innerHTML = myArr[index]["next date"] + " days";
                
                //Size in KB of file.
                cell = row.insertCell(4);
                //console.log("total size " + totalSize);
                totalSize += parseFloat(myArr[index]["size"]);               
                cell.innerHTML = myArr[index]["size"].toFixed(2) + " MB";
                
                //Refresh button.
                cell = row.insertCell(5);
                //cell.setAttribute("class", "hiddenCell");
                var refresh = document.createElement("input");
                refresh.setAttribute("type", "button");
                refresh.setAttribute("value", "refresh");
                refresh.setAttribute("onchange", "refreshFile('" + myArr[index]["id"] + "')"); //Will tell the database to set the "DATE" attribute to the current date.
                cell.appendChild(refresh);
                
                //row.insertCell(5).innerHTML = "<input type='button' value='refresh' onclick=refreshFile(" + myArr[index]["id"] + ")>";
                
                //Download button.
                cell = row.insertCell(6);
                //cell.setAttribute("class", "hiddenCell");
                var download = document.createElement("input");
                download.setAttribute("type", "button");
                download.setAttribute("value", "download");
                download.setAttribute("onclick", "downloadFile(event, '" + myArr[index]["id"] + "')"); //Will call the database to download the file and give it the currect name.
                cell.appendChild(download);
                
                //row.insertCell(6).innerHTML = "<input type='button' value='download' onclick=downloadFile(event, '" + myArr[index]["id"] + "')>";
                
                //Delete button.
                cell = row.insertCell(7);
                //cell.setAttribute("class", "hiddenCell");
                var deleteButton = document.createElement("input");
                deleteButton.setAttribute("type", "button");
                deleteButton.setAttribute("value", "delete");
                deleteButton.setAttribute("onclick", "deleteFile('" + myArr[index]["id"] + "')"); //Will call database to delete the row, then delete the file from the server.
                cell.appendChild(deleteButton);
                
                //row.insertCell(7).innerHTML = "<input type='button' value='delete' onclick=deleteFile('" + myArr[index]["id"] + "')>";

            }
            
            //These 2 will be at the bottom of the page like we talked about. We will change the 1000 to a calculated number based on who the user is.
            document.getElementById("totalSize").innerHTML = "Total size of files: " + totalSize.toFixed(2) + " MB";
            document.getElementById("totalLeft").innerHTML = "Amount left till full: " + ((totalSize / 1000) * 100).toFixed(2) + " %";
        }
    });
}

function changeFileName(e, id)
{
    var node = e.target || e.srcElement;
    var row = node.parentElement.parentElement;
    var name = row.cells[2].childNodes[0].value;
    
    if(name.length <= 50)
    {
        $.get( "/avianMigration/change_file_name", {change_file_name: true, id: id, name: name}, function( data ) {
            refreshDownloads();
        });
    }
    else
        alert("Name must be 50 characters or less.")
}

function refreshFile(id)
{
    $.get( "/avianMigration/refresh_file", {refresh_file: true, id: id}, function( data ) {
        refreshDownloads();
    });
}

function downloadExcel(url, data)
{
    var form = $('<form></form>').attr('action', url).attr('method', 'post');

    Object.keys(data).forEach(function(key){
        var value = data[key];

        if(value instanceof Array) {
            value.forEach(function (v) {
                form.append($("<input></input>").attr('type', 'hidden').attr('name', key).attr('value', v));
            });
        } else {
            form.append($("<input></input>").attr('type', 'hidden').attr('name', key).attr('value', value));
        }

    });    

    //send request
    form.appendTo('body').submit().remove();
}

function downloadFile(e, id)
{
    //console.log("here");
    var node = e.target || e.srcElement;
    var row = node.parentElement.parentElement;
    var name = row.cells[2].childNodes[0].value;
    
    downloadExcel("/avianMigration/download_file", {download_file: true, id: id, name: name});
    
    refreshDownloads();
}
  
function deleteFile(id)
{
    $.get( "/avianMigration/delete_file", {delete_file: true, id: id}, function( data ) {
        refreshDownloads();
    });
}


