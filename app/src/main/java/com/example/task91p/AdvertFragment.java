package com.example.task91p;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AdvertFragment extends DialogFragment {

    private static final String ARG_ID = "id";
    private static final String ARG_TYPE = "type";
    private static final String ARG_TITLE = "title";
    private static final String ARG_NAME = "name";
    private static final String ARG_PHONE = "phone";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_DATE = "date";
    private static final String ARG_LOCATION = "location";

    private int advertId;

    private TextView detailType, detailTitle, detailName, detailPhone, detailDescription, detailDate, detailLocation;
    private Button deleteButton;

    public interface DeleteAdvertListener {
        void onAdvertDelete(int advertId);
    }

    private DeleteAdvertListener listener;

    // Use individual arguments for each field
    public static AdvertFragment newInstance(Advert advert) {
        AdvertFragment fragment = new AdvertFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, advert.getId());
        args.putString(ARG_TITLE, advert.getTitle());
        args.putString(ARG_TYPE, advert.getType());
        args.putString(ARG_NAME, advert.getName());
        args.putString(ARG_PHONE, advert.getPhone());
        args.putString(ARG_DESCRIPTION, advert.getDescription());
        args.putString(ARG_DATE, advert.getDate());
        args.putString(ARG_LOCATION, advert.getLocation());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DeleteAdvertListener) {
            listener = (DeleteAdvertListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement DeleteAdvertListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.advert_fragment, container, false);

        // Get views
        detailType = view.findViewById(R.id.detailType);
        detailTitle = view.findViewById(R.id.detailTitle);
        detailName = view.findViewById(R.id.detailName);
        detailPhone = view.findViewById(R.id.detailPhone);
        detailDescription = view.findViewById(R.id.detailDescription);
        detailDate = view.findViewById(R.id.detailDate);
        detailLocation = view.findViewById(R.id.detailLocation);
        deleteButton = view.findViewById(R.id.deleteButton);

        // Populate views with arguments
        if (getArguments() != null) {
            advertId = getArguments().getInt(ARG_ID);
            detailTitle.setText("Title: " + getArguments().getString(ARG_TITLE));
            detailType.setText("Type: " + getArguments().getString(ARG_TYPE));
            detailName.setText("Name: " + getArguments().getString(ARG_NAME));
            detailPhone.setText("Phone: " + getArguments().getString(ARG_PHONE));
            detailDescription.setText("Description: " + getArguments().getString(ARG_DESCRIPTION));
            detailDate.setText("Date: " + getArguments().getString(ARG_DATE));
            detailLocation.setText("Location: " + getArguments().getString(ARG_LOCATION));
        }

        // Delete button logic
        deleteButton.setOnClickListener(v -> {
            listener.onAdvertDelete(advertId);
            Toast.makeText(getActivity(), "Advert removed successfully.", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
