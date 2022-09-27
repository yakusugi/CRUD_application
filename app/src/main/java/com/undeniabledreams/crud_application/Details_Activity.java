package com.undeniabledreams.crud_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class Details_Activity extends AppCompatActivity {
    // Initialize all the variables for out TextView from XML Activity
    TextView textViewName, textViewAge, textViewMobile, textViewEmail, textViewDate;
    Button buttonEdit, buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_);

        // Get them by Id's
        textViewName = findViewById(R.id.detail_name);
        textViewAge = findViewById(R.id.detail_age);
        textViewMobile = findViewById(R.id.detail_mobile);
        textViewEmail = findViewById(R.id.detail_email);
        textViewDate = findViewById(R.id.detail_date);

        //Button Delete and Edit
        buttonDelete = findViewById(R.id.btnDelete);
        buttonEdit = findViewById(R.id.btnEdit);

        // Get all data from the recyclerView from the onClick Listener ContentLayout
        final String id = getIntent().getExtras().getString("id");
        final String name = Objects.requireNonNull(getIntent().getExtras()).getString("name");
        final String age = getIntent().getExtras().getString("age");
        final String mobile = getIntent().getExtras().getString("mobile");
        final String email = getIntent().getExtras().getString("email");
        final String date = getIntent().getExtras().getString("date");

        // Set your keys gotten from RecyclerView Adapter to the TextView's
        textViewName.setText(name);
        textViewAge.setText(age);
        textViewMobile.setText(mobile);
        textViewEmail.setText(email);
        textViewDate.setText(date);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser();
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Details_Activity.this, EditActivity.class);

                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("age", age);
                intent.putExtra("mobile", mobile);
                intent.putExtra("email", email);

                startActivity(intent);
            }
        });


    }

    private void deleteUser() {

        final String id = Objects.requireNonNull(getIntent().getExtras()).getString("id");

        String url_delete = "http://192.168.0.44/delete.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_delete,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(Details_Activity.this, "User Deleted..", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Details_Activity.this, MainActivity.class));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Details_Activity.this, "Server error "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param_delete = new HashMap<>();
                param_delete.put("id",id);

                return param_delete;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}