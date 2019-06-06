package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.presenter.LoginPresenter;

public class SignupActivity extends AppCompatActivity {


    private RadioGroup radioGroup;
    private String admin = "user";
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final EditText name,roll,phone,password1,password2,email;

        name = findViewById(R.id.et_name);
        phone = findViewById(R.id.et_phone);
        password1 = findViewById(R.id.et_password_sign);
        password2 = findViewById(R.id.et_password2_sign);
        email = findViewById(R.id.et_email_sign);
        radioGroup =findViewById(R.id.radioGroup);
        roll = findViewById(R.id.et_roll_id);
        Button signup = findViewById(R.id.bt_signUp);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                switch (id){
                    case R.id.radio_student:
                        admin = "user";
                        roll.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio_admin:
                        admin = "admin";
                        roll.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.radio_masteradmin:
                        admin = "master_admin";
                        roll.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        admin = "user";
                        roll.setVisibility(View.VISIBLE);
                        break;
                }

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password1.getText().toString().equals(password2.getText().toString()) && !(password1.getText().toString().length()<6)){
                    presenter.performSignUp(email.getText().toString(),
                            password1.getText().toString(),
                            phone.getText().toString(),
                            name.getText().toString(),
                            admin,roll.getText().toString());
                }else{
                    Toast.makeText(SignupActivity.this,"Password Not Match \n Cannot be less than 6 character",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignupActivity.this,LoginActivity.class));
        finish();
    }
}
