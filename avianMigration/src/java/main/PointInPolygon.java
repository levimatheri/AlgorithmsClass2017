/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import java.io.File;
import java.io.IOException;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

/**This is the class that has to have over 100 .jar files associated with it.
 *Even though I put some of this together and worked on it, I still cannot fully
 *understand all that is going on here. The short summary of it is this, it takes
 *a shape file, uses the .dbf file that goes along with the shape file to get 
 *the data, takes a Latitude and Longitude point and somehow converts it into a 
 *point that will be put on the shape file I.E. simply picture using vectors. 
 *It then uses that point to find out which polygon the point is in. Once it 
 *has that, it will use the .dbf file to retrieve the data for that point. There
 *is more data than what we need. We are only pulling back out the climate
 *division code from the polygon and returning that.
 */
public class PointInPolygon {
	public FilterFactory2 filterFactory;
	private SimpleFeatureCollection features;
    
    /**
     *  Give a Latitude and Longitude point and get back the climate 
     *  division that holds that point.
     * @param lat The Latitude point
     * @param lon The longitude point
     * @return The climate division ID or null if the point was in none
     *      of the climate divisions.
     * @throws IOException 
     */
    public Integer getId(double lat, double lon) throws IOException
    {
        //
        //System.out.println("Working dir: " + System.getProperty("user.dir"));
        File file = new File("..\\..\\avianMigration\\src\\java\\resources\\CONUS_CLIMATE_DIVISIONS.shp\\GIS.OFFICIAL_CLIM_DIVISIONS.shp");

		PointInPolygon tester = new PointInPolygon();
		FileDataStore store = FileDataStoreFinder.getDataStore(file);
        
        
        String[] typeNames = store.getTypeNames();
        String typeName = typeNames[0];

		SimpleFeatureSource featureSource = store.getFeatureSource(typeName);
		tester.setFeatures(featureSource.getFeatures());
		GeometryFactory fac = new GeometryFactory();
        
        //Long, Lat
        Point p = fac.createPoint(new Coordinate(lon, lat));
        Integer flag = tester.isInShape(p);
       
//        if (flag != null) {
//            System.out.println(flag);
//        }
        store.dispose();
        return flag;
    }

	public PointInPolygon() {

		filterFactory = CommonFactoryFinder.getFilterFactory2(GeoTools
				.getDefaultHints());
	}

	private Integer isInShape(Point p) 
    {
		Expression propertyName = filterFactory.property("the_geom");
		Filter filter = filterFactory.contains(propertyName,
				filterFactory.literal(p));
		SimpleFeatureCollection sub = features.subCollection(filter);
        
        
        Integer id = null;
        if(sub.size() > 0)
        {
        
            try (FeatureIterator<SimpleFeature> featuresIt = sub.features()) 
            {
                while (featuresIt.hasNext()) 
                {
                    SimpleFeature feature = featuresIt.next();
                    for (Property attribute : feature.getProperties()) 
                    {
//                        if(attribute.getName().toString().equals("STATE"))
//                            System.out.println(attribute.getValue().toString() + " Longitude: " + p.getY() + " Latitude: " + p.getX());
                        
                        if(attribute.getName().toString().equals("CLIMDIV"))
                            id = Integer.parseInt(attribute.getValue().toString());
                    }
                }
            }
        }
		return id;
	}

	private void setFeatures(SimpleFeatureCollection features) 
    {
		this.features = features;
	}
        
        public static void main(String[] args) throws IOException
        {
            PointInPolygon pip = new PointInPolygon();
            
            System.out.println(pip.getId(39.6856753, -74.1448591));
        }

}