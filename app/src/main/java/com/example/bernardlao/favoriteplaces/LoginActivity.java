package com.example.bernardlao.favoriteplaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    DBUtils db;
    ClassCollection cc;
    EditText txtUsername,txtPassword;
    Button btnLogin;
    TextView lblRegister;
    public static String loggedInUser = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DBUtils(getApplicationContext());
        cc = new ClassCollection(getApplicationContext());
        initialize();
        setEvents();
    }
    private void initialize(){
        txtUsername = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        lblRegister = (TextView) findViewById(R.id.lblRegister);
    }
    private void setEvents(){
        txtUsername.setFilters(new InputFilter[]{cc.getAlphaNumericFilter()});
        txtPassword.setFilters(new InputFilter[]{cc.getAlphaNumericFilter()});
        lblRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loggedInUser = db.getUserIDIfExist(txtUsername.getText().toString(),txtPassword.getText().toString());
                if(!loggedInUser.equals("")){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"User Doesn't Exist",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
