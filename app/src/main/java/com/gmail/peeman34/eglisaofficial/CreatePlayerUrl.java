package com.gmail.peeman34.eglisaofficial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pee on 2/21/2018.
 */

public class CreatePlayerUrl extends AppCompatActivity implements View.OnClickListener {
final String TAG = this.getClass().getSimpleName();
Button refreshbutton;

    String bitrate ="640";
    String height = "288";
    String label ="Peemyurl";

    String width ="384";

    public CreatePlayerUrl() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshbutton = (Button)findViewById(R.id.REFRESHBUTTON);
         setContentView(R.layout.createplayerurl);
         refreshbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (getTaskId()){
            case  R.id.REFRESHBUTTON:
       String url= "http://api.cloud.wowza.com/api/[v1]/players/1234abcd/urls";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Error while reading this", Toast.LENGTH_SHORT);


                    }

                }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                           Map<String, String>params = new HashMap<>();

                           params.put("bitrate", bitrate);
                           params.put("height", height);
                           params.put("label", label);
                           params.put("url","https://93d4ad.entrypoint.cloud.wowza.com/app-32f3/ngrp:3f489aaf_all/playlist.m3u8" );
                           params.put("width",width);
                        return super.getParams();


                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                         Map<String , String> headers = new HashMap<>();
                          headers.put("Content-Type", "application/json");
                          headers.put("wsc-api-key","9hqzt2imIrKrtCUO8svpbcLjmTQku62nL3YbUxGEGIwxakMFFHzm2GJnZ3PY3548");
                           headers.put("wsc-access-key" ,"BdoHucQQW2NzrqMGQadg6OCzP3qFYN9YMQgccuqzWMcMbUfrsOybjfM5uzd1363a");
                              return getHeaders();
                    }

                }



                        ;
                MySingleTon.getInstance(this).addToRequestQueue(stringRequest);
        }


    }
}
