package com.pistudiosofficial.myclass.activities;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.presenter.LoginPresenter;
import com.pistudiosofficial.myclass.view.LoginActivityView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


public class LoginActivity extends AppCompatActivity implements LoginActivityView {

    private EditText email,password;
    private Button login;
    private TextView createAcc;
    private LoginPresenter presenter;
    private RadioGroup radioGroup;
    private String admin = "user";
    private EditText roll;
    private Dialog dialog;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_login);

        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        login = findViewById(R.id.bt_logIn);
        createAcc = findViewById(R.id.tv_createAccount);
        dialog = new Dialog(LoginActivity.this);
        progressDialog = new ProgressDialog(this);
        if (getIntent().getStringExtra("data")!=null){
            Toast.makeText(this,getIntent().getStringExtra("data") , Toast.LENGTH_SHORT).show();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString().matches("")||password.getText().toString().matches("")) {
                    Toast.makeText(LoginActivity.this, "Please fill Credentials", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                presenter.performLogin(email.getText().toString(),password.getText().toString());
            }
        });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new LoginPresenter(this);
    }

    @Override
    public void showErrorFailed() {
        progressDialog.dismiss();
        Toast.makeText(this,"FAILED! Try Again",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successLogin() {
        progressDialog.dismiss();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

}
