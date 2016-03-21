package scot.wildcamping.wildscotland;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapsFragment;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import scot.wildcamping.wildscotland.model.Site;
import scot.wildcamping.wildscotland.model.knownSite;

public class MapsFragment extends MapFragment implements View.OnClickListener  {
	
	public MapsFragment(){}

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    MapView mMapView;
    private GoogleMap googleMap;
    private static final int MINIMUM_ZOOM_LEVEL_SERVER_REQUEST = 7;
    private static final int DEFAULT_ZOOM_LEVEL = 4;

    LatLngBounds SCOTLAND = new LatLngBounds(new LatLng(55, -8), new LatLng(59.5, -1.7));
    LatLng bunSite;
    boolean add = false;
    double newLat;
    double newLon;
    ImageButton addSite;
    Button gpsAdd;
    Button manualAdd;
    Button longLatAdd;
    Button btnDismiss;
    List<LatLng> knownSites = new ArrayList<LatLng>();
    List<LatLng> unknownSites = new ArrayList<LatLng>();
    int unknownSitesSize;
    Cluster<AppClusterItem> clickedCluster;
    AppClusterItem clickedClusterItem;
    private ProgressDialog pDialog;
    private ProgressDialog pDialog2;
    final int relatOwn = 90;
    final int relatTrade = 45;
    String user;
    MarkerManager mMarkerManager;
    MarkerManager.Collection coll;
    knownSite inst = new knownSite();
    boolean clicked;


