package scot.wildcamping.wildscotland;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import android.widget.Toast;

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
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.geometry.Bounds;
import com.google.maps.android.geometry.Point;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import scot.wildcamping.wildscotland.AppClusterItem;

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
    LatLng newSite = null;
    boolean add;
    double newLat;
    double newLon;
    ImageButton addSite;
    Button gpsAdd;
    Button manualAdd;
    Button longLatAdd;
    Button btnDismiss;
    List<LatLng> knownSites = new ArrayList<LatLng>();
    int knownSitesSize;
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

        }else{//center map on newly created site
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(newSite).zoom(10).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }


        if(add == true){
            knownSites.add(newSite);
            addMarker(newSite);
        }

        //initialize views
        addSite = (ImageButton)v.findViewById(R.id.fab);

        // set listeners for buttons
        addSite.setOnClickListener(this);

        // GET sites
        new FetchKnownSites().execute();
        new FetchUnknownSites().execute();

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
            }
        });

        manualAdd.setOnClickListener(new Button.OnClickListener() {


            @Override
            public void onClick(View v) {

                popupWindow.dismiss();

                //TODO make clusters dissapera before listening for map click so user is not restricted by clusters

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

        // Initialize the manager with the context and the map.
        mClusterManager = new ClusterManager<AppClusterItem>(this.getActivity(), googleMap, mMarkerManager);

        // Point the map's listeners at the listeners implemented by the cluster manager.
        googleMap.setOnCameraChangeListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mMarkerManager);

        //GoogleMap.OnMarkerClickListener markerClickListener
        coll.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(getActivity().getApplicationContext(), KnownSiteActivity.class);
                startActivity(intent);
                return true;
            }
        });

        googleMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());

        //instal custom infoWindowAdapter as the adpater for one or both of the marker collections
        mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForClusters());

        // Add cluster items (markers) to the cluster manager.
        addClusterMarkers(mClusterManager);

        //mMarkerManager.onMarkerClick(knownSites.get(i));

        //coll.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener markerClickListener);

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<AppClusterItem>() {
            @Override
            public boolean onClusterClick(Cluster<AppClusterItem> cluster) {
                clickedCluster = cluster;
                return false;
            }
        });
    }

    class FetchKnownSites extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Fetching Your Sites ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            user = AppController.getString(getActivity(), "uid");

            // issue the post request
            try {
                String json = getKnownSites(user, relatOwn);
                System.out.println("json: " + json);
                String postResponse = doPostRequest(Appconfig.URL_REGISTER, json);      //json
                System.out.println("post response: " + postResponse);

                try {

                    JSONObject jObj = new JSONObject(postResponse);
                    Boolean error = jObj.getBoolean("error");
                    int size = jObj.getInt("size");

                    if(!error) {
                        for (int i = 0; i < size; i++) {
                            JSONObject site = jObj.getJSONObject("site" + i);
                            String longitude = site.getString("longitude");
                            String latitude = site.getString("latitude");
                            double lon = Double.parseDouble(longitude);
                            double lat = Double.parseDouble(latitude);
                            LatLng known = new LatLng(lat, lon);
                            knownSites.add(known);
                        }

                        knownSitesSize= knownSites.size();

                    } else {
                        //error message
                    }

                } catch (JSONException e){

                }

            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog and add markers
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();

            mMarkerManager = new MarkerManager(googleMap);
            coll = mMarkerManager.newCollection();

            if(knownSitesSize > 0) {
                //MarkerOptions mopt = new MarkerOptions();
                for (int i = 0; i < knownSitesSize; i++) {
                    //addMarker(knownSites.get(i));
                    //googleMap.addMarker(new MarkerOptions().position(knownSites.get(i)));
                    coll.addMarker(new MarkerOptions().position(knownSites.get(i)));
                }

            }
        }

        private String doGetRequest(String url)throws IOException{
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }

        private String doPostRequest(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            System.out.println("request: "+request);
            Response response = client.newCall(request).execute();
            return response.body().string();
        }

        private String getKnownSites(String uid, int relat) {
            return "{\"tag\":\"" + "knownSites" + "\","
                    + "\"uid\":\"" + uid + "\","
                    + "\"relat\":\"" + relat + "\"}";
        }
    }

    class FetchUnknownSites extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog2 = new ProgressDialog(getActivity());
            pDialog2.setMessage("Fetching Unknown Sites ...");
            pDialog2.setIndeterminate(false);
            pDialog2.setCancelable(true);
            pDialog2.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            user = AppController.getString(getActivity(), "uid");
            // issue the post request
            try {
                String json = getKnownSites(user, relatOwn, relatTrade);
                System.out.println("json: " + json);
                String postResponse = doPostRequest(Appconfig.URL_REGISTER, json);      //json
                System.out.println("post response: " + postResponse);

                try {

                    JSONObject jObj = new JSONObject(postResponse);
                    Boolean error = jObj.getBoolean("error");
                    int size = jObj.getInt("size");

                    if(!error) {
                        for (int i = 0; i < size; i++) {
                            JSONObject site = jObj.getJSONObject("site" + i);
                            String longitude = site.getString("longitude");
                            String latitude = site.getString("latitude");
                            double lon = Double.parseDouble(longitude);
                            double lat = Double.parseDouble(latitude);
                            LatLng unknown = new LatLng(lat, lon);
                            unknownSites.add(unknown);
                        }
                        unknownSitesSize= unknownSites.size();

                    } else {
                        //error message
                    }

                } catch (JSONException e){

                }

            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog2.dismiss();
            //add the unknown sites as cluster items
            setUpClustering();
        }

        private String doGetRequest(String url)throws IOException{
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }

        private String doPostRequest(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            System.out.println("request: "+request);
            Response response = client.newCall(request).execute();
            return response.body().string();
        }

        private String getKnownSites(String uid, int relatOwn, int relatTrade) {
            return "{\"tag\":\"" + "unknownSites" + "\","
                    + "\"uid\":\"" + uid + "\","
                    + "\"relatOwn\":\"" + relatOwn + "\","
                    + "\"relatTrade\":\"" + relatTrade + "\"}";
        }
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

    private void addClusterMarkers(ClusterManager<AppClusterItem> mClusterManager) {

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < unknownSitesSize; i++) {

            LatLng point = unknownSites.get(i);
            double lon = point.longitude;
            double lat = point.latitude;

            AppClusterItem unknownSitesList = new AppClusterItem(lat, lon);

            mClusterManager.setRenderer(new CustomRenderer<AppClusterItem>(this.getActivity(), googleMap, mClusterManager));
            mClusterManager.setAlgorithm(new CustomAlgorithm<AppClusterItem>());
            mClusterManager.addItem(unknownSitesList);
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

