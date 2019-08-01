package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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
                goToUrl("https://www.facebook.com/najimulx");
            }
        });
        linkedin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl("https://www.linkedin.com/in/nazimulhoque6/");

            }
        });
        gmail1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this, "najimulx@gmail.com", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent (Intent.ACTION_SEND );
//                intent.putExtra(Intent.EXTRA_EMAIL,"najimulx@gmail.com");
////                intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
////                intent.putExtra(Intent.EXTRA_TEXT, "your_text");
//                startActivity(intent);
            }
        });

        fb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl("https://www.facebook.com/profile.php?id=100009383604704");
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
                Toast.makeText(AboutActivity.this, "myselfvivek9@gmail.com", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent (Intent.ACTION_SEND);
//                intent.putExtra(Intent.EXTRA_EMAIL,"myselfvivek9@gmail.com");
////                intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
////                intent.putExtra(Intent.EXTRA_TEXT, "your_text");
//                startActivity(intent);
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
