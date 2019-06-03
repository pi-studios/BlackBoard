package com.pistudiosofficial.myclass.activities;


import android.app.Dialog;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_login);

        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        login = findViewById(R.id.bt_logIn);
        createAcc = findViewById(R.id.tv_createAccount);
        dialog = new Dialog(LoginActivity.this);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString().matches("")||password.getText().toString().matches("")) {
                    Toast.makeText(LoginActivity.this, "Please fill Credentials", Toast.LENGTH_SHORT).show();
                    return;
                }

                presenter.performLogin(email.getText().toString(),password.getText().toString());
            }
    });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.signup_dialog);
                final EditText name,phone,password1,password2,email;

                name = dialog.findViewById(R.id.et_name);
                phone = dialog.findViewById(R.id.et_phone);
                password1 = dialog.findViewById(R.id.et_password_sign);
                password2 = dialog.findViewById(R.id.et_password2_sign);
                email = dialog.findViewById(R.id.et_email_sign);
                radioGroup = dialog.findViewById(R.id.radioGroup);
                roll = dialog.findViewById(R.id.et_roll_id);
                Button signup = dialog.findViewById(R.id.bt_signUp);
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
                            Toast.makeText(LoginActivity.this,"Password Not Match \n Cannot be less than 6 character",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();

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
        Toast.makeText(this,"FAILED! Try Again",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successLogin() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void successSignup() {
        dialog.dismiss();
        Toast.makeText(this,"Success",Toast.LENGTH_SHORT);
    }

}
