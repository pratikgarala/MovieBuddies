package com.example.thatsmepratik.moviebuddies.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thatsmepratik.moviebuddies.R;
import com.example.thatsmepratik.moviebuddies.models.Event;

/**
 * Created by pratikgarala on 8/06/2016.
 */

public class EventViewHolder extends RecyclerView.ViewHolder {

    public TextView eventName;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView eventDesc;
    public TextView movieName;
    public TextView eventDate;
    public TextView eventTime;
    public TextView eventVenue;

    public EventViewHolder(View itemView) {
        super(itemView);

        eventName = (TextView) itemView.findViewById(R.id.eventName);
        authorView = (TextView) itemView.findViewById(R.id.event_author);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.event_num_stars);
        movieName = (TextView) itemView.findViewById(R.id.movieName);
        eventDate = (TextView) itemView.findViewById(R.id.eventDate);
        eventTime = (TextView) itemView.findViewById(R.id.eventTime);
        eventVenue = (TextView) itemView.findViewById(R.id.eventVenue);
        eventDesc = (TextView) itemView.findViewById(R.id.eventDesc);
    }

    public void bindToPost(Event event, View.OnClickListener starClickListener) {
        eventName.setText(event.eventName);
        authorView.setText(event.author);
        numStarsView.setText(String.valueOf(event.starCount));
        movieName.setText(event.movieName);
        eventDate.setText(event.eventDate);
        eventTime.setText(event.eventTime);
        eventVenue.setText(event.eventVenue);
        eventDesc.setText(event.eventDesc);

        starView.setOnClickListener(starClickListener);
    }
}
