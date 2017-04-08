package scot.wildcamping.wildscotland.Objects;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;


/**
 * Created by Chris on 04-Feb-16.
 *
 */
public class AppClusterItem implements ClusterItem {

    private final LatLng mPosition;

    public AppClusterItem(double latitude, double longitude) {
        mPosition = new LatLng(latitude, longitude);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}