package com.bitbay.mbart.bitbayapp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bitbay.mbart.bitbayapp.models.AllObjects;
import com.bitbay.mbart.bitbayapp.models.RssSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mbart on 13.02.2018.
 */

public class AllDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 14;
    List<RssSource> rssSourceList = new ArrayList<>();
    private static final String DATABASE_NAME = "AllApi";

    private static final String TABLE_NAME = "alljson";
    private static final String TABLE_NAME_RSS = "rss";
    //private static final String TAG = AllDatabaseHandler.class.getName();

    private static String CRYPTO_CURRENCY = "crypto";
    private static String FIAT_CURRENCY = "fiat";
    private static String CURRENCY_AMOUNT = "amount";
    private static String RSS_SOURCE = "rssSource";
    private static String RSS_URL = "rssUrl";
    private static String RSS_LANG = "rssLang";
    private static String RSS_SUBSCRIBE = "rssSubscribe";

    public AllDatabaseHandler(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        if (rssSourceList.size() == 0)
        {
            addRssSourceList();
        }

        String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        CRYPTO_CURRENCY + " TEXT," +
                        FIAT_CURRENCY + " TEXT," +
                        CURRENCY_AMOUNT + " TEXT DEFAULT 0" + ")";
        db.execSQL(CREATE_TABLE);

        String CREATE_TABLE_RSS =
                "CREATE TABLE " + TABLE_NAME_RSS + "(" +
                        RSS_URL + " TEXT," +
                        RSS_SOURCE + " TEXT," +
                        RSS_LANG + " TEXT," +
                        RSS_SUBSCRIBE + " INTEGER DEFAULT 0" + ")";
        db.execSQL(CREATE_TABLE_RSS);

        addRssToDB(rssSourceList, db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            // case, to wersja z ktorej upgradujemy
            for (int i = oldVersion; i < newVersion; i++) {
                switch (i){
                    case 1:
                        //upgrade do 2
                        break;
                    case 2:
                        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_RSS);
                        this.onCreate(db);
                        break;
                    case 13:
                        rssSourceList.clear();
                        addRssSourceList();
                        db.delete(TABLE_NAME_RSS, null, null);
                        addRssToDB(rssSourceList, db);
                        break;
                }
            }
        }
        catch (Exception e)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_RSS);
            this.onCreate(db);
        }


    }

    // Adding new record
    public void addToDB(AllObjects allObjects) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CRYPTO_CURRENCY, allObjects.getCryptoCurrency());
        values.put(FIAT_CURRENCY, allObjects.getFiatCurrency());
        values.put(CURRENCY_AMOUNT, String.valueOf(BigDecimal.valueOf(allObjects.getCurrencyAmount())));
        //insert
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    void addRssToDB(List<RssSource> rssSource, SQLiteDatabase db) {
        //SQLiteDatabase db = this.getWritableDatabase();
        for (int i =0; i < rssSourceList.size(); i++)
        {
            ContentValues values = new ContentValues();
            values.put(RSS_URL, rssSource.get(i).getRssUrl());
            values.put(RSS_SOURCE, rssSource.get(i).getRssSource());
            values.put(RSS_LANG, rssSource.get(i).getRssLang());
            values.put(RSS_SUBSCRIBE, rssSource.get(i).getIssubscribe());
            db.insert(TABLE_NAME_RSS, null, values);
        }
        //insert
        //db.close();
    }

    void addOneRssToDB(String rssUrl, String rssSource, String rssLang, int rssIsSubscribe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(RSS_URL, rssUrl);
        values.put(RSS_SOURCE, rssSource);
        values.put(RSS_LANG, rssLang);
        values.put(RSS_SUBSCRIBE, rssIsSubscribe);

        //insert
        db.insert(TABLE_NAME_RSS, null, values);
        db.close();
    }

    // Getting All Contacts
    public List<AllObjects> getAllDB() {
        List<AllObjects> ObjectList = new ArrayList<AllObjects>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                try
                {
                    AllObjects allObjects = new AllObjects();
                    allObjects.setCryptoCurrency(cursor.getString(0));
                    allObjects.setFiatCurrency(cursor.getString(1));
                    allObjects.setCurrencyAmount(cursor.getDouble(2));

                    // Adding contact to list
                    ObjectList.add(allObjects);
                }catch (Exception e)
                {
                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                    this.onCreate(db);
                    //Log.e(TAG,"catchprzyczytaniuDB");
                }

            } while (cursor.moveToNext());
        }

        // return contact list
        cursor.close();
        db.close();
        return ObjectList;
    }

    public List<RssSource> getAllRssDB() {
        List<RssSource> RssList = new ArrayList<RssSource>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_RSS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                try
                {
                    RssSource rssObjects = new RssSource();
                    rssObjects.setRssUrl(cursor.getString(0));
                    rssObjects.setRssSource(cursor.getString(1));
                    rssObjects.setRssLang(cursor.getString(2));
                    rssObjects.setIssubscribe(cursor.getInt(3));

                    // Adding contact to list
                    RssList.add(rssObjects);
                }catch (Exception e)
                {
                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_RSS);
                    this.onCreate(db);
                    //Log.e(TAG,"catchprzyczytaniuDB");
                }

            } while (cursor.moveToNext());
        }

        // return contact list
        cursor.close();
        db.close();
        return RssList;
    }
    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    void removeAllRss()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(TABLE_NAME_RSS, null, null);
    }

    void removeOneRss(String source)
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(TABLE_NAME_RSS, RSS_SOURCE + "=?", new String[] {source});
    }

    public void updateOneRss(String source, String rssUrl, String rssSource, String rssLang, int rssIsSubscribe)
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        ContentValues values = new ContentValues();

        values.put(RSS_URL, rssUrl);
        values.put(RSS_SOURCE, rssSource);
        values.put(RSS_LANG, rssLang);
        values.put(RSS_SUBSCRIBE, rssIsSubscribe);

        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.update(TABLE_NAME_RSS, values, RSS_SOURCE + "=?", new String[] {source});
        db.close();
    }

    private void addRssSourceList()
    {
        rssSourceList.add(new RssSource("http://bithub.pl/feed", "bithub", "PL", 0));
        rssSourceList.add(new RssSource("https://bitcoin.pl/?format=feed&type=rss", "bitcoin", "PL", 0));
        rssSourceList.add(new RssSource("http://bitmon.pl/rss?format=feed&type=rss", "bitmon", "PL", 0));
        rssSourceList.add(new RssSource("http://cryptoprofit.pl/feed", "cryptoprofit", "PL", 0));
        rssSourceList.add(new RssSource("https://www.coindesk.com/feed", "coindesk", "ENG", 0));
        rssSourceList.add(new RssSource("https://cointelegraph.com/rss", "cointelegraph", "ENG", 0));
        rssSourceList.add(new RssSource("https://www.newsbtc.com/feed", "newsbtc", "ENG", 0));
        rssSourceList.add(new RssSource("https://www.cryptoninjas.net/feed", "cryptoninjas", "ENG", 0));
        rssSourceList.add(new RssSource("http://bitcoinist.com/feed", "bitcoinist", "ENG", 0));
        rssSourceList.add(new RssSource("https://bitcoinmagazine.com/feed", "bitcoinmagazine", "ENG", 0));
        rssSourceList.add(new RssSource("https://www.fxmag.pl/rss", "fxmag", "PL", 0));
        Collections.sort(rssSourceList);
    }
}
