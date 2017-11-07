package com.example.bernardlao.favoriteplaces;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SetLocationActivity extends AppCompatActivity {
    DBUtils db;
    ArrayList<String> types = new ArrayList<String>();
    EditText txtPlaceName,txtAddress;
    Spinner cmbTypes;
    Button btnSave;
    RatingBar rateRate;
    Location loc;
    List<Address> address;
    double lati,longi;
    boolean isSave = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        db = new DBUtils(getApplicationContext());
        initialize();
    }
    private void initialize(){
        address = new ArrayList<>();
        txtPlaceName = (EditText)findViewById(R.id.txtPlaceName);
        txtAddress = (EditText)findViewById(R.id.txtAddress);
        cmbTypes = (Spinner)findViewById(R.id.cmbTypes);
        btnSave = (Button)findViewById(R.id.btnSave);
        rateRate = (RatingBar)findViewById(R.id.rateRate);
        prepareSpinner();
        setIfUpdate();
        setEvents();
    }
    private void setIfUpdate(){
        Intent in = getIntent();
        isSave = in.getBooleanExtra("isSave",true);
        if(!isSave) {
            loc = (Location) in.getSerializableExtra("location");
            lati = loc.getLati();
            longi = loc.getLongi();
            txtPlaceName.setText(loc.getPlaceName());
            txtAddress.setText(loc.getAddressName());
            rateRate.setRating((float)loc.getRate());
            String type = loc.getType();
            for(int i = 0; i < cmbTypes.getCount();i++){
                String t = cmbTypes.getItemAtPosition(i).toString();
                if(t.equals(type))
                    cmbTypes.setSelection(i);
            }
        }
    }
    private void prepareSpinner(){
        types.clear();
        types.add("Hospital");
        types.add("Restaurant");
        types.add("School");
        types.add("Office");
        types.add("Park");
        bindValueToList();
    }
    private void bindValueToList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SetLocationActivity.this, android.R.layout.simple_list_item_1, types);
        cmbTypes.setAdapter(adapter);
        cmbTypes.setSelection(0);
    }
    private void setEvents(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSave && isAllValid()){
                    saveDB();
                }else if(!isSave && isAllValid()) {
                    updateDB();
                }
            }
        });
    }
    private void saveDB(){
        long res = db.addLocation(LoginActivity.loggedInUser,txtPlaceName.getText().toString(),
                rateRate.getRating(),lati,longi,txtAddress.getText().toString(), cmbTypes.getSelectedItem().toString());
        if(res > -1){
            Toast.makeText(getApplicationContext(),"Save successfully.",Toast.LENGTH_SHORT).show();
            setResult(1);
            finish();
        }
    }
    private void updateDB(){
        long res = db.updateLocation(loc.getLocationID(),txtPlaceName.getText().toString(),
                rateRate.getRating(),lati,longi,txtAddress.getText().toString(),cmbTypes.getSelectedItem().toString());
        if(res > -1){
            Toast.makeText(getApplicationContext(),"Update successfully.",Toast.LENGTH_SHORT).show();
            setResult(1);
            finish();
        }
    }
    private boolean isAllValid(){
        txtPlaceName.setText(txtPlaceName.getText().toString().trim());
        txtAddress.setText(txtAddress.getText().toString().trim());
        if(txtAddress.getText().toString().equals("") || txtPlaceName.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please fill up the information.",Toast.LENGTH_SHORT).show();
            return false;
        }
        address.clear();
        Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
        String loc = txtAddress.getText().toString();
        try{
            address = geo.getFromLocationName(loc,5);
            if(address.size() > 0){
                lati = address.get(0).getLatitude();
                longi = address.get(0).getLongitude();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Location Invalid. Please specify the address clearly.",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(isSave) {
            if (db.isLocationExist(txtPlaceName.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Place name exist. Please specify another name.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            if(db.isLocationExistOnUpdate(txtPlaceName.getText().toString(),String.valueOf(this.loc.getLocationID()))){
                Toast.makeText(getApplicationContext(), "Place name exist. Please specify another name.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
