package eu.vicci.ecosystem.standalone.controlcenter.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.HelpDetailActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.HelpListActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.HelpWikipediaActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.BreadcrumbsFragmentActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;
import eu.vicci.ecosystem.standalone.controlcenter.android.help.HelpContent;
import eu.vicci.ecosystem.standalone.controlcenter.android.help.HelpItem;

/**
 * A fragment representing a single Help detail screen. This fragment is either
 * contained in a {@link HelpListActivity} in two-pane mode (on tablets) or a
 * {@link HelpDetailActivity} on handsets.
 */
public class HelpDetailFragment extends Fragment {

	public static final String HELP_ITEM_KEY = "HelpItemId";
	private HelpItem helpItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public HelpDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(HELP_ITEM_KEY)) {
			helpItem = HelpContent.getHelpItemsMap().get(
					getArguments().getString(HELP_ITEM_KEY));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout._fragment_help_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (helpItem != null) {
			((TextView) rootView.findViewById(R.id.help_txtHeadline))
					.setText(helpItem.getHeadline());
			WebView webView = (WebView) rootView
					.findViewById(R.id.help_webView);
			webView.setWebViewClient(new HelpWebViewClient());
			webView.getSettings().setDefaultFontSize(
					rootView.getResources().getDimensionPixelSize(
							R.dimen.tsHelpNormal));
			System.out.println(Common.getThemedString(getActivity().getTheme(),
					R.attr.linkColor).substring(3));
			webView.loadDataWithBaseURL(
					"fake://not/needed",
					"<html><head><style type=\"text/css\">body {margin: 0; padding: 0; color: #"
							+ Common.getThemedString(getActivity().getTheme(),
									R.attr.textColor).substring(3)
							+ ";} a:link {color:#"
							+ Common.getThemedString(getActivity().getTheme(),
									R.attr.linkColor).substring(3) + "}"
							+ "</style></head><body>"
							+ helpItem.getContentHtml() + "</body></html>",
					"text/html", "utf-8", "");
			webView.setBackgroundColor(0x00000000);
			webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
		}

		return rootView;
	}

	/**
	 * A custom HelpWebViewClient.
	 */
	private class HelpWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.contains("smartcps")) {
				Common.startBreadcrumbsActivityWithStringExtra(
						(BreadcrumbsFragmentActivity) getActivity(),
						HelpWikipediaActivity.class,
						HelpWikipediaActivity.WIKIPEDIA_LINK_KEY,
						url.replaceFirst("(smartcps://)(.+)",
								HelpWikipediaActivity.WIKIPEDIA_BASE_URL + "$2"));
			} else
				view.loadUrl(url);
			return true;
		}
	}
}
