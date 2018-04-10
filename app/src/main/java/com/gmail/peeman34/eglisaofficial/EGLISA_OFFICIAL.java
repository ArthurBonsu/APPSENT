package com.gmail.peeman34.eglisaofficial;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class EGLISA_OFFICIAL extends AppCompatActivity {

   // Thread mythread = null;

    //private Button eglisasignupbutton;
    //Handler handler = null;


    public EGLISA_OFFICIAL() {
        super();
    }
    //}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eglisa_official);


        Thread timer = new Thread() {

            @Override
            public void run() {

                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent openStartingPoint = new Intent(EGLISA_OFFICIAL.this, BRIDGE.class);
                startActivity(openStartingPoint);
                finish();

            }
        };
        timer.start();
    }

}

























