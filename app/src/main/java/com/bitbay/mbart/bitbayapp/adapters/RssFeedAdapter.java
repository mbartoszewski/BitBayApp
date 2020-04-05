package com.bitbay.mbart.bitbayapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitbay.mbart.bitbayapp.R;
import com.bitbay.mbart.bitbayapp.models.RSSItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.bitbay.mbart.bitbayapp.adapters.MyOffersAdapter.getDate;

public class RssFeedAdapter extends RecyclerView.Adapter<RssFeedAdapter.ViewHolder>
{

    List<RSSItem> RSSData;
    Context context;
    onClickListenerInterface onClickListenerInterface;

    public RssFeedAdapter(List<RSSItem> RSSData, Context context, onClickListenerInterface onClickListenerInterface)
    {
        this.RSSData = RSSData;
        this.context = context;
        this.onClickListenerInterface = onClickListenerInterface;
    }

    public interface onClickListenerInterface
    {
        void rssOnClickListener(String url);
    }

    @NonNull
    @Override
    public RssFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.rss_feed_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RssFeedAdapter.ViewHolder holder, int position)
    {
        if (RSSData != null)
        {
            holder.newsTitleTextView.setText(RSSData.get(holder.getAdapterPosition()).getTitle());
            holder.newsPubDateTextView.setText(getDate(RSSData.get(holder.getAdapterPosition()).getPubDate(), "rss"));
            try
            {
                Picasso.get().load(context.getResources().getIdentifier(RSSData.get(holder.getAdapterPosition()).getSource(), "drawable", context.getPackageName())).into(holder.newsImageView);

            } catch (Exception e)
            {

            }

            holder.newsDescriptionTextView.setText(Html.fromHtml(RSSData.get(holder.getAdapterPosition()).getDescription()).toString());
            holder.rssSourceTextView.setText(RSSData.get(holder.getAdapterPosition()).getRssSourceUrl());
        }
    }

    @Override
    public int getItemCount()
    {
        return RSSData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView newsPubDateTextView, newsTitleTextView, rssSourceTextView, newsDescriptionTextView;
        ImageView newsImageView;
        ConstraintLayout rssFeedConstraint;

        public ViewHolder(View itemView)
        {
            super(itemView);
            newsDescriptionTextView = itemView.findViewById(R.id.news_description_text_view);
            newsPubDateTextView = itemView.findViewById(R.id.news_pub_date_text_view);
            rssSourceTextView = itemView.findViewById(R.id.rss_source_text_view);
            newsTitleTextView = itemView.findViewById(R.id.news_title_text_view);
            newsImageView = itemView.findViewById(R.id.rss_source_logo_image_view);
            rssFeedConstraint = itemView.findViewById(R.id.rss_feed_constraint);

            rssFeedConstraint.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onClickListenerInterface.rssOnClickListener(RSSData.get(getAdapterPosition()).getLink());
                }
            });
        }
    }
}
