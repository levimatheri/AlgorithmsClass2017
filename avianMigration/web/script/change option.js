var sci_bool = false;
﻿var comm_bool = false;
﻿var tax_bool = false;
//When you click on a checkbox for options to be seen.
function inputOptionChange(evt) {
    
    if(document.getElementById('mainChbxCheck').style.display !== 'none')
        document.getElementById('mainChbxCheck').style.display = "none";

    
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
            document.getElementById('firstWarningText').style = "color: red";
            document.getElementById('warningText').style = "color: red";
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
            document.getElementById('firstWarningText').style = "display:none; color:red";
            document.getElementById('warningText').style = "display:none; color:red";
        }

        //If a checbox is unchecked then hide its div.
        document.getElementById(node.value).style = "display:none";
    }

}

function toggle(source)
{
    document.getElementById("warningCheck").style.display = "none"; 
    var checkboxes;
    
    if(document.getElementById('gen_rd').checked)
        checkboxes = document.getElementsByName("myReturnVars");
    else if(document.getElementById('hist_rd').checked)
        checkboxes = document.getElementsByName("myHistReturnVars");
    
    
    //console.log(checkboxes);
    for(var i = 0, n = checkboxes.length; i < n; i++)
    {
        checkboxes[i].checked = source.checked;
    }
}

function clearBirdTable()
{
    $('#birdInputTable tr').each(function() {
        if($(this).attr('id') !== 'birdHeader')
        {
            $(this).remove();
        }
    });
    
    document.getElementById('birdFinalInput').innerHTML = ""; 
}


function returnSideChange()
{
    document.getElementById("warningCheck").style.display = "none"; 
}

//this will ensure a user cannot select an ending year/month/day that is less than the beginning year/month/day
function selectOption(evt, ele)
{
    //hide --Please Select-- option   
    $(".base").hide();
    
    //make sure JS sees this as a number an not a string using the unary plus(+)
    var begin = +(evt.target.value);   
    
    var elements = document.getElementsByClassName(ele.className);
    
    //get the end part of the variable i.e endYear or endMonth etc.
    var endOptions = document.getElementById(elements[1].id);
    
    
    //set every option to default display at the beginning of each change
    for(var j = 0; j < endOptions.length; j++)
    {
        if(endOptions[j].className !== 'base')
            endOptions[j].style="display:";
    }
    
    //loop through the options in end year and hide options that are less than the beginning year
    //also make sure the first element in the list is always beginYear
    for(var i = 0; i < endOptions.length; i++)
    {
        //make sure JS sees endOptions[i].value as a number an not a string using the unary plus(+)
        if(+endOptions[i].value < begin)
        {            
            endOptions[i].style.display = "none";
        }
        if((+endOptions[i].value === begin))
            endOptions[i].selected = true;        
    }
}


//If a radio button is pressed anywhere on the page.
function inputRadioChange(evt) {
    
    var node = evt.target || evt.srcElement;

    //Get the parent that is holding all of the radio buttons.
    node = node.parentElement;

    //Go over every button to see which one is selected.
    for (var i = 0; i < node.childNodes.length; i++) {
        
        if (node.childNodes[i].type === "radio" && node.childNodes[i].checked) {
            //Make sure the radio button is valid.
            if (document.getElementById(node.childNodes[i].value)) {
                //Set it to the default display and tab over 1.
                document.getElementById(node.childNodes[i].value).style = "display:;padding-left: 4em";
            }
 
            if(node.childNodes[i].id === 'noneChbx')
                document.getElementById('lowerDiv').style.display = 'block';
            else if(node.childNodes[i].id === 'bpchlt')
                document.getElementById('noOfBirdsDiv').style.display = 'block';
//            else if(node.childNodes[i].id === 'sci_radio')
//            {
//                if(sci_bool === false)
//                {
//                    clearBirdTable();
//                }
//                getBirdJSON(node.childNodes[i].id);
//                sci_bool = true;
//                comm_bool = false;
//                tax_bool = false;
//            }
//            else if(node.childNodes[i].id === 'comm_radio')
//            {
//                if(comm_bool === false)
//                {
//                    clearBirdTable();
//                }
//                getBirdJSON(node.childNodes[i].id);
//                comm_bool = true;
//                sci_bool = false;
//                tax_bool = false;
//            }
//            else if(node.childNodes[i].id === 'tax_radio')
//            {
//                 if(tax_bool === false)
//                {
//                    clearBirdTable();
//                }
//                getBirdJSON(node.childNodes[i].id);
//                comm_bool = false;
//                sci_bool = false;
//                tax_bool = true;
//            }
            if(node.childNodes[i].name === 'birdNameCalc')
            {
                clearBirdTable();
            }
           
           else if(node.childNodes[i].id === 'equals_sign')
           {              
               document.getElementById('addChkList').style = "display:";
               document.getElementById('chkLstTableHeader').style = "display:block";
               document.getElementById('checkListInputTable').style = "display:block";
           }
           
           else if(node.childNodes[i].name === 'operator' && node.childNodes[i].id !== 'equals_sign')
           {
               document.getElementById('addChkList').style = "display:none";
               document.getElementById('chkLstTableHeader').style = "display:none";
               document.getElementById('checkListInputTable').style = "display:none";
           }
                
//            else if(node.childNodes[i].id === 'obschlt' || node.childNodes[i].id === 'bpchlt' || node.childNodes[i].id === 'novar')
//                document.getElementById('lowerDiv').style.display = 'none';
        }
        else if (node.childNodes[i].type == "radio") {
            if (document.getElementById(node.childNodes[i].value)) {
                document.getElementById(node.childNodes[i].value).style = "display:none";
            }
        }
    }
}

function getBirdJSON(id)
{
    var responseBirdJSON = [];
    $.getJSON('/avianMigration/submit_job', {birdData: true, id: id}, function(data) {
        $.each(data, function(key, val) {
            responseBirdJSON.push(val);
        });
    });
    
   
    $("#birdNameCalcInput").autocomplete({
        source: function(request, response) {
            //get all values whose character match the current input
            var matches = $.map(responseBirdJSON, function(item) {
                if(item.toUpperCase().indexOf(request.term.toUpperCase()) === 0){
                    return item;
                }
            });
            //put all those matching values into the dropdown list
            response(matches);
        }                              
    }); 
}