    private final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(54.187, -9.61), new LatLng(62.814, 0.541));
    private final int MAX_ZOOM = 13;
    private final int MIN_ZOOM = 7;
    private OverscrollHandler mOverscrollHandler = new OverscrollHandler();

    SparseArray<Site> knownSitesMap;
    int knownSiteSize;

    SparseArray<Site> ownedSitesMap;
    int ownedSiteSize;

    Site newlyAdded;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if(isNetworkAvailable()){
            new FetchKnownSites(getActivity()).execute();
            new FetchUnknownSites(getActivity()).execute();
        }


        knownSitesMap = inst.getKnownSitesMap();
        knownSiteSize = inst.getKnownSiteSize();

        ownedSitesMap = inst.getOwnedSitesMap();
        ownedSiteSize = inst.getOwnedSiteSize();

        System.out.println(ownedSitesMap.size());

        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null)
        {
            newLat = extras.getDouble("latitude");
            newLon = extras.getDouble("longitude");
            add = extras.getBoolean("add");

            System.out.println(newLat);
            System.out.println(newLon);
            System.out.println(add);

            bunSite = new LatLng(newLat, newLon);
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

        googleMap.getUiSettings().setMapToolbarEnabled(false);

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

        if(add){
            addMarker(bunSite);
        }

        //initialize views
        addSite = (ImageButton)v.findViewById(R.id.fab);

        // set listeners for buttons
        addSite.setOnClickListener(this);

        //add the unknown sites as cluster items
        setUpClustering();

        // center map on Scotland
        if(!add){
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(SCOTLAND.getCenter()).zoom(6).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

        }else{//center map on newly created site
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(bunSite).zoom(9).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            add = false;
        }

        //mOverscrollHandler.sendEmptyMessageDelayed(0,100);

        return v;
    }

    /**
     * Returns the correction for Lat and Lng if camera is trying to get outside of visible map
     * @param cameraBounds Current camera bounds
     * @return Latitude and Longitude corrections to get back into bounds.
     */
    private LatLng getLatLngCorrection(LatLngBounds cameraBounds) {
        double latitude=0, longitude=0;
        if(cameraBounds.southwest.latitude < BOUNDS.southwest.latitude) {
            latitude = BOUNDS.southwest.latitude - cameraBounds.southwest.latitude;
        }
        if(cameraBounds.southwest.longitude < BOUNDS.southwest.longitude) {
            longitude = BOUNDS.southwest.longitude - cameraBounds.southwest.longitude;
        }
        if(cameraBounds.northeast.latitude > BOUNDS.northeast.latitude) {
            latitude = BOUNDS.northeast.latitude - cameraBounds.northeast.latitude;
        }
        if(cameraBounds.northeast.longitude > BOUNDS.northeast.longitude) {
            longitude = BOUNDS.northeast.longitude - cameraBounds.northeast.longitude;
        }
        return new LatLng(latitude, longitude);
    }

    /**
     * Bounds the user to the overlay.
     */
    private class OverscrollHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            CameraPosition position = googleMap.getCameraPosition();
            VisibleRegion region = googleMap.getProjection().getVisibleRegion();
            float zoom = 0;
            if(position.zoom < MIN_ZOOM) zoom = MIN_ZOOM;
            if(position.zoom > MAX_ZOOM) zoom = MAX_ZOOM;
            LatLng correction = getLatLngCorrection(region.latLngBounds);
            if(zoom != 0) {     //|| correction.latitude != 0 || correction.longitude != 0
                zoom = (zoom==0)?position.zoom:zoom;
                double lat = position.target.latitude + correction.latitude;
                double lon = position.target.longitude + correction.longitude;
                CameraPosition newPosition = new CameraPosition(new LatLng(lat, lon), zoom, position.tilt, position.bearing);
                //CameraUpdate update = CameraUpdateFactory.newLatLngZoom(position.target, zoom);
                CameraUpdate update = CameraUpdateFactory.newCameraPosition(newPosition);
                googleMap.moveCamera(update);
            }
        /* Recursively call handler every 100ms */
            sendEmptyMessageDelayed(0,100);
        }
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
            }
        });

        manualAdd.setOnClickListener(new Button.OnClickListener() {


            @Override
            public void onClick(View v) {

                popupWindow.dismiss();

                //TODO make clusters dissaper before listening for map click so user is not restricted by clusters

                //googleMap.clear();

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
                        clicked = true;
                        newLat = point.latitude;
                        newLon = point.longitude;

                        Intent intent = new Intent(getActivity().getApplicationContext(), AddSite.class);
                        intent.putExtra("latitude", point.latitude);
                        intent.putExtra("longitude", point.longitude);
                        getActivity().startActivity(intent);
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
            }
        });

        popupWindow.showAtLocation(addSite, Gravity.CENTER, 0, 0);
    }

    private void addMarker(LatLng newSite){

        MarkerOptions marker = new MarkerOptions().position(newSite);

        googleMap.addMarker(marker);

    }

    private void setUpClustering() {
        // Declare a variable for the cluster manager.
        ClusterManager<AppClusterItem> mClusterManager;
        mMarkerManager = new MarkerManager(googleMap);

        // Initialize the manager with the context and the map.
        mClusterManager = new ClusterManager<>(this.getActivity(), googleMap, mMarkerManager);

        coll = mMarkerManager.newCollection();

        if(knownSiteSize > 0) {
            for (int i = 0; i < knownSiteSize; i++) {
                Site currentSite = knownSitesMap.get(i);
                coll.addMarker(new MarkerOptions().position(currentSite.getPosition()));
            }
        } else {
            //no known sites
        }

        if(ownedSiteSize > 0){
            for(int i = 0; i< ownedSiteSize; i++){

                Site currentSite = ownedSitesMap.get(i);
                System.out.println(i + "  " + currentSite.getTitle());
                coll.addMarker(new MarkerOptions().position(currentSite.getPosition()));
            }

        } else {
            //no owned sites
        }

        // Point the map's listeners at the listeners implemented by the cluster manager.
        googleMap.setOnCameraChangeListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mMarkerManager);

        //GoogleMap.OnMarkerClickListener markerClickListener
        coll.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                if(knownSiteSize > 0) {
                    for (int i = 0; i < knownSiteSize; i++) {
                        Site currentSite = knownSitesMap.get(i);

                        if (marker.getPosition().equals(currentSite.getPosition())) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), KnownSiteActivity.class);
                            intent.putExtra("arrayPosition", i);
                            intent.putExtra("latitude", currentSite.getPosition().latitude);
                            intent.putExtra("longitude", currentSite.getPosition().longitude);
                            intent.putExtra("cid", currentSite.getCid());
                            intent.putExtra("title", currentSite.getTitle());
                            intent.putExtra("description", currentSite.getDescription());
                            intent.putExtra("rating", currentSite.getRating());
                            intent.putExtra("feature1", currentSite.getFeature1());
                            intent.putExtra("feature2", currentSite.getFeature2());
                            intent.putExtra("feature3", currentSite.getFeature3());
                            intent.putExtra("feature4", currentSite.getFeature4());
                            intent.putExtra("feature5", currentSite.getFeature5());
                            intent.putExtra("feature6", currentSite.getFeature6());
                            intent.putExtra("feature7", currentSite.getFeature7());
                            intent.putExtra("feature8", currentSite.getFeature8());
                            intent.putExtra("feature9", currentSite.getFeature9());
                            intent.putExtra("feature10", currentSite.getFeature10());
                            intent.putExtra("image", currentSite.getImage());
                            intent.putExtra("prevState", 0);
                            startActivity(intent);
                            getActivity().finish();
                            break;
                        }
                    }
                } else {

                }

                if(ownedSiteSize > 0) {
                    for (int i = 0; i < ownedSiteSize; i++) {
                        Site currentSite = ownedSitesMap.get(i);

                        if (marker.getPosition().equals(currentSite.getPosition())) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), OwnedSiteActivity.class);
                            intent.putExtra("arrayPosition", i);
                            intent.putExtra("cid", currentSite.getCid());
                            intent.putExtra("prevState", 0);
                            startActivity(intent);
                            getActivity().finish();
                            break;
                        }
                    }
                } else {

                }

                return true;
            }
        });

        googleMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());

        //instal custom infoWindowAdapter as the adpater for one or both of the marker collections
        mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForClusters());

        // Add cluster items (markers) to the cluster manager.
        addClusterMarkers(mClusterManager);

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<AppClusterItem>() {
            @Override
            public boolean onClusterClick(Cluster<AppClusterItem> cluster) {
                clickedCluster = cluster;
                return false;
            }
        });
    }

    class MyCustomAdapterForClusters implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker){

            if (clickedCluster != null) {

                if(inst.getOwnedSiteSize() == 0){

                    Toast.makeText(getActivity(), "You do not own a site to trade!", Toast.LENGTH_LONG).show();

                } else {

                    ArrayList<LatLng> cluster = new ArrayList<>();
                    Intent intent = new Intent(getActivity().getApplicationContext(), TradeActivitySimple.class);

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

    private void addClusterMarkers(ClusterManager<AppClusterItem> mClusterManager) {

        //knownSite inst = new knownSite();
        SparseArray<Site> map = inst.getUnknownSitesMap();
        int size = inst.getUnknownSitesSize();
        //System.out.println("maps fragment "+size);

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < size; i++) {
            //System.out.println(i);
            Site currentSite = map.get(i);

            LatLng point = currentSite.getPosition();
            double lon = point.longitude;
            double lat = point.latitude;

            AppClusterItem unknownSitesList = new AppClusterItem(lat, lon);

            mClusterManager.setRenderer(new CustomRenderer<AppClusterItem>(this.getActivity(), googleMap, mClusterManager));
            mClusterManager.setAlgorithm(new CustomAlgorithm<AppClusterItem>());
            mClusterManager.addItem(unknownSitesList);
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



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}





