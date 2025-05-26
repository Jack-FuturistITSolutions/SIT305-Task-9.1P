package com.example.task91p;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.task91p.databinding.ActivityMapsBinding;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private ActivityMapsBinding binding;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Set the back button up with a listener that closes the activity on click, returning to MainActivity
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Handle system window insets for padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mapsLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMapVar) {

        // Declare the map variable
        googleMap = googleMapVar;

        // Load in the LostAndFoundDatabase and get all adverts from the database
        LostAndFoundDatabase database = new LostAndFoundDatabase(this);
        List<Advert> adverts = database.getAllAdverts();

        // Set the geocoder
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        // Iterate through each advert
        for (Advert advert : adverts) {

            // Get the location string from the advert in the adverts list
            String locationString = advert.getLocation();

            try {
                // Geocode the address and attempt to set a position on the map
                List<Address> addresses = geocoder.getFromLocationName(locationString, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    // Add a marker
                    googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(advert.getTitle())
                            .snippet(advert.getType() + " - " + advert.getDescription()));
                } else {
                    Log.w("Geocoding", "No coordinates found for: " + locationString);
                }
            } catch (IOException e) {
                Log.e("Geocoding", "Geocoding failed for: " + locationString, e);
            }
        }

        // Optionally zoom to a default area in Sydney
        LatLng defaultZoom = new LatLng(-33.8688, 151.2093); // Sydney
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultZoom, 5));
    }

}