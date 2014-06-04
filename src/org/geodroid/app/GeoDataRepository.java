package org.geodroid.app;

import java.io.File;

import android.util.Log;
import org.jeo.android.geopkg.GeoPackage;
import org.jeo.android.mbtiles.MBTiles;
import org.jeo.carto.CartoCSS;
import org.jeo.csv.CSV;
import org.jeo.data.DataRepository;
import org.jeo.data.DataRepositoryView;
import org.jeo.data.DirectoryRepository;
import org.jeo.data.DriverRegistry;
import org.jeo.data.JSONRepository;
import org.jeo.data.StaticDriverRegistry;
import org.jeo.data.mem.Memory;
import org.jeo.gdal.GeoTIFF;
import org.jeo.geojson.GeoJSON;
import org.jeo.ogr.Shapefile;

import android.os.Environment;

/**
 * Create a DataRepositoryView that exposes data from a directory.
 * <p>
 * If not overridden by the user the default directory is named "GeoData" on the root of the SDCard
 * of the device, obtained from {@link Environment#getExternalStorageDirectory()}. 
 * </p>
 * <p>
 * The registry will operate in one of two modes. If the specified data directory contains an 
 * <tt>index.json</tt> file it will operate as {@link JSONRegistry}. Otherwise it will simply scan 
 * the directory contains as {@link DirectoryRegistry}.
 * </p>
 * @author Justin Deoliveira, OpenGeo
 */
public class GeoDataRepository {

    static {
        // explicitly load proj library so gdal/ogr can do reprojection
        try {
            System.loadLibrary("proj");
        }
        catch(Throwable t) {
            Log.w("Geodroid", "Unable to load proj library, reprojection through GDAL/OGR will not function", t);
        }
    }

    /**
     * Returns the GeoData directory handle.
     */
    public static File directory() {
        return new File(Environment.getExternalStorageDirectory(), "Geodata");
    }

    public static DataRepositoryView create() {
        return create(null, null);
    }

    public static DataRepositoryView create(DriverRegistry drivers) {
        return create(null, drivers);
    }

    public static DataRepositoryView create(File dir, DriverRegistry drivers) {
        if (dir == null) {
            dir = directory();
        }

        if (drivers == null) {
            drivers = new StaticDriverRegistry(new GeoPackage(), new MBTiles(),
                new GeoJSON(), new CSV(), new Shapefile(), new Memory(), new CartoCSS(),
                new GeoTIFF());
        }

        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("not a directory: " + dir.getPath());
        }

        File index = new File(dir, "index.json");
        DataRepository repo;
        if (index.exists()) {
            repo = new JSONRepository(index, drivers);
        }
        else {
            repo = new DirectoryRepository(dir, drivers);
        }

        return new DataRepositoryView(repo);
    }

}
