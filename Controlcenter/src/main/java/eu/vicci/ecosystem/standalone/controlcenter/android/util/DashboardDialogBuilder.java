package eu.vicci.ecosystem.standalone.controlcenter.android.util;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;
import de.tud.melissa.visualization.Visualization;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.DashboardActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.DashboardFilterListViewAdapter;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.DashboardListAdapter;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.DashboardSortType;
import eu.vicci.ecosystem.standalone.controlcenter.android.content.device.DashboardDevice;
import eu.vicci.ecosystem.standalone.controlcenter.android.visualization.DeviceVisualizationList;
import eu.vicci.ecosystem.standalone.controlcenter.android.visualization.VisualizationType;

/**
 * Helper to build {@link AlertDialog}s for the dashboard activity
 * 
 * @author Andreas Hippler
 * 
 */
public class DashboardDialogBuilder {
	private final Context context;
	private final FilterDialogBuilder filterDialogBuilder;

	/**
	 * @param context
	 */
	public DashboardDialogBuilder(Context context) {
		this.context = context;
		filterDialogBuilder = new FilterDialogBuilder(context, new DashboardFilterListViewAdapter(context));
	}

	/**
	 * An filter dialog is build and shown.
	 * 
	 * @param listAdapter
	 *            the {@link DashboardListAdapter}, used to get all possible
	 *            options
	 * 
	 * @see DashBoardFilterManager
	 */
	public void showFilterDialog(final DashboardListAdapter listAdapter) {
		filterDialogBuilder.showFilterDialog(listAdapter, 
				new DashboardFilterListViewAdapter(context, listAdapter.getLastSelectedFilter()));
	}

	/**
	 * 
	 * 
	 * @param sensor
	 *            the {@link DashboardDevice}
	 */
	@SuppressLint("InflateParams")
	public void showChangeIdealsDialog(final DashboardDevice sensor) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dashboard_grenzwerte_dialog, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(view);

		final EditText minEditText = (EditText) view.findViewById(R.id.grenzwert_min);
		final EditText maxEditText = (EditText) view.findViewById(R.id.grenzwert_max);

		minEditText.setText(String.format(Locale.US, "%.2f", sensor.getValueObject().getIdealValueMin()));
		maxEditText.setText(String.format(Locale.US, "%.2f", sensor.getValueObject().getIdealValueMax()));

		builder.setTitle(context.getString(R.string.dashboard_change_ideal_values_of)+ sensor.getName());
		builder.setPositiveButton(context.getString(R.string.dashboard_options_change), null);
		builder.setNegativeButton(context.getString(R.string.dashboard_filter_cancel), null);

		final AlertDialog dialog = builder.create();
		dialog.show();

		dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				double min;
				double max;
				try {
					min = Double.parseDouble(minEditText.getText().toString());
				} catch (NumberFormatException e) {
					Toast.makeText(context, 
							context.getString(R.string.dashboard_options_min_must_be_number), 
							Toast.LENGTH_LONG).show();
					return;
				}
				try {
					max = Double.parseDouble(maxEditText.getText().toString());
				} catch (NumberFormatException e) {
					Toast.makeText(context, 
							context.getString(R.string.dashboard_options_max_must_be_number), 
							Toast.LENGTH_LONG).show();
					return;
				}
				if (max < min) {
					Toast.makeText(context, 
							context.getString(R.string.dashboard_options_min_must_be_lower), 
							Toast.LENGTH_LONG).show();
					return;
				}
				sensor.getValueObject().setIdealValueMin(min);
				sensor.getValueObject().setIdealValueMax(max);
				dialog.dismiss();
			}
		});
	}

	/**
	 * Build and show an dialog for renaming the device. Checks for valid name.
	 * Notifies the listAdapter if the GridView in {@link DashboardActivity} is
	 * sorted by names.
	 * 
	 * @param listAdapter
	 *            the {@link DashboardListAdapter}
	 * @param device
	 */
	public void showRennameDeviceDialog(final DashboardListAdapter listAdapter, final DashboardDevice device) {
		// Set an EditText view to get user input
		final EditText input = new EditText(context);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setTitle(context.getString(R.string.dashboard_rename_of) + device.getName());
		builder.setView(input);
		builder.setPositiveButton(context.getString(R.string.dashboard_ok), null);
		builder.setNegativeButton(context.getString(R.string.dashboard_filter_cancel), null);

		final AlertDialog dialog = builder.create();

		dialog.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface di) {
				dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						if (input.getText().toString().trim().isEmpty()) {
							Toast.makeText(context, 
									context.getString(R.string.dashboard_options_enter_name), 
									Toast.LENGTH_SHORT).show();
						} else {
							device.setName(input.getText().toString().trim());
							if (listAdapter.isSortedBy().equals(DashboardSortType.SORT_BY_NAME))
								listAdapter.sortBy(DashboardSortType.SORT_BY_NAME);
							else
								listAdapter.notifyDataSetChanged();
							dialog.dismiss();
						}
					}
				});
			}
		});

		dialog.show();
	}

	private static int checkedItem = -1;

	/**
	 * Build and show an {@link AlertDialog} for changing the current
	 * {@link Visualization} of the device. Options are the possible
	 * visualizations for the device from {@link DeviceVisualizationList}.
	 * 
	 * @param listAdapter
	 * @param device
	 */
	public void showChangeVisualizationDialog(final DashboardListAdapter listAdapter, final DashboardDevice device) {
		final Hashtable<String, VisualizationType> visualizations = DeviceVisualizationList
				.getVisualizationsFromDevice(device);

		final String[] visualizationNames = new String[visualizations.size()];
		final VisualizationType[] visualizationTypes = new VisualizationType[visualizations.size()];

		Integer i = -1;

		for (Iterator<Entry<String, VisualizationType>> iterator = visualizations.entrySet().iterator(); iterator
				.hasNext();) {
			Entry<String, VisualizationType> entry = (Entry<String, VisualizationType>) iterator.next();
			visualizationNames[++i] = entry.getKey();
			visualizationTypes[i] = entry.getValue();

			if (entry.getValue().equals(device.getVisualizationType())) {
				checkedItem = i;
			}
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.dashboard_change_vis_of) + device.getName());
		builder.setSingleChoiceItems(visualizationNames, checkedItem, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				checkedItem = which;
			}
		}).setPositiveButton(context.getString(R.string.dashboard_ok), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				device.setVisualizationType(visualizationTypes[checkedItem]);
				listAdapter.notifyDataSetChanged();
			}
		}).setNegativeButton(context.getString(R.string.dashboard_filter_cancel), null).create().show();
		;
	}

	/**
	 * Build and show an dialog for choosing the current room of the device. It
	 * is possible to name a new room in the last cell of the list.
	 * 
	 * @param device
	 * @param rooms
	 *            all possible rooms
	 */
	public void showChangeRoomDialog(final DashboardDevice device, final List<String> rooms) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.dashboard_change_room_of) + device.getName());
		final RoomsListAdapter rListAdapter = new RoomsListAdapter(rooms, device.getLocationName());
		ListView listview = new ListView(context);
		listview.setFocusable(true);
		listview.setFocusableInTouchMode(true);
		listview.setAdapter(rListAdapter);
		builder.setView(listview);
		builder.setPositiveButton(context.getString(R.string.dashboard_ok), null);
		builder.setNegativeButton(context.getString(R.string.dashboard_filter_cancel), null);

		final AlertDialog dialog = builder.create();
		dialog.show();
		// necessary, else the keyboard will not be displayed, if the edittext
		// is focused
		dialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String newRoom = rListAdapter.getCheckedItem();
				if (!newRoom.isEmpty()) {
					device.setLocationName(newRoom);
					dialog.dismiss();
				} else {
					Toast.makeText(context, context.getString(R.string.dashboard_type_in_room), 
							Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	private class RoomsListAdapter extends BaseAdapter {

		private List<String> list;
		private LayoutInflater infaltor;
		private int checkedItem;
		private HashSet<Checkable> checkboxes = new HashSet<Checkable>();
		private EditText newRoomEditText;

		public RoomsListAdapter(List<String> list, String checkedItem) {
			this.list = list;
			this.list.add("");
			this.checkedItem = list.indexOf(checkedItem);
			infaltor = LayoutInflater.from(context);
		}

		public String getCheckedItem() {
			return getItem(checkedItem);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getItemViewType(int position) {
			return position == list.size() - 1 ? 1 : 0;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (getItemViewType(position) == 0) {
				final CheckedTextView ctv;
				if (convertView == null) {
					convertView = infaltor.inflate(R.layout.checktextview_single_choice_row, null);
					ctv = (CheckedTextView) convertView.findViewById(R.id.checkedTextView);
					ctv.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (newRoomEditText != null) {
								newRoomEditText.clearFocus();
							}
							setAllCheckboxes(false);
							ctv.setChecked(true);
							checkedItem = position;
						}
					});
					checkboxes.add(ctv);
				} else {
					ctv = (CheckedTextView) convertView;
				}
				ctv.setText(list.get(position));
				ctv.setChecked((position == checkedItem));
				ctv.setFocusable(true);
			} else {
				if (convertView == null)
					convertView = infaltor.inflate(R.layout.dashboard_change_room_list_newroom_row, null);
				newRoomEditText = (EditText) convertView.findViewById(R.id.editText0);
				final RadioButton CheckBox = (RadioButton) convertView.findViewById(R.id.radioButton0);
				newRoomEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							checkedItem = position;
							setAllCheckboxes(false);
							CheckBox.setChecked(true);
						}
					}
				});
				newRoomEditText.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
						String str = s.toString();
						if (!str.isEmpty())
							list.set(list.size() - 1, s.toString());
					}
				});
				newRoomEditText.setText("");
				convertView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						checkedItem = position;
						setAllCheckboxes(false);
						CheckBox.setChecked(true);
					}
				});
				CheckBox.setChecked((position == checkedItem));
				checkboxes.add(CheckBox);
			}
			return convertView;
		}

		private void setAllCheckboxes(boolean checked) {
			for (Checkable checkable : checkboxes) {
				checkable.setChecked(checked);
			}
		}
	}
}
