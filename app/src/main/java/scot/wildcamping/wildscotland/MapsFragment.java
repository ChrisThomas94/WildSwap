package scot.wildcamping.wildscotland;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
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

        //center map on newly created site
        if(newSite != null){
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(newSite).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

        }
        else{// center map on Scotland
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(SCOTLAND.getCenter()).zoom(6).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }

        if(add == true){
            addMarker(newSite);
        }


        //initialize views
        addSite = (ImageButton)v.findViewById(R.id.fab);


        // set listeners for buttons
        addSite.setOnClickListener(this);

        // clustering
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

        LayoutInflater layoutInflater
                = (LayoutInflater)getActivity().getBaseContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                // start new activity
                Intent intent = new Intent(getActivity().getApplicationContext(),
                        AddSite.class);
                getActivity().startActivity(intent);
                //getActivity().finish();
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
                // TODO Auto-generated method stub
                // start new activity
                Intent intent = new Intent(getActivity().getApplicationContext(),
                        AddSite.class);
                getActivity().startActivity(intent);
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

        // Add cluster items (markers) to the cluster manager.
        addClusterMarkers(mClusterManager);
    }

    private void addClusterMarkers(ClusterManager<AppClusterItem> mClusterManager) {

        // Set some lat/lng coordinates to start with.
        double latitude = 56.797599;
        double longitude = -5.060633;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            latitude = latitude + offset;
            longitude = longitude + offset;
            AppClusterItem offsetItem = new AppClusterItem(latitude, longitude);
            mClusterManager.addItem(offsetItem);
        }
    }

    private void addNewClusterMarker(ClusterManager<AppClusterItem> mClusterManager) {
        AppClusterItem offsetItem = new AppClusterItem(newLat, newLon);
        mClusterManager.addItem(offsetItem);
    }

}
