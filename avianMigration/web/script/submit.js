
//Booleans to say whether or not the warning text inside of the date div should be shown or not.
var yearBoolean = false;
var monthBoolean = false;
var dayBoolean = false;

//Get the correct data for whichever option on the page is currently being checked.
function getOptionString(option) {
    //Will be returned
    var result = "=";
    switch (option) {
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
            for (var i = 0; i < parent.childNodes.length; i++) {
                var child = parent.childNodes[i];
                if (child.type == "radio") {
                    if (child.checked) {
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
            for (var i = 0; i < parent.childNodes.length; i++) {
                var child = parent.childNodes[i];
                if (child.type == "radio") {
                    if (child.checked) {
                        result += child.value + "/";
                    }
                }
            }
            result += document.getElementById('birdNameCalcInput').value;
            break;

            //long or short migration
        case "ls":
            var parent = document.getElementById('ls');
            for (var i = 0; i < parent.childNodes.length; i++) {
                var child = parent.childNodes[i];
                if (child.type == "radio") {
                    if (child.checked) {
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
            for (var i = 0; i < parent.childNodes.length; i++) {
                var child = parent.childNodes[i];
                if (child.type == "radio") {
                    if (child.checked) {
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
            for (var i = 0; i < parent.childNodes.length; i++) {
                var child = parent.childNodes[i];
                if (child.type == "radio") {
                    if (child.checked) {
                        result += child.value + "/";
                    }
                }
            }
            result += document.getElementById('checklistNumber').value;
            break;

            //Calculate the number of a certain variable.
        case "cl":
            var parent = document.getElementById('variableOptions');
            for (var i = 0; i < parent.childNodes.length; i++) {
                var child = parent.childNodes[i];

                if (child.type == "radio") {
                    if (child.checked) {
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
function submit() {
    finishSubmit("");
}

//Will finish the submit process based off of what button calls it.
function finishSubmit(application) {
    if (!application) {
        //Activate the loading icon to show the user something is happening.
        document.getElementById('load').className = "loader";
    }

    //Prepare a request.
    var xmlhttp = new XMLHttpRequest();

    //Set it to the submit_jobs servlet.
    var url = "/avianMigration/submit_job";

    //Prepare the parameters.
    var params = application;

    //console.log(!params);

    //Figure out which tab is currently the selected one. Only one tab can be selected at a time.
    tablinks = document.getElementsByClassName("tablinks");

    var id;
    for (i = 0; i < tablinks.length; i++) {
        if (tablinks[i].className == "col-xs-12 tablinks active")
            id = tablinks[i].id;
    }

    if (id == "birdTab") {
        var options = document.getElementById('birdOptions');
        for (var i = 0; i < options.childNodes.length; i++) {
            if (options.childNodes[i].checked) {
                //If the parameters variable has data in it already, then you need to add a "&" because there is a variable there already.
                if (!params) {
                    params = options.childNodes[i].value + getOptionString(options.childNodes[i].value);
                }
                else {
                    params += "&" + options.childNodes[i].value + getOptionString(options.childNodes[i].value);
                }
            }
        }
    }
    else if (id == "mainTab") {
        var options = document.getElementById('options');
        console.log(options);

        for (var i = 0; i < options.childNodes.length; i++) {
            var checkBox = options.childNodes[i];
            if (checkBox.childNodes.length > 0) {
                checkBox = checkBox.childNodes[0];
                if (checkBox.checked) {
                    //Check whether the date checkbox is selected. The date option overrides the year, month, and day options, so we do not 
                    //want those showing up if date is checked.
                    if (!document.getElementById('checkDate').checked) {
                        if (checkBox.value == "year") {
                            if (!params) {
                                params = "yr=" + document.getElementById('beginYearText').value + "/" + document.getElementById('endYearText').value;
                            }
                            else {
                                params += "&yr=" + document.getElementById('beginYearText').value + "/" + document.getElementById('endYearText').value;
                            }
                        }
                        else if (checkBox.value == "month") {
                            if (!params) {
                                params = "mh=" + document.getElementById('beginMonthText').value + "/" + document.getElementById('endMonthText').value;
                            }
                            else {
                                params += "&mh=" + document.getElementById('beginMonthText').value + "/" + document.getElementById('endMonthText').value;
                            }
                        }
                        else if (checkBox.value == "day") {
                            if (!params) {
                                params = "dy=" + document.getElementById('beginDayText').value + "/" + document.getElementById('endDayText').value;
                            }
                            else {
                                params += "&dy=" + document.getElementById('beginDayText').value + "/" + document.getElementById('endDayText').value;
                            }
                        }
                    }

                    //This is a loop so we cannot put an else after the last if to add date because it will just show up later and
                    //be added again. So just have an if prepared for when it does.
                    if (checkBox.value == "date") {
                        var beginDate = document.getElementById('beginDateText').value;
                        
                        //console.log("Begin date: " + beginDate);

                        var endDate = document.getElementById('endDateText').value;
                        
                        //console.log("End date: " + endDate);

                        if (!params) {
                            params = "dt=" + beginDate + "/" + endDate;
                        }
                        else {
                            params += "&dt=" + beginDate + "/" + endDate;
                        }
                    }

                    //For first arrival date.
                    if (checkBox.value == "firstArrival") {
                        var beginDate = document.getElementById('beginDateText_fArrival').value;                        
                        var endDate = document.getElementById('endDateText_fArrival').value;
                        
                        if (!params) {
                            params = "dt=" + beginDate + "/" + endDate;
                        }
                        else {
                            params += "&dt=" + beginDate + "/" + endDate;
                        }
                    }

                    //If it is any other variable then go out and get the correct data for it and return.
                    var sections = document.getElementById(checkBox.value);
                    for (var x = 0; x < sections.childNodes.length; x++) {
                        console.log(sections.childNodes[x].type);
                        if (sections.childNodes[x].type == "radio") {
                            if (sections.childNodes[x].checked) {
                                if (!params) {
                                    params = sections.childNodes[x].value + getOptionString(sections.childNodes[x].value);
                                }
                                else {
                                    params += "&" + sections.childNodes[x].value + getOptionString(sections.childNodes[x].value);
                                }
                            }
                        }
                    }
                }
            }
        }

        //Find which calculation option is selected.
        options = document.getElementById('calcOptions');

        for (var i = 0; i < options.childNodes.length; i++) {
            if (options.childNodes[i].checked) {
                if (!params) {
                    params = options.childNodes[i].value + getOptionString(options.childNodes[i].value);
                }
                else {
                    params += "&" + options.childNodes[i].value + getOptionString(options.childNodes[i].value);
                }
            }
        }
    }

    console.log(document.getElementById("gen_rd").checked)
    if (document.getElementById("gen_rd").checked) {
        if (!params)
            params = "select=gen";
        else
            params += "&select=gen";
    }
    else {
        if (!params)
            params = "select=hist";
        else
            params += "&select=hist";
    }

    //For debugging what will be going over to the server.
    console.log(params);

    //Open the connection using the url and set it to a Post request.
    xmlhttp.open("Post", url, true);

    //Set that we will be sending url encoded strings to the server.
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

    //Will be called when a state of the connection has changed.
    xmlhttp.onreadystatechange = function () {
        //When a response has been returned and it set to 200 "ok".
        if (this.readyState == 4 && this.status == 200) {
            //myArr is a standard variable I started to use to show you are working with a returned json object. It is used all over every website as of right now.
            //It can be changed if wanted, but make sure to do ctrl + h to replace all
            var myArr = JSON.parse(this.responseText);
            if (myArr) {
                //Get the results table.
                var table = document.getElementById('results');
                table.innerHTML = "<caption>Sample of Query Results</caption>";

                //Insert the variables' names into the headers. Do so in order using the "order" object returned with the json object.
                var header = table.insertRow(0);
                var i = 0;
                for (var key in myArr["order"]) {
                    header.insertCell(i).outerHTML = "<th>" + myArr["order"][key] + "</th>";
                    i++;
                }

                for (var num = 1; num <= myArr[myArr["order"][0]].length; num++) {
                    table.insertRow(table.rows.length);
                }

                var cellNum = 0;
                for (var key in myArr["order"]) {
                    var value = myArr[myArr["order"][key]];
                    i = 1;
                    for (var x in value) {
                        var row = table.rows[i];

                        if (value[x])
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
        else if (this.readyState == this.HEADERS_RECEIVED && this.status == 500) {
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
function exportFile() {
    finishSubmit("application=true");
}
