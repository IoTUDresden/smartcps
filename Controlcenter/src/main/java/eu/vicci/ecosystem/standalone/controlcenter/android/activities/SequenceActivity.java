package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import java.util.Map;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.SmartCPSActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.processview.ItemDetailFragment;
import eu.vicci.ecosystem.standalone.controlcenter.android.sequence.PortListAdapter;
import eu.vicci.ecosystem.standalone.controlcenter.android.sequence.SequenceView;
import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.kpseus.connect.handlers.AbstractClientHandler;
import eu.vicci.process.kpseus.connect.handlers.ProcessInfosHandler;
import eu.vicci.process.model.sofia.Process;
import eu.vicci.process.model.sofia.ProcessStep;

public class SequenceActivity extends SmartCPSActivity {

	private Process model = null;
	private String modelId;
	private String activeId;
	private ProcessInfosHandler pih;
	boolean isInstance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		modelId = intent.getStringExtra(ItemDetailFragment.EXTRA_MESSAGE_MODEL_ID);
		activeId = intent.getStringExtra(ItemDetailFragment.EXTRA_MESSAGE_PROCESS_INSTANCEID);

		// //Comment out these three lines if you do not want the server to get
		// the process model.
		pih = new ProcessInfosHandler(modelId);
		pih.addHandlerFinishedListener(this);
		ProcessEngineClient.getInstance().getProcessInfos(pih);
	}

	/**
	 * animates a
	 * 
	 * @param view
	 *            the view to animate
	 * @param reveal
	 *            true if reveal animation, false if hide animation is wanted
	 */
	public static void animateProperties(final View view, final boolean reveal) {
		final int steps = 400;
		ValueAnimator varl = ValueAnimator.ofInt(steps);
		varl.setDuration(steps);
		varl.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
				int width = lp.width;
				if (reveal && lp.rightMargin != 0)
					lp.setMargins(0, 0, -width + width * (Integer) animation.getAnimatedValue() / steps, 0);
				if (!reveal && lp.rightMargin != -width) {
					lp.setMargins(0, 0, -width * (Integer) animation.getAnimatedValue() / steps, 0);
				}
				view.setLayoutParams(lp);
			}
		});
		varl.start();
	}

	public static void makeProperties(RelativeLayout propertiesHolder, ProcessStep step, Context context, Map<String, String> modelInstanceMapping) {
		
		TextView name = (TextView) propertiesHolder.findViewById(R.id.properties_name);
		TextView id = (TextView) propertiesHolder.findViewById(R.id.properties_id);
		TextView description = (TextView) propertiesHolder.findViewById(R.id.properties_description);
		TextView type = (TextView) propertiesHolder.findViewById(R.id.properties_type);
		TextView resource = (TextView) propertiesHolder.findViewById(R.id.properties_resource);
		ListView ports = (ListView) propertiesHolder.findViewById(R.id.properties_port_list);
		
		ProcessStep rootStep = step;
		while(rootStep.getParentstep()!=null)
			rootStep = rootStep.getParentstep();
		
		name.setText(step.getName());
		if(modelInstanceMapping==null)
			id.setText(step.getId());
		else
			id.setText(modelInstanceMapping.get(step.getId()));
		description.setText(step.getDescription());
		type.setText(step.getType());
		resource.setText(step.getResource());

		ports.setAdapter(new PortListAdapter(context, R.layout.sequence_view_port_item, step.getPorts(), modelInstanceMapping.get(rootStep.getId()), null));
		if (((RelativeLayout.LayoutParams)propertiesHolder.getLayoutParams()).rightMargin<0) {
			return;
		}
		int flyInDistance = 20;
		for (int i = 0; i < propertiesHolder.getChildCount(); i++) {
			View child = propertiesHolder.getChildAt(i);
			if (child.getAnimation() != null) {
				child.getAnimation().cancel();
				child.getAnimation().reset();
			}
			child.setAlpha(0);
			ViewPropertyAnimator animation = child.animate();
			animation.setInterpolator(new DecelerateInterpolator(1));
			animation.setDuration(250);
			animation.setStartDelay((long) (Math.max(0, child.getY())));
			child.setTranslationY(-flyInDistance);
			animation.alpha(1f);

			animation.translationYBy(flyInDistance);

			animation.start();
		}
	}

	@Override
	public void onHandlerFinished(AbstractClientHandler handler, Object arg) {
		if (handler instanceof ProcessInfosHandler) {
			model = pih.getProcess();
			ProcessEngineClient.getInstance().getLocalProcesses().put(model.getId(), model);

			setContentView(R.layout.sequence_view_holder);
			FrameLayout sequenceViewContainer = (FrameLayout) findViewById(R.id.sequenceViewContainer);
			final RelativeLayout propertiesHolder = (RelativeLayout) findViewById(R.id.properties);
			ImageButton bCloseProperties = (ImageButton) findViewById(R.id.properties_close_button);
			bCloseProperties.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					animateProperties(propertiesHolder, false);
				}
			});
			sequenceViewContainer.addView(new SequenceView(model, getApplicationContext(), propertiesHolder, activeId, null));
			// sequenceViewContainer.getChildAt(0).getLayoutParams().width =
			// LayoutParams.MATCH_PARENT;
		}
	}
}
