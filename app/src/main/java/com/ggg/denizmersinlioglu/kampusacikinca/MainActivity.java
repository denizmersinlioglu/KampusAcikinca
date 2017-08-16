package com.ggg.denizmersinlioglu.kampusacikinca;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements MainListFragment.OnDealSelectedListener {

    private static final int REQUEST_LOCATION = 12;
    private Location mLocation;
    private TextView statusTextView;
    private FusedLocationProviderClient mFusedLocationClient;
    private Button subs;
    private FirebaseAuth mAuth;

    //   private ShareActionProvider mShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Coupons");
        mAuth = FirebaseAuth.getInstance();
        subs = (Button) findViewById(R.id.subs);
        statusTextView = (TextView) findViewById(R.id.Status);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    public static void sendNotificationToUser(String user, final String _message) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference notifications = database.getReference("notifications");

        Map notification = new HashMap<>();
        notification.put("campusName", user);
        notification.put("message", _message);

        notifications.push().setValue(notification);
    }
    @Override
    protected void onStart() {
        super.onStart();
        createLocationRequest();
        getLastLocation();

        subs.setOnClickListener(new View.OnClickListener() {
            public static final String TAG = "SUBS";

            @Override
            public void onClick(View v) {
                // [START subscribe_topics]
                FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");
                // [END subscribe_topics]

                sendNotificationToUser("campusName", "Hi there puf!");
                // Log and toast
                String msg = "Message sent";
                Log.d(TAG, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.Create_new_deal:
                Intent createDeal_Intent = new Intent(this, CreateDealActivity.class);
                startActivity(createDeal_Intent);
                return true;
            case R.id.action_Settings:
                Intent settings_Intent = new Intent(this, SettingsActivity.class);
                startActivity(settings_Intent);
                return true;
            case R.id.menu_sign_out:
                mAuth.signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDealSelected(long selectedItemId) {

    }

    private void getLastLocation() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

           return;
        }
        statusTextView.setText("Location sent");
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mLocation = location;
                            sendLocationToFireBase();
                            statusTextView.setText(location.toString());
                        } else {
                            statusTextView.setText("CUCUK");
                        }
                    }
                });

    }

    private boolean sendLocationToFireBase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();


        //Can be changed with SQLite user.
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        final String userPath = arrangeEmailToBePath(email);
        final DatabaseReference userDatabaseRef = database.getReference("User/"+userPath);
        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get all the children at this level.
                    userDatabaseRef.child("Location").setValue(mLocation);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        return true;
    }

    private String arrangeEmailToBePath(String email) {
        String output = email.replace(".","-");
        return output;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }
}
