package com.example.a71p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a71p.data.DatabaseHelper;
import com.example.a71p.model.LostArticle;
import com.example.a71p.util.Util;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {
    Button toCreateButton, toLFButton;
    FragmentContainerView fragmentContainerView;
    EditText nameIn, phoneIn, descIn, dateIn, locationIn;
    TextView locationSearchedText;
    RadioGroup radioGroup;
    DatabaseHelper dbHelper;
    ListView listview;
    Double latitude, longitude;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.defaultfragment);
        dbHelper = new DatabaseHelper(getApplicationContext());

        toCreateButton = findViewById(R.id.toCreateButton);
        toLFButton = findViewById(R.id.toLFButton);
        fragmentContainerView = findViewById(R.id.fragmentContainerView);

        nameIn = findViewById(R.id.nameIn);
        phoneIn = findViewById(R.id.phoneIn);
        descIn = findViewById(R.id.descIn);
        dateIn = findViewById(R.id.dateIn);
        locationIn = findViewById(R.id.locationIn);

        latitude = null;
        longitude = null;

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(@NonNull Location location) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        setContentView(R.layout.main_activity);
    }

    public void getPlace(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }


    public void tryPlaces(View view){
        Intent placeIntent = new Intent(this, PlacesActivity.class);
        startActivityForResult(placeIntent,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            this.latitude = data.getDoubleExtra("lat",0);
            this.longitude = data.getDoubleExtra("lon",0);
        }
    }
    public void mapShowArticles(View view){
        Intent mapIntent = new Intent(this, MapsActivity.class);
        List<LostArticle> table = new ArrayList<>();
        table = dbGet();
        mapIntent.putExtra("table", (Serializable) table);
        startActivity(mapIntent);
    }

    public void selectArticleFragment(int position){
        Bundle bundle = new Bundle();
        bundle.putInt("key", position);
        Fragment removeAdvert = new RemoveAdvert();
        removeAdvert.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, removeAdvert).commit();
    }

    public void selectFragment(View view){
        Fragment fragment = null;
        Fragment fCreateAdvert = new CreateAdvert();
        Fragment fLostnFound = new LostnFound();
        Fragment fDefaultFragment = new DefaultFragment();

        switch (view.getId()){
            case R.id.toCreateButton:
                fragment = fCreateAdvert;
                break;
            case R.id.toLFButton:
                fragment = fLostnFound;
                break;
            default:
                fragment = fDefaultFragment;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment).commit();

    }

    public List<LostArticle> dbGet() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + Util.TABLE_NAME, null);

        ArrayList<LostArticle> table = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                table.add(new LostArticle(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        Double.parseDouble(cursor.getString(7)),
                        Double.parseDouble(cursor.getString(8))));
            } while (cursor.moveToNext());
        }
        return table;
    }

    public LostArticle dbGetArticle(Integer findID){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] args = {findID.toString()};
        Cursor cursor = db.rawQuery("select * from " + Util.TABLE_NAME + " where " + Util.ID + "= ?", args, null);

        ArrayList<LostArticle> table = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                table.add(new LostArticle(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        Double.parseDouble(cursor.getString(7)),
                        Double.parseDouble(cursor.getString(8))));
            } while (cursor.moveToNext());
        }
        return table.get(0);
    }

    public void fullUpdateID(){
        List<LostArticle> table = new ArrayList<>();
        table = dbGet();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Util.TABLE_NAME, null, null);
        int len = table.size();

        for(int i = 0; i<len; i++){
            LostArticle newEntry = table.get(i);
            newEntry.ID=i+1;
            dbHelper.insertNew(newEntry);
        }
        db.close();
    }

    public void dbDelete(Integer delID){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Util.TABLE_NAME, Util.ID + "=?", new String[]{delID.toString()});
        db.close();
        fullUpdateID();
    }

    public void deleteButton(View view){
        int pass = (int) view.getTag();
        dbDelete(pass);
        selectFragment(view);
    }

    public void dbInsert(View view){
        nameIn = findViewById(R.id.nameIn);
        phoneIn = findViewById(R.id.phoneIn);
        descIn = findViewById(R.id.descIn);
        dateIn = findViewById(R.id.dateIn);
        locationIn = findViewById(R.id.locationIn);
        String condition = "Lost";

        radioGroup = findViewById(R.id.radioGroup);
        if(radioGroup.getCheckedRadioButtonId() == R.id.foundIn){
            condition = "Found";
        }

        String nam, pho, dsc, dte, loc;
        Double lat, lon;

        nam = nameIn.getText().toString();
        pho = phoneIn.getText().toString();
        dsc = descIn.getText().toString();
        dte = dateIn.getText().toString();
        loc = locationIn.getText().toString();
        lat = this.latitude;
        lon = this.longitude;
        Integer len = dbGet().size();


        if(nam.length()>0 && pho.length()>0 && dsc.length()>0 && dte.length()>0 && loc.length()>0 && lat != null && lon != null){
            LostArticle newLostArticle = new LostArticle(len, condition, nam, pho, dsc, dte, loc, lat, lon);
            dbHelper.insertNew(newLostArticle);
            this.latitude = null;
            this.longitude = null;
            selectFragment(view);
        }
        else{
            Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_LONG).show();
        }
    }
}