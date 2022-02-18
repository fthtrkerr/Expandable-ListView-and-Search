package com.application.szlk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import javax.microedition.khronos.opengles.GL;

public class ContentActivity extends AppCompatActivity {
TextView baslik,aciklama;
ImageView resim;
FirebaseDatabase database;
DatabaseReference reference;
String altBaslik,ustBaslik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        baslik = findViewById(R.id.baslik);
        aciklama = findViewById(R.id.aciklama);
        resim = findViewById(R.id.resim);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        Toast.makeText(this, getIntent().getExtras().getString("altBaslik"), Toast.LENGTH_SHORT).show();

        altBaslik = getIntent().getExtras().getString("altBaslik");
        ustBaslik = getIntent().getExtras().getString("ustBaslik");
        Log.e("ContentActivity",altBaslik+" "+ustBaslik);
        reference.child("veriler").child(ustBaslik).child(altBaslik).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    Map map = (Map) dataSnapshot.getValue();
                    baslik.setText(altBaslik);
                    String str = map.get("aciklama").toString();
                    aciklama.setMovementMethod(new ScrollingMovementMethod());
                    aciklama.setText(Html.fromHtml(str));
                    Glide.with(ContentActivity.this).load(map.get("resimLink").toString()).into(resim);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}