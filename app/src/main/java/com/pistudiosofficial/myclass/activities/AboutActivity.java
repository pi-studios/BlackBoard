package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.pistudiosofficial.myclass.R;

public class AboutActivity extends AppCompatActivity {

    ImageButton fb1,linkedin1,gmail1,fb2,linkedin2,gmail2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        fb1=findViewById(R.id.fb_1);
        linkedin1=findViewById(R.id.linkedin_1);
        gmail1=findViewById(R.id.gmail_1);
        fb2=findViewById(R.id.fb_2);
        linkedin2=findViewById(R.id.linkedin_2);
        gmail2=findViewById(R.id.gmail_2);
        final ActionBar abar = getSupportActionBar();
        if(abar!=null) {
            abar.setDisplayHomeAsUpEnabled(true);
            abar.setTitle("About Us");
        }
        setInfolink();
    }

    private void setInfolink() {
        fb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        linkedin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        gmail1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        linkedin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl("https://www.linkedin.com/in/vivek-verma-4302a7154/");
            }
        });
        gmail2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void goToUrl(String url) {
        Uri uri=Uri.parse(url);
        Intent launchBrowser=new Intent(Intent.ACTION_VIEW,uri);
        startActivity(launchBrowser);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up
        return false;
    }

}
