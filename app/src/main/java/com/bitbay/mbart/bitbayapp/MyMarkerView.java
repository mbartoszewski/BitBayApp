package com.bitbay.mbart.bitbayapp;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.TextView;

import com.bitbay.mbart.bitbayapp.models.chart.ItemClass;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MyMarkerView extends MarkerView {

    private TextView dateTextView, openTextView, closeTextView, highTextView, lowTextView, volumeTextView;
    String date;
    HashMap<String, ItemClass> itemClassList;

    public MyMarkerView(Context context, int layoutResource, HashMap<String, ItemClass> itemClassList)
    {
        super(context, layoutResource);
        // find your layout components
        this.itemClassList = itemClassList;
        dateTextView = findViewById(R.id.date_text_view);
        openTextView = findViewById(R.id.open_text_view);
        closeTextView = findViewById(R.id.close_text_view);
        highTextView = findViewById(R.id.high_text_view);
        lowTextView = findViewById(R.id.low_text_view);
        volumeTextView = findViewById(R.id.volume_text_view);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        dateTextView.setText(getDate(((long) e.getX())));
        if (itemClassList.containsKey(String.valueOf(e.getX())))
        {
            closeTextView.setText("Close: " + itemClassList.get(String.valueOf(e.getX())).getC());
            openTextView.setText("Open: " + itemClassList.get(String.valueOf(e.getX())).getO());
            lowTextView.setText("Low: " + itemClassList.get(String.valueOf(e.getX())).getL());
            highTextView.setText("High: " + itemClassList.get(String.valueOf(e.getX())).getH());
            volumeTextView.setText("Volume: " + itemClassList.get(String.valueOf(e.getX())).getV());
        }
        else
        {
            Log.e("MYMARKERVIEW", "NIE DZIALA ");

        }
        // this will perform necessary layouting
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;
/*
    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), getHeight());
        }

        return mOffset;
    }
*/
    @Override
    public void draw(Canvas canvas, float posx, float posy)
    {
        posx = -(getWidth() - canvas.getWidth())/2;
        posy = -10f;
        canvas.translate(posx, posy);
        draw(canvas);
        canvas.translate(-posx, -posy);
    }
    private String getDate(Long timeStampStr)
    {
        try {
            DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            Date netDate = (new Date(timeStampStr));
            return sdf.format(netDate);
        } catch (Exception ignored) {
            return "Error programista do zwolnienia";
        }
    }
}
