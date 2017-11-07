package com.example.bernardlao.favoriteplaces;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DBUtils db;
    ListView lstPlaces;
    CustomAdapter adapter;
    Button btnViewAll;
    public static List<Location> locations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBUtils(getApplicationContext());
        locations = new ArrayList<>();
        lstPlaces = (ListView)findViewById(R.id.lstPlaces);
        btnViewAll = (Button)findViewById(R.id.btnViewAll);
        loadList();
        setEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simplemenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menuAdd){
            Intent intent = new Intent(MainActivity.this,SetLocationActivity.class);
            startActivityForResult(intent,1);
        }else if(id == R.id.menuLogout){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            LoginActivity.loggedInUser = "";
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }//int locID, int userID, String pName, double rate, double latitude, double longitude,String addName,String type
    private void loadList(){
        locations.clear();
        Cursor c = db.selectCursor("SELECT * FROM tblLocation WHERE userID=" + LoginActivity.loggedInUser +
        " ORDER BY rate DESC");
        if(c.moveToFirst()){
            do{
                int locID = c.getInt(c.getColumnIndex("locationID"));
                int uID = c.getInt(c.getColumnIndex("userID"));
                String pName = c.getString(c.getColumnIndex("placeName"));
                double rate = c.getDouble(c.getColumnIndex("rate"));
                double latitude = c.getDouble(c.getColumnIndex("lati"));
                double longi = c.getDouble(c.getColumnIndex("longi"));
                String addName = c.getString(c.getColumnIndex("addressName"));
                String type = c.getString(c.getColumnIndex("type"));
                Location l = new Location(locID,uID,pName,rate,latitude,longi,addName,type);
                locations.add(l);
            }while(c.moveToNext());
            adapter = new CustomAdapter(MainActivity.this,locations);
            lstPlaces.setAdapter(adapter);
        }
        db.closeConnection();
    }
    private void setEvents(){
        lstPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,SetLocationActivity.class);
                intent.putExtra("isSave",false);
                intent.putExtra("location",locations.get(i));
                startActivityForResult(intent,1);
            }
        });
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MapViewActivity.class);
                intent.putExtra("index",-1);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 1){
            loadList();
        }
    }
}
