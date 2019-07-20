package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.presenter.CreatePollPresenter;
import com.pistudiosofficial.myclass.view.CreatePollView;

import java.util.ArrayList;

public class CreatePollActivity extends AppCompatActivity implements CreatePollView {

    Button bt_addPollOption,bt_pushPoll,bt_cancelPoll;
    EditText et_pollDescription, et_addPollOption;
    ListView listView;
    ArrayList<String> pollOption;
    ArrayAdapter adapter;
    CreatePollPresenter presenter;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);
        presenter = new CreatePollPresenter(this);
        bt_addPollOption = findViewById(R.id.bt_addPollOption);
//        bt_pushPoll = findViewById(R.id.bt_pushPoll);
//        bt_cancelPoll=findViewById(R.id.bt_cancelPoll);
        et_addPollOption = findViewById(R.id.et_addPollOption);
        et_pollDescription = findViewById(R.id.et_poll_description);
        listView = findViewById(R.id.recyclerView_create_poll);
        //Add action bar and remove title
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
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setHomeButtonEnabled(true);
        pollOption = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pollOption);
        listView.setAdapter(adapter);
        bt_addPollOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String x = et_addPollOption.getText().toString();
                if(x!=null && !x.equals("")){
                    pollOption.add(x);
                    et_addPollOption.setText("");
                    adapter.notifyDataSetChanged();
                }
            }
        });
//        bt_pushPoll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (pollOption.size()>=2 && !et_pollDescription.getText().toString().equals("")){
//                    presenter.performCreatePoll(et_pollDescription.getText().toString(),pollOption);
//                }
//                else{
//                    Toast.makeText(CreatePollActivity.this,"Insufficient Option",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        bt_cancelPoll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
////                startActivity(new Intent(getApplicationContext(),CheckAttendanceActivity.class));
//            }
//        });

    }

    @Override
    public void createPollSuccess() {
        Toast.makeText(this, "Poll Created !", Toast.LENGTH_SHORT).show();
        finish();
    }
    //Add extra menu buttons to replace pushPoll and Cancel buttons
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_poll, menu);
        this.menu=menu;
        return true;
    }
    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_pushpoll){
            if (pollOption.size()>=2 && !et_pollDescription.getText().toString().equals("")){
                presenter.performCreatePoll(et_pollDescription.getText().toString(),pollOption);
            }
            else{
                Toast.makeText(CreatePollActivity.this,"Insufficient Option",Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up
        return false;
    }

    @Override
    public void createPollFailed() {
        Toast.makeText(this,"Poll Upload Failed",Toast.LENGTH_SHORT).show();
    }
}
