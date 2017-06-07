
//When you click on a checkbox for options to be seen.
function inputOptionChange(evt) {
    //Get the object clicked.
    var node = evt.target || evt.srcElement;

    if (node.checked) {
        //Check whether the year, month, or day boxes were clicked.
        if (node.value == "year") {
            yearBoolean = true;
        }

        if (node.value == "month") {
            monthBoolean = true;
        }

        if (node.value == "day") {
            dayBoolean = true;
        }

        //If any of them were clicked show the warning text.
        if (yearBoolean || monthBoolean || dayBoolean) {
            document.getElementById('warningText').style = "display:";
        }

        //Display the corresponding div to the check box by making the display option be its default.
        document.getElementById(node.value).style = "display:";
    }
    else {
        if (node.value == "year") {
            yearBoolean = false;
        }

        if (node.value == "month") {
            monthBoolean = false;
        }

        if (node.value == "day") {
            dayBoolean = false;
        }

        if (!yearBoolean && !monthBoolean && !dayBoolean) {
            document.getElementById('warningText').style = "display:none";
        }

        //If a checbox is unchecked then hide its div.
        document.getElementById(node.value).style = "display:none";
    }

}

//this will ensure a user cannot select an ending year that is less than the beginning year
function selectOption(evt)
{
    var beginYear = evt.target.value;
    
    var endYearOptions = document.getElementById('endYearText');
    
    //set every option to default display at the beginning of each change
    for(var j = 0; j < endYearOptions.length; j++)
    {
        endYearOptions[j].style="display:";
    }
    
    //loop through the options in end year and hide options that are less than the beginning year
    //also make sure the first element in the list is always beginYear
    for(var i = 0; i < endYearOptions.length; i++)
    {
        //console.log(endYearOptions[i].value);
        if(endYearOptions[i].value < beginYear)
        {
            endYearOptions[i].style.display = "none";
        }
        if(endYearOptions[i].value === beginYear)
            endYearOptions[i].selected = true;
    }
}


//If a radio button is pressed anywhere on the page.
function inputRadioChange(evt) {
    var node = evt.target || evt.srcElement;

    //Get the parent that is holding all of the radio buttons.
    node = node.parentElement;

    //Go over every button to see which one is selected.
    for (var i = 0; i < node.childNodes.length; i++) {
        if (node.childNodes[i].type == "radio" && node.childNodes[i].checked) {
            //Make sure the radio button is valid.
            if (document.getElementById(node.childNodes[i].value)) {
                //Set it to the default display and tab over 1.
                document.getElementById(node.childNodes[i].value).style = "display:;padding-left: 4em";
            }
        }
        else if (node.childNodes[i].type == "radio") {
            if (document.getElementById(node.childNodes[i].value)) {
                document.getElementById(node.childNodes[i].value).style = "display:none";
            }
        }
    }
}