package eu.vicci.ecosystem.standalone.controlcenter.android.util;

public class ProcessMainViewFilterManager {		
	
	public enum FilterType{
		GroupedState,
		State;
		
		// muss die selbe Reihenfolge wie dieses Enum haben
		private static String[] headerNames = new String[] { "Grouped State" ,"State"};

		/**
		 * Gets a String[] with the header names. order must be the same as in
		 * this enum
		 * 
		 * @return the names of the headers for displaying in the filter dialog
		 */
		public static String[] getHeaderNames() {
			return headerNames;
		}
	}	
	
	public enum GroupTypes{
		Finished,
		Unfinished
	}
	


}
