package com.example.pickpacklogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class profilemain extends AppCompatActivity
{
   RecyclerView recview;
   profileadapter adapter;
   FloatingActionButton fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);
        setTitle("Search here..");

        recview=(RecyclerView)findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<profilemodel> options =
                new FirebaseRecyclerOptions.Builder<profilemodel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Profile"), profilemodel.class)
                        .build();

        adapter=new profileadapter(options);
        recview.setAdapter(adapter);

        fb=(FloatingActionButton)findViewById(R.id.fadd);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), profileadd.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
         getMenuInflater().inflate(R.menu.profilesearchmenu,menu);

         MenuItem item=menu.findItem(R.id.search);

         SearchView searchView=(SearchView)item.getActionView();

         searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
         {
             @Override
             public boolean onQueryTextSubmit(String s) {

                 processsearch(s);
                 return false;
             }

             @Override
             public boolean onQueryTextChange(String s) {
                 processsearch(s);
                 return false;
             }
         });

        return super.onCreateOptionsMenu(menu);
    }

    private void processsearch(String s)
    {
        FirebaseRecyclerOptions<profilemodel> options =
                new FirebaseRecyclerOptions.Builder<profilemodel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Profile").orderByChild("name").startAt(s).endAt(s+"\uf8ff"), profilemodel.class)
                        .build();

        adapter=new profileadapter(options);
        adapter.startListening();
        recview.setAdapter(adapter);

    }
}