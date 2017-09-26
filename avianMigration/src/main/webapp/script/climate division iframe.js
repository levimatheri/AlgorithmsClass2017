//initialize the map
var map = L.map('map').setView([37, -90], 4);
var geojson;
var info = L.control();
var interval = window.setInterval(start, 100);

function start() {
    if (parent.startLoad)
    {
        window.clearInterval(interval);
        info.onAdd = function (map) {
            this._div = L.DomUtil.create('div', 'info'); // create a div with a class "info"
            this.update();
            return this._div;
        };

        // method that we will use to update the control based on feature properties passed
        info.update = function (props) {
            this._div.innerHTML = props ? "State: " + props.STATE + "<br> Climate Divison name: " + props.NAME + "<br> Climate Division code: " + props.CLIMDIV : "State: N/A<br> Climate Divison name: N/A<br> Climate Division code: N/A";
        };

        info.addTo(map);

        $.getJSON("GIS.json", function (hoodData) {
            geojson = L.geoJson(hoodData,
            {
                style: function (feature) {
                    var fillColor,
                        density = feature.properties.density;
                    fillColor = "#f7f7f7";  // no data
                    return { color: "#999", weight: 1, fillColor: fillColor, fillOpacity: .6 };
                },
                onEachFeature: function (feature, layer) {
                    layer.on({
                        mouseover: highlightFeature,
                        mouseout: resetHighlight,
                        click: clicked
                    });
                }
            }).addTo(map);
        });

        map.addOneTimeEventListener('layeradd', callBack);
    }
}

function callBack() {
    parent.loadDone = true;
}

function clicked(e) {
    if (parent.cdFrameResults.length != 0) {
        if (parent.cdFrameResults.includes("," + e.target.feature.properties.CLIMDIV)) {
            parent.cdFrameResults = parent.cdFrameResults.replace("," + e.target.feature.properties.CLIMDIV, "");
            geojson.resetStyle(e.target);
        }
        else if (parent.cdFrameResults.includes(e.target.feature.properties.CLIMDIV)) {
            parent.cdFrameResults = parent.cdFrameResults.replace(e.target.feature.properties.CLIMDIV, "");
            geojson.resetStyle(e.target);
        }
        else {
            e.target.setStyle({ fillColor: '#000000' });
            e.target.setStyle({ color: '#FFFFFF' });
            parent.cdFrameResults += "," + e.target.feature.properties.CLIMDIV;
        }

        if (parent.cdFrameResults.startsWith(",")) {
            parent.cdFrameResults = parent.cdFrameResults.substring(1, parent.cdFrameResults.length);
        }
    }
    else {
        e.target.setStyle({ fillColor: '#000000' });
        e.target.setStyle({ color: '#FFFFFF' });
        parent.cdFrameResults += e.target.feature.properties.CLIMDIV;
    }
}

function resetHighlight(e) {
    if (!parent.cdFrameResults.includes(e.target.feature.properties.CLIMDIV)) {
        geojson.resetStyle(e.target);
        info.update();
    }
}

function highlightFeature(e) {
    var layer = e.target;
    if (!parent.cdFrameResults.includes(e.target.feature.properties.CLIMDIV)) {

        layer.setStyle({
            weight: 5,
            color: '#666',
            dashArray: '',
            fillOpacity: 0.7
        });

        if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
            layer.bringToFront();
        }
    }
    info.update(layer.feature.properties);
}