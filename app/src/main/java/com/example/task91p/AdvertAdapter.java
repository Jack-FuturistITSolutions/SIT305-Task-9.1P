package com.example.task91p;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.AdvertViewHolder> {

    private List<Advert> advertList;

    // Use the onItemClickListener from here: https://developer.android.com/reference/android/widget/AdapterView.OnItemClickListener
    // Handle all advert item clicks in the recycler
    public interface OnItemClickListener {
        void onItemClick(Advert advert);
    }
    private OnItemClickListener listener;

    // Constructor
    public AdvertAdapter(List<Advert> advertList) {
        this.advertList = advertList;
    }

    // Click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdvertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_advert, parent, false);
        return new AdvertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertViewHolder holder, int position) {
        Advert advert = advertList.get(position);

        // Bind data to the advertviewholder variables set below
        holder.textType.setText(advert.getTitle() + " | " + advert.getType());
        holder.textName.setText("Name: " + advert.getName());
        holder.textPhone.setText("Phone: " + advert.getPhone());
        holder.textDescription.setText("Description: " + advert.getDescription());
        holder.textDate.setText("Date: " + advert.getDate());
        holder.textLocation.setText("Location: " + advert.getLocation());

        // Bind the click listener to the advert item and open the fragment
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(advert);
            }
        });
    }

    @Override
    public int getItemCount() {
        return advertList.size();
    }

    // Compartmentalise the views into a singular holder object, reference in onBindViewHolder
    static class AdvertViewHolder extends RecyclerView.ViewHolder {
        TextView textType, textName, textPhone, textDescription, textDate, textLocation;

        // Bind the data from the views into the TextView variables
        public AdvertViewHolder(@NonNull View itemView) {
            super(itemView);
            textType = itemView.findViewById(R.id.textType);
            textName = itemView.findViewById(R.id.textName);
            textPhone = itemView.findViewById(R.id.textPhone);
            textDescription = itemView.findViewById(R.id.textDescription);
            textDate = itemView.findViewById(R.id.textDate);
            textLocation = itemView.findViewById(R.id.textLocation);
        }
    }
}
