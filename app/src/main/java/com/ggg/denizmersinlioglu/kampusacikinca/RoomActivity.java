package com.ggg.denizmersinlioglu.kampusacikinca;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class RoomActivity extends Activity {

    public static String roomPath;

    private String authEmail ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        CreateRoomPathForFragments();

        //Initial screen is the chat screen
        Fragment chatFragment = new RoomChatFragment();
        doFrameLayoutTransaction(R.id.room_frameLayout,chatFragment);

        Fragment roomListFragment = new RoomMainListFragment();
        doFrameLayoutTransaction(R.id.room_list_frameLayout,roomListFragment);
        //send the paths to navigate in firebase
        sendPathToTheFragments(chatFragment);
        sendPathToTheFragments(roomListFragment);

    }

    private void CreateRoomPathForFragments() {
        getIntents();
        //Create room object to the Firebase
        //To the path determined by user email address.
        //It need to be unique room according to Firebase features.
        roomPath = arrangeEmailToBePath(authEmail);

    }

    private String arrangeEmailToBePath(String email) {
        String output = email.replace(".","-");
        return output;
    }

    private void getIntents() {
        authEmail = getIntent().getStringExtra("authEmail");
    }

    private void doFrameLayoutTransaction(int id, Fragment fragment) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(id, fragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    //Sends the parth of Room in the firebase
    private void sendPathToTheFragments(Fragment fragment){
        Bundle bundle = new Bundle();
        bundle.putString("RoomPath", roomPath);
        // set Fragmentclass Arguments
        fragment.setArguments(bundle);
    }
    private void navigateToMainPage(){
        Intent myIntent = new Intent(this,MainActivity.class);
        startActivity(myIntent);

    }

    @Override
    public void onBackPressed() {
        LeaveTheRoom();
    }

    private void LeaveTheRoom(){
        deleteUserFromDatabase();
        navigateToMainPage();
    }

    private void deleteUserFromDatabase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference listDatabaseRef = database.getReference("Rooms/"+roomPath+"/roomParticipantList");
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        DatabaseReference userDatabaseRef = database.getReference("Rooms/"+roomPath+"/roomParticipantList/"+userName+"participantStatus");

        Query queryUserRef = listDatabaseRef.orderByChild("userName").equalTo(userName);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().setValue(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        queryUserRef.addValueEventListener(listener);
        queryUserRef.removeEventListener(listener);


    }

}
