//This top 2 variables are global which means iFrames can use them by calling parent.[variable], and this allows them to change
//these 2 which we can then keep track of.

//Variables for the climate division iFrame.
var cdFrameResults = "";	//The results from clicking in the climate division iFrame.
var startLoad = false;		//Boolean to tell the iFrame to start loading the massive GeoJSON file for the map.
var loadDone = false;		//Boolean for the iFrame to set to true when the map is fully loaded.
var loadComplete;			//A set interval object that will be started when the user clicks on the link, and cancelled when the map is done loading.

//The results from clicking in the state iFrame.
var stFrameResults = "";

//Called when the user clicks on the link to load the climate division map.
function openCdMap() {
    document.getElementById('cdIframeLink').style = "display:none;";
    document.getElementById('cdIframe').style = "border:none;display:;";

    //Set the start load boolean to true to tell the climate division iFrame to load the map.
    startLoad = true;

    //Start the loading GIF.
    document.getElementById('load').className = "loader";

    //Check every 100 seconds to see if the map is done loading.
    loadComplete = window.setInterval(checkLoadDone, 100);
}

function checkLoadDone() {
    if (loadDone) {
        window.clearInterval(loadComplete);
        document.getElementById('load').className = "temp";
    }
}