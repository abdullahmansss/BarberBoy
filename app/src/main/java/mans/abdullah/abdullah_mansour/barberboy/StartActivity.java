package mans.abdullah.abdullah_mansour.barberboy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity
{
    FloatingActionButton floatingActionButton;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    BookingAdapter bookingAdapter;

    List<BookList> bookLists;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        floatingActionButton = findViewById(R.id.booking_fab);
        recyclerView = findViewById(R.id.recyclerview);

        bookLists = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getApplicationContext());

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("allbooks");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        bookingAdapter = new BookingAdapter(getApplicationContext(), bookLists);

        recyclerView.setAdapter(bookingAdapter);

        progressDialog = new ProgressDialog(StartActivity.this);
        progressDialog.setMessage("Geting Books ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                bookLists.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    BookList bookList = postSnapshot.getValue(BookList.class);
                    bookLists.add(bookList);
                }

                bookingAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(getApplicationContext() , databaseError.getMessage() , Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BookingActivity.class);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed()
    {
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                Intent n = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(n);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
