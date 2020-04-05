package com.bitbay.mbart.bitbayapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bitbay.mbart.bitbayapp.activities.MainActivity;
import com.bitbay.mbart.bitbayapp.R;

import java.util.Locale;

/**
 * Created by mbart on 28.12.2017.
 */

public class CalculatorFragment extends Fragment {

    double investmentAmountValue;
    double purchaseRateValue;
    double salesRateValue;
    double profitValue;
    double feeValue = 0.43;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_calculator, container, false);
        final EditText investmentAmountEdit = rootView.findViewById(R.id.investment_amount_edit);
        final TextView investmentAmount = rootView.findViewById(R.id.investment_amount);
        final EditText purchaseRateEdit = rootView.findViewById(R.id.purchase_rate_edit);
        final TextView purchaseRate = rootView.findViewById(R.id.purchase_rate);
        final TextView calculatorInstructions = rootView.findViewById(R.id.calculator_instructions);
        final EditText salesRateEdit = rootView.findViewById(R.id.sales_rate_edit);
        TextView salesRate = rootView.findViewById(R.id.sales_rate);
        final EditText feeEdit = rootView.findViewById(R.id.fee_edit);
        TextView fee = rootView.findViewById(R.id.fee);
        final EditText profitEdit = rootView.findViewById(R.id.profit_edit);
        final TextView profit = rootView.findViewById(R.id.profit);

        feeEdit.setText(String.valueOf(feeValue));

        investmentAmountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(investmentAmountEdit.getText().toString())){
                    try {
                        investmentAmountValue = Double.parseDouble(investmentAmountEdit.getText().toString());
                    }catch (NumberFormatException e) {

                    }
                }else if (TextUtils.isEmpty(investmentAmountEdit.getText().toString())){
                    investmentAmountValue = 0;
                    profitEdit.setText("");
                    salesRateEdit.setText("");
                }
                if (investmentAmountValue > 0 && purchaseRateValue > 0 && feeValue > 0 && salesRateValue > 0) {
                    profitEdit.setText(String.valueOf(calcProfit(investmentAmountValue,purchaseRateValue,salesRateValue,feeValue)));
                } else if (investmentAmountValue > 0 && purchaseRateValue > 0 && feeValue > 0 && profitValue > 0) {
                    salesRateEdit.setText(String.valueOf(calcSalesToProfit(profitValue,purchaseRateValue,investmentAmountValue,feeValue)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        purchaseRateEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(purchaseRateEdit.getText().toString())) {
                    try {
                        purchaseRateValue = Double.parseDouble(purchaseRateEdit.getText().toString());
                    }catch (NumberFormatException e) {

                    }
                }else if (TextUtils.isEmpty(purchaseRateEdit.getText().toString())){
                    purchaseRateValue = 0;
                    profitEdit.setText("");
                    salesRateEdit.setText("");
                }
                if (investmentAmountValue > 0 && purchaseRateValue > 0 && feeValue > 0 && salesRateValue > 0) {
                    profitEdit.setText(String.valueOf(calcProfit(investmentAmountValue,purchaseRateValue,salesRateValue,feeValue)));
                } else if (investmentAmountValue > 0 && purchaseRateValue > 0 && feeValue > 0 && profitValue > 0) {
                    salesRateEdit.setText(String.valueOf(calcSalesToProfit(profitValue,purchaseRateValue,investmentAmountValue,feeValue)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        salesRateEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(salesRateEdit.getText().toString()) && salesRateEdit.isEnabled()) {
                    try {
                        salesRateValue = Double.parseDouble(salesRateEdit.getText().toString());
                        profitEdit.setEnabled(false);
                    }catch (NumberFormatException e) {

                    }
                    if (investmentAmountValue > 0 && purchaseRateValue > 0 && feeValue > 0 && salesRateValue > 0) {
                        profitEdit.setText(String.valueOf(calcProfit(investmentAmountValue,purchaseRateValue,salesRateValue,feeValue)));
                    }
                } else if (TextUtils.isEmpty(salesRateEdit.getText().toString()) && salesRateEdit.isEnabled() && !profitEdit.isEnabled()) {
                    salesRateValue = 0;
                    profitEdit.setEnabled(true);
                    profitEdit.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        feeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(feeEdit.getText().toString())) {
                    try {
                        feeValue = Double.parseDouble(feeEdit.getText().toString());
                    }catch (NumberFormatException e) {

                    }
                } else if (TextUtils.isEmpty(feeEdit.getText().toString())){
                    feeValue = 0;
                    profitEdit.setText("");
                    salesRateEdit.setText("");
                }
                if (investmentAmountValue > 0 && purchaseRateValue > 0 && feeValue > 0 && salesRateValue > 0) {
                    profitEdit.setText(String.valueOf(calcProfit(investmentAmountValue,purchaseRateValue,salesRateValue,feeValue)));
                } else if (investmentAmountValue > 0 && purchaseRateValue > 0 && feeValue > 0 && investmentAmountValue > 0) {
                    salesRateEdit.setText(String.valueOf(calcSalesToProfit(profitValue,purchaseRateValue,investmentAmountValue,feeValue)));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        profitEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(profitEdit.getText().toString()) && profitEdit.isEnabled()) {
                    try {
                        profitValue = Double.parseDouble(profitEdit.getText().toString());
                        salesRateEdit.setEnabled(false);
                    }catch (NumberFormatException e) {

                    }////
                    if (investmentAmountValue > 0 && purchaseRateValue > 0 && feeValue > 0 && investmentAmountValue > 0) {
                        salesRateEdit.setText(String.valueOf(calcSalesToProfit(profitValue,purchaseRateValue,investmentAmountValue,feeValue)));
                    }
                } else if (TextUtils.isEmpty(profitEdit.getText().toString()) && profitEdit.isEnabled() && !salesRateEdit.isEnabled()) {
                    profitValue = 0;
                    salesRateEdit.setEnabled(true);
                    salesRateEdit.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return rootView;
    }

    private double calcProfit(double investmentAmount, double purchaseRate, double salesRate, double fee)
    {
        double bought = (investmentAmount/purchaseRate)-((investmentAmount/purchaseRate)*(fee/100));
        double sold = (bought*salesRate)-((bought*salesRate)*(fee/100));
        return Double.parseDouble(String.format(Locale.US,"%.2f",sold-investmentAmount));
    }

    private double calcSalesToProfit(double profitValue, double purchaseRate, double investmentAmount, double fee)
    {
        double bought = (investmentAmount/purchaseRate)-((investmentAmount/purchaseRate)*(fee/100));
        double profitInvest = profitValue+investmentAmount;
        double sold = (profitInvest/bought)+((profitInvest/bought)*(fee/100));
        return Double.parseDouble(String.format(Locale.US,"%.2f",sold));
    }

    @Override
    public void onResume() {
        Log.e("SELECTED", "onResume of CalculatorFragment");
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle(getContext().getString(R.string.menu_calculator));
    }
    @Override
    public void onPause() {
        Log.e("SELECTED", "onPause of CalculatorFragment");
        super.onPause();
    }
}
