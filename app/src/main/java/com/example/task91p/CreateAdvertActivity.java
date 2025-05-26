package com.example.task91p;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.gms.common.api.Status;
import android.Manifest;

public class CreateAdvertActivity extends AppCompatActivity {

    RadioGroup radioGroupType;
    RadioButton radioLost, radioFound;
    EditText editName, editTitle, editPhone, editDescription, editDate, editLocation;
    Button buttonSaveAdvert, buttonGetCurrentLocation;
    ImageButton backButton;
    LostAndFoundDatabase database;
    LocationManager locationManager;
    Geocoder geocoder;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 123;


    // Launcher for the Places API selector activity
    private final ActivityResultLauncher<Intent> locationSelectLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                // Check if the user selected a place successfully
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Place place = Autocomplete.getPlaceFromIntent(result.getData());

                    // Build an address string from the address components
                    StringBuilder locationName = new StringBuilder();
                    if (place.getAddressComponents() != null) {
                        for (AddressComponent component : place.getAddressComponents().asList()) {
                            locationName.append(component.getShortName()).append(", ");
                        }

                        // Remove the trailing comma and space
                        if (locationName.length() > 2) {
                            locationName.setLength(locationName.length() - 2);
                        }
                    }

                    // Place the address string into the editLocation textview
                    editLocation.setText(locationName.toString());

                } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(result.getData());
                    Toast.makeText(this, "Error: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_advert);

        // Initialise locationManager and geocoder
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        geocoder = new Geocoder(this, Locale.getDefault());

        // Initialise database
        database = new LostAndFoundDatabase(this);

        // Initialise the Places API with the same Maps API Key
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCqXJuNVeKv6BfL2rr0ul0y15Jf4wTMyac", Locale.getDefault());
        }

        // Initialise views
        radioGroupType = findViewById(R.id.radioGroupType);
        radioLost = findViewById(R.id.radioLost);
        radioFound = findViewById(R.id.radioFound);
        editTitle = findViewById(R.id.editTitle);
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editDescription = findViewById(R.id.editDescription);
        editDate = findViewById(R.id.editDate);
        editLocation = findViewById(R.id.editLocation);
        buttonSaveAdvert = findViewById(R.id.buttonSaveAdvert);
        buttonGetCurrentLocation = findViewById(R.id.buttonGetCurrentLocation);

        // Open the location picker when the user clicks within the editLocation field
        editLocation.setFocusable(false);
        editLocation.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS_COMPONENTS, Place.Field.NAME);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(CreateAdvertActivity.this);
            locationSelectLauncher.launch(intent);
        });

        // Set the back button up with a listener that closes the activity on click, returning to MainActivity
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        buttonGetCurrentLocation.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                getCurrentLocation();
            }
        });

        // Collect inputs and handle logic for saving the advert
        buttonSaveAdvert.setOnClickListener(v -> {
            int selectedTypeId = radioGroupType.getCheckedRadioButtonId();
            String type = (selectedTypeId == R.id.radioLost) ? "Lost" : "Found";
            String title = editTitle.getText().toString().trim();
            String name = editName.getText().toString().trim();
            String phone = editPhone.getText().toString().trim();
            String description = editDescription.getText().toString().trim();
            String date = editDate.getText().toString().trim(); // Convert this to a Date Picker like in previous task for 9.1P
            String location = editLocation.getText().toString().trim();

            // Ensure all fields are filled
            if (name.isEmpty() || title.isEmpty() || selectedTypeId == -1 || description.isEmpty() || date.isEmpty() || location.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert the advert into the database, await confirmation for successful insert
            boolean success = database.insertAdvert(title, type, name, phone, description, date, location);

            // Handle insert outcome
            if (success) {
                Toast.makeText(this, "Advert saved successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to save advert.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle system window insets for padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.advert), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Get the permissions of the user to allow location services to be used
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Get current location of emulator device
    private void getCurrentLocation() {
        try {
            if (locationManager == null) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            }
            if (geocoder == null) {
                geocoder = new Geocoder(this, Locale.getDefault());
            }

            // Use GPS or Network as available
            String provider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    ? LocationManager.GPS_PROVIDER
                    : LocationManager.NETWORK_PROVIDER;

            // Get the current devices location and add the string into the editLocation textview
            locationManager.requestSingleUpdate(provider, location -> {
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        String fullAddress = addresses.get(0).getAddressLine(0);
                        editLocation.setText(fullAddress);
                    } else {
                        Toast.makeText(this, "Unable to resolve coordinates to address.", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(this, "Geocoding error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }, null);

        } catch (SecurityException e) {
            Toast.makeText(this, "Permission error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}