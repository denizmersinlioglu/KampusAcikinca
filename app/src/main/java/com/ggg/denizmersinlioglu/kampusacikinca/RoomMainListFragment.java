package com.ggg.denizmersinlioglu.kampusacikinca;



import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RoomMainListFragment extends Fragment {

    private String path;
    private FirebaseDatabase database;
    private DatabaseReference roomDatabaseRef;
    private ListView participantUserListView;
    private List<User> userHolder = new ArrayList<User>();
    private  UserAdapter userAdapter;

    public RoomMainListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        path = getArguments().getString("RoomPath");
        return inflater.inflate(R.layout.fragment_room_main_list, container, false);
    }



    @Override
    public void onStart() {
        super.onStart();

        database = FirebaseDatabase.getInstance();
        roomDatabaseRef = database.getReference("Rooms/"+path+"/roomParticipantList");


        // This method will be invoked any time the data changes in the database.
        // It will be invoked as soon as we connect to the database
        // Therefore we can get the initial snapshot of the data in the database.
        roomDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get all the children at this level.
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for(DataSnapshot child : children)
                {
                    User user = child.getValue(User.class);
                    userHolder.add(user);
                    userAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        userAdapter = new UserAdapter(this.getActivity(), R.layout.room_list_row, userHolder );
        participantUserListView = getView().findViewById(R.id.room_user_list);
        userAdapter.notifyDataSetChanged();
        participantUserListView.setAdapter(userAdapter);
    }

    private void doProfileOperation(View v){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.room_frameLayout, new RoomMainListFragment());
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }



}
