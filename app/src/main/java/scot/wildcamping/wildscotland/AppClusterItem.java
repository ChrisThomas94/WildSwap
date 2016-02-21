package scot.wildcamping.wildscotland;


import android.content.Context;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by Chris on 04-Feb-16.
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