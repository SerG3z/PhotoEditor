package com.example.serg3z.photoeditor.adapter;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.serg3z.photoeditor.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by serg3z on 11.05.16.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ListClickListener listClickListener;
    private List<Drawable> data = new ArrayList<>();

    public void setOnItemClickListener(ListClickListener listClickListener) {
        this.listClickListener = listClickListener;
    }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_view, parent, false);
        holder = new ViewHolder(view, listClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        new AsyncTask<ViewHolder, Void, Void>() {
            private ViewHolder holder1 = holder;
            private int progressStatus = 0;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                holder1.progressBar.setVisibility(View.VISIBLE);
                holder1.imageView.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(ViewHolder... params) {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    new Thread() {
                        public void run() {
                            holder1.progressBar.setProgress(progressStatus);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                holder1.imageView.setImageDrawable(data.get(position));
                holder1.progressBar.setVisibility(View.GONE);
                holder1.imageView.setVisibility(View.VISIBLE);
            }
        }.execute(holder);

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(Drawable drawable) {
        data.add(drawable);
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        data.remove(position);
        notifyDataSetChanged();
    }

    public Drawable getDataPosition(int position) {
        return data.get(position);
    }

    public interface ListClickListener {
        void onItemClick(int position, View v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ListClickListener listClickListener;
        private ImageView imageView;
        private ProgressBar progressBar;

        public ViewHolder(View itemView, ListClickListener listener) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_image_view);
            progressBar = (ProgressBar) itemView.findViewById(R.id.item_progressbar_result);
            listClickListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listClickListener.onItemClick(getPosition(), v);
        }
    }
}
