package org.geodroid.app;

import org.jeo.data.DataRepository;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

/**
 * Extension of android Application that manages global state for geo applications. 
 * <p>
 * A GeoApplication instance exposes a {@link Registry} object to the application
 * activities through {@link GeoApplication#getDataRegistry(Activity)}.
 * </p>
 * @author Justin Deoliveira, OpenGeo
 */
public class GeoApplication extends Application {

    public static GeoApplication get(Activity activity) {
        return getOrFail(activity.getApplication());
    }

    public static GeoApplication get(Service service) {
        return getOrFail(service.getApplication());
    }
    
    static GeoApplication getOrFail(Application app) {
        if (app instanceof GeoApplication) {
            return (GeoApplication) app;
        }
        throw new IllegalStateException("Application object is not a GeoApplication");
    }

    public DataRepository createDataRepository() {
        return new GeoDataRepository();
    }

}
