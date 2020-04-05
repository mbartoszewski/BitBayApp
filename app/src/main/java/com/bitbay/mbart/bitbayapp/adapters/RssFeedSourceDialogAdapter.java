package com.bitbay.mbart.bitbayapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitbay.mbart.bitbayapp.R;
import com.bitbay.mbart.bitbayapp.models.RssSource;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RssFeedSourceDialogAdapter extends RecyclerView.Adapter<RssFeedSourceDialogAdapter.ViewHolder> {


    List<RssSource> rssSourceList;
    List<RssSource> subscribeRssSourceList;
    private RssSourceInterface rssSourceInterface;
    Context context;

    public RssFeedSourceDialogAdapter(List<RssSource> rssSourceList, List<RssSource> subscribeRssSourceList, Context context, RssSourceInterface rssSourceInterface) {
        this.rssSourceList = rssSourceList;
        this.subscribeRssSourceList = subscribeRssSourceList;
        this.context = context;
        this.rssSourceInterface = rssSourceInterface;
    }

    public interface RssSourceInterface
    {
        void checkBoxClick(CheckBox checkBox, int position);
    }
    @NonNull
    @Override
    public RssFeedSourceDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.rss_feed_source_dialog_item, parent, false);
        return new RssFeedSourceDialogAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RssFeedSourceDialogAdapter.ViewHolder holder, int position) {
        if (rssSourceList != null)
        {
            holder.rssUrlTextView.setText(rssSourceList.get(holder.getAdapterPosition()).getRssUrl());
            holder.rssSourceTextView.setText(rssSourceList.get(holder.getAdapterPosition()).getRssSource());
            Picasso.get().load(context.getResources().getIdentifier(rssSourceList.get(holder.getAdapterPosition()).getRssSource(), "drawable", context.getPackageName())).into(holder.rssSourceImageView);
            //holder.rssSourceImageView.setImageResource(context.getResources().getIdentifier(rssSourceList.get(holder.getAdapterPosition()).getRssSource(), "drawable", context.getPackageName()));
            if (rssSourceList.get(holder.getAdapterPosition()).getIssubscribe() == 1)
            {
                holder.rssSourceCheckBox.setChecked(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return rssSourceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView rssSourceImageView;
        TextView rssSourceTextView, rssUrlTextView;
        CheckBox rssSourceCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            rssSourceCheckBox = itemView.findViewById(R.id.rss_source_check_box);
            rssSourceImageView = itemView.findViewById(R.id.rss_source_image_view);
            rssSourceTextView = itemView.findViewById(R.id.rss_source_text_view);
            rssUrlTextView = itemView.findViewById(R.id.rss_url_text_view);

            rssSourceCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rssSourceInterface.checkBoxClick(rssSourceCheckBox ,getAdapterPosition());
                }
            });
        }
    }
}
