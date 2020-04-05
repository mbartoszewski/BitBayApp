package com.bitbay.mbart.bitbayapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitbay.mbart.bitbayapp.R;
import com.bitbay.mbart.bitbayapp.models.orderbook.OrderbookItems;

import java.util.List;

import static com.bitbay.mbart.bitbayapp.activities.MainActivity.df2;
import static com.bitbay.mbart.bitbayapp.activities.MainActivity.df8;

public class OrderbookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ORDERBOOK_LIST = 0, VIEW_TYPE_ORDERBOOK_LIST_HEADER = 1;
    private Context context;
    private List<OrderbookItems> orderbookList;
    String fiatCurrency, cryptoCurrency, orderbookType;
    orderbookAdapter orderbookAdapter;

    public OrderbookAdapter(Context context, List<OrderbookItems> orderbookList, String fiatCurrency, String cryptoCurrency, String orderbookType, orderbookAdapter orderbookAdapter)
    {
        this.context = context;
        this.orderbookList = orderbookList;
        this.fiatCurrency = fiatCurrency;
        this.cryptoCurrency = cryptoCurrency;
        this.orderbookType = orderbookType;
        this.orderbookAdapter = orderbookAdapter;
    }

    public interface orderbookAdapter
    {
      void onOrderbookClicked(int position);
    }
    public void addItems(String orderbookType, List<OrderbookItems> orderbookItems)
    {
        this.orderbookType = orderbookType;
        orderbookList.addAll(orderbookItems);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if (viewType == VIEW_TYPE_ORDERBOOK_LIST)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.recycler_orderbook, parent, false);
            return new OrderbookViewHolder(v);
        }

        if (viewType == VIEW_TYPE_ORDERBOOK_LIST_HEADER)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.orderbook_header, parent, false);
            return new OrderbookHeaderViewHolder(v);
        }

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.orderbook_header, parent, false);
        return new OrderbookHeaderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        try{
            if(holder instanceof OrderbookViewHolder && orderbookList.size() != 0)
            {
                    switch (fiatCurrency)
                    {
                        case "PLN":
                            ((OrderbookViewHolder) holder).orderValue.setText(String.valueOf(df2.format(Double.parseDouble(orderbookList.get(position-1).getCa()) * Double.parseDouble(orderbookList.get(position-1).getRa()))) + " PLN");
                            break;
                        case "USD":
                            ((OrderbookViewHolder) holder).orderValue.setText(String.valueOf(df2.format(Double.parseDouble(orderbookList.get(position-1).getCa()) * Double.parseDouble(orderbookList.get(position-1).getRa()))) + " $");
                            break;
                        case "EUR":
                            ((OrderbookViewHolder) holder).orderValue.setText(String.valueOf(df2.format(Double.parseDouble(orderbookList.get(position-1).getCa()) * Double.parseDouble(orderbookList.get(position-1).getRa()))) + " €");
                            break;
                        case "BTC":
                            ((OrderbookViewHolder) holder).orderValue.setText(String.valueOf(df8.format(Double.parseDouble(orderbookList.get(position-1).getCa()) * Double.parseDouble(orderbookList.get(position-1).getRa()))) + " ฿");
                            break;
                    }

                    ((OrderbookViewHolder) holder).orderRate.setText(orderbookList.get(position-1).getRa());
                    ((OrderbookViewHolder) holder).orderAmount.setText(orderbookList.get(position-1).getCa());
                    ((OrderbookViewHolder) holder).orderRate.setTextColor(context.getResources().getColor(context.getResources().getIdentifier(orderbookType, "color", context.getPackageName())));
                    //((OrderbookViewHolder) holder).orderValue.setTextColor(context.getResources().getColor(context.getResources().getIdentifier(fiatCurrency.toLowerCase(), "color", context.getPackageName())));
                    //((OrderbookViewHolder) holder).orderAmount.setTextColor(context.getResources().getColor(context.getResources().getIdentifier(cryptoCurrency.toLowerCase(), "color", context.getPackageName())));
            }

            if(holder instanceof OrderbookHeaderViewHolder)
            {
                    switch (fiatCurrency)
                    {
                        case "PLN":
                            ((OrderbookHeaderViewHolder) holder).orderRateHeader.setText(context.getResources().getString(R.string.orderbook_header_rate) + " PLN");
                            break;
                        case "USD":
                            ((OrderbookHeaderViewHolder) holder).orderRateHeader.setText(context.getResources().getString(R.string.orderbook_header_rate) + " $");
                            break;
                        case "EUR":
                            ((OrderbookHeaderViewHolder) holder).orderRateHeader.setText(context.getResources().getString(R.string.orderbook_header_rate) + " €");
                            break;
                        case "BTC":
                            ((OrderbookHeaderViewHolder) holder).orderRateHeader.setText(context.getResources().getString(R.string.orderbook_header_rate) + " ฿");
                            break;
                    }
                    ((OrderbookHeaderViewHolder) holder).orderbookHeader.setTextColor(context.getResources().getColor(context.getResources().getIdentifier(orderbookType, "color", context.getPackageName())));
                    ((OrderbookHeaderViewHolder) holder).orderbookHeader.setText(context.getResources().getString(context.getResources().getIdentifier("orderbook_header_" + orderbookType, "string", context.getPackageName())));
                    ((OrderbookHeaderViewHolder) holder).orderAmountHeader.setText(context.getResources().getString(R.string.orderbook_header_amount) + " " + cryptoCurrency);
            }
        }
        catch (Exception e)
        {

        }

    }

    @Override
    public int getItemCount()
    {
    return orderbookList.size() + 1;
    }

    @Override
    public int getItemViewType(int position)
    {
        if(position == 0)
        {
           return VIEW_TYPE_ORDERBOOK_LIST_HEADER;
        }
        if(position - 1 < orderbookList.size())
        {
           return VIEW_TYPE_ORDERBOOK_LIST;
        }
        return -1;
    }

    public class OrderbookViewHolder extends RecyclerView.ViewHolder
    {
        TextView orderRate;
        TextView orderAmount;
        TextView orderValue;
        ImageView shoppingIcon;
        ConstraintLayout orderbookItemConstraint;
        public OrderbookViewHolder(View itemView)
        {
            super(itemView);
            orderRate = itemView.findViewById(R.id.order_rate_header_text);
            orderValue = itemView.findViewById(R.id.order_value_header_text);
            orderAmount = itemView.findViewById(R.id.order_amount_header_text);
            shoppingIcon = itemView.findViewById(R.id.shopping_icon);
            orderbookItemConstraint = itemView.findViewById(R.id.orderbook_item_constraint);

            orderbookItemConstraint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    orderbookAdapter.onOrderbookClicked(getAdapterPosition()-1);
                }
            });
        }
    }


    public class OrderbookHeaderViewHolder extends RecyclerView.ViewHolder
    {
        TextView orderRateHeader;
        TextView orderAmountHeader;
        TextView orderValueHeader;
        TextView orderbookHeader;
        ConstraintLayout orderbookHeaderConstrain;

        public OrderbookHeaderViewHolder(View itemView)
        {
            super(itemView);
            orderRateHeader = itemView.findViewById(R.id.order_rate_header_text);
            orderValueHeader = itemView.findViewById(R.id.order_value_header_text);
            orderAmountHeader = itemView.findViewById(R.id.order_amount_header_text);
            orderbookHeader = itemView.findViewById(R.id.orderbook_header_text);
            orderbookHeaderConstrain = itemView.findViewById(R.id.orderbook_header_constraint);
        }
    }
}
