package com.somitykeeper.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationBell extends AppCompatActivity {

    public boolean isavailnotiy = false;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private RecyclerView recyclerView;
    public static List<NotificationModel> list;
    private ProgressBar progressBar;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_bell);
//        if (isavailnotiy){
//            setContentView(R.layout.activity_notification_bell);
//        }else {
//            setContentView(R.layout.no_notifaction);
//        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView = findViewById(R.id.noti_back);
        recyclerView = findViewById(R.id.notification_rv);
        progressBar = findViewById(R.id.noti_progress);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        list = new ArrayList<>();

        final NotificationAdapter adapter = new NotificationAdapter(list);
        recyclerView.setAdapter(adapter);

        myRef.child("Notification").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    list.add(new NotificationModel(dataSnapshot1.child("title").getValue().toString(),dataSnapshot1.child("description").getValue().toString(),dataSnapshot1.child("offer_img").getValue().toString(),dataSnapshot1.child("date").getValue().toString()));
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NotificationBell.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                finish();
            }
        });
        recyclerView.setLayoutManager(layoutManager);
    }

    public void checknotfy(){
        //todo conditon such as pass intent with notify
    }
}