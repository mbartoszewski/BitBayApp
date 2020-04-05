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
import com.bitbay.mbart.bitbayapp.models.youTubeChannelVideo.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

public class YouTubeChannelAdapter extends RecyclerView.Adapter<YouTubeChannelAdapter.ViewHolder>
{
    List<Item> YouTubeChannelAdapterData;
    Context context;
    onClickListenerInterface onClickListenerInterface;

    public YouTubeChannelAdapter(List<Item> YouTubeChannelAdapterData, Context context, onClickListenerInterface onClickListenerInterface) {
        this.YouTubeChannelAdapterData = YouTubeChannelAdapterData;
        this.context = context;
        this.onClickListenerInterface = onClickListenerInterface;
    }

    public interface onClickListenerInterface
    {
        void youTubeVideoOnClickListener(String url);
    }
    @NonNull
    @Override
    public YouTubeChannelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.rss_feed_item, parent, false);
        return new YouTubeChannelAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull YouTubeChannelAdapter.ViewHolder holder, int position) {
        if (YouTubeChannelAdapterData != null)
        {

            holder.newsTitleTextView.setText(YouTubeChannelAdapterData.get(holder.getAdapterPosition()).getSnippet().getTitle());
            holder.newsPubDateTextView.setVisibility(View.GONE);
            try
            {
                Picasso.get().load(YouTubeChannelAdapterData.get(holder.getAdapterPosition()).getSnippet().getThumbnails().getMedium().getURL()).into(holder.newsImageView);
            }catch (Exception e)
            {

            }

            holder.newsDescriptionTextView.setText(Html.fromHtml(YouTubeChannelAdapterData.get(holder.getAdapterPosition()).getSnippet().getDescription()).toString());
            holder.youTubeChannelSourceTextView.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {
        return YouTubeChannelAdapterData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView newsPubDateTextView, newsTitleTextView, youTubeChannelSourceTextView, newsDescriptionTextView;
        ImageView newsImageView;
        ConstraintLayout youTubeChannelConstraint;
        public ViewHolder(View itemView) {
            super(itemView);
            newsDescriptionTextView = itemView.findViewById(R.id.news_description_text_view);
            newsPubDateTextView = itemView.findViewById(R.id.news_pub_date_text_view);
            youTubeChannelSourceTextView = itemView.findViewById(R.id.rss_source_text_view);
            newsTitleTextView = itemView.findViewById(R.id.news_title_text_view);
            newsImageView = itemView.findViewById(R.id.rss_source_logo_image_view);
            youTubeChannelConstraint = itemView.findViewById(R.id.rss_feed_constraint);

            youTubeChannelConstraint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListenerInterface.youTubeVideoOnClickListener(YouTubeChannelAdapterData.get(getAdapterPosition()).getID().getVideoId());
                }
            });
        }
    }
}
