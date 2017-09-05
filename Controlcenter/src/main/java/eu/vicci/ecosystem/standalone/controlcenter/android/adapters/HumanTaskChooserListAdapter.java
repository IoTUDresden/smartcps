package eu.vicci.ecosystem.standalone.controlcenter.android.adapters;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.SmartCPSActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.humantask.HumanTaskDataManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.humantask.HumanTaskDataManagerListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.FilterDialogBuilder.FilterableListViewAdaper;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.HumanTaskChooserFilterManager;
import eu.vicci.process.model.sofia.HumanTaskType;
import eu.vicci.process.model.sofia.HumanTaskUseCase;
import eu.vicci.process.model.util.messages.core.IHumanTaskMessage;
import eu.vicci.process.model.util.messages.core.IHumanTaskRequest;

public class HumanTaskChooserListAdapter extends BaseAdapter
		implements ListAdapter, HumanTaskDataManagerListener, StickyGridHeadersSimpleAdapter, FilterableListViewAdaper {
	private SmartCPSActivity context;
	private List<IHumanTaskRequest> requests;
	private HumanTaskChooserSortType sortBy;

	public HumanTaskChooserListAdapter(SmartCPSActivity context, HumanTaskChooserSortType sortBy) {
		this.context = context;
		this.sortBy = sortBy;
		this.requests = HumanTaskDataManager.getInstance().getRequestsList();
		HumanTaskDataManager.getInstance().addHumanTaskDataManagerListener(this);
	}

	@Override
	public int getCount() {
		return requests.size();
	}

	@Override
	public IHumanTaskMessage getItem(int position) {
		if (position >= requests.size())
			return null;
		return requests.get(position);
	}

	@Override
	public long getItemId(int position) {
		if (position >= requests.size())
			return 0;
		return requests.get(position).hashCode();
	}

	public void addHumanTaskRequest(IHumanTaskRequest request) {
		requests.add(request);
		notifyUi();
	}

	public void removeHumanTaskRequest(IHumanTaskRequest request) {
		requests.remove(request);
		notifyUi();
	}

	public void unregisterListeners() {
		HumanTaskDataManager.getInstance().removeHumanTaskDataManagerListener(this);
	}

	@Override
	public void onHumanTaskAdded(IHumanTaskRequest humanTaskRequest) {
		addHumanTaskRequest(humanTaskRequest);
	}

	@Override
	public void onHumanTaskHandledByOther(IHumanTaskRequest humanTaskRequest) {
		removeHumanTaskRequest(humanTaskRequest);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView = inflater.inflate(R.layout.human_task_choosers_icons, null);
		// set value into textview
		TextView textView = (TextView) gridView.findViewById(R.id.grid_item_label);
		textView.setText(requests.get(position).getName());

		// set image based on selected text
		ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);

		HumanTaskType htType = requests.get(position).getHumanTaskType();
		HumanTaskUseCase htUseCase = requests.get(position).getHumanTaskUseCase();

		setStateColor(htType, textView);
		setTypeIcon(htUseCase, imageView);

		return gridView;
	}

	private void setStateColor(HumanTaskType htType, TextView tView) {
		switch (htType) {
		case ERROR:
			tView.setBackgroundColor(Color.rgb(255, 0, 0));
			break;
		case WARNING:
			tView.setBackgroundColor(Color.rgb(255, 127, 0));
			break;
		case HINT:
			tView.setBackgroundColor(Color.rgb(0, 152, 114));
			break;
		default:
			tView.setBackgroundColor(Color.rgb(0, 153, 204));
			break;
		}
	}

	// some icons from http://www.flaticon.com/
	private void setTypeIcon(HumanTaskUseCase htUseCase, ImageView iView) {
		switch (htUseCase) {
		case COFFEE:
			iView.setImageResource(R.drawable.ic_action_coffee);
			break;
		case HEATING:
			iView.setImageResource(R.drawable.ic_action_temperature);
			break;
		case ORDER:
			iView.setImageResource(R.drawable.ic_action_cart);
			break;
		case PLANTS:
			iView.setImageResource(R.drawable.ic_action_grow);
			break;
		case HEALTH:
			iView.setImageResource(R.drawable.ic_action_hospital);
			break;
		case BELL:
			iView.setImageResource(R.drawable.ic_action_bell);
			break;
		default:
			iView.setImageResource(R.drawable.ic_action_info);
			break;
		}
	}

	private void notifyUi() {
		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				notifyDataSetChanged();
			}
		});
	}

	public void sortBy(HumanTaskChooserSortType sortBy) {
		this.sortBy = sortBy;
		if (sortBy.isSorted()) {
			Collections.sort(requests, sortComparator());
		} else {
			// if (DashBoardFilterManager.getLastSelectedFilter() == null) {
			// requests = new ArrayList<IHumanTaskRequest>(getOrgList());
			// } else {
			// requests =
			// DashBoardFilterManager.filterBy(DashBoardFilterManager.getLastSelectedFilter(),
			// context);
			// }
		}
		notifyDataSetInvalidated();
	}

	private Comparator<IHumanTaskMessage> sortComparator() {
		return sortBy.getComperatorBySortType();
	}

	private List<IHumanTaskRequest> getOrgList() {
		return HumanTaskDataManager.getInstance().getRequestsList();
	}

	/**
	 * @return if the list id sorted
	 */
	public boolean isSorted() {
		return sortBy.isSorted();
	}

	@Override
	public long getHeaderId(int position) {
		IHumanTaskRequest request = requests.get(position);
		switch (sortBy) {
		case SORT_BY_NAME:
			return request.getName().charAt(0);
		case SORT_BY_DATE:
			return request.getTimestamp();
		case SORT_BY_TYPE:
			return request.getHumanTaskType().ordinal();
		default:
			return 0;
		}
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		IHumanTaskMessage req = requests.get(position);
		String headerName = null;
		switch (sortBy) {
		case SORT_BY_NAME:
			headerName = String.valueOf(req.getName().charAt(0)).toUpperCase(Locale.getDefault());
			break;
		case SORT_BY_TYPE:
			headerName = req.getHumanTaskType().toString();
			break;
		case SORT_BY_DATE:
			Date date = new Date(req.getTimestamp());
			SimpleDateFormat sDateFormat = new SimpleDateFormat("d MMM yyyy - HH:mm:ss", Locale.UK);
			String d = sDateFormat.format(date);
			headerName = String.valueOf(d);
			break;
		default:
			break;
		}

		TextView view = new TextView(context);
		view.setText(headerName);
		return view;
	}

	@Override
	public void filterBy(SparseArray<List<String>> selectedFilter) {
		requests = HumanTaskChooserFilterManager.filterBy(selectedFilter, context);

		if (sortBy.isSorted())
			Collections.sort(requests, sortComparator());

		notifyDataSetChanged();
	}

	@Override
	public SparseArray<List<String>> getLastSelectedFilter() {
		return HumanTaskChooserFilterManager.getLastSelectedFilter();
	}
}
