package com.undeniabledreams.crud_application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditActivity extends AppCompatActivity {
    //Import the Packages and set the variable for all your EditText input
    private EditText editName, editAge, editMobile, editEmail, editTextID;
    private Button buttonUpdate;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Get the edit by Id's
        editName = findViewById(R.id.editName);
        editAge = findViewById(R.id.editAge);
        editMobile = findViewById(R.id.editMobile);
        editEmail = findViewById(R.id.editEmail);
        buttonUpdate = findViewById(R.id.btnUpdate);

        // Get all data from the Detail Activity from the onClick Listener ContentLayout
        final String id = Objects.requireNonNull(getIntent().getExtras()).getString("id");
        String name = Objects.requireNonNull(getIntent().getExtras()).getString("name");
        String age = getIntent().getExtras().getString("age");
        String mobile = getIntent().getExtras().getString("mobile");
        String email = getIntent().getExtras().getString("email");

        // Set the previous value to the Edit Text input
        editName.setText(name);
        editAge.setText(age);
        editMobile.setText(mobile);
        editEmail.setText(email);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = editName.getText().toString();
                final String age = editAge.getText().toString();
                final String mobile = editMobile.getText().toString();
                final String email = editEmail.getText().toString();

                if (name.isEmpty()){
                    editName.setError("Please enter name");
                }else if (age.isEmpty()){
                    editAge.setError("Please enter age");
                }else if (mobile.isEmpty()){
                    editMobile.setError("Please enter mobile");
                }else if (email.isEmpty()){
                    editEmail.setError("Please enter email");
                }else{
                    String url_edit = "http://192.168.43.48/real_crud_volley/updateData.php";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url_edit,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {

                                        JSONObject jsonObject = new JSONObject(response);
                                        String success = jsonObject.getString("success");

                                        if (success.equals("1")){
                                            Toast.makeText(EditActivity.this, "Yea, User Updated", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(EditActivity.this, MainActivity.class));
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(EditActivity.this, "Server error "+error.toString(), Toast.LENGTH_SHORT).show();
                                }
                             }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params_update = new HashMap<>();
                            params_update.put("id",id);
                            params_update.put("name", name);
                            params_update.put("age", age);
                            params_update.put("mobile", mobile);
                            params_update.put("email", email);

                            return params_update;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(EditActivity.this);
                    requestQueue.add(stringRequest);
                    }
            }
        });
    }
}