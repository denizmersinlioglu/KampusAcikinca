package com.ggg.denizmersinlioglu.kampusacikinca;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;


import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainListFragment extends android.support.v4.app.Fragment {


   //  Container Activity must implement this interface
    public interface OnDealSelectedListener {
        public void onDealSelected(long selectedItemId);
    }

    OnDealSelectedListener mListener;

    //this would hold our deals from firebase
    private List<Deal> dealHolder = new ArrayList<Deal>();
    private ListView listViewDeals;
    private DealAdapter dealAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnDealSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_list, container, false);
    }


        @Override
    public void onStart() {
        super.onStart();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference dealDatabaseRef = database.getReference("Deals");
            dealAdapter = new DealAdapter(this.getActivity(), R.layout.list_row, dealHolder );

            // This method will be invoked any time the data changes in the database.
            // It will be invoked as soon as we connect to the database
            // Therefore we can get the initial snapshot of the data in the database.
            dealDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //get all the children at this level.
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                    for(DataSnapshot child : children)
                    {
                        Deal deal = child.getValue(Deal.class);
                        dealHolder.add(deal);
                        dealAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

                listViewAdapterClickListenerImplementation();

    }

    private void listViewAdapterClickListenerImplementation() {
        listViewDeals = getView().findViewById(R.id.deal_list);
        dealAdapter.notifyDataSetChanged();
        listViewDeals.setAdapter(dealAdapter);

        listViewDeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dealAdapter.notifyDataSetChanged();
                Intent intent = new Intent(getActivity(),RoomActivity.class);
                String authEmail = dealHolder.get(i).getUser().getEmail();
                User m_user = new User(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                updateDatabase(i, m_user);
                intent.putExtra("authEmail",authEmail);
                startActivity(intent);

            }
        });
    }

    private String arrangeEmailToBePath(String email) {
        String output = email.replace(".","-");
        return output;
    }

    private void updateDatabase(int i, User user){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String path = arrangeEmailToBePath(dealHolder.get(i).getUser().getEmail());
        DatabaseReference listDatabaseRef = database.getReference("Rooms/"+path+"/roomParticipantList");
        String key = arrangeEmailToBePath(user.getEmail());
        listDatabaseRef.child(key).setValue(user);
    }
}
