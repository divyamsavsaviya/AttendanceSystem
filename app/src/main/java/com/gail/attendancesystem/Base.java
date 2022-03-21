package com.gail.attendancesystem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Base#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Base extends Fragment implements LocationListener, OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MapView mapView;
    ImageView image;
    Button cap;
    String Location;
    Uri imageUri;
    Bitmap bitmap;
    String current;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    WebView webView;
    String la="22.6072446",lo="72.8086444";

    public Base() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Base.
     */
    // TODO: Rename and change types and number of parameters
    public static Base newInstance(String param1, String param2) {
        Base fragment = new Base();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_base, container, false);
        mapView=v.findViewById(R.id.map);
        webView=v.findViewById(R.id.web);
        mapView.getMapAsync(Base.this);
        mapView.onCreate(savedInstanceState);
        webView.loadUrl("https://www.google.com/maps/search/?api=1&query="+la+","+lo);
        webView.getSettings().setJavaScriptEnabled(true);
        cap=v.findViewById(R.id.cap_button);
        image=v.findViewById(R.id.image);
        cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{
                            Manifest.permission.CAMERA
                    },100);
                }
                dispatchTakePictureIntent();
            }
        });
        return v;
    }

    private void dispatchTakePictureIntent() {
        String name="photo";
        File storage=getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File imageFile=File.createTempFile(name,".jpg",storage);
            current=imageFile.getAbsolutePath();
            imageUri= FileProvider.getUriForFile(getContext(),"com.rajaryan.analytica.fileprovider",imageFile);
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == -1) {
            // on below line we are getting
            // data from our bundles. .


            bitmap= BitmapFactory.decodeFile(current);

            // below line is to set the
            // image bitmap to our image.
            image.setImageBitmap(bitmap);


        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Location="Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude();
        la=String.valueOf(location.getLatitude());
        lo=String.valueOf(location.getLongitude());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        GoogleMap mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Double.parseDouble(la), Double.parseDouble(lo));
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}