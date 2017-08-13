package com.ggg.denizmersinlioglu.kampusacikinca;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateDealFragment extends Fragment {

    private EditText editText_dealName;
    private CheckBox privateCheckBox;
    private Spinner campusSelection;
    private Spinner restaurantSelection;
    private EditText editText_descriptionText;
    private Button submitButton;
    private Spinner timeSpinner;
    private Spinner maxParticipantSpinner;
    private EditText editText_maxTotalAmount;



    private DatabaseReference databaseDealReference;
    private DatabaseReference databaseRoomReference;

    private long maxTotalAmount;
    private int maxParticipant;
    private int time;
    private String dealName;
    private boolean isPrivate;
    private String campusName;
    private String restaurantName;
    private String description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        databaseDealReference = FirebaseDatabase.getInstance().getReference("Deals");
        databaseRoomReference = FirebaseDatabase.getInstance().getReference("Rooms");
        return inflater.inflate(R.layout.fragment_create_deal, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        initializeUI();
        setSpinnersAdapter();

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
                {
                    if(checkInputDataFromUI()) {
                        createNewDeal();
                        navigateTheRoomYouCreated();
                    }
                }
            }
        );
    }

    private void initializeUI() {
        //<--------------  Initialization zone --------------->

        editText_dealName = (EditText) getView().findViewById(R.id.deal_name);
        privateCheckBox = (CheckBox) getView().findViewById(R.id.private_deal_checkbox);
        campusSelection = (Spinner) getView().findViewById(R.id.select_campus_spinner);
        restaurantSelection = (Spinner) getView().findViewById(R.id.select_restaurant_spinner);
        editText_descriptionText = (EditText) getView().findViewById(R.id.description_text);
        submitButton = (Button) getView().findViewById(R.id.submit_button);
        timeSpinner = (Spinner) getView().findViewById(R.id.create_deal_time_spinner);
        maxParticipantSpinner = (Spinner) getView().findViewById(R.id.create_deal_maxParticipant_spinner);
        editText_maxTotalAmount = (EditText) getView().findViewById(R.id.create_deal_maxTotalAmount_EditText);
        //<--------------------------------------------------->

    }

    private void navigateTheRoomYouCreated(){
        Intent myIntent = new Intent(this.getActivity(),RoomActivity.class);
        String authEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        myIntent.putExtra("authEmail",authEmail);
        startActivity(myIntent);
    }

    private void setSpinnersAdapter() {
        ArrayAdapter<String> campusAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"campus0","campus1","campus2","campus3","campus4","campus5"});
        campusSelection.setAdapter(campusAdapter);

        ArrayAdapter<String> restaurantAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"restaurant0","restaurant1","restaurant2","restaurant3","restaurant4","restaurant5"});
        restaurantSelection.setAdapter(restaurantAdapter);

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","20","25","30","35","40","45","50","55","60"});
        timeSpinner.setAdapter(timeAdapter);

        ArrayAdapter<String> participantAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"1","2","3","4","5","6","7","8","9","10"});
        maxParticipantSpinner.setAdapter(participantAdapter);
    }

    //Set the variables from interface
    private boolean checkInputDataFromUI() {
        boolean valid = true;

        dealName = editText_dealName.getText().toString();
        if (TextUtils.isEmpty(dealName)) {
            editText_dealName.setError("Required.");
            valid = false;
        } else {
            editText_dealName.setError(null);
        }

        isPrivate = privateCheckBox.isChecked();

        campusName = campusSelection.getSelectedItem().toString();
        if (TextUtils.isEmpty(campusName)) {
            TextView errorText = (TextView)campusSelection.getSelectedView();
            errorText.setError("Campus selection Required");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Campus selection Required");//changes the selected item text to this
            valid = false;
        } else {
            TextView errorText = (TextView)campusSelection.getSelectedView();
            errorText.setError(null);
        }

        restaurantName = restaurantSelection.getSelectedItem().toString();
        if (TextUtils.isEmpty(restaurantName)) {
            TextView errorText = (TextView)restaurantSelection.getSelectedView();
            errorText.setError("Restaurant selection Required");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Restaurant selection Required");//changes the selected item text to this
            valid = false;
        } else {
            TextView errorText = (TextView)restaurantSelection.getSelectedView();
            errorText.setError(null);
        }

        String timeIntHolder = timeSpinner.getSelectedItem().toString();
        time = Integer.parseInt(timeIntHolder);
        if (TextUtils.isEmpty(timeIntHolder)) {
            TextView errorText = (TextView)timeSpinner.getSelectedView();
            errorText.setError("Total time selection Required");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Total time selection Required");//changes the selected item text to this
            valid = false;
        } else {
            TextView errorText = (TextView)timeSpinner.getSelectedView();
            errorText.setError(null);
        }

        String participantIntHolder = maxParticipantSpinner.getSelectedItem().toString();
        maxParticipant = Integer.parseInt(participantIntHolder);
        if (TextUtils.isEmpty(participantIntHolder)) {
            TextView errorText = (TextView)maxParticipantSpinner.getSelectedView();
            errorText.setError("maxParticipant selection Required");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("maxParticipant selection Required");//changes the selected item text to this
            valid = false;
        } else {
            TextView errorText = (TextView)maxParticipantSpinner.getSelectedView();
            errorText.setError(null);
        }

        String maxTotalAmountLongHolder  = editText_maxTotalAmount.getText().toString();
        maxTotalAmount = Long.parseLong(maxTotalAmountLongHolder);
        if (TextUtils.isEmpty(maxTotalAmountLongHolder)) {
            editText_maxTotalAmount.setError("Required.");
            valid = false;
        } else {
            editText_maxTotalAmount.setError(null);
        }

        description = editText_descriptionText.getText().toString();
        if (TextUtils.isEmpty(description)) {
            editText_descriptionText.setError("Required.");
            valid = false;
        } else {
            editText_descriptionText.setError(null);
        }

        return valid;
    }

    private void createNewDeal() {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String userName = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();

        User user = new User(email,userName);

        Room room = new Room(dealName, new User(email,userName), maxParticipant,maxTotalAmount,isPrivate,time);
        room.getRoomParticipantList().add(user);

        Deal newDeal = new Deal(time,isPrivate,dealName,campusName,restaurantName,description);
        newDeal.setUser(user);
        databaseDealReference.child(arrangeEmailToBePath(email)).setValue(newDeal);
        databaseRoomReference.child(arrangeEmailToBePath(email)).setValue(room);
    }

    private String arrangeEmailToBePath(String email) {
        String output = email.replace(".","-");
        return output;
    }


}
