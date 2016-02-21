package scot.wildcamping.wildscotland;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapsFragment;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.geometry.Bounds;
import com.google.maps.android.geometry.Point;

import java.util.ArrayList;
import java.util.List;

import scot.wildcamping.wildscotland.AppClusterItem;

public class MapsFragment extends MapFragment implements View.OnClickListener  {
	
	public MapsFragment(){}

    MapView mMapView;
    private GoogleMap googleMap;
    private static final int MINIMUM_ZOOM_LEVEL_SERVER_REQUEST = 7;
    private static final int DEFAULT_ZOOM_LEVEL = 4;

    LatLngBounds SCOTLAND = new LatLngBounds(new LatLng(55, -8), new LatLng(59.5, -1.7));
    LatLng newSite = null;
    boolean add;
    double newLat;
    double newLon;
    ImageButton addSite;
    Button gpsAdd;
    Button manualAdd;
    Button longLatAdd;
    Button btnDismiss;
    Button btnDismissLatLong;
    List<LatLng> knownSites = new ArrayList<LatLng>();
    Cluster<AppClusterItem> clickedCluster;
    AppClusterItem clickedClusterItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null)
        {
            newLat = extras.getDouble("latitude");
            newLon = extras.getDouble("longitude");
            add = extras.getBoolean("add");

            newSite = new LatLng(newLat, newLon);

        }

        // inflate and return the layout
        View v = inflater.inflate(R.layout.fragment_maps, container,
                false);

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

        // center map on Scotland
        if(newSite == null){
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(SCOTLAND.getCenter()).zoom(6).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

        }
        else{//center map on newly created site
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(newSite).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }


        if(add == true){
            //knownSites.add(newSite);
            addMarker(newSite);
        }


        //initialize views
        addSite = (ImageButton)v.findViewById(R.id.fab);


        // set listeners for buttons
        addSite.setOnClickListener(this);

        // clustering
        /*if(knownSites.size() == 0){

        }else{
        setUpClustering();
        }*/

        setUpClustering();

        // Perform any camera updates here
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    @Override
    public void onClick(View view) {

        LayoutInflater layoutInflater = (LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                AbsListView.LayoutParams.WRAP_CONTENT,
                AbsListView.LayoutParams.WRAP_CONTENT);

        //initialize views
        btnDismiss = (Button)popupView.findViewById(R.id.cancelNewSite);
        gpsAdd = (Button)popupView.findViewById(R.id.gps);
        manualAdd = (Button)popupView.findViewById(R.id.manual);
        longLatAdd = (Button)popupView.findViewById(R.id.longlat);


        //set on click listeners
        btnDismiss.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        gpsAdd.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                double latitude = 0;
                double longitude = 0;

                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    googleMap.getMyLocation();
                    latitude = googleMap.getMyLocation().getLatitude();
                    longitude = googleMap.getMyLocation().getLongitude();
                } else {
                    // Show rationale and request permission.
                }

                // start new activity
                Intent intent = new Intent(getActivity().getApplicationContext(), AddSite.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        manualAdd.setOnClickListener(new Button.OnClickListener() {


            @Override
            public void onClick(View v) {

                popupWindow.dismiss();

                /*LayoutInflater layoutInflater
                        = (LayoutInflater) getActivity().getBaseContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View popupView = layoutInflater.inflate(R.layout.popup_manualadd, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        AbsListView.LayoutParams.WRAP_CONTENT,
                        AbsListView.LayoutParams.WRAP_CONTENT);

                popupWindow.showAtLocation(addSite, Gravity.CENTER, 0, 0);*/

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng point) {

                        newLat = point.latitude;
                        newLon = point.longitude;

                        Intent intent = new Intent(getActivity().getApplicationContext(), AddSite.class);
                        intent.putExtra("latitude", point.latitude);
                        intent.putExtra("longitude", point.longitude);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                });

            }
        });

        longLatAdd.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(),
                        AddSite.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        popupWindow.showAtLocation(addSite, Gravity.CENTER, 0, 0);

    }

    private void addMarker(LatLng newSite){

        MarkerOptions marker = new MarkerOptions().position(newSite).title("New Marker");

        googleMap.addMarker(marker);

    }

    private void setUpClustering() {
        // Declare a variable for the cluster manager.
        ClusterManager<AppClusterItem> mClusterManager;

        // Initialize the manager with the context and the map.
        mClusterManager = new ClusterManager<AppClusterItem>(this.getActivity(), googleMap);

        // Point the map's listeners at the listeners implemented by the cluster manager.
        googleMap.setOnCameraChangeListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());

        //instal custom infoWindowAdapter as the adpater for one or both of the marker collections
        mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForClusters());
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForItems());

        // Add cluster items (markers) to the cluster manager.
        addClusterMarkers(mClusterManager);

        googleMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<AppClusterItem>() {
            @Override
            public boolean onClusterClick(Cluster<AppClusterItem> cluster) {
                clickedCluster = cluster;
                return false;
            }
        });
        //not sure this is needed as unknown sites should all show as a cluster rather than an item
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<AppClusterItem>() {
            @Override
            public boolean onClusterItemClick(AppClusterItem appClusterItem) {
                clickedClusterItem = appClusterItem;
                return false;
            }
        });

    }

    class MyCustomAdapterForClusters implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker){

            if (clickedCluster != null) {

                ArrayList<LatLng> cluster = new ArrayList<>();
                Intent intent = new Intent(getActivity().getApplicationContext(), TradeActivity.class);

                for (AppClusterItem item : clickedCluster.getItems()) {
                    // Extract data from each item in the cluster as needed
                    //use the position to pass through to the trade screen where the position can be used to find the campsite id and display other info without giving away the location
                    cluster.add(item.getPosition());
                    System.out.println("maps fragment"+item.getPosition());
                }

                intent.putParcelableArrayListExtra("cluster", cluster);

                // Launching  main activity
                startActivity(intent);
            }

            View view = getActivity().getLayoutInflater().inflate(R.layout.cluster_popup, null);
            return view;
        }

        //for some reason this is not executing however the method above is instead so use that
        @Override
        public View getInfoContents(Marker marker) {
            if (clickedCluster != null) {
                for (AppClusterItem item : clickedCluster.getItems()) {
                    // Extract data from each item in the cluster as needed
                    System.out.println(item);
                }
            }

            View view = getActivity().getLayoutInflater().inflate(R.layout.activity_login, null);
            return view;
        }
    }

    class MyCustomAdapterForItems implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker){

            View view = getActivity().getLayoutInflater().inflate(R.layout.activity_register, null);
            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            if (clickedCluster != null) {
                for (AppClusterItem item : clickedCluster.getItems()) {
                    // Extract data from each item in the cluster as needed
                }
            }

            View view = getActivity().getLayoutInflater().inflate(R.layout.activity_trade, null);
            return view;
        }
    }


    private void addClusterMarkers(ClusterManager<AppClusterItem> mClusterManager) {

        double latitude = 56.79;
        double longitude = -5.06;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {

            double offset = i/60d;
            latitude = latitude + offset;
            longitude = longitude + offset;

            LatLng genPoint = new LatLng(latitude, longitude);
            knownSites.add(genPoint);

            LatLng point = knownSites.get(i);
            double lon = point.longitude;
            double lat = point.latitude;

            AppClusterItem knownSitesList = new AppClusterItem(lat, lon);

            mClusterManager.setRenderer(new CustomRenderer<AppClusterItem>(this.getActivity(), googleMap, mClusterManager));
            mClusterManager.setAlgorithm(new CustomAlgorithm<AppClusterItem>());
            mClusterManager.addItem(knownSitesList);



        }
    }

}

class CustomRenderer<T extends ClusterItem> extends DefaultClusterRenderer<T>
{
    public CustomRenderer(Context context, GoogleMap map, ClusterManager<T> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<T> cluster) {
        //start clustering if at least 2 items overlap
        return cluster.getSize() >= 1;
    }


}

class CustomAlgorithm<T extends ClusterItem> extends NonHierarchicalDistanceBasedAlgorithm<T>{

    public CustomAlgorithm(){
        super();
    }

}

