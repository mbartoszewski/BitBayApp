package com.bitbay.mbart.bitbayapp.adapters;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bitbay.mbart.bitbayapp.R;
import com.bitbay.mbart.bitbayapp.models.AllObjects;
import com.bitbay.mbart.bitbayapp.helpers.ItemTouchHelperAdapter;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExchangeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter
{
    private final int VIEW_TYPE_EXCHANGE = 0;
    private final int VIEW_TYPE_DONATE = 1;
    private final List<String> fiatCurrency;
    private final List<String> cryptoCurrency;
    private List<AllObjects> mData;
    private Context context;
    private ArrayAdapter<String> cryptoCurrencyAdapter;
    private ArrayAdapter<String> fiatCurrencyAdapter;
    private CurrencyAdapterListener onClickListener;
    private String chronometerFormat;
    private String currencySign;
    private FloatingActionButton mFab;
    private static final String TAG = ExchangeAdapter.class.getName();

    @Override
    public boolean onItemMove(int fromPosition, int toPosition)
    {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mData, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mData, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position)
    {
        if (position < mData.size())
        {
            mData.remove(position);
            notifyItemRemoved(position);
            if (!mFab.isShown())
            {
                mFab.show();
            }
        }
    }

    public interface CurrencyAdapterListener
    {
        void cardOnClick(View v, int position);
        void onCryptoSelected(String currency, int position);
        void onFiatSelected(String currency, int position);
        void donateOnClick();
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ExchangeAdapter(Context context, String chronometerFormat, FloatingActionButton fab, List<AllObjects> mData, List<String> fiatCurrency, List<String> cryptoCurrency, CurrencyAdapterListener adapterListener)
    {
        this.mData = mData;
        this.context = context;
        this.chronometerFormat = chronometerFormat;
        this.mFab = fab;
        this.fiatCurrency = fiatCurrency;
        this.cryptoCurrency = cryptoCurrency;
        this.onClickListener = adapterListener;
    }

    //Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater;
        View v;

        cryptoCurrencyAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, cryptoCurrency);
        fiatCurrencyAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,fiatCurrency);
        cryptoCurrencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fiatCurrencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        inflater = LayoutInflater.from(parent.getContext());
        v = inflater.inflate(R.layout.currency_card, parent, false);
        return new ExchangeTypeViewHolder(v);
        /*
        switch (viewType)
        {
            case VIEW_TYPE_EXCHANGE:
                cryptoCurrencyAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, cryptoCurrency);
                fiatCurrencyAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,fiatCurrency);
                cryptoCurrencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                fiatCurrencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                inflater = LayoutInflater.from(parent.getContext());
                v = inflater.inflate(R.layout.currency_card, parent, false);
                return new ExchangeTypeViewHolder(v);
            case VIEW_TYPE_DONATE:
                inflater = LayoutInflater.from(parent.getContext());
                v = inflater.inflate(R.layout.donate_card, parent, false);
                return new DonateTypeViewHolder(v);
                default:
                    cryptoCurrencyAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, cryptoCurrency);
                    fiatCurrencyAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,fiatCurrency);
                    cryptoCurrencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    fiatCurrencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    inflater = LayoutInflater.from(parent.getContext());
                    v = inflater.inflate(R.layout.currency_card, parent, false);
                    return new ExchangeTypeViewHolder(v);
        }
         */
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        switch (holder.getItemViewType())
        {
            case VIEW_TYPE_EXCHANGE:
                if (mData!= null)
                {
                    try
                    {
                        switch (mData.get(position).getFiatCurrency())
                        {
                            case "PLN":
                                currencySign = " PLN";
                                break;
                            case "USD":
                                currencySign = " $";
                                break;
                            case "EUR":
                                currencySign = " €";
                                break;
                            case "BTC":
                                currencySign = " ฿";
                                break;
                        }
                        if (Double.parseDouble(mData.get(position).getLast()) < Double.parseDouble(mData.get(position).getPreviousRate()))
                        {
                           ((ExchangeTypeViewHolder) holder).mTrendingUpDown.setImageResource(R.drawable.baseline_trending_down_24);
                           ((ExchangeTypeViewHolder) holder).mTrendingUpDown.setColorFilter(context.getResources().getColor(R.color.sell), android.graphics.PorterDuff.Mode.SRC_IN);
                        }
                        else  if (Double.parseDouble(mData.get(position).getLast()) > Double.parseDouble(mData.get(position).getPreviousRate()))
                        {
                            ((ExchangeTypeViewHolder) holder).mTrendingUpDown.setImageResource(R.drawable.baseline_trending_up_24);
                            ((ExchangeTypeViewHolder) holder).mTrendingUpDown.setColorFilter(context.getResources().getColor(R.color.buy), android.graphics.PorterDuff.Mode.SRC_IN);
                        }
                        else
                        {
                            ((ExchangeTypeViewHolder) holder).mTrendingUpDown.setImageResource(R.drawable.baseline_trending_flat_24);
                            ((ExchangeTypeViewHolder) holder).mTrendingUpDown.setColorFilter(context.getResources().getColor(R.color.buy), android.graphics.PorterDuff.Mode.SRC_IN);
                        }
                        ((ExchangeTypeViewHolder) holder).mParentConstraintLayout.setBackgroundResource(context.getResources().getIdentifier("card_border_" + mData.get(position).getCryptoCurrency().toLowerCase(), "drawable", context.getPackageName()));
                        ((ExchangeTypeViewHolder) holder).mCurrencyCurrent.setTextColor(context.getResources().getColor(context.getResources().getIdentifier(mData.get(position).getCryptoCurrency().toLowerCase(), "color", context.getPackageName())));

                        ((ExchangeTypeViewHolder) holder).mCurrencyCurrent.setText(mData.get(position).getLast() + currencySign);
                        ((ExchangeTypeViewHolder) holder).mCurrencyMax.setText(mData.get(position).getMax() + currencySign);
                        ((ExchangeTypeViewHolder) holder).mCurrencyMin.setText(mData.get(position).getMin() + currencySign);
                        ((ExchangeTypeViewHolder) holder).mCurrencyVolume.setText(mData.get(position).getVolume());

                        long currentTimeInSecond = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                        long timeAfterRefreshData = (currentTimeInSecond - mData.get(position).getTimestamp());
                        ((ExchangeTypeViewHolder) holder).mTimeAgo.setBase(SystemClock.elapsedRealtime() - timeAfterRefreshData*1000);
                        ((ExchangeTypeViewHolder) holder).mTimeAgo.setFormat(chronometerFormat);
                        ((ExchangeTypeViewHolder) holder).mTimeAgo.start();

                        ((ExchangeTypeViewHolder) holder).mCryptoCurrency.setAdapter(cryptoCurrencyAdapter);
                        ((ExchangeTypeViewHolder) holder).mFiatCurrency.setAdapter(fiatCurrencyAdapter);

                        ((ExchangeTypeViewHolder) holder).mCryptoCurrency.setSelection(cryptoCurrencyAdapter.getPosition(mData.get(position).getCryptoCurrency()));
                        ((ExchangeTypeViewHolder) holder).mFiatCurrency.setSelection(fiatCurrencyAdapter.getPosition(mData.get(position).getFiatCurrency()));
                    }
                    catch (Exception e)
                    {
                        //Log.d(TAG, "wyjebało błąd: " + e);
                    }
                }
                break;
            case VIEW_TYPE_DONATE:

                break;
        }

    }

    @Override
    public int getItemViewType(int position)
    {
        return VIEW_TYPE_EXCHANGE;
        /*
        if (position == mData.size())
        {
            return VIEW_TYPE_DONATE;
        }
        else
        {
            return VIEW_TYPE_EXCHANGE;
        }
        */
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    public class ExchangeTypeViewHolder extends RecyclerView.ViewHolder
    {
        CardView mCardView;
        TextView mCurrencyMin;
        TextView mCurrencyMax;
        TextView mCurrencyVolume;
        TextView mCurrencyCurrent;
        ImageView mTrendingUpDown;
        Spinner mCryptoCurrency;
        Spinner mFiatCurrency;
        Chronometer mTimeAgo;
        ConstraintLayout mDataConstraintLayout;
        ConstraintLayout mParentConstraintLayout;
        public View layout;

        ExchangeTypeViewHolder(final View v) {
            super(v);
            layout = v;
            mCardView = v.findViewById(R.id.card_view);
            mCurrencyCurrent = v.findViewById(R.id.currency_current);
            mCurrencyMax = v.findViewById(R.id.currency_max);
            mCurrencyMin = v.findViewById(R.id.currency_min);
            mCurrencyVolume = v.findViewById(R.id.currency_volume);
            mCryptoCurrency = v.findViewById(R.id.crypto_currency_spinner);
            mFiatCurrency = v.findViewById(R.id.fiat_currency_spinner);
            mTimeAgo = v.findViewById(R.id.currency_current_hint);
            mTrendingUpDown = v.findViewById(R.id.trending_image);
            mDataConstraintLayout = v.findViewById(R.id.data_constraint);
            mParentConstraintLayout = v.findViewById(R.id.parent_constraint);

            mDataConstraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    onClickListener.cardOnClick(v, getAdapterPosition());
                }
            });

            mCryptoCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    onClickListener.onCryptoSelected(cryptoCurrencyAdapter.getItem(position), getAdapterPosition());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mFiatCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    onClickListener.onFiatSelected(fiatCurrencyAdapter.getItem(position), getAdapterPosition());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public class DonateTypeViewHolder extends RecyclerView.ViewHolder
    {
        ImageButton payPalDonateButton;

        public DonateTypeViewHolder(View itemView)
        {
            super(itemView);
            payPalDonateButton = itemView.findViewById(R.id.paypal_donate_button);

            payPalDonateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.donateOnClick();
                }
            });
        }
    }
}