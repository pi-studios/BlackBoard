package com.pistudiosofficial.myclass.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.pistudiosofficial.myclass.AdapterClassList;
import com.pistudiosofficial.myclass.AdapterConnectionList;
import com.pistudiosofficial.myclass.AdapterPagerView;
import com.pistudiosofficial.myclass.ClassObject;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.StudentClassObject;
import com.pistudiosofficial.myclass.UserObject;
import com.pistudiosofficial.myclass.presenter.CheckAttendancePresenter;
import com.pistudiosofficial.myclass.presenter.MainPresenter;
import com.pistudiosofficial.myclass.view.MainActivityView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.pistudiosofficial.myclass.Common.ATTD_PERCENTAGE_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_ADMIN_CLASS_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER_CLASS_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER_CLASS_LIST_ID;
import static com.pistudiosofficial.myclass.Common.FIREBASE_DATABASE;
import static com.pistudiosofficial.myclass.Common.FIREBASE_USER;
import static com.pistudiosofficial.myclass.Common.POST_OBJECT_LIST;
import static com.pistudiosofficial.myclass.Common.ROLL_LIST;
import static com.pistudiosofficial.myclass.Common.SHARED_PREFERENCES;
import static com.pistudiosofficial.myclass.Common.TEMP01_LIST;
import static com.pistudiosofficial.myclass.Common.mAUTH;
import static com.pistudiosofficial.myclass.Common.mREF_admin_classList;
import static com.pistudiosofficial.myclass.Common.mREF_classList;
import static com.pistudiosofficial.myclass.Common.mREF_connections;
import static com.pistudiosofficial.myclass.Common.mREF_oldRecords;
import static com.pistudiosofficial.myclass.Common.mREF_student_classList;
import static com.pistudiosofficial.myclass.Common.mREF_users;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainActivityView {

    public static MainPresenter presenter;
    CheckAttendancePresenter checkAttendancePresenter;
    NavigationView navigationView;
    ProgressDialog progressDialog;
    View headerView;
    Dialog connectionListDialog;
    AdapterPagerView adapterPagerView;
    TabLayout tabLayout;
    ViewPager viewPager;
    boolean flag = false;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        headerView.findViewById(R.id.headerImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
            }
        });

        viewPager = findViewById(R.id.fragment_container);
        adapterPagerView = new AdapterPagerView(getSupportFragmentManager());
        tabLayout = findViewById(R.id.tabbar);
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_signout) {
            mREF_users.child(mAUTH.getUid()).child("token").setValue("");
            mAUTH.signOut();
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawer.closeDrawers();
        int id = item.getItemId();
        if (id == R.id.nav_connections) {
        }
        else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasPermission = (ContextCompat.checkSelfPermission(getBaseContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        },1);
            }
        }

        //Variable Initialization
        mAUTH = FirebaseAuth.getInstance();
        FIREBASE_DATABASE = FirebaseDatabase.getInstance();

        mREF_users = FIREBASE_DATABASE.getReference("users");
        mREF_connections = FIREBASE_DATABASE.getReference("connections");
        mREF_classList = FIREBASE_DATABASE.getReference("class_list");
        mREF_oldRecords = FIREBASE_DATABASE.getReference("old_records");
        mREF_users.keepSynced(true);
        mREF_classList.keepSynced(true);
        mREF_connections.keepSynced(true);
        CURRENT_CLASS_ID_LIST = new ArrayList<>();
        CURRENT_ADMIN_CLASS_LIST = new ArrayList<>();
        ATTD_PERCENTAGE_LIST = new ArrayList<>();
        TEMP01_LIST = new ArrayList<>();
        ROLL_LIST = new ArrayList<>();
        CURRENT_USER_CLASS_LIST = new ArrayList<>();
        CURRENT_USER_CLASS_LIST_ID = new ArrayList<>();
        POST_OBJECT_LIST = new ArrayList<>();
        //Logged in Check and perform DataLoad
        FIREBASE_USER = mAUTH.getCurrentUser();
        FirebaseUser currentUser = mAUTH.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            mREF_student_classList = mREF_users.child(mAUTH.getUid()).child("class_list"); //FIREBASE_DATABASE.getReference("student_class_list");
            mREF_admin_classList = mREF_users.child(mAUTH.getUid()).child("class_list");

            SHARED_PREFERENCES = getSharedPreferences("", Context.MODE_PRIVATE);

            //Check for Double attendance (for Admin Level)
            Date todayDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String todayString = formatter.format(todayDate);
            String day = SHARED_PREFERENCES.getString("day","");
            if(!todayString.equals(day)){
                for(int i=0; i<CURRENT_CLASS_ID_LIST.size();i++){
                    SHARED_PREFERENCES.edit().putString(CURRENT_CLASS_ID_LIST.get(i), "false").apply();
                }
            }


            presenter = new MainPresenter(this);
            progressDialog = ProgressDialog.show(MainActivity.this, "",
                    "Loading. Please wait...", true);
            presenter.performDataDownload();
        }
    }

    @Override
    public void downloadDataSuccess() {
        Menu menu = navigationView.getMenu();
        MenuItem nav_connections = menu.findItem(R.id.nav_connections);
            if(CURRENT_USER.AdminLevel.equals("admin")){
                nav_connections.setTitle("Students");
                presenter.performAdminClassListDownload();
            }
            if(CURRENT_USER.AdminLevel.equals("user")){
                nav_connections.setTitle("Faculties");
                presenter.performUserClassListDownload();
            }
            if(CURRENT_USER.AdminLevel.equals("master_admin")){
                nav_connections.setTitle("Professors");
            }
            TextView textViewName = headerView.findViewById(R.id.tv_header_title);
            textViewName.setText(CURRENT_USER.Name);
            TextView textViewEmail = headerView.findViewById(R.id.tv_header_email);
            textViewEmail.setText(CURRENT_USER.Email);
    }

    @Override
    public void downloadDataFailed() {
        Toast.makeText(MainActivity.this,"Data Load Failed",Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    @Override
    public void addAdminClassSuccess() {
        presenter.performAdminClassListDownload();
        Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addAdminClassFailed() {
        Toast.makeText(MainActivity.this,"Failed! Try Again",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void endSession(int index) {
        presenter.endSession(index);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void loadAdminClassList(ArrayList<ClassObject> classObjectArrayList) {
        CURRENT_ADMIN_CLASS_LIST = classObjectArrayList;
        presenter.performPostLoad();
        viewPager.setAdapter(adapterPagerView);
        if(!flag){viewPager.setCurrentItem(1);flag = true;}
        else {viewPager.setCurrentItem(2);}
        progressDialog.dismiss();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void loadUserClassList(ArrayList<ClassObject> classObjectsList,
                                  ArrayList<String> userAttendanceList) {
        CURRENT_ADMIN_CLASS_LIST = classObjectsList;
        presenter.performPostLoad();
        viewPager.setAdapter(adapterPagerView);
        if(!flag){viewPager.setCurrentItem(1);flag = true;}
        else {viewPager.setCurrentItem(2);}
        progressDialog.dismiss();
    }

    @Override
    public void addUserClassSuccess() {
        Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT).show();
        presenter.performUserClassListDownload();
    }

    @Override
    public void addUserClassFailed() {
        Toast.makeText(MainActivity.this,"Class Add Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addCollab(int index, String email) {
        presenter.addCollab(index,email,true);
    }

    @Override
    public void transferClass(int index, String email) {
        presenter.transferClass(index,email);
    }

    @Override
    public void transferClassFailed() {
        Toast.makeText(this,"Failed Transfer",Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void connectionListDownloadSuccess(ArrayList<UserObject> userList) {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        connectionListDialog = new Dialog(this);
        connectionListDialog.setContentView(R.layout.connection_list_dialog);
        AdapterConnectionList adapterConnectionList = new AdapterConnectionList(userList,connectionListDialog);
        RecyclerView recyclerView = connectionListDialog.findViewById(R.id.recycler_connection_list);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapterConnectionList);
        adapterConnectionList.notifyDataSetChanged();
        connectionListDialog.show();
    }

    @Override
    public void connectionListDownloadFailed() {
        Toast.makeText(this, "Connection List Load Failed",Toast.LENGTH_SHORT).show();
    }

}
