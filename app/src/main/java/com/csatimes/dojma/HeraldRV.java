package com.csatimes.dojma;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import io.realm.RealmResults;

/**
 * Created by Vikramaditya Kukreja on 19-06-2016.
 */

public class HeraldRV extends RecyclerView.Adapter<HeraldRV.ViewHolder> {
    private Context context;
    private RealmResults<HeraldNewsItemFormat> results;
    private int pixels = DHC.dpToPx(25);

    public HeraldRV(Context context, RealmResults<HeraldNewsItemFormat> results) {
        this.context = context;
        this.results = results;
        Fresco.initialize(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View herald_card_view_format = inflater.inflate(R.layout.herald_card_view_format, parent, false);
        // Return a new holder instance
        return new ViewHolder(herald_card_view_format);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HeraldNewsItemFormat foo = results.get(position);
        holder.date.setText(results.get(position).getOriginalDate());
        holder.author.setText(results.get(position).getAuthor());
        holder.title.setText(results.get(position).getTitle());
        //  holder.imageView.setVisibility(View.INVISIBLE);
        //  holder.progressBar.setVisibility(View.VISIBLE);

        final int pos = position;

        holder.imageView.setImageURI(Uri.parse(results.get(position).getImageURL())
        );

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT <= 22) {
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
            return haveConnectedWifi || haveConnectedMobile;
        } else {

        }
        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public SimpleDraweeView imageView;
        public TextView title;
        public TextView author;
        public TextView date;
        //  public ProgressBar progressBar;

        public ViewHolder(final View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.herald_rv_item_image);
            author = (TextView) itemView.findViewById(R.id.herald_rv_item_author);
            date = (TextView) itemView.findViewById(R.id.herald_rv_item_date);
            title = (TextView) itemView.findViewById(R.id.herald_rv_item_title);
            //  progressBar = (ProgressBar) itemView.findViewById(R.id.loadingImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            {
                if (view.getId() == itemView.getId()) {
                    if (isOnline()) {
                        Intent openWebpage = new Intent(context, OpenWebpage.class);
                        openWebpage.putExtra("URL", results.get(getAdapterPosition()).getLink());
                        openWebpage.putExtra("TITLE", results.get(getAdapterPosition()).getTitle());
                        context.startActivity(openWebpage);
                    } else {
                        Snackbar.make(view, "Unable to connect to internet", Snackbar.LENGTH_LONG).show();
                    }
                }
            }

        }
    }
}
