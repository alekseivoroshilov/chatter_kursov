package com.example.chat;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    EditText username, password;
    Button registerButton;
    String user, pass;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        registerButton = (Button)findViewById(R.id.registerButton);
        login = (TextView)findViewById(R.id.login);

        Firebase.setAndroidContext(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //startActivity(new Intent(Registration.this, Login.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if(user.equals("")){
                    username.setError("enter your nickname");
                }
                else if(pass.equals("")){
                    password.setError("enter the password");
                }
                else if(!user.matches("[A-Za-z0-9]+")){
                    username.setError("Unknown symbols used");
                }
                else if(user.length()<5){
                    username.setError("Nickname must be more than 5 letters");
                }
                else if(pass.length()<5){
                    password.setError("Password too short");
                }
                else {
                    final ProgressDialog pd = new ProgressDialog(Registration.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    String url = "https://chat-371b4.firebaseio.com/users.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            Firebase reference = new Firebase("https://chat-371b4.firebaseio.com/users/");

                            if(s.equals("null")) {
                                reference.child(user).child("password").setValue(pass);
                                Toast.makeText(Registration.this, "Registration successful", Toast.LENGTH_LONG).show();
                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        reference.child(user).child("password").setValue(pass);
                                        Toast.makeText(Registration.this, "Registration successful", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(Registration.this, "username already exists", Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }

                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse response = error.networkResponse;
                            if (error instanceof ServerError && response != null) {
                                try {
                                    String res = new String(response.data,
                                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                    // Now you can use any deserializer to make sense of data
                                    JSONObject obj = new JSONObject(res);
                                } catch (UnsupportedEncodingException e1) {
                                    // Couldn't properly decode data to string
                                    e1.printStackTrace();
                                } catch (JSONException e2) {
                                    // returned data is not JSONObject?
                                    e2.printStackTrace();
                                }
                            }
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(Registration.this);



                    rQueue.add(request);
                }
            }
        });

    }
}

/*
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.example.chat.UserDetails;
import com.example.chat.Services.IFireBaseAPI;
import com.example.chat.Services.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;

public class Registration extends AppCompatActivity {


    EditText et_Email, et_Password, et_FirstName, et_LastName;
    Button btn_Register;
    ProgressDialog pd;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Firebase.setAndroidContext(this);

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");

        et_Email = (EditText) findViewById(R.id.username);
        et_Password = (EditText) findViewById(R.id.password);
        //btn_Register = findViewById(R.id.registerButton);
    }

    public void btn_RegClick(View view) {
        if (et_Email.getText().toString().equals("")){
            et_Email.setError("Enter Valid Email");
        } else if (et_Password.getText().toString().equals("")) {
            et_Password.setError("Enter Password");
        } else {
            email = et_Email.getText().toString();
            RegisterUserTask t = new RegisterUserTask();
            t.execute();
        }
    }

    public class RegisterUserTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            IFireBaseAPI api = Tools.makeRetroFitApi();
            Call<String> call = api.getSingleUserByEmail(StaticInfo.UsersURL + "/" + email + ".json");
            try {
                return call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                pd.hide();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            try {
                if (jsonString.trim().equals("null")) {
                    Firebase firebase = new Firebase(StaticInfo.UsersURL);
                    firebase.child(email).child("FirstName").setValue(et_FirstName.getText().toString());
                    firebase.child(email).child("LastName").setValue(et_LastName.getText().toString());
                    firebase.child(email).child("Email").setValue(email);
                    firebase.child(email).child("Password").setValue(et_Password.getText().toString());
                    DateFormat dateFormat = new SimpleDateFormat("dd MM yy hh:mm a");
                    Date date = new Date();
                    firebase.child(email).child("Status").setValue(dateFormat.format(date));
                    Toast.makeText(getApplicationContext(), "Signup Success", Toast.LENGTH_SHORT).show();
                    pd.hide();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                    pd.hide();
                }
            } catch (Exception e) {
                pd.hide();
                e.printStackTrace();
            }
        }
    }


}*/