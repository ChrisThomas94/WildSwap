package com.wildswap.wildswapapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.wildswap.wildswapapp.AsyncTask.AsyncResponse;
import com.wildswap.wildswapapp.AsyncTask.FetchSiteImages;
import com.wildswap.wildswapapp.Objects.AppClusterItem;
import com.wildswap.wildswapapp.Objects.Gallery;
import com.wildswap.wildswapapp.Objects.Site;
import com.wildswap.wildswapapp.Objects.StoredData;
import com.wildswap.wildswapapp.Objects.User;

public class MapsFragment extends MapFragment implements OnMapReadyCallback{

	public MapsFragment(){}

    MapView mMapView;

    LatLng userCountry;
    LatLng bunSite;
    double country_lon;
    double country_lat;
    float countryZoom;
    boolean add = false;
    boolean trade = false;
    boolean update = false;
    boolean register = false;
    boolean clickActive = false;
    boolean image = false;
    double newLat;
    double newLon;
    boolean filtersShowing = false;

    ImageButton addSite;
    Button gpsAdd;
    Button manualAdd;
    Button longLatAdd;
    Button btnDismiss;
    CheckBox owned;
    CheckBox known;
    CheckBox unknown;
    RelativeLayout ownedFilterLayout;
    RelativeLayout knownFilterLayout;
    RelativeLayout unknownFilterLayout;
    FrameLayout frame;
    ImageButton filter;
    RelativeLayout filterLayout;

    Marker ownedMarker;
    Marker knownMarker;

    Cluster<AppClusterItem> clickedCluster;
    Cluster<AppClusterItem> previouslyClickedCluster = null;
    User user;
    ClusterManager<AppClusterItem> mClusterManager;
    MarkerManager mMarkerManager;
    MarkerManager.Collection coll;
    MarkerManager.Collection collKnown;
    StoredData inst = new StoredData();
    boolean clicked;
    FrameLayout layout_main;
    Intent intent;
    Geocoder geocoder;
    Snackbar snackBar = null;

    private final int MAX_ZOOM = 8;
    private float prevZoom = 5;
    Boolean API = false;
    LocationManager manager = null;

    SparseArray<Site> knownSitesMap;
    int knownSiteSize;

    SparseArray<Site> ownedSitesMap;
    int ownedSiteSize;

    SparseArray<Site> unknownSitesMap;
    int unknownSiteSize;

    List<Marker> ownedMarkersList;
    BadgeManager bM;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        bM = new BadgeManager(getActivity());
        //.getAppli

        geocoder = new Geocoder(getActivity(), Locale.getDefault());


        ownedSitesMap = inst.getOwnedSitesMap();
        ownedSiteSize = inst.getOwnedSiteSize();

        knownSitesMap = inst.getKnownSitesMap();
        knownSiteSize = inst.getKnownSiteSize();

        unknownSitesMap = inst.getUnknownSitesMap();
        unknownSiteSize = inst.getUnknownSitesSize();

        user = inst.getLoggedInUser();
        String country = user.getCountry();

        String countryRes_lat = "@string/" + country + "_lat";
        String countryRes_lon = "@string/" + country + "_lon";
        String countryRes_zoom = "@string/" + country + "_zoom";

        if(getResources().getIdentifier(countryRes_lat, null, getActivity().getPackageName()) != 0){
            country_lat = Double.parseDouble(getResources().getString(getResources().getIdentifier(countryRes_lat, null, getActivity().getPackageName())));
        } else {
            country_lat = Double.parseDouble(getResources().getString(R.string.GB_lat));
        }

        if(getResources().getIdentifier(countryRes_lon, null, getActivity().getPackageName()) != 0){
            country_lon = Double.parseDouble(getResources().getString(getResources().getIdentifier(countryRes_lon, null, getActivity().getPackageName())));
        } else {
            country_lon = Double.parseDouble(getResources().getString(R.string.GB_lon));
        }

        if(getResources().getIdentifier(countryRes_zoom, null, getActivity().getPackageName()) != 0){
            countryZoom = Float.parseFloat(getResources().getString(getResources().getIdentifier(countryRes_zoom, null, getActivity().getPackageName())));
        } else {
            countryZoom = Float.parseFloat(getResources().getString(R.string.GB_zoom));
        }

