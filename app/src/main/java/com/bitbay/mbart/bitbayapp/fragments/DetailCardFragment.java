package com.bitbay.mbart.bitbayapp.fragments;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bitbay.mbart.bitbayapp.activities.MainActivity;
import com.bitbay.mbart.bitbayapp.MyMarkerView;
import com.bitbay.mbart.bitbayapp.adapters.OrderbookAdapter;
import com.bitbay.mbart.bitbayapp.R;
import com.bitbay.mbart.bitbayapp.interfaces.BitBayInterface3;
import com.bitbay.mbart.bitbayapp.models.chart.Chart;
import com.bitbay.mbart.bitbayapp.models.chart.ItemClass;
import com.bitbay.mbart.bitbayapp.models.marketConfiguration.MarketConfiguration;
import com.bitbay.mbart.bitbayapp.models.orderbook.OrderbookItems;
import com.bitbay.mbart.bitbayapp.models.orderbook.Orderbook;
import com.bitbay.mbart.bitbayapp.models.placeOrder.PlaceOrder;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.bitbay.mbart.bitbayapp.fragments.ExchangeFragment.createTransactionID;
import static com.bitbay.mbart.bitbayapp.fragments.ExchangeFragment.encode;
import static com.bitbay.mbart.bitbayapp.fragments.LoginFragment.hideSoftKeyboard;
import static com.bitbay.mbart.bitbayapp.fragments.LoginFragment.isConnected;
import static com.bitbay.mbart.bitbayapp.fragments.LoginFragment.privateKey;
import static com.bitbay.mbart.bitbayapp.fragments.LoginFragment.publicKey;
import static com.bitbay.mbart.bitbayapp.activities.MainActivity.df2;
import static com.bitbay.mbart.bitbayapp.activities.MainActivity.df8;

