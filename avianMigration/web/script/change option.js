
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