package com.ggg.denizmersinlioglu.kampusacikinca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by DenizMersinlioglu on 11/08/2017.
 */

public class UserAdapter extends ArrayAdapter<User>{
    private int resource;
    private LayoutInflater inflater;
    private Context context;

    public UserAdapter ( Context ctx, int resourceId, List<User> objects) {
        super( ctx, resourceId, objects );
        resource = resourceId;
        inflater = LayoutInflater.from( ctx );
        context=ctx;
    }


    @Override
    public View getView (int position, View convertView, ViewGroup parent ) {
        convertView = inflater.inflate( resource, null );

        User user = getItem( position );

        TextView userName_textView = (TextView) convertView.findViewById(R.id.room_list_userName_textView);
        userName_textView.setText(user.getUserName());

        TextView userRating_textView = (TextView) convertView.findViewById(R.id.room_list_userRating);
        userRating_textView.setText(Long.toString(user.getUserRating()));

//        ImageView profilePic_ImageView = (ImageView) convertView.findViewById(R.id.room_user_profile_picture);
//        String uri = "drawable/" + user.getImage();
//        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
//        Drawable image = context.getResources().getDrawable(imageResource);
//        legendImage.setImageDrawable(image);


        return convertView;
    }
}
