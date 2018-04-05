package com.example.siddhant.cookbook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by siddhant on 3/22/18.
 */
public class RecipieListAdapter extends ArrayAdapter<Recipie> implements Filterable {
    private Context mContext;
    int mResource;

    CustomFilter filter;
    ArrayList<Recipie> recipies;
    ArrayList<Recipie> filterList;


    public RecipieListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Recipie> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        this.recipies = objects;
        this.filterList = objects;
    }

    @Override
    public int getCount() {
        return recipies.size();
    }

    @Override
    public Recipie getItem(int pos) {
        return recipies.get(pos);
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

    @Override
    public Filter getFilter() {
        if(filter == null) {
            filter = new CustomFilter();
        }
        return filter;
    }

    // Code referred from https://www.youtube.com/watch?v=cC5vz9vIGy8
    class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();
                ArrayList<Recipie> filters = new ArrayList<>();

                for(int i = 0; i< filterList.size(); i++) {
                    if(filterList.get(i).getTitle().toUpperCase().contains(constraint)) {
                        Recipie r = new Recipie(filterList.get(i).getId(), filterList.get(i).getTitle(), filterList.get(i).getDescription(), "", "", "");
                        filters.add(r);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            } else {
                results.count = filterList.size();
                results.values = filterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            recipies = (ArrayList<Recipie>) results.values;
            notifyDataSetChanged();
        }
    }
}
