package com.example.task91p;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdvertsListActivity extends AppCompatActivity implements AdvertFragment.DeleteAdvertListener {

    private RecyclerView recyclerView;
    private LostAndFoundDatabase database;
    private AdvertAdapter adapter;
    private List<Advert> advertList;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adverts_list);

        // Set the recycler as a vertical list
        recyclerView = findViewById(R.id.recyclerViewAdverts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialise database and load all saved adverts
        database = new LostAndFoundDatabase(this);
        loadAdverts();

        // Set the back button up with a listener that closes the activity on click, returning to MainActivity
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Handle system window insets for padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.adverts), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Handle loading of adverts from the database
    private void loadAdverts() {
        advertList = database.getAllAdverts();
        adapter = new AdvertAdapter(advertList);

        // Open the fragment upon advert item click in the recycler
        adapter.setOnItemClickListener(advert -> {
            AdvertFragment fragment = AdvertFragment.newInstance(advert);
            fragment.show(getSupportFragmentManager(), "AdvertDetails");
        });

        recyclerView.setAdapter(adapter);
    }

    // Delete the advert by the currently opened advert's (in the fragment) ID
    @Override
    public void onAdvertDelete(int advertId) {
        database.deleteAdvertById(advertId);
        loadAdverts();
    }
}
