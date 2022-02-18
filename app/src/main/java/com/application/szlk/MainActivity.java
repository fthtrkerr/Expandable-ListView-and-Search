package com.application.szlk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.szlk.ContentActivity;
import com.application.szlk.ExpandleAdapter;
import com.application.szlk.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.TRANSPARENT;

public class MainActivity extends AppCompatActivity {
    private ExpandableListView listView;
    private ExpandleAdapter listAdapter;
    private List<String> listDataHeader;
    List<String> veri = new ArrayList<>();
    List<HashMap<String, Object>> lv_country = new ArrayList<>();
    List<HashMap<String, Object>> search_item = new ArrayList<>();
    int i;
    FirebaseDatabase database;
    DatabaseReference reference;
    CountDownTimer count;
    EditText search;
    private HashMap<String, List<String>> listHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        listHash = new HashMap<>();
        search = findViewById(R.id.search_bar);
        listDataHeader = new ArrayList<>();
        listView = (ExpandableListView) findViewById(R.id.list);
//initData();

//one-signal


        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Log.e("DataHeader", String.valueOf(lv_country.get(i).get("Name")));

                TextView textView = view.findViewById(R.id.lblListItem);
                Intent intent = new Intent(MainActivity.this, ContentActivity.class);

                //Toast.makeText(MainActivity.this, String.valueOf(l), Toast.LENGTH_SHORT).show();

                intent.putExtra("ustBaslik",String.valueOf(lv_country.get(i).get("Name")));
                intent.putExtra("altBaslik", textView.getText().toString());
                  Log.e("ust_alt",String.valueOf(lv_country.get(i).get("Name")));
                 startActivity(intent);
               Toast.makeText(MainActivity.this, textView.getText().toString(), Toast.LENGTH_SHORT).show();

                return true;
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int ii, int i1, int i2) {
             lv_country = search_item;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (search.getText().toString().length() == 0) {

                    lv_country = search_item;
                    listAdapter = new ExpandleAdapter(lv_country, getApplicationContext());
                    listView.setAdapter(listAdapter);

                    for (int i = 0; i < listAdapter.getGroupCount(); i++) {
                        listView.expandGroup(i);
                    }
                } else {

                    List<HashMap<String, Object>> lv_search = new ArrayList<>();

                    for (int i = 0; i < lv_country.size(); i++) {
                        List<HashMap<String, Object>> lv_searchstate = new ArrayList<>();

                        List<HashMap<String, Object>> lv_state = (List<HashMap<String, Object>>) lv_country.get(i).get("State");
                        for (int j = 0; j < lv_state.size(); j++) {
                            if (lv_state.get(j).get("Name").toString().toLowerCase().contains(search.getText().toString())) {
                                lv_searchstate.add(lv_state.get(j));
                            }
                        }

                        if (lv_searchstate.size() != 0) {
                            HashMap<String, Object> hashMap_search = new HashMap<>();
                            hashMap_search.put("Name", lv_country.get(i).get("Name").toString());
                            hashMap_search.put("State", lv_searchstate);

                            lv_search.add(hashMap_search);
                        }
                    }



                    listAdapter = new ExpandleAdapter(lv_search, getApplicationContext());
                    listView.setAdapter(listAdapter);

                    for (int i = 0; i < listAdapter.getGroupCount(); i++) {
                        listView.expandGroup(i);
                    }
                    lv_country = lv_search;

                    Log.e("Activity", String.valueOf(lv_country.size()));

                }
            }
        });

//Toast.makeText(this, "f", Toast.LENGTH_SHORT).show();
        reference.child("veriler").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (final DataSnapshot ds : snapshot.getChildren()) {
                    final String key = ds.getKey();
                    final HashMap<String, Object> hashMap_country = new HashMap<>();
                    hashMap_country.put("Name", ds.getKey());


                    listDataHeader.add(ds.getKey());

                    reference.child("veriler").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            List<String> arrays = new ArrayList<>();
// Log.e("veri","veri sıfırlandı");
                            List<HashMap<String, Object>> lv_state = new ArrayList<>();
                            HashMap<String, Object> hashMap_state = new HashMap<>();
                            for (DataSnapshot db : dataSnapshot.getChildren()) {

                                Log.e("MainActivity", db.getKey());
                                arrays.add(db.getKey());
                                hashMap_state = new HashMap<>();
                                hashMap_state.put("Name", db.getKey());
                                lv_state.add(hashMap_state);


                            }
                            hashMap_country.put("State", lv_state);
                            listHash.put(ds.getKey(), arrays);
                            lv_country.add(hashMap_country);
                            search_item.add(hashMap_country);

//
// arrays = veri;


                            Log.e("veri", "veri sıfırlandı");
                            veri.clear();


                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }


                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        geri_sayim();
    }

    private void listeyiDoldur(DataSnapshot dataSnapshot) {
        Toast.makeText(this, "çalıştı", Toast.LENGTH_SHORT).show();

    }

    public void geri_sayim() {

        count = new CountDownTimer(3000, 500) {
            public void onTick(long millisUntilFinished) {


            }

            @Override
            public void onFinish() {

                listAdapter = new ExpandleAdapter(lv_country, getApplicationContext());

                listView.setAdapter(listAdapter);

                Toast.makeText(MainActivity.this, String.valueOf( lv_country.size()), Toast.LENGTH_SHORT).show();
            }

        }.start();


    }




}