public class DetailCardFragment extends Fragment implements View.OnClickListener
{
    //VIEW
    Spinner chartTypeSpinner, chartResolutionSpinner;
    ConstraintLayout offerPlaceConstraint, chartConstraint;
    CheckBox offerPostOnly;
    TextView offerAmountYouGet, getOfferAmountYouGetHint, offerTypeBuyTitle;
    Button offerPlaceButton, buyOrderbookButton, sellOrderbookButton;
    TextInputLayout offerInputRate, offerInputAmount, offerInputValue;
    TextInputEditText offerEditRate, offerEditAmount, offerEditValue;
    LineChart lineChart;
    CandleStickChart candleStickChart;
    RecyclerView orderbookRecyclerView;
    Button offerTypeSellTitle;
    RecyclerView.LayoutManager orderbookLayoutManager;
    CoordinatorLayout detailConstraint;
    //BOTTOM SHEET
    ConstraintLayout bottomSheetConstraint;
    BottomSheetBehavior bottomSheetBehavior;
    //LOGIC
    ArrayList<String> chartTypList = new ArrayList<>();
    ArrayList<String> chartResolutionList;
    HashMap<String, String> chartResolutionHashMap = new HashMap<String, String>();
    private ArrayAdapter<String> chartTypAdapter;
    private ArrayAdapter<String> chartResolutionAdapter;
    String chartType = "Line";
    LineDataSet dataSet;
    HashMap<String, ItemClass> itemClassMap = new HashMap<String, ItemClass>();
    String cryptoCurrency, fiatCurrency, lastRate;
    String currencySign = "";
    private static final String TAG = DetailCardFragment.class.getName();
    ArrayList<Entry> chartData = new ArrayList<>();
    IMarker marker;
    LineData lineData = new LineData();
    CandleData candleData;
    ArrayList<CandleEntry> candleEntryArrayList = new ArrayList<>();
    List<OrderbookItems> orderbookList = new ArrayList<>();
    OrderbookAdapter orderbookAdapter;
    String offerType = "buy", offerTypeOrderbook = "buy";
    boolean offerTypePostOnly = false;
    String offerRateUserInputString = "\"rate\":0,";
    String offerAmountUserInputString = "\"amount\":0,";
    String offerValueUserInputString = "\"price\":0,";
    double offerRateUserInputDouble = 0;
    double offerAmountUserInputDouble = 0;
    double offerValueUserInputDouble = 0;
    double marketConfigurationBuyMaker = 0;
    //double marketConfigurationBuyTaker = 0;
    double marketConfigurationSellMaker = 0;
    //double marketConfigurationSellTaker = 0;
    int chartResolutionS = 1800;
    int daysAgo = 10;
    int primaryColor;
    BitBayInterface3 restApi3Service;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.detail_card_view, container, false);
        bottomSheetConstraint = rootView.findViewById(R.id.orderbook_bottom_sheet_constraint);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetConstraint);

        orderbookRecyclerView = rootView.findViewById(R.id.recycler_orderbook_bottom_sheet);
        buyOrderbookButton = rootView.findViewById(R.id.buy_orderbook_button);
        sellOrderbookButton = rootView.findViewById(R.id.sell_orderbook_button);
        lineChart = rootView.findViewById(R.id.lineChart);
        chartConstraint = rootView.findViewById(R.id.chart_constraint);
        chartResolutionSpinner = rootView.findViewById(R.id.chart_resolution_spinner);
        chartTypeSpinner = rootView.findViewById(R.id.chart_type_spinner);
        candleStickChart = rootView.findViewById(R.id.candleStickChart);
        offerPlaceConstraint = rootView.findViewById(R.id.offer_place_constraint);
        offerPostOnly = rootView.findViewById(R.id.offer_post_only);
        offerAmountYouGet = rootView.findViewById(R.id.offer_amount_you_get);
        getOfferAmountYouGetHint = rootView.findViewById(R.id.offer_amount_you_get_hint);
        offerPlaceButton = rootView.findViewById(R.id.offer_place_button);
        offerInputRate = rootView.findViewById(R.id.offer_input_rate);
        offerEditRate = rootView.findViewById(R.id.offer_edit_rate);
        offerInputAmount = rootView.findViewById(R.id.offer_input_amount);
        offerEditAmount = rootView.findViewById(R.id.offer_edit_amount);
        offerInputValue = rootView.findViewById(R.id.offer_input_value);
        offerEditValue = rootView.findViewById(R.id.offer_edit_value);
        offerTypeBuyTitle = rootView.findViewById(R.id.offer_type_buy_title);
        offerTypeSellTitle = rootView.findViewById(R.id.offer_type_sell_title);
        detailConstraint = rootView.findViewById(R.id.detail_fragment);

        restApi3Service = BitBayInterface3.retrofitAPI3.create(BitBayInterface3.class);
        offerTypeBuyTitle.setOnClickListener(this);
        offerTypeSellTitle.setOnClickListener(this);
        offerPostOnly.setOnClickListener(this);
        offerPlaceButton.setOnClickListener(this);
        buyOrderbookButton.setOnClickListener(this);
        sellOrderbookButton.setOnClickListener(this);
        orderbookRecycler(offerTypeOrderbook);

        if (isConnected)
        {
            offerPlaceConstraint.setVisibility(View.VISIBLE);
            getMarketConfiguration(publicKey, privateKey, cryptoCurrency, fiatCurrency);
        }
        else if (!isConnected)
        {
            offerPlaceConstraint.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) chartConstraint.getLayoutParams();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            chartConstraint.setLayoutParams(params);
        }

        offerInputRate.setHint((getActivity().getResources().getString(R.string.orderbook_header_rate) + currencySign));
        offerInputAmount.setHint((getActivity().getResources().getString(R.string.orderbook_header_amount) + " " + cryptoCurrency));
        offerInputValue.setHint((getActivity().getResources().getString(R.string.orderbook_header_value) + currencySign));

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        TypedArray arr =
                getActivity().obtainStyledAttributes(typedValue.data, new int[]{
                        android.R.attr.textColorPrimary});
        primaryColor = arr.getColor(0, -1);

        addChartSettings();

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
        {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState)
            {
                // Check Logs to see how bottom sheets behaves
                switch (newState)
                {
                    case BottomSheetBehavior.STATE_COLLAPSED:

                        Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Orderbook(cryptoCurrency, fiatCurrency, offerTypeOrderbook);
                        Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("Bottom Sheet Behaviour", "STATE_SETTLING");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset)
            {
            }
        });

        chartTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                chartType = chartTypAdapter.getItem(i);
                switch (chartTypAdapter.getItem(i))
                {
                    case "Line":
                        lineChart.setVisibility(View.VISIBLE);
                        candleStickChart.setVisibility(View.GONE);
                        break;
                    case "Candles":
                        lineChart.setVisibility(View.GONE);
                        candleStickChart.setVisibility(View.VISIBLE);
                        break;
                }
                getChart(cryptoCurrency, fiatCurrency, chartResolutionS, getCalculatedDate("dd-MM-yyyy HH:mm:ss", -8), chartType);
                Log.e("TYPE SPINNER", " SELECTED " + chartType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        chartResolutionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                //chartResolutionS = chartResolutionHashMap.get();
                Log.e("RESOLUTION SPINNER", " SELECTED ");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        getChart(cryptoCurrency, fiatCurrency, chartResolutionS, getCalculatedDate("dd-MM-yyyy HH:mm:ss", -daysAgo), chartType);

        offerEditRate.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (!TextUtils.isEmpty(offerEditRate.getText().toString()) && !offerEditRate.getText().toString().equals(".") && !offerEditRate.getText().toString().equals(","))
                {
                    offerRateUserInputString = "\"rate\":" + offerEditRate.getText().toString() + ",";
                    offerRateUserInputDouble = Double.parseDouble(offerEditRate.getText().toString());

                    if (offerRateUserInputDouble > 0 && offerAmountUserInputDouble > 0 && offerEditRate.isEnabled() && offerEditAmount.isEnabled())
                    {
                        offerEditValue.setEnabled(false);
                        offerEditValue.setText(String.valueOf(offerRateUserInputDouble * offerAmountUserInputDouble));
                    }
                    else if (offerRateUserInputDouble > 0 && offerValueUserInputDouble > 0 && offerEditRate.isEnabled() && offerEditValue.isEnabled())
                    {
                        offerEditAmount.setEnabled(false);
                        offerEditAmount.setText(String.valueOf(offerValueUserInputDouble / offerRateUserInputDouble));
                    }
                }
                else if (TextUtils.isEmpty(offerEditRate.getText().toString()) && offerEditRate.isEnabled() && !offerEditAmount.isEnabled())
                {
                    offerRateUserInputDouble = 0;
                    offerAmountUserInputDouble = 0;
                    offerEditAmount.setText("");
                    offerEditAmount.setEnabled(true);
                }
                else if (TextUtils.isEmpty(offerEditRate.getText().toString()) && offerEditRate.isEnabled() && !offerEditValue.isEnabled())
                {
                    offerRateUserInputDouble = 0;
                    offerValueUserInputDouble = 0;
                    offerEditValue.setText("");
                    offerEditValue.setEnabled(true);
                }
                else if (TextUtils.isEmpty(offerEditRate.getText().toString()))
                {
                    offerRateUserInputDouble = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
        offerEditAmount.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (!TextUtils.isEmpty(offerEditAmount.getText().toString()) && !offerEditAmount.getText().toString().equals(".") && !offerEditAmount.getText().toString().equals(","))
                {
                    offerAmountUserInputString = "\"amount\":" + String.valueOf(df8.format(Double.parseDouble(offerEditAmount.getText().toString()))) + ",";
                    offerAmountUserInputDouble = Double.parseDouble(offerEditAmount.getText().toString());

                    if (offerRateUserInputDouble > 0 && offerAmountUserInputDouble > 0 && offerEditAmount.isEnabled() && offerEditRate.isEnabled())
                    {
                        offerEditValue.setEnabled(false);
                        offerEditValue.setText(String.valueOf(offerRateUserInputDouble * offerAmountUserInputDouble));
                    }
                    else if (offerValueUserInputDouble > 0 && offerAmountUserInputDouble > 0 && offerEditAmount.isEnabled() && offerEditValue.isEnabled())
                    {
                        offerEditRate.setEnabled(false);
                        offerEditRate.setText(String.valueOf(offerValueUserInputDouble / offerAmountUserInputDouble));
                    }
                    switch (offerType)
                    {
                        case "buy":
                            offerAmountYouGet.setText(String.valueOf(df8.format(amountYouGetWithFee(Double.parseDouble(offerEditAmount.getText().toString()), marketConfigurationBuyMaker))) + " " + cryptoCurrency);
                            break;
                        case "sell":
                            //offerAmountYouGet.setText(String.valueOf(amountYouGetWithFee(Double.parseDouble(offerEditAmount.getText().toString()), marketConfigurationSellMaker))+ " " + cryptoCurrency);
                            break;
                    }
                }
                else if (TextUtils.isEmpty(offerEditAmount.getText().toString()) && offerEditAmount.isEnabled() && !offerEditRate.isEnabled())
                {
                    offerAmountUserInputDouble = 0;
                    offerRateUserInputDouble = 0;
                    offerEditRate.setText("");
                    offerEditRate.setEnabled(true);
                }
                else if (TextUtils.isEmpty(offerEditAmount.getText().toString()) && offerEditAmount.isEnabled() && !offerEditValue.isEnabled())
                {
                    offerAmountUserInputDouble = 0;
                    offerValueUserInputDouble = 0;
                    offerEditValue.setText("");
                    offerEditValue.setEnabled(true);
                }
                else if (!TextUtils.isEmpty(offerEditAmount.getText().toString()) && !offerEditAmount.isEnabled())
                {
                    switch (offerType)
                    {
                        case "buy":
                            offerAmountYouGet.setText(String.valueOf(df8.format(amountYouGetWithFee(Double.parseDouble(offerEditAmount.getText().toString()), marketConfigurationBuyMaker))) + " " + cryptoCurrency);
                            break;
                        case "sell":
                            //offerAmountYouGet.setText("0");
                            break;
                    }
                }
                else if (TextUtils.isEmpty(offerEditAmount.getText().toString()))
                {
                    offerAmountUserInputDouble = 0;
                    offerAmountYouGet.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
        offerEditValue.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (!TextUtils.isEmpty(offerEditValue.getText().toString()) && !offerEditValue.getText().toString().equals(".") && !offerEditValue.getText().toString().equals(","))
                {
                    offerValueUserInputString = "\"price\":" + String.valueOf(df2.format(Double.parseDouble(offerEditValue.getText().toString()))) + ",";
                    offerValueUserInputDouble = Double.parseDouble(offerEditValue.getText().toString());

                    if (offerRateUserInputDouble > 0 && offerValueUserInputDouble > 0 && offerEditValue.isEnabled() && offerEditRate.isEnabled())
                    {
                        offerEditAmount.setEnabled(false);
                        offerEditAmount.setText(String.valueOf(offerValueUserInputDouble / offerRateUserInputDouble));
                    }
                    else if (offerAmountUserInputDouble > 0 && offerValueUserInputDouble > 0 && offerEditValue.isEnabled() && offerEditAmount.isEnabled())
                    {
                        offerEditRate.setEnabled(false);
                        offerEditRate.setText(String.valueOf(offerValueUserInputDouble / offerAmountUserInputDouble));
                    }
                    switch (offerType)
                    {
                        case "sell":
                            offerAmountYouGet.setText(String.valueOf(df2.format(amountYouGetWithFee(Double.parseDouble(offerEditValue.getText().toString()), marketConfigurationSellMaker))) + " " + fiatCurrency);
                            break;
                        case "buy":
                            //offerAmountYouGet.setText(String.valueOf(amountYouGetWithFee(Double.parseDouble(offerEditAmount.getText().toString()), marketConfigurationSellMaker))+ " " + cryptoCurrency);
                            break;
                    }
                }
                else if (TextUtils.isEmpty(offerEditValue.getText().toString()) && offerEditValue.isEnabled() && !offerEditRate.isEnabled())
                {
                    offerValueUserInputDouble = 0;
                    offerRateUserInputDouble = 0;
                    offerEditRate.setText("");
                    offerEditRate.setEnabled(true);
                }
                else if (TextUtils.isEmpty(offerEditValue.getText().toString()) && offerEditValue.isEnabled() && !offerEditAmount.isEnabled())
                {
                    offerValueUserInputDouble = 0;
                    offerAmountUserInputDouble = 0;
                    offerEditAmount.setText("");
                    offerEditAmount.setEnabled(true);
                }
                else if (TextUtils.isEmpty(offerEditValue.getText().toString()))
                {
                    offerValueUserInputDouble = 0;
                    offerAmountYouGet.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        return rootView;
    }

    public void displayReceivedData(String fiatCurrencyData, String cryptoCurrencyData, String lastRateData)
    {
        this.cryptoCurrency = cryptoCurrencyData;
        this.fiatCurrency = fiatCurrencyData;
        this.lastRate = lastRateData;
        getCurrencySign(fiatCurrencyData);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.offer_type_buy_title:
                offerType = "buy";
                cleanAndEnableInput();
                offerAmountYouGet.setText("0");
                offerPlaceButton.setText(getActivity().getResources().getString(R.string.place_offer_button_buy));
                offerPlaceButton.setBackgroundColor(getActivity().getResources().getColor(R.color.buy));
                break;
            case R.id.offer_type_sell_title:
                offerType = "sell";
                cleanAndEnableInput();
                offerAmountYouGet.setText("0");
                offerPlaceButton.setText(getActivity().getResources().getString(R.string.place_offer_button_sell));
                offerPlaceButton.setBackgroundColor(getActivity().getResources().getColor(R.color.sell));
                break;
            case R.id.buy_orderbook_button:
                offerTypeOrderbook = "buy";
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                {
                    Orderbook(cryptoCurrency, fiatCurrency, offerTypeOrderbook);
                }
                else
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.sell_orderbook_button:
                offerTypeOrderbook = "sell";
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                {
                    Orderbook(cryptoCurrency, fiatCurrency, offerTypeOrderbook);
                }
                else
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.offer_post_only:
                if (offerPostOnly.isChecked())
                {
                    offerTypePostOnly = true;
                }
                else if (offerPostOnly.isChecked())
                {
                    offerTypePostOnly = false;
                }
                break;
            case R.id.offer_place_button:
                hideSoftKeyboard(getActivity());
                String s = "{" + offerAmountUserInputString + offerRateUserInputString + "\"offerType\":\"" + offerType + "\",\"mode\":\"limit\",\"postOnly\":" + offerTypePostOnly + ",\"fillOrKill\":false}";
                placeOrder(cryptoCurrency, fiatCurrency, publicKey, privateKey, s);
                break;
        }
    }

    public void orderbookRecycler(String typeOrderbook)
    {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                orderbookRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        orderbookAdapter = new OrderbookAdapter(getActivity(), orderbookList, fiatCurrency, cryptoCurrency, typeOrderbook, new OrderbookAdapter.orderbookAdapter()
        {
            @Override
            public void onOrderbookClicked(int position)
            {
                if (isConnected)
                {
                    switch (offerTypeOrderbook)
                    {
                        case "buy":
                            offerType = "sell";
                            offerPlaceButton.setText(getActivity().getResources().getString(R.string.place_offer_button_sell));
                            offerPlaceButton.setBackgroundColor(getActivity().getResources().getColor(R.color.sell));
                            break;
                        case "sell":
                            offerType = "buy";
                            offerPlaceButton.setText(getActivity().getResources().getString(R.string.place_offer_button_buy));
                            offerPlaceButton.setBackgroundColor(getActivity().getResources().getColor(R.color.buy));
                            break;
                    }
                    offerEditRate.setText(orderbookList.get(position).getRa());
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        orderbookLayoutManager = new LinearLayoutManager(getActivity());
        orderbookRecyclerView.setLayoutManager(orderbookLayoutManager);
        orderbookRecyclerView.addItemDecoration(dividerItemDecoration);
        orderbookRecyclerView.setHasFixedSize(true);
        orderbookRecyclerView.setAdapter(orderbookAdapter);
    }

    public void cleanAndEnableInput()
    {
        offerEditRate.setText("");
        offerEditAmount.setText("");
        offerEditValue.setText("");
        offerEditRate.setEnabled(true);
        offerEditAmount.setEnabled(true);
        offerEditValue.setEnabled(true);

    }

    public void getCurrencySign(String fiatCurrency)
    {
        switch (fiatCurrency)
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
    }

    @SuppressLint("CheckResult")
    private void getChart(String cryptoCurrency, String fiatCurrency, int resolution, long from, String chartType)
    {
        chartData.clear();
        Observable<Chart> chartObservable = restApi3Service.chartApi3(cryptoCurrency, fiatCurrency, resolution, from, System.currentTimeMillis());
        chartObservable
                .subscribeOn(Schedulers.io())
                .map(chart -> {
                    if (chart.getStatus().equals("Ok") && getActivity() != null)
                    {
                        for (Map.Entry<String, ItemClass> entry : chart.getItems().entrySet())
                        {
                            chartData.add(new Entry(Float.parseFloat(entry.getKey()), Float.parseFloat(entry.getValue().getC())));
                            candleEntryArrayList.add(new CandleEntry(Float.parseFloat(entry.getKey()), Float.parseFloat(entry.getValue().getH()), Float.parseFloat(entry.getValue().getL()), Float.parseFloat(entry.getValue().getO()), Float.parseFloat(entry.getValue().getC())));
                            itemClassMap.put(String.valueOf(Float.parseFloat(entry.getKey())), new ItemClass(String.valueOf(Float.parseFloat(entry.getKey())), entry.getValue().getO(), entry.getValue().getC(), entry.getValue().getH(), entry.getValue().getL(), entry.getValue().getV()));
                        }
                        return "true";
                    }
                    else
                    {

                        return chart.getError()[0];
                    }

                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(chart ->
                {
                    if (chart == "true")
                    {
                        marker = new MyMarkerView(getActivity(), R.layout.custom_marker_view, itemClassMap);

                        switch (chartType)
                        {
                            case "Line":
                                //lineChart
                                dataSet = new LineDataSet(chartData, cryptoCurrency + "/" + fiatCurrency);
                                lineData = new LineData(dataSet);

                                dataSet.setColor(getActivity().getResources().getColor(getResources().getIdentifier(cryptoCurrency.toLowerCase(), "color", getActivity().getPackageName())));
                                dataSet.setLineWidth(1.3f);
                                dataSet.setDrawValues(false);
                                dataSet.setDrawCircles(false);
                                lineChart.setData(lineData);

                                XAxis xAxis = lineChart.getXAxis();
                                YAxis yAxisRight = lineChart.getAxisRight();
                                YAxis yAxisLeft = lineChart.getAxisLeft();
                                lineChart.getLegend().setTextColor(Color.GRAY);
                                lineChart.setExtraOffsets(0, 35, 0, 0);
                                yAxisRight.setEnabled(false);
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                xAxis.setDrawGridLines(true);
                                xAxis.setTextSize(4);
                                xAxis.setTextColor(Color.GRAY);
                                yAxisLeft.setTextColor(Color.GRAY);
                                xAxis.setValueFormatter(new IAxisValueFormatter()
                                {
                                    @Override
                                    public String getFormattedValue(float value, AxisBase axis)
                                    {
                                        return getDate(new Float(value).longValue());
                                    }
                                });
                                lineChart.getDescription().setEnabled(false);
                                lineChart.setMarker(marker);
                                lineChart.setEnabled(false);
                                lineChart.setOnChartGestureListener(new OnChartGestureListener()
                                {
                                    @Override
                                    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture)
                                    {
                                        Log.e(TAG, " onChartGestureStart " + lastPerformedGesture);
                                    }

                                    @Override
                                    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture)
                                    {
                                        if (lineChart.getLowestVisibleX() == lineData.getXMin())
                                        {
                                            Log.e(TAG, " DOLADOWANIE CHARTA ");
                                        }
                                    }

                                    @Override
                                    public void onChartLongPressed(MotionEvent me)
                                    {
                                        Log.e(TAG, " onChartLongPressed " + me);
                                    }

                                    @Override
                                    public void onChartDoubleTapped(MotionEvent me)
                                    {
                                        Log.e(TAG, " onChartDoubleTapped " + me);
                                    }

                                    @Override
                                    public void onChartSingleTapped(MotionEvent me)
                                    {
                                        Log.e(TAG, " onChartSingleTapped " + me);
                                    }

                                    @Override
                                    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY)
                                    {
                                        Log.e(TAG, " onChartFling " + " FlingX " + velocityX + " FlingY " + velocityY);
                                    }

                                    @Override
                                    public void onChartScale(MotionEvent me, float scaleX, float scaleY)
                                    {
                                        Log.e(TAG, " onChartScale " + " ScaleX " + scaleX + " ScaleY " + scaleY);
                                    }

                                    @Override
                                    public void onChartTranslate(MotionEvent me, float dX, float dY)
                                    {
                                        //Log.e(TAG, " onChartTranslate " + " TranslateX " + dX + " TranslateY " + dY);
                                    }
                                });
                                lineChart.invalidate();
                                break;
                            case "Candles":
                                CandleDataSet candleDataSet = new CandleDataSet(candleEntryArrayList, cryptoCurrency + "/" + fiatCurrency);
                                candleData = new CandleData(candleDataSet);
                                candleData.setDrawValues(false);

                                candleDataSet.setColor(Color.rgb(80, 80, 80));
                                candleDataSet.setShadowColor(Color.DKGRAY);
                                candleDataSet.setShadowWidth(0.7f);
                                candleDataSet.setBarSpace(0.3f);
                                candleDataSet.setDecreasingColor(Color.RED);
                                candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
                                candleDataSet.setIncreasingColor(Color.rgb(122, 242, 84));
                                candleDataSet.setIncreasingPaintStyle(Paint.Style.STROKE);
                                candleDataSet.setNeutralColor(Color.BLUE);
                                candleDataSet.setValueTextColor(Color.RED);
                                XAxis xCandleAxis = candleStickChart.getXAxis();
                                YAxis yCandleAxisRight = candleStickChart.getAxisRight();
                                yCandleAxisRight.setEnabled(false);
                                xCandleAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                //xCandleAxis.setDrawGridLines(true);
                                xCandleAxis.setTextSize(4);
                                xCandleAxis.setTextColor(Color.GRAY);

                                xCandleAxis.setValueFormatter(new IAxisValueFormatter()
                                {
                                    @Override
                                    public String getFormattedValue(float value, AxisBase axis)
                                    {
                                        return getDate(Float.valueOf(value).longValue());
                                    }
                                });

                                candleStickChart.getLegend().setTextColor(Color.GRAY);
                                candleStickChart.setExtraOffsets(0, 35, 0, 0);
                                candleStickChart.getDescription().setEnabled(false);
                                candleStickChart.setMarker(marker);
                                //candleStickChart.setEnabled(false);
                                candleStickChart.setData(candleData);
                                candleStickChart.invalidate();
                                break;
                        }
                    }
                    else
                    {
                        Snackbar chartSnackbar = Snackbar.make(detailConstraint, chart, Snackbar.LENGTH_SHORT);
                        chartSnackbar.show();
                    }
                }, e ->
                {
                    Log.e(TAG, "Chart SERVICE " + e);

                });
    }

    @SuppressLint("CheckResult")
    private void Orderbook(String cryptoCurrency, String fiatCurrency, String offerTypeOrderbook)
    {
        Observable<Orderbook> orderbookObservable = restApi3Service.orderbookApi3(cryptoCurrency, fiatCurrency, 39);

        orderbookObservable
                .subscribeOn(Schedulers.io())
                .map(orderbook ->
                {
                    if (orderbook.getStatus().equals("Ok"))
                    {
                        switch (offerTypeOrderbook)
                        {
                            case "buy":
                                orderbookList.clear();
                                orderbookAdapter.addItems(offerTypeOrderbook, orderbook.getBuy());
                                break;
                            case "sell":
                                orderbookList.clear();
                                orderbookAdapter.addItems(offerTypeOrderbook, orderbook.getSell());
                                break;
                        }
                        return "true";
                    }
                    else
                    {
                        return orderbook.getError()[0];
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status ->
                {
                    if (status.equals("true"))
                    {
                        switch (offerTypeOrderbook)
                        {
                            case "buy":
                                sellOrderbookButton.setTextColor(primaryColor);
                                buyOrderbookButton.setTextColor(getContext().getResources().getColor(R.color.buy));
                                break;
                            case "sell":
                                buyOrderbookButton.setTextColor(primaryColor);
                                sellOrderbookButton.setTextColor(getContext().getResources().getColor(R.color.sell));
                                break;
                        }
                        orderbookAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Snackbar orderbookSnackbar = Snackbar.make(detailConstraint, status, Snackbar.LENGTH_SHORT);
                        orderbookSnackbar.show();
                    }
                }, e ->
                {
                    Log.e(TAG, "ORDERBOOK SERVICE " + e);

                });
    }

    @SuppressLint("CheckResult")
    private void getMarketConfiguration(String publicKey, String privateKey, String cryptoCurrency, String fiatCurrency)
    {
        String apiHash = null;
        String uid = null;
        try
        {
            apiHash = encode(privateKey, publicKey, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), "");
            uid = createTransactionID();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        Observable<MarketConfiguration> marketConfigurationObservable = restApi3Service.marketConfigurationApi3(publicKey, apiHash, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), uid, cryptoCurrency, fiatCurrency);
        marketConfigurationObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(marketConfiguration ->
                {
                    if (marketConfiguration.getStatus().equals("Ok"))
                    {
                        marketConfigurationBuyMaker = marketConfiguration.getConfig().getBuy().getCommissions().getMaker();
                        marketConfigurationBuyMaker = marketConfiguration.getConfig().getBuy().getCommissions().getMaker();
                        marketConfigurationSellMaker = marketConfiguration.getConfig().getSell().getCommissions().getMaker();
                        marketConfigurationSellMaker = marketConfiguration.getConfig().getSell().getCommissions().getMaker();
                    }
                    else if (marketConfiguration.getStatus().equals("Fail"))
                    {
                        Log.e(TAG, " maker buy " + marketConfiguration.getErrors()[0]);
                    }
                }, e ->
                {
                    Log.e(TAG, "MarketConfiguration SERVICE " + e);

                });
    }

    @SuppressLint("CheckResult")
    private void placeOrder(String cryptoCurrency, String fiatCurrency, String publicKey, String privateKey, String requestData)
    {
        String apiHash = null;
        String uid = null;
        JsonObject jsonObjectBodyParameter = new JsonObject();
        try
        {
            apiHash = encode(privateKey, publicKey, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), requestData);
            jsonObjectBodyParameter = (new JsonParser()).parse(requestData).getAsJsonObject();
            uid = createTransactionID();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        Observable<PlaceOrder> placeOrderObservable = restApi3Service.placeOrderApi3(cryptoCurrency, fiatCurrency, publicKey, apiHash, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), uid, jsonObjectBodyParameter);

        placeOrderObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(placeOrder ->
                {
                    if (placeOrder.getStatus().equals("Ok"))
                    {
                        Snackbar placeOfferSnackbar = Snackbar.make(detailConstraint, getActivity().getResources().getString(R.string.placeOfferStatus), Snackbar.LENGTH_SHORT);
                        placeOfferSnackbar.show();
                    }
                    else if (placeOrder.getStatus().equals("Fail"))
                    {
                        Snackbar placeOfferSnackbar = Snackbar.make(detailConstraint, placeOrder.getError()[0].toString(), Snackbar.LENGTH_SHORT);
                        placeOfferSnackbar.show();
                    }
                }, e ->
                {
                    Log.e(TAG, "PlaceOrder SERVICE " + e);

                });
    }

    private double amountYouGetWithFee(double currencyAmount, double fee)
    {
        return currencyAmount - (currencyAmount * fee);
    }

    private String getDate(Long timeStampStr)
    {
        try
        {
            DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Date netDate = (new Date(timeStampStr));
            return sdf.format(netDate);
        } catch (Exception ignored)
        {
            return "xx";
        }
    }

    public static long getCalculatedDate(String dateFormat, int days)
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return cal.getTimeInMillis();
    }

    private void addChartSettings()
    {
        chartResolutionHashMap.put("1 minute", "60");
        chartResolutionHashMap.put("3 minute", "180");
        chartResolutionHashMap.put("5 minute", "300");
        chartResolutionHashMap.put("15 minute", "900");
        chartResolutionHashMap.put("30 minute", "1800");
        chartResolutionHashMap.put("1 hour", "3600");
        chartResolutionHashMap.put("2 hour", "7200");
        chartResolutionHashMap.put("4 hour", "14400");
        chartResolutionHashMap.put("6 hour", "21600");
        chartResolutionHashMap.put("12 hour", "43200");
        chartResolutionHashMap.put("1 day", "86400");
        chartResolutionHashMap.put("3 day", "259200");
        chartResolutionHashMap.put("1 week", "604800");
        chartTypList.add("Line");
        chartTypList.add("Candles");
        chartResolutionList = new ArrayList<>(chartResolutionHashMap.keySet());
        Collections.sort(chartTypList);
        //Collections.sort(chartResolutionList);
        chartResolutionAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, chartResolutionList);
        chartTypAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, chartTypList);
        chartTypeSpinner.setAdapter(chartTypAdapter);
        chartResolutionSpinner.setAdapter(chartResolutionAdapter);

        chartTypeSpinner.setSelection(1);
        chartResolutionSpinner.setSelection(2);
    }

    @Override
    public void onPause()
    {
        Log.e("SELECTED", "OnPause of DetailCardViewFragment");
        super.onPause();
    }

    @Override
    public void onResume()
    {
        Log.e("SELECTED", "onResume of DetailCardViewFragment");
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle(getContext().getString(R.string.details_fragment_name) + " " + cryptoCurrency);
    }
}