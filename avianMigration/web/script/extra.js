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
    }
    else {
        document.getElementById('tableResults').style = "display:";
        document.getElementById('buttons').style = "display:";
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
    }

    else {
        //if general radio button is clicked, go back to original state
        document.getElementById('day_chbx').style.display = 'inline';
        document.getElementById('firstArrive').style.display = 'none';
        document.getElementById('ap_chbx').style.display = 'inline';
        document.getElementById('dateSelect').style.display = 'inline';
        document.getElementById('obs_chbx').style.display = 'inline';
        document.getElementById('calcSide').style.display = 'inline';

        document.getElementById('firstArrival').style.display = 'none';
        document.getElementById('checkFirstArrival').checked = false;
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

function start()
{
    //Set the main tab as selected.
    document.getElementById('mainTab').click();


    //Do a request to get all of the possible variables that can be used.
    var xmlhttp = new XMLHttpRequest();
    var url = "/avianMigration/submit_job";
    var params = "?vars=true";
    xmlhttp.open("Get", url + params, true);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log(this.responseText);
            var myArr = JSON.parse(this.responseText);
            if (myArr) {
                for (var i in myArr["names"]) {
                    //Names will be put in a select list for the calculating variable section.
                    document.getElementById('variableOptions').innerHTML += "<input type=\"radio\" name=\"variables\" value=\"" + myArr["names"][i] + "\">" + myArr["names"][i] + "<br>";

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

    xmlhttp = new XMLHttpRequest();
    var url = "/avianMigration/submit_job";
    var params = "?files=true&user=jcourter";
    xmlhttp.open("Get", url + params, true);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log(this.responseText);
            var myArr = JSON.parse(this.responseText);
            if (myArr) {
                var div = document.getElementById("downloadView");
                for (var i in myArr["files"]) {
                    var a = document.createElement('a');
                    a.href = "/avianMigration/query_files/" + myArr["files"][i];
                    a.download = myArr["files"][i];
                    a.innerHTML = myArr["files"][i];
                    div.appendChild(document.createElement("br"));
                    div.appendChild(a);
                }
                console.log(div);
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