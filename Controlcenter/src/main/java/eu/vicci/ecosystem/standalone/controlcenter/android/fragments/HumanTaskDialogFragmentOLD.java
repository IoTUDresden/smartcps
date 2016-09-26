package eu.vicci.ecosystem.standalone.controlcenter.android.fragments;


import java.util.ArrayList;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;


public class HumanTaskDialogFragmentOLD extends DialogFragment implements OnEditorActionListener,OnClickListener{
	
	public interface EditListener{
		void onFinishEdit(ListView mylist);
	}
	
	public static HumanTaskDialogFragmentOLD newInstance(){
		HumanTaskDialogFragmentOLD ht = new HumanTaskDialogFragmentOLD();
		return ht;
	}
	
	public HumanTaskDialogFragmentOLD() {

	}
	
	private EditText input;
	ArrayList<Object> list = new ArrayList<Object>();
    ArrayAdapter<Object> adapter;
	ListView mylist;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.human_task_list_input, container);
        getDialog().setTitle("Output Port");
        
        mylist = (ListView) view.findViewById(R.id.list);
        input = (EditText) view.findViewById(R.id.txtItem);      
        Button btn = (Button)view.findViewById(R.id.btnAdd);    
        Button confirmBtn = (Button) view.findViewById(R.id.confirmBtn);
        
        adapter = new ArrayAdapter<Object>(getActivity(), R.layout.human_task_simple_list_item, R.id.listItem, list);        
        
           
        //input.requestFocus();
        //getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //input.setOnEditorActionListener(this);
        
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
                list.add(input.getText().toString());
                input.setText("");
                adapter.notifyDataSetChanged();
			}
		});
        
        mylist.setAdapter(adapter);
        
        confirmBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// send to the list to reponse
				getTargetFragment().onActivityResult(getTargetRequestCode(), 1, getActivity().getIntent().putExtra("Array", list));
				//EditListener fragment = (EditListener)getTargetFragment();
	            //fragment.onFinishEdit(mylist);
				dismiss();
			}
		});
        
        return view;
    }
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (EditorInfo.IME_ACTION_DONE == actionId) {			
            //EditListener activity = (EditListener)getActivity();
            //activity.onFinishEdit(mylist);
            //this.dismiss();
            return false;
        }
        return false;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
	
	

}
