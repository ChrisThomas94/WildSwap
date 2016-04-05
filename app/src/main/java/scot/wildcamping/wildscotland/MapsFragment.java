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
import android.support.design.widget.Snackbar;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import com.google.maps.android.clustering.algo.GridBasedAlgorithm;
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

    MapView mMapView;
    private GoogleMap googleMap;
    private static final int MINIMUM_ZOOM_LEVEL_SERVER_REQUEST = 7;
    private static final int DEFAULT_ZOOM_LEVEL = 4;

    LatLngBounds SCOTLAND = new LatLngBounds(new LatLng(55, -8), new LatLng(59.5, -1.7));
    LatLng bunSite;
    boolean add = false;
    boolean trade = false;
    double newLat;
    double newLon;
    ImageButton addSite;
    Button gpsAdd;
    Button manualAdd;
    Button longLatAdd;
    Button btnDismiss;
    Cluster<AppClusterItem> clickedCluster;
    AppClusterItem clickedClusterItem;
    private ProgressDialog pDialog;
    private ProgressDialog pDialog2;
    final int relatOwn = 90;
    final int relatTrade = 45;
    String user;
    ClusterManager<AppClusterItem> mClusterManager;
    MarkerManager mMarkerManager;
    MarkerManager.Collection coll;
    MarkerManager.Collection collKnown;
    knownSite inst = new knownSite();
    boolean clicked;
    boolean zoomed = false;
    boolean threshold = false;


    private final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(54.187, -9.61), new LatLng(62.814, 0.541));
    private final int MAX_ZOOM = 8;
    private float prevZoom = 6;
    private final int MIN_ZOOM = 7;
    private OverscrollHandler mOverscrollHandler = new OverscrollHandler();

    SparseArray<Site> knownSitesMap;
    int knownSiteSize;

    SparseArray<Site> ownedSitesMap;
    int ownedSiteSize;

    Site newlyAdded;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        knownSitesMap = inst.getKnownSitesMap();
        knownSiteSize = inst.getKnownSiteSize();

        ownedSitesMap = inst.getOwnedSitesMap();
        ownedSiteSize = inst.getOwnedSiteSize();

        System.out.println("owned size: "+ownedSitesMap.size());

        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null)
        {
            newLat = extras.getDouble("latitude");
            newLon = extras.getDouble("longitude");
            add = extras.getBoolean("add");
            trade = extras.getBoolean("trade");

            System.out.println(newLat);
            System.out.println(newLon);
            System.out.println(add);

            bunSite = new LatLng(newLat, newLon);
        }

        // inflate and return the layout
        v = inflater.inflate(R.layout.fragment_maps, container,
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

        /*if(add){
            addMarker(bunSite);
        }*/

        //initialize views
        addSite = (ImageButton)v.findViewById(R.id.fab);

        // set listeners for buttons
        addSite.setOnClickListener(this);

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mClusterManager.onCameraChange(cameraPosition);

                if(prevZoom>MAX_ZOOM && cameraPosition.zoom < MAX_ZOOM){
                    //make clusters appear
                    addClusterMarkers(mClusterManager);
                } else if (prevZoom < MAX_ZOOM && cameraPosition.zoom > MAX_ZOOM){
                    //make clusters dissapear
                    mClusterManager.clearItems();
                }

                prevZoom = cameraPosition.zoom;

            }
        });

        //add the unknown sites as cluster items
        setUpClustering();

        // center map on Scotland
        if(add){
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(bunSite).zoom(10).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            getActivity().getIntent().removeExtra("add");
        } else if (trade){
            LatLng tradeSite = new LatLng(newLat, newLon);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(tradeSite).zoom(7).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

        } else {//center map on newly created site
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(SCOTLAND.getCenter()).zoom(prevZoom).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }

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
    public void onClick(final View view) {

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

                //Snackbar.make(v, "Touch point on map to add a marker!", Snackbar.LENGTH_INDEFINITE).show();

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
        mMarkerManager = new MarkerManager(googleMap);

        // Initialize the manager with the context and the map.
        mClusterManager = new ClusterManager<>(this.getActivity(), googleMap, mMarkerManager);

        //collKnown = mMarkerManager.newCollection();
        coll = mMarkerManager.newCollection();

        if(knownSiteSize > 0) {
            for (int i = 0; i < knownSiteSize; i++) {
                Site currentSite = knownSitesMap.get(i);
                coll.addMarker(new MarkerOptions().position(currentSite.getPosition()).icon(BitmapDescriptorFactory.fromResource(R.drawable.greenpin32)).title(currentSite.getTitle()).snippet(currentSite.getDescription()));
            }
        } else {
            //no known sites
        }

        if(ownedSiteSize > 0){
            System.out.println("I have an owned site");
            for(int i = 0; i< ownedSiteSize; i++){

                Site currentSite = ownedSitesMap.get(i);
                System.out.println(i + "  " + currentSite.getTitle());
                coll.addMarker(new MarkerOptions().position(currentSite.getPosition()).icon(BitmapDescriptorFactory.fromResource(R.drawable.redpin32)).title(currentSite.getTitle()).snippet(currentSite.getDescription()));
            }

        } else {
            //no owned sites
        }

        // Point the map's listeners at the listeners implemented by the cluster manager.
        //googleMap.setOnCameraChangeListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mMarkerManager);
        googleMap.setInfoWindowAdapter(mMarkerManager);

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                if(knownSiteSize > 0) {
                    for (int i = 0; i < knownSiteSize; i++) {
                        Site currentSite = knownSitesMap.get(i);

                        if (marker.getPosition().equals(currentSite.getPosition())) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), KnownSiteActivity.class);
                            intent.putExtra("arrayPosition", i);
                            intent.putExtra("cid", currentSite.getCid());
                            intent.putExtra("prevState", 0);
                            startActivity(intent);
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
                            break;
                        }
                    }
                } else {

                }
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

    private void removeClusterMarkers(ClusterManager<AppClusterItem> mClusterManager) {


        //knownSite inst = new knownSite();
        SparseArray<Site> map = inst.getUnknownSitesMap();
        int size = inst.getUnknownSitesSize();
        //System.out.println("maps fragment "+size);

        for (int i = 0; i < size; i++) {
            //System.out.println(i);
            Site currentSite = map.get(i);

            LatLng point = currentSite.getPosition();
            double lon = point.longitude;
            double lat = point.latitude;

            AppClusterItem unknownSitesList = new AppClusterItem(lat, lon);

            //mClusterManager.setRenderer(new CustomRenderer<AppClusterItem>(this.getActivity(), googleMap, mClusterManager));
            //mClusterManager.setAlgorithm(new CustomAlgorithm<AppClusterItem>());
            mClusterManager.removeItem(unknownSitesList);
        }
    }

    class CustomRenderer<T extends ClusterItem> extends DefaultClusterRenderer<T>
    {
        public CustomRenderer(Context context, GoogleMap map, ClusterManager<T> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<T> cluster) {
            //cluster all items
            return cluster.getSize() >= 1;
        }


    }

    class CustomAlgorithm<T extends ClusterItem> extends NonHierarchicalDistanceBasedAlgorithm<T>{

        public CustomAlgorithm(){
            super();
        }

    }
}





