package com.example.android.booklisting;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;


public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView ratingView = (TextView) convertView.findViewById(R.id.rating);
        ratingView.setText(formatRating(currentBook.getRating()));
        GradientDrawable ratingCircle = (GradientDrawable) ratingView.getBackground();
        ratingCircle.setColor(getRatingColor(currentBook.getRating()));

        TextView authorView = (TextView) convertView.findViewById(R.id.author);
        authorView.setText(currentBook.getAuthors());

        TextView titleView = (TextView) convertView.findViewById(R.id.title);
        titleView.setText(currentBook.getTitle());

        TextView dateView = (TextView) convertView.findViewById(R.id.date);
        dateView.setText(currentBook.getDate());

        return convertView;
    }

    private String formatRating(double rating) {
        DecimalFormat ratingFormat = new DecimalFormat("0.0");
        return rating != 0 ? ratingFormat.format(rating) : "-";
    }

    private int getRatingColor(double rating) {
        int ratingColorResourceId;
        int ratingFloor = (int) Math.floor(rating);
        switch (ratingFloor) {
            case 0:
                ratingColorResourceId = R.color.rating0;
                break;
            case 1:
                ratingColorResourceId = R.color.rating1;
                break;
            case 2:
                ratingColorResourceId = R.color.rating2;
                break;
            case 3:
                ratingColorResourceId = R.color.rating3;
                break;
            case 4:
                ratingColorResourceId = R.color.rating4;
                break;
            default:
                ratingColorResourceId = R.color.rating5plus;
                break;
        }
        return ContextCompat.getColor(getContext(), ratingColorResourceId);
    }
}
