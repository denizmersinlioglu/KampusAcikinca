package com.ggg.denizmersinlioglu.kampusacikinca;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    private ImageView profilePicture_ImageView;
    private TextView userName_TextView;
    private TextView email_TextView;
    private Spinner campusSpinner;
    private TextView campusTextView;
    private EditText description_EditText;
    private Button doneButton;

    private String userName;
    private String email;
    private String campusName;
    private String description;

    private SQLiteDatabase db;

    private Cursor userCursor;

    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        profilePicture_ImageView = (ImageView) getView().findViewById(R.id.UserProfile_picture);
        userName_TextView = (TextView) getView().findViewById((R.id.UserProfile_Name));
        email_TextView = (TextView) getView().findViewById((R.id.UserProfile_Email));
        campusSpinner = (Spinner) getView().findViewById(R.id.UserProfile_campus_spinner);
        description_EditText = (EditText) getView().findViewById(R.id.UserProfile_description_text);
        doneButton = (Button) getView().findViewById(R.id.UserProfile_done_button);
        campusTextView = (TextView) getView().findViewById((R.id.UserProfile_CurrentCampus));

        ArrayAdapter<String> campusAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"campus0","campus1","campus2","campus3","campus4","campus5"});
        campusSpinner.setAdapter(campusAdapter);

        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if(checkData()) {
                    new UpdateDrinkTask().execute(1);
                }
            }
        });

        takeInputFromSQL();
        setUI();
    }

    private boolean checkData() {
        boolean valid = true;

        userName = userName_TextView.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            userName_TextView.setError("Required.");
            valid = false;
        } else {
            userName_TextView.setError(null);
        }

        campusName = campusSpinner.getSelectedItem().toString();
        if (TextUtils.isEmpty(campusName)) {
            TextView errorText = (TextView)campusSpinner.getSelectedView();
            errorText.setError("Campus selection Required");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Campus selection Required");//changes the selected item text to this
            valid = false;
        } else {
            TextView errorText = (TextView)campusSpinner.getSelectedView();
            errorText.setError(null);
        }


        description = description_EditText.getText().toString();
        if (TextUtils.isEmpty(description)) {
            description_EditText.setError("Required.");
            valid = false;
        } else {
            description_EditText.setError(null);
        }

        return valid;
    }

    private void takeInputFromSQL(){
        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new KampusAcikincaUserDatabaseHelper(this.getActivity());
            db = starbuzzDatabaseHelper.getReadableDatabase();
            userCursor = db.query("MY_USER",
                    new String[]{"NAME", "EMAIL", "CAMPUS","DESCRIPTION"}, "_id = 1",
                    null, null, null, null);
            if(userCursor.moveToFirst()){
                userName = userCursor.getString(0);
                email = userCursor.getString(1);
                campusName = userCursor.getString(2);
                description = userCursor.getString(3);

            }
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this.getActivity(), "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void setUI()
    {
        userName_TextView.setText(userName);
        email_TextView.setText(email);
        campusTextView.setText(campusName);
        description_EditText.setText(description);

    }

    private class UpdateDrinkTask extends AsyncTask<Integer, Void, Boolean> {
        ContentValues userValues;

        protected void onPreExecute() {
            campusName = campusSpinner.getSelectedItem().toString();

            userValues = new ContentValues();
            userValues.put("CAMPUS", campusName);
        }

        protected Boolean doInBackground(Integer... users) {
            int userNo = users[0];
            SQLiteOpenHelper kampusAcikincaUserDatabaseHelper = new KampusAcikincaUserDatabaseHelper(getContext());
            try {
                SQLiteDatabase db = kampusAcikincaUserDatabaseHelper.getWritableDatabase();
                db.update("MY_USER", userValues,
                        "_id = ?", new String[]{Integer.toString(userNo)});
                db.close();
                return true;
            } catch (SQLiteException e) {
                return false;
            }
        }

        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(getContext(),
                        "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}

