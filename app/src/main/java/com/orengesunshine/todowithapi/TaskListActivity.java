package com.orengesunshine.todowithapi;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.orengesunshine.todowithapi.adapter.TaskListAdapter;
import com.orengesunshine.todowithapi.model.ResponseMessage;
import com.orengesunshine.todowithapi.model.Task;
import com.orengesunshine.todowithapi.service.TodoClient;


import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskListActivity extends AppCompatActivity {

    //this activity shows all the tasks that are saved in the server
    @BindView(R.id.task_list_view)
    ListView listView;
    @BindView(R.id.task_progress)
    ProgressBar progressBar;

    SharedPreferences pref;
    TaskListAdapter adapter;

    private String api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        ButterKnife.bind(this);
        //get api from sharedPreferences
        pref = getSharedPreferences(LoginActivity.USER_PREF,MODE_PRIVATE);
        api = pref.getString(LoginActivity.USER_API,null);
        loadTaskList();
    }

    private void loadTaskList() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(TodoClient.baseUrl)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit ret = builder.build();
        TodoClient client = ret.create(TodoClient.class);

        //apply api
        Call<List<Task>> call = client.loadAllTask(api);
        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                if (response.body()!=null){
                    adapter = new TaskListAdapter(TaskListActivity.this,response.body());
                    listView.setAdapter(adapter);
                }else if (response.errorBody()!=null){
                    Gson gson = new Gson();
                    try {
                        Log.d(MainActivity.TAG, "onResponse error: "+gson.fromJson(response.errorBody().string(),ResponseMessage.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    Log.d(MainActivity.TAG, "onResponse: total failure");
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d(MainActivity.TAG, "onFailure: "+t.getMessage());
            }
        });
    }
}
