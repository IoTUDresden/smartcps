package eu.vicci.ecosystem.standalone.controlcenter.android.sequence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.SequenceActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.processview.ProcessDatabaseHelper;
import eu.vicci.process.model.sofia.CompositeStep;
import eu.vicci.process.model.sofia.Port;
import eu.vicci.process.model.sofia.ProcessStep;
import eu.vicci.process.model.sofia.Transition;

public class SequenceView extends View {

	private DrawableStep rootDrawable;
	private float currentX = 0f;
	private float currentY = 0f;
	private float targetX = 0f;
	private float targetY = 0f;
	private float zoom = 1f;
	private float previousZoom = 1f;
	private float animationProgress = 0;
	private boolean animating = false;
	private DrawableStep selected;
	private List<DrawableTransition> drawableTransitions = new ArrayList<DrawableTransition>();
	private RelativeLayout propertiesHolder;
	private int instanceCount;
	private Paint textPaint = new Paint();
	private boolean set;
	private int canvasHeight;
	private int canvasWidth;
	private String activeId;
	private Map<String, String> modelToInstanceMapping;

	public SequenceView(ProcessStep model, Context context, RelativeLayout propertiesHolder, String activeId, Map<String, String> modelInstanceMapping) {
		super(context);
		this.modelToInstanceMapping = modelInstanceMapping;
		textPaint.setColor(Color.BLACK);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(20);

		this.activeId = activeId;
		instanceCount = getInstanceCount(model, context);
		this.propertiesHolder = propertiesHolder;
		// this.setLayerType(LAYER_TYPE_HARDWARE, null);
		this.rootDrawable = new DrawableStep(model, null);
		while (relayout(rootDrawable))
			;
		try {
			model.eResource().save(System.out, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int getInstanceCount(ProcessStep model, Context context) {
		ProcessDatabaseHelper dbHelper = new ProcessDatabaseHelper(context);
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from " + ProcessDatabaseHelper.TABLE_INSTANCES + " where name = '" + model.getName() + "';", null);
		return cursor.getCount();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP && event.getEventTime() - event.getDownTime() < 350) {
			DrawableStep clickedStep = rootDrawable.isClicked(event.getX(), event.getY());
			if (clickedStep != null && clickedStep.getStep() != null && clickedStep != selected) {
				selected = clickedStep;
				SequenceActivity.makeProperties(propertiesHolder, clickedStep.getStep(), getContext(), modelToInstanceMapping);
				SequenceActivity.animateProperties(propertiesHolder, true);
				DrawableStep selectedParent = selected.getParent() != null ? selected.getParent() : selected;
				zoom = Math.min(canvasWidth / selectedParent.width, canvasHeight / selectedParent.height);
				targetX = selectedParent.x + selectedParent.width / 2;
				targetY = selectedParent.y + selectedParent.height / 2;
				// animating = true;
				animationProgress = 0;
			} else {
				selected = null;
				SequenceActivity.animateProperties(propertiesHolder, false);
			}
		}
		return true;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed) {
			set = false;
			rootDrawable.resetDimensions();
			for (DrawableTransition trans : drawableTransitions) {
				trans.resetDimensions();
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (!set) {
			currentX = rootDrawable.getX() + rootDrawable.getWidth() / 2;
			currentY = rootDrawable.getY() + rootDrawable.getHeight() / 2;
			set = true;
			canvasWidth = canvas.getWidth();
			canvasHeight = canvas.getHeight();
		}
		super.onDraw(canvas);
		// canvas.drawRGB((int) (Math.random()*256), (int) (Math.random()*256),
		// (int) (Math.random()*256));
		if (animating) {
			float animationSteps = 10;
			if (animationProgress >= 1) {
				animating = false;
				previousZoom = zoom;
				animationProgress = 0;
				canvas.scale(zoom, zoom, targetX, targetY);

				currentX = targetX;
				currentY = targetY;
			} else {
				animationProgress += 1f / animationSteps;
				float newZoom = previousZoom * (1 - animationProgress) + zoom * animationProgress;
				canvas.scale(newZoom, newZoom, targetX, targetY);
				// float newX = currentX*(1-animationProgress) +
				// targetX*animationProgress;
				// float newY = currentY*(1-animationProgress) +
				// targetY*animationProgress;
				float newX = (targetX - currentX) * 1f / animationSteps;
				float newY = (targetY - currentY) * 1f / animationSteps;
				canvas.translate(newX, newY);
			}
		}
		// canvas.drawColor(Color.BLACK);
		rootDrawable.draw(canvas);
		for (DrawableTransition trans : drawableTransitions) {
			trans.draw(canvas);
		}
		canvas.drawText("Running instances: " + instanceCount, 15, textPaint.getTextSize() + 10, textPaint);
		// if (animating)
		invalidate();
	}

	/**
	 * places the DrawableSteps in order according to the transitions between
	 * them
	 * 
	 * @param drawableStep
	 * @return whether a change was made
	 */
	private boolean relayout(DrawableStep drawableStep) {
		boolean result = false;
		EList<Port> ports = drawableStep.getStep().getPorts();
		for (Port port : ports) {
			List<Transition> transitions = new ArrayList<Transition>();
			// transitions.addAll(port.getInTransitions());
			transitions.addAll(port.getOutTransitions());

			// make Transitions
			for (Transition transition : transitions) {
				Port source = transition.getSourcePort();
				Port target = transition.getTargetPort();
				// get the transitions other step (other than 'this')
				ProcessStep targetProcessStep = target.getProcessStep();

				// get the drawableStep for that step
				if (rootDrawable == null)
					return false;
				DrawableStep targetDrawableStep = findDrawableStep(rootDrawable, targetProcessStep);

				drawableTransitions.add(new DrawableTransition(drawableStep, targetDrawableStep));

				if (drawableStep.isSibling(targetDrawableStep)) {
					if (targetDrawableStep.getColumn() <= drawableStep.getColumn()) {
						targetDrawableStep.setColumn(drawableStep.getColumn() + 1);
						result = true;
					} else {
						// drawableStep.setColumn(targetDrawableStep.getColumn()
						// - 1);
					}
				}
			}
		}

		if (drawableStep.getParent() != null) {
			boolean[] populatedColumns = new boolean[drawableStep.getParent().getColumnCount()];
			for (int i = 0; i < populatedColumns.length; i++)
				populatedColumns[i] = false;
			for (DrawableStep sibling : drawableStep.getSiblings(true)) {
				populatedColumns[sibling.getColumn()] = true;
			}
			for (int i = 0; i < populatedColumns.length; i++) {
				if (!populatedColumns[i]) {
					for (DrawableStep sibling : drawableStep.getSiblings(true)) {
						if (sibling.getColumn() > i) {
							sibling.setColumn(sibling.getColumn() - 1);
						}
					}
				}
			}
		}

		if (drawableStep.getParent() != null) {
			int[] itemCountPerColumn = new int[drawableStep.getParent().getColumnCount()];
			for (int i = 0; i < itemCountPerColumn.length; i++) {
				itemCountPerColumn[i] = 0;
			}
			for (DrawableStep child : drawableStep.getParent().getChildren()) {
				child.setRow(itemCountPerColumn[child.getColumn()]);
				itemCountPerColumn[child.getColumn()] = itemCountPerColumn[child.getColumn()] + 1;
			}
		}

		for (DrawableStep child : drawableStep.getChildren()) {
			if (relayout(child))
				result = true;
		}
		return result;
	}

	private class DrawableStep {
		private ProcessStep step;
		private int depth;
		private int parentChildrenSize;
		private Set<DrawableStep> children = new HashSet<DrawableStep>();
		private DrawableStep parent;
		private int row = 0, preferredRow, column = 0;
		private float width, height, x, y;
		private static final float minWidth = 200, minHeight = 150;
		private boolean set = false;
		private Paint fillPaint = new Paint();
		private Paint shadowPaint = new Paint();
		private Paint portPaint;

		private Paint textPaint = new Paint();
		private Map<ProcessStep, DrawableStep> mapping = new HashMap<ProcessStep, DrawableStep>();
		private float minVisibleSize = 0.05f, totallyVisibleSize = 0.15f;
		private int brightness;

		public DrawableStep(ProcessStep step, DrawableStep parent) {
			mapping.put(step, this);
			this.step = step;
			this.depth = getDepth();
			if (depth > 0) {
				this.parentChildrenSize = step.getParentstep().getSubSteps().size();
				this.row = step.getParentstep().getSubSteps().indexOf(step);
			}
			this.parent = parent;
			brightness = 240 - 25 * depth;

			fillPaint.setAntiAlias(true);
			// fillPaint.setShadowLayer(1f, 0, 2, Color.argb(70, 0, 0, 0));

			shadowPaint.setColor(Color.argb(70, 0, 0, 0));
			shadowPaint.setAntiAlias(true);

			portPaint = new Paint(fillPaint);
			portPaint.setColor(Color.rgb(brightness - 20, brightness - 20, brightness - 20));
			if (brightness < 128)
				textPaint.setColor(Color.WHITE);
			else
				textPaint.setColor(Color.BLACK);

			textPaint.setTextSize(20);
			textPaint.setAntiAlias(true);

			// traverse the tree, populate children
			if (step instanceof CompositeStep) {
				for (ProcessStep substep : ((CompositeStep) step).getSubSteps()) {
					this.children.add(new DrawableStep(substep, this));
				}
			}

		}

		public void draw(Canvas canvas) {
			if (!set) {
				set = true;

				int maxDepth = getMaxDepth();
				width = Math.min((maxDepth + 1) * minWidth, minWidth + (canvas.getWidth() * 0.9f - minWidth) * (maxDepth - depth) / maxDepth);
				height = Math.min((maxDepth + 1) * minHeight, minHeight + (canvas.getHeight() * 0.9f - minHeight) * (maxDepth - depth) / maxDepth);
				x = (canvas.getWidth() - width) / 2;
				y = (canvas.getHeight() - height) / 2;
				// if (depth == 0) {
				// x = canvas.getWidth() * 0.05f;
				// y = canvas.getHeight() * 0.05f;
				// } else {
				// float[] dimensions = parent.calculateChildDimensions(column,
				// row);
				// x = dimensions[0];
				// y = dimensions[1];
				// width = dimensions[2];
				// height = dimensions[3];
				// }
			}

			makePaint();
			float sizeOnScreen = Math.max(zoom * width / (float) canvas.getWidth(), zoom * height / (float) canvas.getHeight());
			if (sizeOnScreen > minVisibleSize) {
				float alpha = Math.max(0, Math.min(1f, (sizeOnScreen - minVisibleSize) / (totallyVisibleSize - minVisibleSize)));
				fillPaint.setAlpha((int) (255f * alpha));

				RectF rect = new RectF(x, y, x + width, y + height);
				RectF shadowRect = new RectF(rect);
				shadowRect.top = shadowRect.bottom - 10;
				shadowRect.bottom = shadowRect.bottom + 2;

				canvas.drawRoundRect(shadowRect, 5, 5, shadowPaint);
				canvas.drawRoundRect(rect, 5, 5, fillPaint);

				float textX = x + 15;
				canvas.drawText(step.getName() + (step.getId().equals(activeId) ? " - Currently running" : ""), textX, y + 30, textPaint);
				canvas.drawLine(x, y + 45, x + width, y + 45, textPaint);

				canvas.drawCircle(x, y + height / 2, 10, portPaint);
				canvas.drawCircle(x + width, y + height / 2, 10, portPaint);

			}
			for (DrawableStep child : children)
				child.draw(canvas);
		}

		private void makePaint() {
			if (modelToInstanceMapping!=null && activeId.equals(modelToInstanceMapping.get(step.getId()))) {
				// fillPaint.setColor(Color.rgb(255, 240, 128));
				int[] colors = { getResources().getColor(R.color.card_background_marked), getResources().getColor(R.color.card_background_marked2),
						getResources().getColor(R.color.card_background_marked) };
				// int[] colors = {Color.rgb(red, green, blue), Color.WHITE,
				// Color.BLUE};
				float halfdiagonal = (float) (Math.sqrt(width * width + height * height) / 2);
				float offset = (float) (((float) (System.currentTimeMillis() % 12000)) / 12000f * Math.PI * 2);
				fillPaint.setShader(new LinearGradient(x + width / 2 - (float) Math.sin(offset) * halfdiagonal, y + height / 2 - (float) Math.cos(offset) * halfdiagonal, x + width
								/ 2 + (float) Math.sin(offset) * halfdiagonal, y + height / 2 + (float) Math.cos(offset) * halfdiagonal, colors, null, TileMode.CLAMP));
			} else {
				fillPaint.setColor(Color.rgb(brightness, brightness, brightness));
			}
		}

		/**
		 * calculates width, height and the position of a child at the given
		 * cell
		 * 
		 * @param column
		 *            the cells horizontal position
		 * @param row
		 *            the cells vertical position
		 * @return an Array where [0] = x, [1] = y, [2] = width, [3] = height
		 */
		private float[] calculateChildDimensions(int column, int row) {

			float gapX = getGapX();
			float gapY = getGapY();
			float width = (this.width - (getColumnCount() + 1) * gapX) / getColumnCount();
			float height = (this.height - 20) / getRowCount() - gapY - 20;
			float x = this.x + gapX + (width + gapX) * column;
			float y = this.y + 20 + gapY + (height + gapY) * row;

			float[] result = { x, y, width, height };

			return result;
		}

		private int getMaxDepth() {
			int max = getDepth();
			for (DrawableStep child : children) {
				int childDepth = child.getMaxDepth();
				if (max < childDepth)
					max = childDepth;
			}

			return max;
		}

		public DrawableStep isClicked(float x, float y) {
			if (this.x < x && this.y < y && this.x + this.width > x && this.y + this.height > y) {
				for (DrawableStep child : children) {
					if (child.isClicked(x, y) != null) {
						return child.isClicked(x, y);
					}
				}

				return this;
			}

			return null;
		}

		public void resetDimensions() {
			set = false;
			for (DrawableStep child : children)
				child.resetDimensions();
		}

		private float getGapX() {

			return width * 0.05f;
		}

		private float getGapY() {
			return height * 0.1f;
		}

		private int getDepth() {
			int result = 0;
			ProcessStep thatStep = step;
			while (thatStep != null && thatStep.getParentstep() != null) {
				result++;
				thatStep = thatStep.getParentstep();
			}
			return result;
		}

		public int getColumnCount() {
			int result = 0;
			for (DrawableStep child : children) {
				if (child.getColumn() > result)
					result = child.getColumn();
			}
			return result + 1;
		}

		public int getRowCount() {
			int result = 0;
			for (DrawableStep child : children) {
				if (child.getRow() > result)
					result = child.getRow();
			}
			return result + 1;
		}

		public boolean isSibling(DrawableStep step) {
			if (this.getParent() != null && this.getParent().getChildren().contains(step))
				return true;
			return false;
		}

		public Set<DrawableStep> getSiblings(boolean includeThis) {
			Set<DrawableStep> result = new HashSet<DrawableStep>();
			if (this.getParent() != null) {
				result.addAll(this.getParent().getChildren());
				if (!includeThis)
					result.remove(this);
			}
			return result;
		}

		@Override
		public String toString() {
			return "DrawableStep of " + step.getName() + "(" + step.getClass().getSimpleName() + ")\n   column: " + column + ", row: " + row;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int positionRow) {
			this.row = positionRow;
		}

		public int getColumn() {
			return column;
		}

		public void setColumn(int positionColumn) {
			this.column = positionColumn;
		}

		public ProcessStep getStep() {
			return step;
		}

		public Set<DrawableStep> getChildren() {
			return children;
		}

		public DrawableStep getParent() {
			return parent;
		}

		public float getWidth() {
			return width;
		}

		public float getHeight() {
			return height;
		}

		public float getX() {
			return x;
		}

		public float getY() {
			return y;
		}

	}

	private static class DrawableTransition {

		private DrawableStep source;
		private DrawableStep target;
		private static Paint paint, pArrowhead;
		private Path path;
		private Path arrowhead = new Path();
		private boolean set = false;

		public DrawableTransition(DrawableStep source, DrawableStep target) {
			this.source = source;
			this.target = target;

			paint = new Paint();
			paint.setColor(Color.BLACK);
			paint.setAntiAlias(true);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(0.3f);

			pArrowhead = new Paint();
			pArrowhead.setColor(Color.BLACK);
			pArrowhead.setAntiAlias(true);
			pArrowhead.setStyle(Style.FILL);
		}

		public void resetDimensions() {
			set = false;
		}

		public void makePath() {
			this.path = new Path();
			PointF start = null, end = null;
			LinkedList<PointF> points = new LinkedList<PointF>();
			if (source.isSibling(target)) {
				start = new PointF(source.getX() + source.getWidth(), source.getY() + source.getHeight() / 2);
				end = new PointF(target.getX(), target.getY() + target.getHeight() / 2);

				float x, y, gapX = target.getParent().getGapX(), gapY = target.getParent().getGapY();

				if (start.y > end.y) {

					// 1
					x = start.x + source.getParent().getGapX() / 2;
					y = start.y;
					points.add(new PointF(x, y));

					// 2
					y -= source.getParent().getGapX() / 2;
					points.add(new PointF(x, y));

					if (target.getColumn() > source.getColumn() + 1) {
						// 3
						y = target.y + target.height + gapY / 2 + gapX / 2;
						points.add(new PointF(x, y));

						// 4
						y = target.y + target.height + gapY / 2;
						points.add(new PointF(x, y));

						// 5
						x += gapX / 2;
						points.add(new PointF(x, y));

						// 6
						x = target.x - gapX;
						points.add(new PointF(x, y));

						// 7
						x = target.x - gapX / 2;
						points.add(new PointF(x, y));

						// 8
						y -= gapX / 2;
						points.add(new PointF(x, y));
					}
					// 9
					y = end.y + gapX / 2;
					points.add(new PointF(x, y));

					// 10
					y = end.y;
					points.add(new PointF(x, y));
				}

				if (start.y == end.y) {
					// if there's a step between these
					if (source.getColumn() + 1 < target.getColumn()) {
						x = source.x + source.width + gapX / 2;
						y = source.y + source.height / 2;
						points.add(new PointF(x, y));

						y -= gapX / 2;
						points.add(new PointF(x, y));

						y = source.y;
						points.add(new PointF(x, y));

						y -= gapX / 2;
						points.add(new PointF(x, y));

						x += gapX / 2;
						points.add(new PointF(x, y));

						x = target.x - gapX;
						points.add(new PointF(x, y));

						x += gapX / 2;
						points.add(new PointF(x, y));

						y += gapX / 2;
						points.add(new PointF(x, y));

						y = end.y - gapX / 2;
						points.add(new PointF(x, y));

						y = end.y;
						points.add(new PointF(x, y));
					} else {
						points.add(start);
						points.add(end);
					}
				}

				if (start.y < end.y) {

					// 1
					x = start.x + source.getParent().getGapX() / 2;
					y = start.y;
					points.add(new PointF(x, y));

					// 2
					y += source.getParent().getGapX() / 2;
					points.add(new PointF(x, y));

					if (target.getColumn() > source.getColumn() + 1) {
						// 3
						y = target.y - gapY / 2 - gapX / 2;
						points.add(new PointF(x, y));

						// 4
						y += gapX / 2;
						points.add(new PointF(x, y));

						// 5
						x += gapX / 2;
						points.add(new PointF(x, y));

						// 6
						x = target.x - gapX;
						points.add(new PointF(x, y));

						// 7
						x = target.x - gapX / 2;
						points.add(new PointF(x, y));

						// 8
						y += gapX / 2;
						points.add(new PointF(x, y));
					}
					// 9
					y = end.y - gapX / 2;
					points.add(new PointF(x, y));

					// 10
					y = end.y;
					points.add(new PointF(x, y));
				}

				path.moveTo(start.x, start.y);
				for (int i = 0; i < points.size(); i += 3) {
					if (points.size() >= i + 3) {
						path.quadTo(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);
						path.lineTo(points.get(i + 2).x, points.get(i + 2).y);
					} else if (points.size() >= i + 1) {
						path.quadTo(points.get(i).x, points.get(i).y, end.x, end.y);
						break;
					}
				}

				makeArrowHead(points);
			}

			else if (source.getParent() == target) {
				start = new PointF(source.getX() + source.getWidth(), source.getY() + source.getHeight() / 2);
				end = new PointF(target.getX() + target.getWidth(), target.getY() + target.getHeight() / 2);
			}

			else if (target.getParent() == source) {
				start = new PointF(source.getX(), source.getY() + source.getHeight() / 2);
				end = new PointF(target.getX(), target.getY() + target.getHeight() / 2);
			}

		}

		public void makeArrowHead(LinkedList<PointF> points) {
			arrowhead.rewind();
			PointF position = new PointF();
			PointF direction = new PointF();

			int size = 10;
			PointF start;
			PointF end;
			if (points.size() > 2) {
				start = points.get(points.size() / 2 - 1);
				end = points.get(points.size() / 2);
			} else {
				start = points.get(0);
				end = points.get(1);
			}

			position.x = (start.x + end.x) / 2;
			position.y = (start.y + end.y) / 2;

			direction.x = end.x - start.x;
			direction.y = end.y - start.y;
			float length = direction.length();
			direction.x /= length;
			direction.y /= length;

			arrowhead.moveTo(position.x + size * direction.x, position.y + size * direction.y);
			Matrix m = new Matrix();
			m.setRotate(150);
			float[] vector = { direction.x, direction.y };
			m.mapVectors(vector);
			arrowhead.lineTo(position.x + vector[0] * size, position.y + vector[1] * size);
			m.setRotate(-150);
			vector[0] = direction.x;
			vector[1] = direction.y;
			m.mapVectors(vector);
			arrowhead.lineTo(position.x + vector[0] * size, position.y + vector[1] * size);
			arrowhead.close();
		}

		public void draw(Canvas canvas) {
			if (!set) {
				set = true;
				makePath();
			}

			// canvas.drawPath(path, paint);
			if (source.isSibling(target)) {
				// canvas.drawLine(source.getX() + source.getWidth(),
				// source.getY() + source.getHeight() / 2, target.getX(),
				// target.getY() + target.getHeight() / 2, paint);
				canvas.drawPath(path, paint);
				canvas.drawPath(arrowhead, pArrowhead);
			} else if (source.getParent() == target) {
				canvas.drawLine(source.getX() + source.getWidth(), source.getY() + source.getHeight() / 2, target.getX() + target.getWidth(), target.getY() + target.getHeight()
								/ 2, paint);
			} else if (target.getParent() == source) {
				canvas.drawLine(source.getX(), source.getY() + source.getHeight() / 2, target.getX(), target.getY() + target.getHeight() / 2, paint);
			}
		}

	}

	public static DrawableStep findDrawableStep(DrawableStep root, ProcessStep searchFor) {
		if (root == null)
			return null;
		if (root.getStep() == searchFor)
			return root;

		for (DrawableStep child : root.getChildren()) {
			if (child.getStep() == searchFor)
				return child;
		}

		for (DrawableStep child : root.getChildren()) {
			return findDrawableStep(child, searchFor);
		}
		return null;
	}
}
