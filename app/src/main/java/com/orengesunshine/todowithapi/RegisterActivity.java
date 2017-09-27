package com.orengesunshine.todowithapi;

import android.content.Intent;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.name_field)
    EditText name;
    @BindView(R.id.email_field)
    EditText email;
    @BindView(R.id.password_field)
    EditText password;
    @BindView(R.id.sign_up_button)
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        //when button is clicked, make api call
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(TodoClient.baseUrl)
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit = builder.build();
                TodoClient client = retrofit.create(TodoClient.class);
                //apply name, email, and password as parameter
                Call<ResponseMessage> call = client.registerUser(name.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString());
                call.enqueue(new Callback<ResponseMessage>() {
                    @Override
                    public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                        if (response.body()!=null){ //success!
                            Log.d(MainActivity.TAG, "onClick: success "+response.body().getMessage());

                            //bring the user back to MainActivity if success
                            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                        }else if (response.errorBody()!=null){ //error with a message
                            Gson gson = new Gson();
                            try {
                                ResponseMessage message = gson.fromJson(response.errorBody().string(),ResponseMessage.class);
                                Log.d(MainActivity.TAG, "registerA onClick: error: "+message.getMessage());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else { //error without message
                            Log.d(MainActivity.TAG, "registerA onClick: everything is null");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMessage> call, Throwable t) {
                        Log.d(MainActivity.TAG, "onClick: error");
                    }
                });
            }
        });
    }
}
