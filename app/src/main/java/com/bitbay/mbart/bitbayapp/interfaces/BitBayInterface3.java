package com.bitbay.mbart.bitbayapp.interfaces;

import com.bitbay.mbart.bitbayapp.models.CryptoAddressGenerated;
import com.bitbay.mbart.bitbayapp.models.CryptoDeposit;
import com.bitbay.mbart.bitbayapp.models.DeleteOrder;
import com.bitbay.mbart.bitbayapp.models.RSSFeed;
import com.bitbay.mbart.bitbayapp.models.chart.Chart;
import com.bitbay.mbart.bitbayapp.models.cryptoWithdraw.CryptoWithdraw;
import com.bitbay.mbart.bitbayapp.models.fiatDeposit.FiatDeposit;
import com.bitbay.mbart.bitbayapp.models.marketConfiguration.MarketConfiguration;
import com.bitbay.mbart.bitbayapp.models.openItems.OpenItems;
import com.bitbay.mbart.bitbayapp.models.orderbook.Orderbook;
import com.bitbay.mbart.bitbayapp.models.placeOrder.PlaceOrder;
import com.bitbay.mbart.bitbayapp.models.transactionHistory.TransactionHistory;
import com.bitbay.mbart.bitbayapp.models.wallet.WalletLists;
import com.bitbay.mbart.bitbayapp.models.youTubeChannelVideo.YouTubeChannelVideoAPI;
import com.google.gson.JsonObject;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;


import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by mbart on 07.12.2017.
 */

public interface BitBayInterface3 {

    String BASE_URL_API3 = "https://api.bitbay.net/rest/";

    Retrofit retrofitAPI3 = new Retrofit.Builder()
            .baseUrl(BASE_URL_API3)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    Retrofit retrofitXML = new Retrofit.Builder()
            .baseUrl("https://www.coindesk.com/")
            .client(new OkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build();

    Retrofit retrofitYouTubeApiV3 = new Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/youtube/v3/")
            .client(new OkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET
    Observable<RSSFeed> getProducts(@Url String url);


    @GET("trading/ticker")
    Observable <Object> tickerApi3();

    @GET("trading/stats")
    Observable <Object> statsApi3();

    @GET("trading/orderbook/{crypto}-{fiat}")
    Observable<Orderbook> orderbookApi3(
            @Path("crypto") String crypto,
            @Path("fiat") String fiat,
            @Query("limit") int limit
    );

    @GET("balances/BITBAY/balance")
    Observable <WalletLists> balanceListsApi3(
           @Header("API-Key") String publicKey,
           @Header("API-Hash") String apiHash,
           @Header("Request-Timestamp") long timestamp,
           @Header("Operation-Id") String operationId
    );

    @GET("trading/offer")
    Observable <OpenItems> openItemsApi3(
            @Header("API-Key") String publicKey,
            @Header("API-Hash") String apiHash,
            @Header("Request-Timestamp") long timestamp,
            @Header("Operation-Id") String operationId
    );

    @DELETE("trading/offer/{order_id}")
    Observable <DeleteOrder> deleteOrderApi3(
            @Header("API-Key") String publicKey,
            @Header("API-Hash") String apiHash,
            @Header("Request-Timestamp") long timestamp,
            @Header("Operation-Id") String operationId,
            @Path("order_id") String orderId
    );

    @GET("trading/candle/history/{crypto}-{fiat}/{resolution}")
    Observable <Chart> chartApi3(
            @Path("crypto") String crypto,
            @Path("fiat") String fiat,
            @Path("resolution") int resolution,
            @Query("from") long from,
            @Query("to") long to
    );

    @GET("trading/config/{crypto}-{fiat}")
    Observable <MarketConfiguration> marketConfigurationApi3(
            @Header("API-Key") String publicKey,
            @Header("API-Hash") String apiHash,
            @Header("Request-Timestamp") long timestamp,
            @Header("Operation-Id") String operationId,
            @Path("crypto") String crypto,
            @Path("fiat") String fiat
    );

    @Headers("Content-Type: application/json")
    @POST("trading/offer/{crypto}-{fiat}")
    Observable <PlaceOrder> placeOrderApi3(
            @Path("crypto") String crypto,
            @Path("fiat") String fiat,
            @Header("API-Key") String publicKey,
            @Header("API-Hash") String apiHash,
            @Header("Request-Timestamp") long timestamp,
            @Header("Operation-Id") String operationId,
            @Body JsonObject body
    );

    @GET("payments/crypto-address/{wallet_id}")
    Observable <CryptoDeposit> cryptoDepositAddressApi3(
            @Path("wallet_id") String walletID,
            @Header("API-Key") String publicKey,
            @Header("API-Hash") String apiHash,
            @Header("Request-Timestamp") long timestamp,
            @Header("Operation-Id") String operationId
    );

    @GET("payments/deposit/igoria_deposit/{currency}/customs")
    Observable <FiatDeposit> fiatDepositAddressApi3(
            @Path("currency") String currencySymbol,
            @Header("API-Key") String publicKey,
            @Header("API-Hash") String apiHash,
            @Header("Request-Timestamp") long timestamp,
            @Header("Operation-Id") String operationId
    );

    @Headers("Content-Type: application/json")
    @POST("payments/crypto-address/{walletID}")
    Observable <CryptoAddressGenerated> cryptoAddressGenerateApi3(
            @Path("walletID") String walletID,
            @Header("API-Key") String publicKey,
            @Header("API-Hash") String apiHash,
            @Header("Request-Timestamp") long timestamp,
            @Header("Operation-Id") String operationId,
            @Body JsonObject body
    );

    @Headers("Content-Type: application/json")
    @POST("payments/withdrawal/{walletID}")
    Observable <CryptoWithdraw> withdrawCryptoApi3(
            @Path("walletID") String walletID,
            @Header("API-Key") String publicKey,
            @Header("API-Hash") String apiHash,
            @Header("Request-Timestamp") long timestamp,
            @Header("Operation-Id") String operationId,
            @Body JsonObject body
    );

    @Headers("Content-Type: application/json")
    @GET("trading/history/transactions")
    Observable <TransactionHistory>transactionHistoryApi3(
            @Header("API-Key") String publicKey,
            @Header("API-Hash") String apiHash,
            @Header("Request-Timestamp") long timestamp,
            @Header("Operation-Id") String operationId,
            @Query("query") String query
    );

    @GET("search")
    Observable <YouTubeChannelVideoAPI> youTubeChannelVideoApi3(
            @QueryMap(encoded=true) Map<String, String> options
    );
}

