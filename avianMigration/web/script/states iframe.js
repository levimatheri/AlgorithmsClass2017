//initialize the map
var map = L.map('map').setView([37, -90], 4);
var geojson;
var info = L.control();

function start()
{
    info.onAdd = function (map) {
        this._div = L.DomUtil.create('div', 'info'); // create a div with a class "info"
        this.update();
        return this._div;
    };

    // method that we will use to update the control based on feature properties passed
    info.update = function (props) {
        this._div.innerHTML = props ? "State: " + props.STATE_NAME : "State: N/A";
    };

    info.addTo(map);

    $.getJSON("states.json", function (hoodData) {
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
}



function highlightFeature(e) {
    var layer = e.target;
    if (!parent.stFrameResults.includes(e.target.feature.properties.STATE_NAME)) {

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

function resetHighlight(e) {
    if (!parent.stFrameResults.includes(e.target.feature.properties.STATE_NAME)) {
        geojson.resetStyle(e.target);
        info.update();
    }
}

function clicked(e) {
    if (parent.stFrameResults.length != 0) {
        if (parent.stFrameResults.includes("," + e.target.feature.properties.STATE_NAME)) {
            parent.stFrameResults = parent.stFrameResults.replace("," + e.target.feature.properties.STATE_NAME, "");
            geojson.resetStyle(e.target);
        }
        else if (parent.stFrameResults.includes(e.target.feature.properties.STATE_NAME)) {
            parent.stFrameResults = parent.stFrameResults.replace(e.target.feature.properties.STATE_NAME, "");
            geojson.resetStyle(e.target);
        }
        else {
            e.target.setStyle({ fillColor: '#000000' });
            e.target.setStyle({ color: '#FFFFFF' });
            parent.stFrameResults += "," + e.target.feature.properties.STATE_NAME;
        }

        if (parent.stFrameResults.startsWith(",")) {
            parent.stFrameResults = parent.stFrameResults.substring(1, parent.stFrameResults.length);
        }
    }
    else {
        e.target.setStyle({ fillColor: '#000000' });
        e.target.setStyle({ color: '#FFFFFF' });
        parent.stFrameResults += e.target.feature.properties.STATE_NAME;
    }
}