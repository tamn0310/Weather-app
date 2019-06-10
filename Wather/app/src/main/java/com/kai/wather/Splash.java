package com.kai.wather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                }catch(Exception ex){

                }finally {
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        thread.start();
    }
}
