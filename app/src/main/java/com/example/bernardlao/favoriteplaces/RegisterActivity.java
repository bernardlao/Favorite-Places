package com.example.bernardlao.favoriteplaces;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    ClassCollection cc;
    DBUtils db;
    EditText txtRegFullname, txtRegUsername,txtRegPassword, txtRegRetype;
    Button btnRegRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new DBUtils(getApplicationContext());
        cc = new ClassCollection(getApplicationContext());
        initialize();
        setEvents();
    }
    private void initialize(){
        txtRegFullname = (EditText)findViewById(R.id.txtRegFullname);
        txtRegPassword = (EditText)findViewById(R.id.txtRegPassword);
        txtRegUsername = (EditText)findViewById(R.id.txtRegUsername);
        btnRegRegister = (Button)findViewById(R.id.btnRegRegister);
        txtRegRetype = (EditText)findViewById(R.id.txtRegRetype);
    }
    private void setEvents(){
        txtRegUsername.setFilters(new InputFilter[]{cc.getAlphaNumericFilter()});
        txtRegPassword.setFilters(new InputFilter[]{cc.getAlphaNumericFilter()});
        btnRegRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isAllValid()){
                    long res = db.registerUser(txtRegFullname.getText().toString(),
                            txtRegUsername.getText().toString(),
                            txtRegPassword.getText().toString());
                    if(res > -1){
                        Toast.makeText(getApplicationContext(),"Register successfully.",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }
    private boolean isAllValid(){
        if(txtRegPassword.getText().toString().equals("") || txtRegUsername.getText().toString().equals("") ||
                txtRegRetype.getText().toString().equals("") || txtRegFullname.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please fill up the whole form.",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!txtRegPassword.getText().toString().equals(txtRegRetype.getText().toString())){
            Toast.makeText(getApplicationContext(),"Password doesn't match.",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(db.isUserExist(txtRegUsername.getText().toString())){
            Toast.makeText(getApplicationContext(),"User Exist.",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
