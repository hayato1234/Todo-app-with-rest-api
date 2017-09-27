package com.orengesunshine.todowithapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.orengesunshine.todowithapi.model.ResponseMessage;
import com.orengesunshine.todowithapi.service.TodoClient;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateActivity extends AppCompatActivity {

    @BindView(R.id.task_content)
    EditText content;

    private SharedPreferences pref;
    private String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ButterKnife.bind(this);

        //get api key from sharedPreferences
        pref = getSharedPreferences(LoginActivity.USER_PREF, Context.MODE_PRIVATE);
        apiKey = pref.getString(LoginActivity.USER_API,null);
    }

    @OnClick(R.id.create_task_button)
    public void onCreateClick(View view){
        if (!content.getText().toString().isEmpty()){ // check if something is written
            //use retrofit to make api call
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(TodoClient.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            TodoClient client = retrofit.create(TodoClient.class);
            //set api key and content as parameters
            Call<ResponseMessage> call = client.createTask(apiKey,content.getText().toString());
            call.enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                    if (response.body()!=null){ //no error
                        Log.d(MainActivity.TAG, "CreateA onResponse: "+response.body().getMessage());

                        //reset editText
                        content.setText("");
                    }else if (response.errorBody()!=null){ //error occurred
                        Gson gson = new Gson();
                        try {
                            ResponseMessage responseMessage = gson.fromJson(response.errorBody().string(),ResponseMessage.class);
                            Log.d(MainActivity.TAG, "CreateA onResponse error: "+responseMessage.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) { //retrofit error
                    Log.d(MainActivity.TAG, "CreateA onFailure: "+t.getMessage());
                }
            });
        }else {
            Snackbar.make(view,"The input field is empty",Snackbar.LENGTH_LONG).show();
        }
    }
}
