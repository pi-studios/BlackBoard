package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.presenter.LoginPresenter;
import com.pistudiosofficial.myclass.presenter.SignUpPresenter;
import com.pistudiosofficial.myclass.view.SignUpView;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity implements SignUpView {


    private RadioGroup radioGroup;
    private String admin = "user";
    private SignUpPresenter presenter;
    private ProgressDialog progressDialog;
    private Spinner spnYear,spnDept,spnRoll;
    String year="",dept="",roll="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final EditText name,phone,password1,password2,email;

        progressDialog = new ProgressDialog(this);
        name = findViewById(R.id.et_name);
        phone = findViewById(R.id.et_phone);
        password1 = findViewById(R.id.et_password_sign);
        password2 = findViewById(R.id.et_password2_sign);
        email = findViewById(R.id.et_email_sign);
        radioGroup =findViewById(R.id.radioGroup);
        Button signup = findViewById(R.id.bt_signUp);

        //Setting spinner for scholar ID
        spnYear = findViewById(R.id.spinner_year_signup);
        spnDept = findViewById(R.id.spinner_dept_signup);
        spnRoll = findViewById(R.id.spinner_roll_signup);

        ArrayList<String> rollList = new ArrayList<>();
        ArrayList<String> yearList = new ArrayList<>();
        ArrayList<String> deptList = new ArrayList<>();
        rollList.add("Roll");
        for (int i = 1;i<200;i++){
            String temp = "";
            if (i<=9){
                temp = "00"+i;
            }
            if (i>9 && i<=99){
                temp = "0"+i;
            }
            if (i>99 && i<=200){
                temp = i+"";
            }
            rollList.add(temp);
        }
        yearList.add("Year");yearList.add("16");yearList.add("17");yearList.add("18");yearList.add("19");yearList.add("20");
        deptList.add("Dept");deptList.add("11");deptList.add("12");deptList.add("13");deptList.add("14");deptList.add("15");deptList.add("16");

        ArrayAdapter deptAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,deptList);
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDept.setAdapter(deptAdapter);
        ArrayAdapter yearAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnYear.setAdapter(yearAdapter);
        ArrayAdapter rollAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,rollList);
        rollAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRoll.setAdapter(rollAdapter);

        spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0){
                    year = yearList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnRoll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0){
                    roll = rollList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0) {
                    dept = deptList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                switch (id){
                    case R.id.radio_student:
                        admin = "user";
                        spnDept.setVisibility(View.VISIBLE);
                        spnYear.setVisibility(View.VISIBLE);
                        spnRoll.setVisibility(View.VISIBLE);

                        break;
                    case R.id.radio_admin:
                        admin = "admin";
                        spnRoll.setVisibility(View.INVISIBLE);
                        spnDept.setVisibility(View.INVISIBLE);
                        spnYear.setVisibility(View.INVISIBLE);

                        break;
                    default:
                        admin = "user";
                        spnDept.setVisibility(View.VISIBLE);
                        spnYear.setVisibility(View.VISIBLE);
                        spnRoll.setVisibility(View.VISIBLE);
                        break;
                }

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String schID = year+dept+roll;
                if(phone.getText().toString().length()!=10){
                    phone.setError("Please enter ten digit Mob.No");
                    return;
                }
                if((year.equals("") || roll.equals("") || dept.equals("")) && admin.equals("user")){
                    Toast.makeText(SignupActivity.this,"Enter Scholar ID ",Toast.LENGTH_LONG).show();
                    return;
                }
                if(password1.getText().toString().equals(password2.getText().toString()) && !(password1.getText().toString().length()<6)){
                    presenter.performSignUp(email.getText().toString(),
                            password1.getText().toString(),
                            phone.getText().toString(),
                            name.getText().toString(),
                            admin,schID);
                    progressDialog.show();
                }else{
                    Toast.makeText(SignupActivity.this,"Password Not Match \n Cannot be less than 6 character",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new SignUpPresenter(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignupActivity.this,LoginActivity.class));
        finish();
    }

    @Override
    public void successSignup() {
        Toast.makeText(this,"Success",Toast.LENGTH_SHORT);
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.putExtra("data","Check Email. You have to verify your email.");
        startActivity(intent);
        finish();
        progressDialog.dismiss();
    }
    @Override
    public void showErrorFailed() {
        progressDialog.dismiss();
        Toast.makeText(this,"FAILED! Try Again",Toast.LENGTH_SHORT).show();
    }
}
