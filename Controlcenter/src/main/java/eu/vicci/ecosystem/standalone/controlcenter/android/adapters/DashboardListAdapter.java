package eu.vicci.ecosystem.standalone.controlcenter.android.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import de.tud.melissa.visualization.GraphVisualiszation;
import de.tud.melissa.visualization.Visualization;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.DashboardActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.DashboardContentManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DashboardDevice;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.Robot;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.mock.ProcessMock;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.mock.RobotMock;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.mock.SensorMock;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.ClientRobot;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.model.Navigator;
import eu.vicci.ecosystem.standalone.controlcenter.android.semiwa.SemiwaConnection;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.DashBoardFilterManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.FilterDialogBuilder.FilterableListViewAdaper;
import eu.vicci.ecosystem.standalone.controlcenter.android.visualization.DeviceVisualizationList;
import eu.vicci.ecosystem.standalone.controlcenter.android.visualization.VisualizationType;
import eu.vicci.process.client.android.ProcessEngineClient;

/**
 * ListAdapter of the GridView for the {@link DashboardActivity}
 * 
 * @author Andreas Hippler
 */
public class DashboardListAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter, FilterableListViewAdaper {
	private DashboardActivity context;

	private ArrayList<DashboardDevice> list = new ArrayList<DashboardDevice>();
	private DashboardSortType sortBy;
	private LayoutInflater inflater;

	/**
	 * Gets a DashboardListAdapter. Add mockdevices
	 * 
	 * @param context
	 */
	public DashboardListAdapter(DashboardActivity context) {
		this.context = context;
		this.sortBy = DashboardSortType.NOT_SORTED;
		this.inflater = LayoutInflater.from(context);

		for (ClientRobot robot : Navigator.getRobots()) {
			Robot r = new Robot(robot);
			if (DashboardContentManager.getInstance().getDashboardDevices().contains(r)) {
				r.setVisible(DashboardContentManager.getInstance().getDashboardDeviceById(r.getId()).isVisible());
				DashboardContentManager.getInstance().getDashboardDevices().remove(r);
			}
			DashboardContentManager.getInstance().addDashboardDevice(r);
		}

		addMocks();

		list.addAll(getOrgList());
	}

	private void addMocks() {
		if (!SemiwaConnection.getInstance().isConnected() || list.isEmpty())
			SensorMock.addSensorMocks();
		RobotMock.addRobotMocks();
		if (!ProcessEngineClient.getInstance().isConnected())
			ProcessMock.addProcessMocks();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public DashboardDevice getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return list.get(position).getId().hashCode();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public int getViewTypeCount() {
		return VisualizationType.values().length;
	}

	@Override
	public int getItemViewType(int position) {
		return getItem(position).getVisualizationType().ordinal();
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DashboardDevice device = (DashboardDevice) list.get(position);

		TextView textView;
		Visualization<?> visualization;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.dashboard_single_grid, null);
			textView = (TextView) convertView.findViewById(R.id.device_name);
			LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.deviceGrid_LinearLayout);

			visualization = (Visualization<?>) DeviceVisualizationList.getView(device.getVisualizationType(), context,
					device);
			layout.addView(visualization);
			visualization.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 200));
		} else {
			Object[] tags = (Object[]) convertView.getTag();
			textView = (TextView) tags[0];
			visualization = (Visualization<?>) tags[1];

			if ((device.getVisualizationType() == VisualizationType.TimeLineChart || device.getVisualizationType() == VisualizationType.TimeBarChart)
					&& !device.getId().equals(tags[2])) {
				((GraphVisualiszation<Date>) visualization).clear();
				;
			}
		}

		textView.setText(device.getName());
		DeviceVisualizationList.updateVisualization(visualization, device);

		convertView.setTag(new Object[] { textView, visualization, device.getId() });
		return convertView;
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@Override
	public void filterBy(SparseArray<List<String>> selectedFilter) {
		list = DashBoardFilterManager.filterBy(selectedFilter, context);
		
		if (sortBy.isSorted())
			Collections.sort(list, sortComparator());
		
		notifyDataSetChanged();
	}

	/**
	 * Sorts the list by given {@link DashboardSortType}. Redraws the GridView after
	 * sorting. If the sorting is annulled the order before the sorting is
	 * recovered, even it was filtered.
	 * 
	 * @param sortBy
	 */
	public void sortBy(DashboardSortType sortBy) {
		this.sortBy = sortBy;
		if (sortBy.isSorted()) {
			Collections.sort(list, sortComparator());
		} else {
			if (DashBoardFilterManager.getLastSelectedFilter() == null) {
				list = new ArrayList<DashboardDevice>(getOrgList());
			} else {
				list = DashBoardFilterManager.filterBy(DashBoardFilterManager.getLastSelectedFilter(), context);
			}
		}
		notifyDataSetInvalidated();
	}

	private Comparator<DashboardDevice> sortComparator() {
		return sortBy.getComperatorBySortType();
	}

	@Override
	public long getHeaderId(int position) {
		DashboardDevice device = (DashboardDevice) list.get(position);
		switch (sortBy) {
		case SORT_BY_NAME:
			return device.getName().charAt(0);
		case SORT_BY_ROOM:
			return device.getLocationName().hashCode();
		case SORT_BY_TYPE:
			return device.getDeviceType().ordinal();
		default:
			return 0;
		}
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		DashboardDevice device = (DashboardDevice) list.get(position);
		String headerName = null;
		switch (sortBy) {
		case SORT_BY_NAME:
			headerName = String.valueOf(device.getName().charAt(0)).toUpperCase(Locale.getDefault());
			break;
		case SORT_BY_TYPE:
			headerName = device.getDeviceType().name();
			break;
		case SORT_BY_ROOM:
			headerName = device.getLocationName();
			break;
		default:
			break;
		}

		TextView view = new TextView(context);
		view.setText(headerName);
		return view;
	}

	/**
	 * @return if the list id sorted
	 */
	public boolean isSorted() {
		return sortBy.isSorted();
	}

	/**
	 * Gets the active {@link DashboardSortType}
	 * 
	 * @return the current {@link DashboardSortType}
	 */
	public DashboardSortType isSortedBy() {
		return sortBy;
	}

	/**
	 * Hides a device from the GridView. It is set invisible.The GridView will
	 * redraw.
	 * 
	 * @param device
	 */
	public void removeDevice(DashboardDevice device) {
		device.setVisible(false);
		list.remove(device);
		notifyDataSetChanged();
	}

	private List<DashboardDevice> getOrgList() {
		return DashboardContentManager.getInstance().getVisibleDashboardDevices();
	}

	/**
	 * Gets alls rooms of the devices
	 * 
	 * @return list of rooms
	 */
	public List<String> getRooms() {
		Set<String> set = new HashSet<String>();
		for (DashboardDevice device : DashboardContentManager.getInstance().getDashboardDevices()) {
			set.add(device.getLocationName());
		}
		return new ArrayList<String>(set);
	}

	/**
	 * Load all visible devices from {@link DashboardContentManager} and redraw
	 * GridView.
	 */
	public void contentManagerChanged() {
		list = new ArrayList<DashboardDevice>(getOrgList());
		notifyDataSetInvalidated();
	}

	@Override
	public SparseArray<List<String>> getLastSelectedFilter() {
		return DashBoardFilterManager.getLastSelectedFilter();
	}

}
