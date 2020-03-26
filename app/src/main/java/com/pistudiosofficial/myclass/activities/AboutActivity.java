package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pistudiosofficial.myclass.Common;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.objects.AppFeedbackObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.pistudiosofficial.myclass.Common.CURRENT_USER;

public class AboutActivity extends AppCompatActivity {

    ImageButton fb1,linkedin1,gmail1,fb2,linkedin2,gmail2;
    Dialog feedbackDialog;
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

        Button feedback = findViewById(R.id.bt_feedback_about);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackDialog = new Dialog(AboutActivity.this);
                feedbackDialog.setContentView(R.layout.hod_email_dialog);
                feedbackDialog.show();
                EditText editText = feedbackDialog.findViewById(R.id.et_hod_email_dialog);
                editText.setHint("________Enter Feedback________");
                Button submitFeedback = feedbackDialog.findViewById(R.id.bt_hod_email_dialog);
                submitFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s = editText.getText().toString();
                        if (!s.isEmpty()){
                            AppFeedbackObject appFeedbackObject = new AppFeedbackObject();
                            appFeedbackObject.uid = CURRENT_USER.UID;
                            appFeedbackObject.value = s;
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                            String currentDateandTime = sdf.format(new Date());
                            appFeedbackObject.timestamp = currentDateandTime;
                            Common.FIREBASE_DATABASE.getReference("feedback").push().setValue(appFeedbackObject);
                            feedbackDialog.dismiss();
                        }
                    }
                });
            }
        });
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
