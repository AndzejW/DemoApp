package com.andrzej.demoapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="MainActivity";
    private boolean firstTime=true;

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerAdapter mAdapter;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mainToolbar.setElevation(6f);
        }
        setSupportActionBar(mainToolbar);

        mAdapter= new RecyclerAdapter(new ArrayList<Element>(),R.layout.element_layout);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.elements);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout =
             (SwipeRefreshLayout) findViewById(R.id.swipe);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                if(!requestForData()){
                    finishRefreshing();

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestForData();
    }

    private boolean requestForData() {
        if(isOnline()){
            downloadData();
            return true;
        }
        noInternetConnection();
        return false;
    }

    private void noInternetConnection() {
        final AlertDialog alertDialog =
             new AlertDialog.Builder(this)
                  .setTitle("No Internet connection")
                  .setMessage("Try Again")
                  .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {
                          dialogInterface.cancel();
                      }
                  })
                  .create();
        alertDialog.show();
    }

    public void downloadData(){
        if(firstTime){
            mProgressDialog = ProgressDialog.show(this,"Please wait",
                 "Loading data",true);
        }
        Gson gson = new GsonBuilder()
             .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
             .create();

        Retrofit retrofit = new Retrofit.Builder()
             .baseUrl(MyWebService.URL)
             .addConverterFactory(GsonConverterFactory.create(gson))
             .build();

        MyWebService myWebService = retrofit.create(MyWebService.class);

        Call<List<Element>> listCall = myWebService.getElement();
        listCall.enqueue(repoCall);
    }

    Callback<List<Element>> repoCall = new Callback<List<Element>>(){

        @Override
        public void onResponse(Call<List<Element>> call, Response<List<Element>> response) {
            if(response.isSuccessful()){
                List<Element> repoCall = response.body();
                mAdapter.setList(repoCall);
            }else{
                Log.e(TAG,"ERROR code: "+response.code());
            }
            cancelIndicators();
        }

        @Override
        public void onFailure(Call<List<Element>> call, Throwable t) {
            Log.e(TAG,"ERROR: "+t.getMessage());
            cancelIndicators();
        }
    };

    private boolean isOnline() {
        ConnectivityManager connectivityManager =
             (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void cancelIndicators() {
        dismissProgressDialog();
        finishRefreshing();
    }

    private void dismissProgressDialog() {
        if(firstTime){
            mProgressDialog.dismiss();
            firstTime=false;
        }
    }

    private void finishRefreshing() {
        if(swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
