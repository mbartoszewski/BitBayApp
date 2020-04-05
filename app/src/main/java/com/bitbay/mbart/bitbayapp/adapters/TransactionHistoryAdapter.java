package com.bitbay.mbart.bitbayapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitbay.mbart.bitbayapp.R;
import com.bitbay.mbart.bitbayapp.models.transactionHistory.Item;

import java.util.ArrayList;

import static com.bitbay.mbart.bitbayapp.adapters.MyOffersAdapter.calculateValue;
import static com.bitbay.mbart.bitbayapp.adapters.MyOffersAdapter.getDate;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder>
{

    private ArrayList<Item> transactionItemList;
    private Context context;

    public TransactionHistoryAdapter(Context context, ArrayList<Item> transactionItemList)
    {
        this.context = context;
        this.transactionItemList = transactionItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycler_history_transaction_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if (transactionItemList != null) {
            try {
                switch (transactionItemList.get(holder.getAdapterPosition()).getUserAction()) {
                    case "Sell":
                        holder.transactionType.setTextColor(context.getResources().getColor(R.color.sell));
                        break;
                    case "Buy":
                        holder.transactionType.setTextColor(context.getResources().getColor(R.color.buy));
                        break;
                }
                holder.transactionValue.setText(String.valueOf(calculateValue(transactionItemList.get(holder.getAdapterPosition()).getRate(), transactionItemList.get(holder.getAdapterPosition()).getAmount())));
                holder.transactionRate.setText(String.valueOf(transactionItemList.get(holder.getAdapterPosition()).getRate()));
                holder.transactionAmount.setText(String.valueOf(transactionItemList.get(holder.getAdapterPosition()).getAmount()));
                holder.transactionType.setText(transactionItemList.get(holder.getAdapterPosition()).getUserAction());
                holder.transactionDate.setText(getDate(transactionItemList.get(holder.getAdapterPosition()).getTime(), "transaction"));
                holder.marketCode.setText(transactionItemList.get(holder.getAdapterPosition()).getMarket());
            }
            catch (Exception e)
            {

            }
        }

    }

    @Override
    public int getItemCount()
    {
        return transactionItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView marketCode, transactionType, transactionAmount, transactionRate, transactionDate, transactionValue, noHistoryText;

        public ViewHolder(View itemView)
        {
            super(itemView);
            transactionValue = itemView.findViewById(R.id.transaction_value_text);
            marketCode = itemView.findViewById(R.id.market_code_text);
            transactionType = itemView.findViewById(R.id.transaction_type_text);
            transactionAmount = itemView.findViewById(R.id.transaction_amount_text);
            transactionRate = itemView.findViewById(R.id.transaction_rate_text);
            transactionDate = itemView.findViewById(R.id.transaction_date_text);
        }
    }
}