        userCountry = new LatLng(country_lat, country_lon);

        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null)
        {
            newLat = extras.getDouble("latitude");
            newLon = extras.getDouble("longitude");
            add = extras.getBoolean("add");
            trade = extras.getBoolean("trade");
            update = extras.getBoolean("update");
            register = extras.getBoolean("new");
            image = extras.getBoolean("image");

            getActivity().getIntent().removeExtra("new");

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
        //layout_main.getForeground().setAlpha(0);

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the unknownSitesMap to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //initialize views
        addSite = (ImageButton)v.findViewById(R.id.fab);
        owned = (CheckBox) v.findViewById(R.id.ownedFilter);
        known = (CheckBox) v.findViewById(R.id.knownFilter);
        unknown = (CheckBox) v.findViewById(R.id.unkownFilter);
        ownedFilterLayout = (RelativeLayout) v.findViewById(R.id.ownedFilterLayout);
        knownFilterLayout = (RelativeLayout) v.findViewById(R.id.knownFilterLayout);
        unknownFilterLayout = (RelativeLayout) v.findViewById(R.id.unknownFilterLayout);
        frame = (FrameLayout) v.findViewById(R.id.buttonFrame);
        filter = (ImageButton) v.findViewById(R.id.filterIcon);
        filterLayout = (RelativeLayout) v.findViewById(R.id.filterLayout);

        mMapView.getMapAsync(this);

        if(register){
            bM.awardJoinBadge();
            showcase();
        }

        bM.checkTradeBadges();


        return v;

    }

    public void showcase(){
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // This aligns button to the bottom left side of screen
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lps.setMargins(90,0,0,90);

        ownedFilterLayout.setVisibility(View.INVISIBLE);
        knownFilterLayout.setVisibility(View.INVISIBLE);
        unknownFilterLayout.setVisibility(View.INVISIBLE);

        String addWildLocationString = "You can add a wild location making it available for trading by pressing this button.";

        /*if(newCamper){
            //Create better text for when a new camper installs the app, point them to additional info
            addWildLocationString = "I see that you have never been wild camping before...";
        } else {
            addWildLocationString = "You can add a wild location making it available for trading by pressing this button.";
        }*/

        ShowcaseView sv = new ShowcaseView.Builder(getActivity())
                .setTarget(new ViewTarget(addSite))
                .setContentTitle("Adding Wild Locations")
                .setContentText(getResources().getString(R.string.map_showcase_1))
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
                                .setTarget(new ViewTarget(mMapView))
                                .setContentTitle("Trading")
                                .setContentText(getResources().getString(R.string.map_showcase_2))
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
                                        ShowcaseView sv3 = new ShowcaseView.Builder(getActivity())
                                            .setTarget(new ViewTarget(R.id.spinner_nav, getActivity()))
                                            .setContentTitle("Main Menu")
                                            .setContentText(getResources().getString(R.string.map_showcase_3))
                                            .blockAllTouches()
                                            .setStyle(R.style.CustomShowcaseTheme2)
                                            .setShowcaseEventListener(new OnShowcaseEventListener() {
                                                @Override
                                                public void onShowcaseViewHide(ShowcaseView showcaseView) {
                                                    ownedFilterLayout.setVisibility(View.VISIBLE);
                                                    knownFilterLayout.setVisibility(View.VISIBLE);
                                                    unknownFilterLayout.setVisibility(View.VISIBLE);
                                                    register = false;
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
                                        sv3.setButtonPosition(lps);
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
    public void onMapReady(final GoogleMap googleMap) {

        googleMap.getUiSettings().setMapToolbarEnabled(false);

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){

            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        } else {
            //request permission?
        }

        if(add){
            //center unknownSitesMap on newly created site
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(bunSite).zoom(10).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    updateMap(mClusterManager, googleMap);

                }

                @Override
                public void onCancel() {

                }
            });


            bM.checkSiteBadges();
            bM.checkCountryBadges();

            if(image){
                System.out.println("award image badge");
                bM.awardImageBadge();
            }

            //getActivity().getIntent().removeExtra("add");
        } else if (trade){

            LatLng tradeSite = new LatLng(newLat, newLon);

            bM.checkTradeBadges();

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(tradeSite).zoom(7).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    updateMap(mClusterManager, googleMap);
                }

                @Override
                public void onCancel() {

                }
            });

        } else if(update){
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(bunSite).zoom(10).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    updateMap(mClusterManager, googleMap);
                }

                @Override
                public void onCancel() {

                }
            });
        } else {
            // center Map on Scotland

            //String country = AppController.getString(getContext(), "country");

            CameraPosition cameraPosition = new CameraPosition.Builder()

                    .target(userCountry).zoom(countryZoom).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    updateMap(mClusterManager, googleMap);
                }

                @Override
                public void onCancel() {

                }
            });
        }

        //hide clusters if zoom too much
        googleMap.setOnCameraMoveStartedListener(new MyOnCameraMoveStartedListener(googleMap));
        googleMap.setOnCameraMoveListener(new MyOnCameraMoveListener(googleMap));
        googleMap.setOnCameraIdleListener(new MyOnCameraIdleListener(googleMap));

        googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

        // set listeners for buttons
        addSite.setClickable(true);
        //addSite.setOnClickListener(new MyOnClickListener(googleMap));
        addSite.setOnClickListener(new noPopupClickListener(googleMap));

        owned.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    showOwnedSites();
                } else {
                    hideOwnedSites();
                }
            }
        });

        known.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    showKnownSites();
                } else {
                    hideKnownSites();
                }
            }
        });

        unknown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unknownSiteSize>0) {
                    if(unknown.isChecked()){
                        //make clusters appear
                        showUnknownSites(googleMap);
                    } else {
                        //make clusters dissapear
                        hideUnknownSites(googleMap);
                    }
                }
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                );

                int top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 10, getResources().getDisplayMetrics());

                if (filtersShowing) {

                    int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) -155, getResources().getDisplayMetrics());


                    params.setMargins(left, top, 0, 0);

                    filtersShowing = false;

                } else {

                    params.setMargins(0, top, 0, 0);
                    filtersShowing = true;
                }

                filterLayout.setLayoutParams(params);
            }
        });

        //add the unknown sites as cluster items
        setUpClustering(googleMap);
        updateMap(mClusterManager, googleMap);
    }

    public void showUnknownSites(GoogleMap googleMap){
        addClusterMarkers(mClusterManager, googleMap);
        updateMap(mClusterManager, googleMap);
    }

    public void hideUnknownSites(GoogleMap googleMap){
        mClusterManager.clearItems();
        updateMap(mClusterManager, googleMap);
    }

    public void showOwnedSites(){
        coll.clear();
        ownedMarkersList = new ArrayList<>();

        if(ownedSiteSize > 0){
            System.out.println("I have a owned site");
            for(int i = 0; i< ownedSiteSize; i++){

                Site currentSite = ownedSitesMap.get(i);
                System.out.println(i + "  " + currentSite.getTitle());

                ownedMarker = coll.addMarker(new MarkerOptions()
                        .position(currentSite.getPosition())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.redpin32))
                        .title(currentSite.getTitle())
                        .snippet(currentSite.getDescription()));

                ownedMarkersList.add(i, ownedMarker);
            }

            coll.setOnInfoWindowAdapter(new MyInfoWindowAdapter());

        } else {
            //no known sites
        }
    }

    public void hideOwnedSites(){
        coll.clear();
    }

    public void showKnownSites(){
        List<Marker> knownMarkersList = new ArrayList<>();

        if(knownSiteSize > 0) {
            System.out.println("I have an known site");
            for (int i = 0; i < knownSiteSize; i++) {
                Site currentSite = knownSitesMap.get(i);

                knownMarker = collKnown.addMarker((new MarkerOptions()
                        .position(currentSite.getPosition())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenpin32))
                        .title(currentSite.getTitle())
                        .snippet(currentSite.getDescription())));

                knownMarkersList.add(i, knownMarker);
            }

            collKnown.setOnInfoWindowAdapter(new MyInfoWindowAdapter());

        } else {
            //no known sites
        }
    }

    public void hideKnownSites(){
        collKnown.clear();
    }

    class noPopupClickListener implements View.OnClickListener{

        GoogleMap googleMap;

        public noPopupClickListener(GoogleMap googleMap){
            this.googleMap = googleMap;
        }

        @Override
        public void onClick(final View view) {

            if (clickActive) {

                clickActive = false;

                addSite.setRotation(90);
                frame.setPadding(0, 120, 0, 0);

                snackBar.dismiss();
                showUnknownSites(googleMap);
                showKnownSites();
                showOwnedSites();
                owned.setChecked(true);
                known.setChecked(true);

                googleMap.setOnMapClickListener(null);

            } else {

                clickActive = true;
                snackBar = Snackbar.make(view, "Touch a point on the map to add a marker!", Snackbar.LENGTH_INDEFINITE);

                frame.setPadding(0, 0, 0, 120);
                addSite.setRotation(45);

                googleMap.getUiSettings().setAllGesturesEnabled(true);
                //layout_main.getForeground().setAlpha(0);

                hideUnknownSites(googleMap);
                hideKnownSites();
                hideOwnedSites();
                owned.setChecked(false);
                known.setChecked(false);
                unknown.setChecked(false);

                snackBar.show();

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng point) {
                        clicked = true;
                        newLat = point.latitude;
                        newLon = point.longitude;

                        try {
                            List<Address> address = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                            System.out.println("address: " + address);

                            if (address.isEmpty()) {

                                Toast.makeText(getActivity(), "Unsuitable Location!", Toast.LENGTH_LONG).show();

                            } else {
                                Intent intent = new Intent(getActivity().getApplicationContext(), AddSiteActivity.class);
                                intent.putExtra("latitude", point.latitude);
                                intent.putExtra("longitude", point.longitude);
                                getActivity().startActivity(intent);
                            }

                        } catch (IOException e) {
                            Log.getStackTraceString(e);
                        }
                    }
                });
            }
        }

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
                        Intent intent = new Intent(getActivity().getApplicationContext(), AddSiteActivity.class);
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

                    hideUnknownSites(googleMap);
                    hideKnownSites();
                    hideOwnedSites();
                    owned.setChecked(false);
                    known.setChecked(false);
                    unknown.setChecked(false);

                    Snackbar.make(view, "Touch a point on the map to add a marker!", Snackbar.LENGTH_INDEFINITE).show();

                    addSite.setVisibility(View.GONE);

                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                        @Override
                        public void onMapClick(LatLng point) {
                            clicked = true;
                            newLat = point.latitude;
                            newLon = point.longitude;

                            try {
                                List<Address> address = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                                System.out.println("address: "+ address);

                                if(address.isEmpty()){

                                    Toast.makeText(getActivity(), "Unsuitable Location!", Toast.LENGTH_LONG).show();

                                } else {
                                    Intent intent = new Intent(getActivity().getApplicationContext(), AddSiteActivity.class);
                                    intent.putExtra("latitude", point.latitude);
                                    intent.putExtra("longitude", point.longitude);
                                    getActivity().startActivity(intent);
                                }

                            } catch (IOException e){
                                Log.getStackTraceString(e);
                            }
                        }
                    });
                }
            });

            longLatAdd.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity().getApplicationContext(),
                            AddSiteActivity.class);
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
            } else {

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

    private void setUpClustering(GoogleMap googleMap) {

        System.out.println("SET UP CLUSTERING");

        // Declare a variable for the cluster manager.
        mMarkerManager = new MarkerManager(googleMap);

        // Initialize the manager with the context and the unknownSitesMap.
        mClusterManager = new ClusterManager<>(this.getActivity(), googleMap, mMarkerManager);

        collKnown = mMarkerManager.newCollection();
        coll = mMarkerManager.newCollection();

        showKnownSites();
        showOwnedSites();

        // Point the unknownSitesMap's listeners at the listeners implemented by the cluster manager.
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
                            final Intent intent = new Intent(getActivity().getApplicationContext(), KnownSiteViewerActivity.class);
                            intent.putExtra("arrayPosition", i);
                            intent.putExtra("cid", currentSite.getCid());
                            intent.putExtra("prevState", 0);

                            SparseArray<Gallery> images = inst.getImages();
                            String cid = currentSite.getCid();
                            String id = cid.substring(cid.length()-8);
                            int cidEnd = Integer.parseInt(id);

                            images.get(cidEnd, null);

                            if(images.get(cidEnd) == null){
                                new FetchSiteImages(getActivity(), currentSite.getCid(), new AsyncResponse() {
                                    @Override
                                    public void processFinish(String output) {
                                        startActivity(intent);
                                    }
                                }).execute();

                            } else {
                                //no images previously fetched for this site
                                startActivity(intent);
                            }
                            break;
                        }
                    }
                }

                if (ownedSiteSize > 0) {
                    for (int i = 0; i < ownedSiteSize; i++) {
                        Site currentSite = ownedSitesMap.get(i);

                        if (marker.getPosition().equals(currentSite.getPosition())) {

                            intent = new Intent(getActivity().getApplicationContext(), OwnedSiteViewerActivity.class);
                            intent.putExtra("arrayPosition", i);
                            intent.putExtra("cid", currentSite.getCid());
                            intent.putExtra("prevState", 0);

                            SparseArray<Gallery> images = inst.getImages();
                            String cid = currentSite.getCid();
                            String id = cid.substring(cid.length()-8);
                            int cidEnd = Integer.parseInt(id);

                            images.get(cidEnd, null);

                            if(images.get(cidEnd) == null){
                                new FetchSiteImages(getActivity(), currentSite.getCid(), new AsyncResponse() {
                                    @Override
                                    public void processFinish(String output) {
                                        startActivity(intent);
                                    }
                                }).execute();

                            } else {
                                //no images previously fetched for this site
                                startActivity(intent);
                            }

                            break;
                        }
                    }
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

        mClusterManager.setOnClusterInfoWindowClickListener(new ClusterManager.OnClusterInfoWindowClickListener<AppClusterItem>() {
            @Override
            public void onClusterInfoWindowClick(Cluster<AppClusterItem> cluster) {
                System.out.println("click");
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
                    ArrayList<String> emails = new ArrayList<>();
                    SparseArray<Site> selectedUnknownSites = new SparseArray<>();

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

            if(size != 1){
                popup.setText("There are " + size + " campsites in this area, click it again to trade one of your campsites for one of them.");

            } else {
                popup.setText("There is " + size + " campsite in this area, click it again to trade one of your campsites for it.");

            }

            return view;
        }
    }

    private void addClusterMarkers(ClusterManager<AppClusterItem> mClusterManager, GoogleMap googleMap) {

        unknownSitesMap = inst.getUnknownSitesMap();
        unknownSiteSize = inst.getUnknownSitesSize();

        System.out.println("add cluster markers");

        // Add cluster items
        for (int i = 0; i < unknownSiteSize; i++) {
            Site currentSite = unknownSitesMap.get(i);

            LatLng point = currentSite.getPosition();
            double lon = point.longitude;
            double lat = point.latitude;

            AppClusterItem unknownSites = new AppClusterItem(lat, lon);

            mClusterManager.setRenderer(new CustomRenderer<AppClusterItem>(this.getActivity(), googleMap, mClusterManager));
            mClusterManager.setAlgorithm(new Alg<AppClusterItem>());
            mClusterManager.addItem(unknownSites);
        }
    }

    private void updateMap(ClusterManager<AppClusterItem> mClusterManager, GoogleMap googleMap){
        mClusterManager.setRenderer(new CustomRenderer<>(this.getActivity(), googleMap, mClusterManager));
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

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

        private View view;

        public MyInfoWindowAdapter() {
            view = getActivity().getLayoutInflater().inflate(R.layout.custom_infowindow, null);

            System.out.println("custom info window!");
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView tvTitle = ((TextView)view.findViewById(R.id.title));
            TextView tvSnippet = ((TextView)view.findViewById(R.id.snippet));

            if (knownSiteSize > 0) {
                for (int i = 0; i < knownSiteSize; i++) {
                    Site currentSite = knownSitesMap.get(i);

                    if (marker.getPosition().equals(currentSite.getPosition())) {

                        tvTitle.setText(currentSite.getTitle());
                        tvSnippet.setText(currentSite.getDescription());

                    }
                }
            }

            if (ownedSiteSize > 0) {
                for (int i = 0; i < ownedSiteSize; i++) {
                    Site currentSite = ownedSitesMap.get(i);

                    if (marker.getPosition().equals(currentSite.getPosition())) {

                        tvTitle.setText(currentSite.getTitle());
                        tvSnippet.setText(currentSite.getDescription());
                    }
                }
            }

            return view;
        }

        @Override
        public View getInfoWindow(final Marker marker) {



            return null;
        }

    }
}





