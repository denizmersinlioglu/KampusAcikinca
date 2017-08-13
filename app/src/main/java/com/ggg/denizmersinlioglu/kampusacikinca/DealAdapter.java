package com.ggg.denizmersinlioglu.kampusacikinca;


import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.ggg.denizmersinlioglu.kampusacikinca.Deal;

public class DealAdapter extends ArrayAdapter<Deal> {

    private int resource;
    private LayoutInflater inflater;
    private Context context;

    public DealAdapter ( Context ctx, int resourceId, List<Deal> objects) {
        super( ctx, resourceId, objects );
        resource = resourceId;
        inflater = LayoutInflater.from( ctx );
        context=ctx;
    }


    @Override
    public View getView ( int position, View convertView, ViewGroup parent ) {
        convertView = inflater.inflate( resource, null );

        Deal deal = getItem( position );

        TextView dealName_textView = (TextView) convertView.findViewById(R.id.deal_name_textView);
        dealName_textView.setText(deal.getDealName());

        TextView userName_textView = (TextView) convertView.findViewById(R.id.userName_textView);
        userName_textView.setText(deal.getUserName());

        TextView deal_duration_textView = (TextView) convertView.findViewById(R.id.deal_duration);
        deal_duration_textView.setText("" + deal.getTime());

        return convertView;
    }
}