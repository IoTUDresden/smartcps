package eu.vicci.ecosystem.standalone.controlcenter.android.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.DashboardFilterListViewAdapter;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.HumanTaskChooserFilterListViewAdapter;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.ProcessMainViewFilterListAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Base implementation for a filter list view adapter which is implemented for {@link DashboardFilterListViewAdapter}, 
 * {@link HumanTaskChooserFilterListViewAdapter} or {@link ProcessMainViewFilterListAdapter}
 */
public abstract class AbstractFilterDialogListAdapter extends BaseAdapter implements StickyListHeadersAdapter {
	
	protected ArrayList<FilterElement> list = new ArrayList<FilterElement>();
	protected Context context;
	protected String[] headerNames;
	protected SparseArray<List<String>> lastSelectedFilter;
	
	public AbstractFilterDialogListAdapter(Context context, String[] headerNames, SparseArray<List<String>> lastSelectedFilter){
		this.context = context;
		this.headerNames = headerNames;
		this.lastSelectedFilter = lastSelectedFilter;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public FilterElement getItem(int position) {
		return list.get(position);
	}
	
	@Override
	public long getHeaderId(int arg0) {
		return list.get(arg0).getHeaderID();
	}
	
	@Override
	public long getItemId(int position) {
		return list.get(position).getName().hashCode();
	}
	
	/**
	 * gets selected filter elements
	 * 
	 * @return filter elements
	 */
	public abstract SparseArray<List<String>> getChooseFilter();
	
	@Override
	public View getHeaderView(int pos, View convertView, ViewGroup parent) {
		TextView headerText;
		if (null == convertView) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.dashboard_filter_header, null);
			convertView.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return true;
				}
			});
			headerText = (TextView) convertView.findViewById(R.id.listViewHeaderTextView);
		} else {
			headerText = (TextView) convertView.getTag();
		}
		headerText.setText(headerNames[(int) getHeaderId(pos)]);
		convertView.setTag(headerText);
		return convertView;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final FilterElement el = getItem(position);
		CheckedTextView checkView = null;
		// reuse view
		if (null == convertView) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.checktextview_multiple_choice_row, null);
			checkView = (CheckedTextView) convertView.findViewById(R.id.checkedTextView);
		} else {
			checkView = (CheckedTextView) convertView.getTag();
		}
		checkView.setText(el.getName());
		checkView.setChecked(el.isSelected());
		checkView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckedTextView ctv = (CheckedTextView) v;
				ctv.toggle();
				el.setSelected(ctv.isChecked());
			}
		});
		convertView.setTag(checkView);
		return convertView;
	}

}
