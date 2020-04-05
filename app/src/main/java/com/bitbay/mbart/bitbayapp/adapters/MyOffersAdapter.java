package com.bitbay.mbart.bitbayapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bitbay.mbart.bitbayapp.R;
import com.bitbay.mbart.bitbayapp.interfaces.BitBayInterface3;
import com.bitbay.mbart.bitbayapp.models.DeleteOrder;
import com.bitbay.mbart.bitbayapp.models.openItems.Item;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static android.support.constraint.Constraints.TAG;
import static com.bitbay.mbart.bitbayapp.fragments.ExchangeFragment.createTransactionID;
import static com.bitbay.mbart.bitbayapp.fragments.ExchangeFragment.encode;
import static com.bitbay.mbart.bitbayapp.fragments.LoginFragment.privateKey;
import static com.bitbay.mbart.bitbayapp.fragments.LoginFragment.publicKey;
import static java.math.RoundingMode.HALF_UP;

public class MyOffersAdapter extends RecyclerView.Adapter<MyOffersAdapter.ViewHolder>
{
    private Context context;
    private List<Item> openItemList;

    public MyOffersAdapter(Context context, List<Item> openItemList)
    {
        this.context = context;
        this.openItemList = openItemList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycler_open_item_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if (openItemList != null)
        {
            try
            {
            switch (openItemList.get(holder.getAdapterPosition()).getOfferType())
            {
                case "Sell":
                    holder.offerType.setTextColor(context.getResources().getColor(R.color.sell));
                    break;
                case "Buy":
                    holder.offerType.setTextColor(context.getResources().getColor(R.color.buy));
                    break;
            }

            BigDecimal currentPercentage = calculatePercentage(openItemList.get(holder.getAdapterPosition()).getStartAmount(),openItemList.get(holder.getAdapterPosition()).getCurrentAmount());
            holder.marketCode.setText(openItemList.get(holder.getAdapterPosition()).getMarket());
            holder.offerType.setText(openItemList.get(holder.getAdapterPosition()).getOfferType());
            holder.offerRate.setText(String.valueOf(openItemList.get(holder.getAdapterPosition()).getRate()));
            holder.currentAmount.setText(String.valueOf(openItemList.get(holder.getAdapterPosition()).getCurrentAmount()));
            holder.startAmount.setText(String.valueOf(openItemList.get(holder.getAdapterPosition()).getStartAmount()));
            holder.timeOfAdingOffer.setText(getDate(openItemList.get(holder.getAdapterPosition()).getTime(), "offers"));
            holder.currentOrderValue.setText(String.valueOf(calculateValue(openItemList.get(holder.getAdapterPosition()).getRate(), openItemList.get(holder.getAdapterPosition()).getCurrentAmount())) + " " + openItemList.get(holder.getAdapterPosition()).getMarket().substring(openItemList.get(holder.getAdapterPosition()).getMarket().lastIndexOf("-") +1));
            holder.startOrderValue.setText(String.valueOf(calculateValue(openItemList.get(holder.getAdapterPosition()).getRate(), openItemList.get(holder.getAdapterPosition()).getStartAmount())) + " " + openItemList.get(holder.getAdapterPosition()).getMarket().substring(openItemList.get(holder.getAdapterPosition()).getMarket().lastIndexOf("-") +1));
            holder.currentPercentageValue.setText(String.valueOf(currentPercentage) + " %");
            holder.progressBar.setProgress(currentPercentage.intValue());
            }
            catch (Exception e)
            {

            }
        }
    }


    @Override
    public int getItemCount() {
        return openItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        // openOffers
        TextView marketCode;
        TextView offerType;
        TextView currentAmount;
        TextView offerRate;
        TextView startAmount;
        TextView timeOfAdingOffer;
        TextView currentPercentageValue;
        TextView currentOrderValue;
        TextView startOrderValue;
        ImageButton cancelButton;
        TextView valueHint;
        ProgressBar progressBar;
        ConstraintLayout myOffersConstraint;
        public ViewHolder(View itemView) {
            super(itemView);
            marketCode = itemView.findViewById(R.id.market_code_text);
            offerType = itemView.findViewById(R.id.transaction_type_text);
            offerRate = itemView.findViewById(R.id.transaction_rate_text);
            currentAmount = itemView.findViewById(R.id.current_amount_text);
            startAmount = itemView.findViewById(R.id.transaction_amount_text);
            timeOfAdingOffer = itemView.findViewById(R.id.offer_added_date_text);
            currentPercentageValue = itemView.findViewById(R.id.current_percentage_value_text);
            currentOrderValue = itemView.findViewById(R.id.current_order_value_text);
            startOrderValue = itemView.findViewById(R.id.transaction_value_text);
            cancelButton = itemView.findViewById(R.id.delete_offer_image_button);
            valueHint = itemView.findViewById(R.id.value_hint_text);
            progressBar = itemView.findViewById(R.id.progressBar);
            myOffersConstraint = itemView.findViewById(R.id.fragment_my_offers_constraint);
            cancelButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.delete_offer_image_button:
                    deleteOrder(publicKey, privateKey, openItemList.get(getAdapterPosition()).getId(), getAdapterPosition(), myOffersConstraint);
                    break;
            }
        }
    }

        public static String getDate(String timeStampStr, String caseMethod)
        {
            DateFormat sdf;
            try {
                switch (caseMethod)
                {
                    case "rss":
                        sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
                        break;
                        default:
                            sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
                            break;
                }

                Date netDate = (new Date(Long.parseLong(timeStampStr)));
                return sdf.format(netDate);
            } catch (Exception ignored) {
                return "xx";
            }
        }

        private BigDecimal calculatePercentage(BigDecimal startAmount, BigDecimal currentAmount)
        {
            BigDecimal d = new BigDecimal(100);
            return  (currentAmount.divide(startAmount,2, HALF_UP)).multiply(d);
        }

        public static BigDecimal calculateValue(BigDecimal offerRate, BigDecimal offerValue)
        {
            return offerRate.multiply(offerValue).setScale(2, RoundingMode.HALF_UP);
        }

    @SuppressLint("CheckResult")
    private void deleteOrder(String publicKey, String privateKey, String orderId, int position, View snackBarViewHolder)
    {
        try {
            String apiHash = null;
            String uid = null;
            apiHash = encode(privateKey, publicKey, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), "");
            uid = createTransactionID();

            BitBayInterface3 deleteOrderService = BitBayInterface3.retrofitAPI3.create(BitBayInterface3.class);
            Observable<DeleteOrder> deleteOrderObservable = deleteOrderService.deleteOrderApi3(publicKey, apiHash, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), uid, orderId);
            deleteOrderObservable
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe(deleteOrder ->
                    {
                        if (deleteOrder.getStatus().equals("Ok"))
                        {
                            openItemList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, openItemList.size());
                            notifyDataSetChanged();
                        } else if (deleteOrder.getStatus().equals("Fail"))
                        {
                            Snackbar placeOfferSnackbar = Snackbar.make(snackBarViewHolder, deleteOrder.getErrors()[0], Snackbar.LENGTH_SHORT);
                            placeOfferSnackbar.show();
                        }
                    }, e ->
                    {
                        Log.e(TAG, "MarketConfiguration SERVICE " + e);

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
