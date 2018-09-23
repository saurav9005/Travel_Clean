package com.example.user.travelclean;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {
    private EditText name, email, password, c_password;
    private Button btn_register;
    private ProgressBar loading;
    AwesomeValidation awesomeValidation;
    private static String URL_REGIST = "http://192.168.1.2/travel_clean/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        loading = findViewById(R.id.loading);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        c_password = findViewById(R.id.c_password);
        btn_register = findViewById(R.id.btn_register);

        awesomeValidation.addValidation(RegisterActivity.this, R.id.name, "[a-zA-Z\\s]+", R.string.name);
        awesomeValidation.addValidation(RegisterActivity.this, R.id.email, android.util.Patterns.EMAIL_ADDRESS,R.string.email);
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(RegisterActivity.this, R.id.password, regexPassword, R.string.password);
        awesomeValidation.addValidation(RegisterActivity.this, R.id.c_password, R.id.password, R.string.c_password);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()){
                    Regist();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void Regist(){
        loading.setVisibility(View.VISIBLE);
        btn_register.setVisibility(View.GONE);

        final String name = this.name.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String password = this.password.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response:",response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            //Log.i("success",success);
                            if (success.equals("true")){
                                Toast.makeText(RegisterActivity.this, "Register Success!", Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                btn_register.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Register Error!" + e.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            btn_register.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "Register Error!" + error.toString(), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        btn_register.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
