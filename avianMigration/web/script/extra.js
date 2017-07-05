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

function getClimateDiv(){
    var climDivs = [];
    
    var key = document.getElementById('state_cd').value;
    
    var climDivsArray = actual_JSON_ClimDiv[key];
    
    //console.log(actual_JSON[key]);
    for(var climDiv in climDivsArray){
        //console.log(countyArray[county]);
        climDivs.push(climDivsArray[climDiv]);
    }
    
    $("#cdiv").autocomplete({
        source: climDivs                               
    });    
}

function start()
{
    //Set the main tab as selected.
    document.getElementById('mainTab').click();
    
    
    
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
    
    

    //Do a request to get all of the possible variables that can be used.
    var xmlhttp = new XMLHttpRequest();
    var url = "/avianMigration/submit_job";
    var params = "?vars=true";
    
    xmlhttp.open("Get", url + params, true);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            //console.log(this.responseText);
            var myArr = JSON.parse(this.responseText);
            if (myArr) {
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
            }
        }
    }
    xmlhttp.send(params);
    
    var xmlhttp = new XMLHttpRequest();
    var url = "/avianMigration/submit_job";
    var params = "?hist_vars=true";
    
    xmlhttp.open("Get", url + params, true);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            //console.log(this.responseText);
            var myArr = JSON.parse(this.responseText);
            if (myArr) {
                for (var i in myArr["hist_names"]) {
                    document.getElementById('histReturnVariables').innerHTML += "<input type=\"checkbox\" id=\"" + myArr["hist_names"][i] + "\"" + "name=\"myHistReturnVars\" value=\"" + myArr["hist_names"][i] + "\">" + "&nbsp;" + myArr["hist_names"][i] + "<br>"; 
                }
            }
        }
    }
    xmlhttp.send(params);
    
    document.getElementById('histReturnVariables').style.display = 'none';
    
    refreshDownloads();
    
    //request to get year span from database
    xmlhttp = new XMLHttpRequest();
    var url = "/avianMigration/submit_job";
    var params = "?yearText=true";
    xmlhttp.open("Get", url + params, true);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlhttp.onreadystatechange = function (){
        if(xmlhttp.readyState == 4 && xmlhttp.status == "200"){   
            var arr = JSON.parse(this.responseText);
            
            for(var j in arr["years"]){
                //append option(text, value)
               $("#beginYearText").append(new Option((arr["years"][j]), arr["years"][j]));
               $("#endYearText").append(new Option((arr["years"][j]), arr["years"][j]));
            }      
            
        }
    };
    xmlhttp.send(params);  
    
    var xobj1 = new XMLHttpRequest();
    xobj1.overrideMimeType("application/json");
    xobj1.open('GET', 'statesToClimDiv.json', true);
    xobj1.onreadystatechange = function (){
        if(xobj1.readyState == 4 && xobj1.status == "200"){
            //callback(xobj.responseText);
            actual_JSON_ClimDiv = JSON.parse(xobj1.responseText);
            
            var states_cd = [];
            for(var key in actual_JSON_ClimDiv){
                states_cd.push(key);
               // console.log(key);
            }      
            $("#state_cd").autocomplete({
                source: states_cd                               
            });      
        }
    };
    xobj1.send(null);   
}

function refreshDownloads()
{
    $.get( "/avianMigration/submit_job", {files: true, user: "jcourter"}, function( data ) {
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
            
            //How much data the user currently has.
            var totalSize;
            
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
                text.setAttribute("value", myArr[index]["name"]);
                buttonChangeName.setAttribute("value", "change name");
                buttonChangeName.setAttribute("onclick", "changeFileName(event, '" + myArr[index]["id"] + "')"); //This will be the method to update the name of the file in the database.
                cell.appendChild(text);
                cell.appendChild(buttonChangeName);
                
                //TTL of file.
                cell = row.insertCell(3);
                
                //Code for date difference found from here: http://www.howi.in/2017/03/find-difference-between-two-dates-in-angularjs.html
                var today = new Date();
                var dd = today.getDate();
                var mm = today.getMonth()+1; //January is 0!
                var yyyy = today.getFullYear();
                if(dd < 10) {
                    dd = '0'+ dd;
                }
                if(mm < 10) {
                    mm = '0'+ mm;
                }
                today = yyyy+'/'+mm+'/'+dd;
                var date2 = new Date(today);
                var date1 = new Date(myArr[index]["date"]);
                var timeDiff = Math.abs(date2.getTime() - date1.getTime());
                cell.innerHTML = Math.ceil(timeDiff / (1000 * 3600 * 24)) + " days";
                
                //Size in KB of file.
                cell = row.insertCell(4);
                totalSize += parseFloat(myArr[index]["size"]);
                
                cell.innerHTML = myArr[index]["size"] + " KB";
                
                //Refresh button.
                cell = row.insertCell(5);
                var refresh = document.createElement("input");
                refresh.setAttribute("type", "button");
                refresh.setAttribute("value", "refresh");
                refresh.setAttribute("onchange", "refreshFile('" + myArr[index]["id"] + "')"); //Will tell the database to set the "DATE" attribute to the current date.
                cell.appendChild(refresh);
                
                //row.insertCell(5).innerHTML = "<input type='button' value='refresh' onclick=refreshFile(" + myArr[index]["id"] + ")>";
                
                //Download button.
                cell = row.insertCell(6);
                var download = document.createElement("input");
                download.setAttribute("type", "button");
                download.setAttribute("value", "download");
                download.setAttribute("onclick", "downloadFile(event, '" + myArr[index]["id"] + "')"); //Will call the database to download the file and give it the currect name.
                cell.appendChild(download);
                
                //row.insertCell(6).innerHTML = "<input type='button' value='download' onclick=downloadFile(event, '" + myArr[index]["id"] + "')>";
                
                //Delete button.
                cell = row.insertCell(7);
                var deleteButton = document.createElement("input");
                deleteButton.setAttribute("type", "button");
                deleteButton.setAttribute("value", "delete");
                deleteButton.setAttribute("onclick", "deleteFile('" + myArr[index]["id"] + "')"); //Will call database to delete the row, then delete the file from the server.
                cell.appendChild(deleteButton);
                
                //row.insertCell(7).innerHTML = "<input type='button' value='delete' onclick=deleteFile('" + myArr[index]["id"] + "')>";

            }
            
            //These 2 will be at the bottom of the page like we talked about. We will change the 1000 to a calculated number based on who the user is.
            document.getElementById("totalSize").innerHTML = "Total size of files: " + totalSize + " KB";
            document.getElementById("totalLeft").innerHTML = "Amount left till full: " + (1000 - totalSize) + " KB";
        }
    });
}

function changeFileName(e, id)
{
    var node = e.target || e.srcElement;
    var row = node.parentElement.parentElement;
    var name = row.cells[2].childNodes[0].value;
    
    $.get( "/avianMigration/submit_job", {change_file_name: true, id: id, name: name}, function( data ) {
        refreshDownloads();
    });
}

function refreshFile(id)
{
    $.get( "/avianMigration/submit_job", {refresh_file: true, id: id}, function( data ) {
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
    
    downloadExcel("/avianMigration/submit_job", {download_file: true, id: id, name: name});
    
    refreshDownloads();
}
  
function deleteFile(id)
{
    $.get( "/avianMigration/submit_job", {delete_file: true, id: id}, function( data ) {
        refreshDownloads();
    });
}