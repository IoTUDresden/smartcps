package eu.vicci.ecosystem.standalone.controlcenter.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;

public class ProcessListItemAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public ProcessListItemAdapter(Context context, String[] values) {
        super(context, R.layout.fragment_process_overview_listitem, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.fragment_process_overview_listitem, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        // setting a listener to fav/unfav processes by clicking the imageview
        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // before setting the image resource a tag is also set which will be used here
                Object tag = v.getTag();
                int id = tag == null ? -1 : (Integer) tag; // unboxing to int via Wrapper class

                // TODO Implement update model here
                switch(id){
                    case R.drawable.process_fav:
                        ((ImageView)v).setTag(R.drawable.process_default);
                        ((ImageView)v).setImageResource(R.drawable.process_default);
                        break;
                    case R.drawable.process_default:
                        ((ImageView)v).setTag(R.drawable.process_fav);
                        ((ImageView)v).setImageResource(R.drawable.process_fav);
                        break;
                }
            }
        });

        textView.setText(values[position]);

        // Change the icon for favorite and default processes
        String s = values[position];
        if (s.startsWith("Dummy")) {
            imageView.setTag(R.drawable.process_fav);
            imageView.setImageResource(R.drawable.process_fav);
        } else {
            imageView.setTag(R.drawable.process_default);
            imageView.setImageResource(R.drawable.process_default);
        }

        return rowView;
    }

}