package se.kth.inda.indaprojekt;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A simple upgrade view that allows the user to select an upgrade, if s/he have
 * enough points to do so. Remember to call create().
 */
class UpgradeView extends LinearLayout {

	public interface OnUpgradedListener {
		/**
		 * Fired when an upgrade has been selected and granted. Remember that
		 * the {@link UpgradeView} doesn't hide() itself.
		 */
		public void onUpgraded();
	}

	OnUpgradedListener upgradedListener;

	private LevelProgressHandler levelhandler;

	private int num_points = 0;

	private RelativeLayout btnForcePush;
	private RelativeLayout btnTeleport;
	private TextView txtPoints;

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (btnForcePush.equals(v)) {
				if (num_points > 100) { // TODO: Dont use hardcoded value
					levelhandler.setHasForcePushUpgrade(true);
					upgradedListener.onUpgraded();
				}

			} else if (btnTeleport.equals(v)) {
				if (num_points > 400) { // TODO: Dont use hardcoded value
					levelhandler.setHasTeleportUpgrade(true);
					upgradedListener.onUpgraded();
				}
			}

		}
	};

	public UpgradeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Perform some additional setup after the children has been initialized.
	 * 
	 * @param l
	 */
	public void create(OnUpgradedListener l) {

		btnForcePush = (RelativeLayout) findViewById(R.id.btnForcePush);
		btnTeleport = (RelativeLayout) findViewById(R.id.btnTeleport);
		txtPoints = (TextView) findViewById(R.id.txtNumPoints);

		btnForcePush.setOnClickListener(onClickListener);
		btnTeleport.setOnClickListener(onClickListener);

		upgradedListener = l;
	}

	public void setLevelHandler(LevelProgressHandler h) {
		levelhandler = h;
	}

	/**
	 * Display this view.
	 * 
	 * @param points
	 *            The amount of points available. Hint: use
	 *            {@link LevelProgressHandler}.getNumPoints().
	 */
	public void show(int points) {
		num_points = points;
		post(new Runnable() {
			
			@Override
			public void run() {

				txtPoints.setText(
					String.format(getResources().getString(R.string.num_points), num_points)
				);

				if (levelhandler.hasForcePushUpgrade()) {
					btnForcePush.setEnabled(false);
					btnForcePush.setAlpha(0.5f);
				} else {
					btnForcePush.setEnabled(true);
					btnForcePush.setAlpha(1f);
				}

				if (levelhandler.hasTeleportUpgrade()) {
					btnTeleport.setEnabled(false);
					btnTeleport.setAlpha(0.5f);
				} else {
					btnTeleport.setEnabled(true);
					btnTeleport.setAlpha(1f);
				}

				setVisibility(VISIBLE);
			}
		});
	}

	/** Hide the view. */
	public void hide() {
		post(new Runnable() {
			
			@Override
			public void run() {
				setVisibility(GONE);
			}
		});
	}

}