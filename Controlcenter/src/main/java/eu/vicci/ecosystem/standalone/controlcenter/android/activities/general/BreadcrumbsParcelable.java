package eu.vicci.ecosystem.standalone.controlcenter.android.activities.general;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The BreadcrumbsParcelable including breadcrumb strings.
 */
public class BreadcrumbsParcelable implements Parcelable {

	private List<String> breadcrumbHistory;

	/**
	 * Instantiates a new breadcrumbs parcelable.
	 * 
	 * @param breadcrumbHistory
	 *            the breadcrumb history
	 */
	public BreadcrumbsParcelable(List<String> breadcrumbHistory) {
		this.breadcrumbHistory = breadcrumbHistory;
	}

	// with a probability of 99% not necessary...
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeValue(breadcrumbHistory);
	}

	/** The Constant CREATOR. */
	public static final Parcelable.Creator<BreadcrumbsParcelable> CREATOR = new Parcelable.Creator<BreadcrumbsParcelable>() {
		public BreadcrumbsParcelable createFromParcel(Parcel in) {
			return new BreadcrumbsParcelable(in);
		}

		public BreadcrumbsParcelable[] newArray(int size) {
			return new BreadcrumbsParcelable[size];
		}
	};

	@SuppressWarnings("unchecked")
	private BreadcrumbsParcelable(Parcel in) {
		this.breadcrumbHistory = (ArrayList<String>) in.readValue(getClass().getClassLoader());
	}

	public void removeBreadcrumbHistoryItem(String breadcrumbHistoryItem) {
		this.breadcrumbHistory.remove(breadcrumbHistoryItem);
	}

	public void addBreadcrumbHistoryItem(String breadcrumbHistoryItem) {
		this.breadcrumbHistory.add(breadcrumbHistoryItem);
	}

	/**
	 * Gets the breadcrumb history as a list of strings.
	 * 
	 * @return the breadcrumb history
	 */
	public List<String> getBreadcrumbHistory() {
		return breadcrumbHistory;
	}

}
