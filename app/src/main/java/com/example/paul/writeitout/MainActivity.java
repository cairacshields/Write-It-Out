package com.example.paul.writeitout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        tv = (TextView)findViewById(R.id.items_layout);
        if(mFirebaseUser == null){
            //Check to see if the user is logged in,
            //If they are not, launch the login activity
            loadLogin();
        }else{
            mUserId = mFirebaseUser.getUid();

            //Set up ListView

            ListView listView = (ListView)findViewById(R.id.listView);
            final ArrayAdapter<String> adapter =new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.items_layout);
            listView.setAdapter(adapter);

            //Add Items via the Button and EditText
            final EditText text = (EditText)findViewById(R.id.todoText);
            Button button = (Button)findViewById(R.id.addButton);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Here is where we communicate with the firebase database.
                    //we need to branch down the file tree to the specific location where we want to upload our new data.
                    //In this case we want our data to be stored in a node entitled 'title'.
                    mDatabase.child("users").child(mUserId).child("items").push().child("title").setValue(text.getText().toString());
                    text.setText("");
                }
            });

            mDatabase.child("users").child(mUserId).child("items").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    adapter.add((String)dataSnapshot.child("title").getValue());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                    adapter.remove((String)dataSnapshot.child("title").getValue());
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.logout:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
