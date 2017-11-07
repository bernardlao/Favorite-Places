package com.example.bernardlao.favoriteplaces;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Bernard Lao on 10/20/2017.
 */

public class CustomAdapter extends BaseAdapter {
    private DBUtils db;
    private Activity activity;
    private LayoutInflater inflater;
    List<Location> locations;

    public CustomAdapter(Activity a, List<Location> locs) {
        this.activity = a;
        this.locations = locs;
        this.db = new DBUtils(a.getApplicationContext());
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public Object getItem(int i) {
        return locations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return locations.get(i).getLocationID();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null) {
            view = inflater.inflate(R.layout.listitem, null);
        }

        TextView lblPlaceName = (TextView)view.findViewById(R.id.lblPlaceName);
        TextView lblAddress = (TextView)view.findViewById(R.id.lblAddress);
        TextView lblType = (TextView)view.findViewById(R.id.lblType);
        RatingBar rateRating = (RatingBar)view.findViewById(R.id.rateRating);
        Button btnViewMap = (Button)view.findViewById(R.id.btnViewMap);
        ImageButton btnDeleteLoc = (ImageButton)view.findViewById(R.id.btnDeleteLoc);

        Location l = locations.get(i);

        lblPlaceName.setText(String.format("Place Name : %s", l.getPlaceName()));
        lblAddress.setText(String.format("Address : %s", l.getAddressName()));
        lblType.setText(String.format("Type : %s", l.getType()));
        rateRating.setRating((float)l.getRate());
        final int index = i;
        btnViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,MapViewActivity.class);
                intent.putExtra("index",index);
                activity.startActivity(intent);
            }
        });
        btnDeleteLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder b = new AlertDialog.Builder(activity);
                b.setTitle("Are you sure you want to delete the selected item.");
                b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        long res = db.deleteLocation(locations.get(index).getLocationID());
                        if(res > -1){
                            locations.remove(index);
                            notifyDataSetChanged();
                            Toast.makeText(activity,"Deleted successfully.",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                b.show();
            }
        });

        return view;
    }
}
