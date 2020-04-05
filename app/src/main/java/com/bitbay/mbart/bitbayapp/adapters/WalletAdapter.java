package com.bitbay.mbart.bitbayapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bitbay.mbart.bitbayapp.R;
import com.bitbay.mbart.bitbayapp.models.wallet.Balance;

import java.util.List;
import java.util.Locale;

import static com.bitbay.mbart.bitbayapp.activities.MainActivity.df2;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder>
{
    private List<Balance> walletList;
    private String currencySign = " €";
    walletAdapterListener walletAdapterListener;
    RecyclerView rv;
    private Context context;
    private static final String TAG = WalletAdapter.class.getName();

    public WalletAdapter(Context context, List<Balance> walletList, RecyclerView rv, walletAdapterListener walletAdapterListener)
    {
        this.context = context;
        this.walletList = walletList;
        this.rv = rv;
        this.walletAdapterListener = walletAdapterListener;
    }

    public interface walletAdapterListener
    {
        void onDepositButtonClicked(String walletID, int position);
        void onWithdrawButtonClicked(String walletID, int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycler_wallet_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if (walletList.size() > 0)
        {
            try
            {
            switch (String.valueOf(Locale.getDefault())) {
                case "en_US":
                    currencySign = " $";
                    break;
                case "pl_PL":
                    currencySign = " PLN";
                    break;
                default:
                    currencySign = " €";
                    break;
            }
            holder.availableFunds.setText(String.valueOf(walletList.get(holder.getAdapterPosition()).getAvailableFunds()));
            holder.totalFunds.setText("= " + String.valueOf(walletList.get(holder.getAdapterPosition()).getTotalFunds()));
            holder.lockedFunds.setText(String.valueOf(walletList.get(holder.getAdapterPosition()).getLockedFunds()));
            holder.currencyCode.setText(walletList.get(holder.getAdapterPosition()).getCurrency());
            if (!walletList.get(holder.getAdapterPosition()).getCurrency().matches("PLN|EUR|USD")) {
                holder.fundsValue.setText(String.valueOf(df2.format(walletList.get(holder.getAdapterPosition()).getAvailableFunds() * walletList.get(holder.getAdapterPosition()).getRate())) + currencySign);

            } else {
                holder.fundsValue.setText(String.valueOf(df2.format(walletList.get(holder.getAdapterPosition()).getAvailableFunds() * walletList.get(holder.getAdapterPosition()).getRate())) + " " + walletList.get(holder.getAdapterPosition()).getCurrency());
            }

                holder.currencyCode.setTextColor(context.getResources().getColor(context.getResources().getIdentifier(walletList.get(holder.getAdapterPosition()).getCurrency().toLowerCase(), "color", context.getPackageName())));
                holder.availableFunds.setTextColor(context.getResources().getColor(context.getResources().getIdentifier(walletList.get(holder.getAdapterPosition()).getCurrency().toLowerCase(), "color", context.getPackageName())));
                holder.fundsValue.setTextColor(context.getResources().getColor(context.getResources().getIdentifier(walletList.get(holder.getAdapterPosition()).getCurrency().toLowerCase(), "color", context.getPackageName())));
            }
            catch (Exception e)
            {

            }

        }
    }

    @Override
    public int getItemCount() {
        return walletList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public View layout;
        // wallet List
        TextView availableFunds;
        TextView totalFunds;
        TextView lockedFunds;
        TextView currencyCode;
        TextView fundsValue;
        Button depositButton, withdrawButton;

        ViewHolder(final View v)
        {
            super(v);
            layout = v;
            availableFunds = itemView.findViewById(R.id.available_funds_text);
            totalFunds = itemView.findViewById(R.id.total_funds_text);
            lockedFunds = itemView.findViewById(R.id.locked_funds_text);
            currencyCode = itemView.findViewById(R.id.currency_doe_text);
            fundsValue = itemView.findViewById(R.id.funds_value_text);
            depositButton = itemView.findViewById(R.id.deposit_button);
            withdrawButton = itemView.findViewById(R.id.withdraw_button);

            depositButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (!walletList.get(getAdapterPosition()).getCurrency().matches("PLN|USD|EUR"))
                    {
                        walletAdapterListener.onDepositButtonClicked(walletList.get(getAdapterPosition()).getId(), getAdapterPosition());
                    }
                    else
                    {
                        Snackbar connectionError = Snackbar.make(layout, "SOON", Snackbar.LENGTH_SHORT);
                        connectionError.show();
                    }
                }
            });

            withdrawButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!walletList.get(getAdapterPosition()).getCurrency().matches("PLN|USD|EUR"))
                    {
                        walletAdapterListener.onWithdrawButtonClicked(walletList.get(getAdapterPosition()).getId(), getAdapterPosition());
                    }
                    else
                    {
                        Snackbar connectionError = Snackbar.make(layout, "SOON", Snackbar.LENGTH_SHORT);
                        connectionError.show();
                    }
                }
            });

        }
    }
}