package com.orengesunshine.todowithapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "tag";
    //this class is just for opening new activities
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.sign_up_button:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.login_button:
                startActivity(new Intent(this,LoginActivity.class));
                break;
            case R.id.create_new_task:
                startActivity(new Intent(this,CreateActivity.class));
                break;
            case R.id.see_tasks:
                startActivity(new Intent(this,TaskListActivity.class));
                break;
        }
    }
}
