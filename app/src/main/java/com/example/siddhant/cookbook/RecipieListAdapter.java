package com.example.siddhant.cookbook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by siddhant on 3/22/18.
 */
public class RecipieListAdapter extends ArrayAdapter<Recipie> {
    private Context mContext;
    int mResource;


    public RecipieListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Recipie> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Integer id = getItem(position).getId();
        String title = getItem(position).getTitle();
        String description = getItem(position).getDescription();

        Recipie recipie = new Recipie(id, title, description, "", "", "");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvtitle = (TextView) convertView.findViewById(R.id.title);
        TextView tvdescription = (TextView) convertView.findViewById(R.id.description);

        tvtitle.setText(title);
        tvdescription.setText(description);

        return convertView;
    }
}
