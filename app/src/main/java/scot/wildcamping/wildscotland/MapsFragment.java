package scot.wildcamping.wildscotland;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionItemTarget;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.PointTarget;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapsFragment;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
import java.util.concurrent.ExecutionException;

import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import scot.wildcamping.wildscotland.model.Site;
import scot.wildcamping.wildscotland.model.knownSite;

public class MapsFragment extends MapFragment implements OnMapReadyCallback{

    //implements View.OnClickListener
    //implements OnMapReadyCallback

	public MapsFragment(){}

    MapView mMapView;
    private static final int MINIMUM_ZOOM_LEVEL_SERVER_REQUEST = 7;
    private static final int DEFAULT_ZOOM_LEVEL = 4;

    LatLngBounds SCOTLAND = new LatLngBounds(new LatLng(55, -8), new LatLng(59.5, -1.7));
    LatLng bunSite;
    boolean add = false;
    boolean trade = false;
    boolean update = false;
    boolean register = false;
    double newLat;
    double newLon;
    ImageButton addSite;
    Button gpsAdd;
    Button manualAdd;
    Button longLatAdd;
    Button btnDismiss;
    Cluster<AppClusterItem> clickedCluster;
    Cluster<AppClusterItem> previouslyClickedCluster = null;
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
    int clickedTwice = 0;
    FrameLayout layout_main;
    Intent intent;


