package com.example.a71p;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.a71p.data.DatabaseHelper;
import com.example.a71p.model.LostArticle;
import com.example.a71p.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button toCreateButton, toLFButton;
    FragmentContainerView fragmentContainerView;
    EditText nameIn, phoneIn, descIn, dateIn, locationIn;
    RadioGroup radioGroup;
    DatabaseHelper dbHelper;
    ListView listview;

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

        setContentView(R.layout.main_activity);
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
                        cursor.getString(6)));
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
                        cursor.getString(6)));
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

        String na, ph, de, da, lo;

        na = nameIn.getText().toString();
        ph = phoneIn.getText().toString();
        de = descIn.getText().toString();
        da = dateIn.getText().toString();
        lo = locationIn.getText().toString();

        Integer len = dbGet().size();


        if(na.length()>0 && ph.length()>0 && de.length()>0 && da.length()>0 && lo.length()>0){
            LostArticle newLostArticle = new LostArticle(len, condition, na, ph, de, da, lo);
            dbHelper.insertNew(newLostArticle);
            selectFragment(view);
        }
        else{
            Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_LONG).show();
        }
    }
}