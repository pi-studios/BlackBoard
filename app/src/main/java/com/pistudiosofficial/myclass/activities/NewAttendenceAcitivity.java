package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.pistudiosofficial.myclass.adapters.AdapterNewAttendenceList;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.presenter.NewAttendencePresenter;
import com.pistudiosofficial.myclass.view.NewAttendenceView;

import java.util.Calendar;

import static com.pistudiosofficial.myclass.Common.TEMP01_LIST;

public class NewAttendenceAcitivity extends AppCompatActivity implements AdapterNewAttendenceList.OnItemListener, NewAttendenceView {

    RecyclerView newAttendenceRecycler;
    AdapterNewAttendenceList adapterNewAttendenceList;
    NewAttendencePresenter presenter;
    Menu menu;
    final Calendar newCalendar = Calendar.getInstance();
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_new_attendence);
// Add action bar and set title to middle
        final ActionBar abar = getSupportActionBar();
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actionbar_titletext_layout);

        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        TextView textviewTitle = viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Attendance");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setHomeButtonEnabled(true);
        newAttendenceRecycler = findViewById(R.id.recyclerView_newAttendence);
        adapterNewAttendenceList = new AdapterNewAttendenceList(this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        newAttendenceRecycler.setLayoutManager(llm);
        newAttendenceRecycler.setAdapter(adapterNewAttendenceList);
//        Button bt_done = findViewById(R.id.bt_new_attendence_done);
//        bt_done.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new AlertDialog.Builder(NewAttendenceAcitivity.this)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setTitle("Are You Sure?")
//                        .setMessage("Once Attendance taken CANNOT be changed")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                presenter.performAttendenceUpload();
//                            }
//                        }).setNegativeButton("No",null).show();
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new NewAttendencePresenter(this);
    }

    @Override
    public void onItemClick(int position) {
        if(TEMP01_LIST.get(position).equals("ABSENT")){
            TEMP01_LIST.set(position,"PRESENT");
        }else {
            TEMP01_LIST.set(position,"ABSENT");
        }
        adapterNewAttendenceList.notifyDataSetChanged();
    }

    @Override
    public void uploadSuccess() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void uploadFailed() {
        Toast.makeText(NewAttendenceAcitivity.this,"Upload Failed",Toast.LENGTH_LONG).show();
    }
    // Remove 'done' Button and add menu options to save attendance and cancel
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_attendance, menu);
        this.menu=menu;
        return true;
    }
    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_saveAttendance){
            presenter.performAttendenceUpload();
            /*new AlertDialog.Builder(NewAttendenceAcitivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are You Sure?")
                    .setMessage("Once Attendance taken CANNOT be changed")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.performAttendenceUpload();
                        }
                    }).setNegativeButton("No",null).show();*/
        }

        if(id==R.id.action_date_pick){
            DatePickerDialog  StartTime = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                }

            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                    StartTime .show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up
        return false;
    }

}