    private final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(54.187, -9.61), new LatLng(62.814, 0.541));
    private final int MAX_ZOOM = 8;
    private float prevZoom = 6;
    private final int MIN_ZOOM = 7;
    Boolean API = false;
    LocationManager manager = null;

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
            update = extras.getBoolean("update");
            register = extras.getBoolean("new");

            System.out.println(newLat);
            System.out.println(newLon);
            System.out.println(add);

            bunSite = new LatLng(newLat, newLon);
        }

        // inflate and return the layout
        v = inflater.inflate(R.layout.fragment_maps, container,
                false);

        //check gps
        manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );

        layout_main = (FrameLayout) v.findViewById(R.id.mainrl);
        layout_main.getForeground().setAlpha(0);

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //initialize views
        addSite = (ImageButton)v.findViewById(R.id.fab);

        mMapView.getMapAsync(this);

        if(register){
            Boolean newCamper = false;
            String answer = AppController.getString(this.getActivity(),"newCamper");
            System.out.println("new camper answer: "+answer);
            if(answer.equals("2131755550")){
                newCamper = true;
                showcase(newCamper);
            } else {
                showcase(newCamper);
            }
        }

        return v;

    }

    public void showcase(Boolean newCamper){
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // This aligns button to the bottom left side of screen
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lps.setMargins(90,0,0,90);

        String addWildLocationString = "";

        if(newCamper){
            //Create better text for when a new camper installs the app, point them to additional info
            addWildLocationString = "I see that you have never been wild camping before...";
        } else {
            addWildLocationString = "This button allows you to add a wild location to the map.";
        }

        ShowcaseView sv = new ShowcaseView.Builder(getActivity())
                .setTarget(new ViewTarget(addSite))
                .setContentTitle("Adding Wild Locations")
                .setContentText(addWildLocationString)
                .blockAllTouches()
                .setStyle(R.style.CustomShowcaseTheme)
                .setShowcaseEventListener(new OnShowcaseEventListener() {
                    @Override
                    public void onShowcaseViewHide(ShowcaseView showcaseView) {
                        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        // This aligns button to the bottom left side of screen
                        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        lps.setMargins(90,0,0,90);

                        ShowcaseView sv2 = new ShowcaseView.Builder(getActivity())
                                .setTarget(new ViewTarget(R.id.spinner_nav, getActivity()))
                                .setContentTitle("Main Menu")
                                .setContentText("Other screens can be accessed via this menu.")
                                .blockAllTouches()
                                .setStyle(R.style.CustomShowcaseTheme)
                                .build();
                        sv2.setButtonPosition(lps);
                    }

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                    }

                    @Override
                    public void onShowcaseViewShow(ShowcaseView showcaseView) {

                    }

                    @Override
                    public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

                    }
                })
                .build();

        sv.setButtonPosition(lps);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.getUiSettings().setMapToolbarEnabled(false);

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

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

        } else if(update){
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(bunSite).zoom(10).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        } else {//center map on newly created site
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(SCOTLAND.getCenter()).zoom(prevZoom).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }

        //hide clusters if zoom too much
        googleMap.setOnCameraMoveStartedListener(new MyOnCameraMoveStartedListener(googleMap));
        googleMap.setOnCameraMoveListener(new MyOnCameraMoveListener(googleMap));
        googleMap.setOnCameraIdleListener(new MyOnCameraIdleListener(googleMap));

        // set listeners for buttons
        addSite.setClickable(true);
        addSite.setOnClickListener(new MyOnClickListener(googleMap));

        //add the unknown sites as cluster items
        setUpClustering(googleMap);

        //show initial popup if user is newCamper

    }

    class MyOnClickListener implements View.OnClickListener{

        GoogleMap googleMap;

        public MyOnClickListener(GoogleMap googleMap){
            this.googleMap = googleMap;
        }

        @Override
        public void onClick(final View view) {

            LayoutInflater layoutInflater = (LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View popupView = layoutInflater.inflate(R.layout.popup, null);
            final PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    AbsListView.LayoutParams.WRAP_CONTENT,
                    AbsListView.LayoutParams.WRAP_CONTENT);

            layout_main.getForeground().setAlpha(150);

            //initialize views
            btnDismiss = (Button)popupView.findViewById(R.id.cancelNewSite);
            gpsAdd = (Button)popupView.findViewById(R.id.gps);
            manualAdd = (Button)popupView.findViewById(R.id.manual);
            longLatAdd = (Button)popupView.findViewById(R.id.longlat);

            addSite.setClickable(false);
            //layout_main.setClickable(false);
            googleMap.getUiSettings().setAllGesturesEnabled(false);

            //set on click listeners
            btnDismiss.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    layout_main.getForeground().setAlpha(0);
                    addSite.setClickable(true);
                    googleMap.getUiSettings().setAllGesturesEnabled(true);
                }
            });



            gpsAdd.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {

                    double latitude = 0;
                    double longitude = 0;

                    //Intent gpsOptionsIntent = new Intent(
                    //      android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    //startActivity(gpsOptionsIntent);

                    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, final int id) {
                                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, final int id) {
                                        dialog.cancel();
                                        System.out.println("no");
                                    }
                                });
                        final AlertDialog alert = builder.create();
                        alert.show();
                    }

                    if(manager.isProviderEnabled( LocationManager.GPS_PROVIDER )) {

                        googleMap.getMyLocation();
                        latitude = googleMap.getMyLocation().getLatitude();
                        longitude = googleMap.getMyLocation().getLongitude();

                        // start new activity
                        Intent intent = new Intent(getActivity().getApplicationContext(), AddSite.class);
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("longitude", longitude);
                        getActivity().startActivity(intent);
                        layout_main.getForeground().setAlpha(0);
                    }
                }
            });

            manualAdd.setOnClickListener(new Button.OnClickListener() {


                @Override
                public void onClick(View v) {

                    googleMap.getUiSettings().setAllGesturesEnabled(true);
                    popupWindow.dismiss();
                    layout_main.getForeground().setAlpha(0);

                    Snackbar.make(view, "Touch point on map to add a marker!", Snackbar.LENGTH_INDEFINITE).show();

                    addSite.setVisibility(View.GONE);

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


    }

    class MyOnCameraMoveStartedListener implements GoogleMap.OnCameraMoveStartedListener{

        GoogleMap googleMap;

        public MyOnCameraMoveStartedListener(GoogleMap googleMap){
            this.googleMap = googleMap;
        }

        @Override
        public void onCameraMoveStarted(int reason) {

            if(reason == REASON_GESTURE) {
                prevZoom = googleMap.getCameraPosition().zoom;
                API = false;
            } else if (reason == REASON_API_ANIMATION){
                API = true;
            }
        }

    }

    class MyOnCameraMoveListener implements GoogleMap.OnCameraMoveListener{

        GoogleMap googleMap;

        public MyOnCameraMoveListener(GoogleMap googleMap){
            this.googleMap = googleMap;
        }

        @Override
        public void onCameraMove() {

            if(!API) {
                if (googleMap.getCameraPosition().zoom > MAX_ZOOM) {
                    //make clusters dissapear
                    mClusterManager.clearItems();
                    updateMap(mClusterManager, googleMap);

                }
            }else{

            }
        }
    }

    class MyOnCameraIdleListener implements GoogleMap.OnCameraIdleListener{

        GoogleMap googleMap;

        public MyOnCameraIdleListener(GoogleMap googleMap){
            this.googleMap = googleMap;
        }

        @Override
        public void onCameraIdle(){

            if(!API) {
                if (prevZoom > MAX_ZOOM && googleMap.getCameraPosition().zoom < MAX_ZOOM) {
                    //make clusters appear
                    addClusterMarkers(mClusterManager, googleMap);
                    updateMap(mClusterManager, googleMap);

                } else if (prevZoom < MAX_ZOOM && googleMap.getCameraPosition().zoom > MAX_ZOOM) {
                    //make clusters dissapear
                    mClusterManager.clearItems();
                    updateMap(mClusterManager, googleMap);

                } else if (prevZoom == googleMap.getCameraPosition().zoom){
                    // do nothing

                } else{
                    updateMap(mClusterManager, googleMap);

                }
            }else{
                //do nothing
            }

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


    //not used?
    /*private void addMarker(LatLng newSite){

        MarkerOptions marker = new MarkerOptions().position(newSite);

        googleMap.addMarker(marker);

    }*/

    private void setUpClustering(GoogleMap googleMap) {

        System.out.println("SET UP CLUSTERING");

        // Declare a variable for the cluster manager.
        mMarkerManager = new MarkerManager(googleMap);

        // Initialize the manager with the context and the map.
        mClusterManager = new ClusterManager<>(this.getActivity(), googleMap, mMarkerManager);

        //collKnown = mMarkerManager.newCollection();
        coll = mMarkerManager.newCollection();

        if(knownSiteSize > 0) {
            for (int i = 0; i < knownSiteSize; i++) {
                Site currentSite = knownSitesMap.get(i);
                coll.addMarker((new MarkerOptions().position(currentSite.getPosition()).icon(BitmapDescriptorFactory.fromResource(R.drawable.greenpin32)).title(currentSite.getTitle()).snippet(currentSite.getDescription())));
            }
        } else {
            //no known sites
        }

        System.out.println("owned sites map frag: " + ownedSiteSize);

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

                if (knownSiteSize > 0) {
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

                if (ownedSiteSize > 0) {
                    for (int i = 0; i < ownedSiteSize; i++) {
                        Site currentSite = ownedSitesMap.get(i);

                        if (marker.getPosition().equals(currentSite.getPosition())) {
                            //Intent intent = new Intent(getActivity().getApplicationContext(), OwnedSiteActivity.class);
                            new FetchSiteImages(getContext(), currentSite.getCid(), new AsyncResponse() {
                                @Override
                                public void processFinish(String output) {
                                    startActivity(intent);
                                }
                            }).execute();


                            intent = new Intent(getActivity().getApplicationContext(), OwnedSiteViewerActivity.class);
                            intent.putExtra("arrayPosition", i);
                            intent.putExtra("cid", currentSite.getCid());
                            intent.putExtra("prevState", 0);

                            //startActivity(intent);
                            break;
                        }
                    }
                } else {

                }
            }
        });

        googleMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());

        //install custom infoWindowAdapter as the adpater for one or both of the marker collections
        mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForClusters());

        // Add cluster items (markers) to the cluster manager.
        addClusterMarkers(mClusterManager, googleMap);

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

            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

            View view = getActivity().getLayoutInflater().inflate(R.layout.cluster_popup, null);

            if (clickedCluster != null) {
                for (AppClusterItem item : clickedCluster.getItems()) {
                    // Extract data from each item in the cluster as needed
                    System.out.println(item);
                }

            if(previouslyClickedCluster == clickedCluster) {
                if (inst.getOwnedSiteSize() > 0) {
                    previouslyClickedCluster = null;
                    ArrayList<LatLng> cluster = new ArrayList<>();
                    Intent intent = new Intent(getActivity().getApplicationContext(), TradeActivitySimple.class);

                    for (AppClusterItem item : clickedCluster.getItems()) {
                        // Extract data from each item in the cluster as needed
                        //use the position to pass through to the trade screen where the position can be used to find the campsite id and display other info without giving away the location
                        cluster.add(item.getPosition());
                        System.out.println("maps fragment" + item.getPosition());
                    }

                    intent.putParcelableArrayListExtra("cluster", cluster);

                    // Launching  main activity
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "You do not own a site to trade!", Toast.LENGTH_LONG).show();
                }
            }

                previouslyClickedCluster = clickedCluster;
            }

            int size = clickedCluster.getSize();



            TextView popup = (TextView)view.findViewById(R.id.popup_text);
            LinearLayout cluster_popup = (LinearLayout)view.findViewById(R.id.cluster_popup);

            cluster_popup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("hi");
                }
            });

            if(size != 1){
                popup.setText("There are " + size + " campsites in this area, click it again to trade one of your campsites for one of them.");

            } else {
                popup.setText("There is " + size + " campsite in this area, click it again to trade one of your campsites for it.");

            }

            return view;
        }
    }

    private void addClusterMarkers(ClusterManager<AppClusterItem> mClusterManager, GoogleMap googleMap) {

        //knownSite inst = new knownSite();
        SparseArray<Site> map = inst.getUnknownSitesMap();
        int size = inst.getUnknownSitesSize();

        System.out.println("add cluster markers");

        // Add cluster items
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

    private void updateMap(ClusterManager<AppClusterItem> mClusterManager, GoogleMap googleMap){
        mClusterManager.setRenderer(new CustomRenderer<AppClusterItem>(this.getActivity(), googleMap, mClusterManager));
        mClusterManager.setAlgorithm(new CustomAlgorithm<AppClusterItem>());

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





