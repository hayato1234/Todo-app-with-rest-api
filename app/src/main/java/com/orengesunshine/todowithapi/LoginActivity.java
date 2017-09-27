package com.orengesunshine.todowithapi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.orengesunshine.todowithapi.model.ResponseMessage;
import com.orengesunshine.todowithapi.model.User;
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


public class LoginActivity extends AppCompatActivity {

    public static final String USER_PREF = "userInfo";
    public static final String USER_API = "user_api";

    @BindView(R.id.email_field)
    EditText email;
    @BindView(R.id.password_field)
    EditText password;
    private Retrofit.Builder builder;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        builder = new Retrofit.Builder()
                .baseUrl(TodoClient.baseUrl)
                .addConverterFactory(GsonConverterFactory.create());
        ButterKnife.bind(this);

        //save api into sharedPreferences for later use
        pref = getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
    }
    @OnClick(R.id.login_button)
    public void onLoginClick(View view){
        Retrofit retrofit = builder.build();
        TodoClient client = retrofit.create(TodoClient.class);
        Call<User> call = client.loginUser(email.getText().toString(),password.getText().toString());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {

                if (response.body()!=null){ //success!
                    //saving the api here
                    SharedPreferences.Editor editor= pref.edit();
                    editor.putString(USER_API,response.body().getApi_key());
                    editor.apply();
                    Log.d(MainActivity.TAG, "loginA onClick: success "+pref.getString("user_api","none"));

                    //bring the user back to MainActivity if success
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                } else if (response.errorBody()!=null){ //error with error massage
                    //when there is error, retrofit doesn't convert to an object
                    //so needs to be done using gson
                    Gson gson = new Gson();
                    try {
                        ResponseMessage responseMessage = gson.fromJson(response.errorBody().string(),ResponseMessage.class);
                        Log.d(MainActivity.TAG, "loginA onClick: fail "+responseMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else { //there was error but it didn't include error message
                    Log.d(MainActivity.TAG, "loginA onClick: everything is null ");
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) { //retrofit error
                Log.d(MainActivity.TAG, "loginA onClick: fail "+t.getMessage());
            }
        });
    }
}
