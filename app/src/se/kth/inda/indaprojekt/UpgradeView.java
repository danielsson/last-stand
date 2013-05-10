package se.kth.inda.indaprojekt;

import se.kth.inda.indaprojekt.engine.GameEngine;
import se.kth.inda.indaprojekt.engine.GameState;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


class UpgradeView extends LinearLayout {
	
	private GameEngine engine;
	private GameState state;
	
	private RelativeLayout btnForcePush;
	private RelativeLayout btnTeleport;
	
	private OnClickListener listener = new  OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(btnForcePush.equals(v)) {
				//Do stuff
				
			} else if(btnTeleport.equals(v)) {
				post(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(getContext(), "teleport", Toast.LENGTH_SHORT).show();
					}
				});
			}
			
		}
	};

	public UpgradeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		btnForcePush = (RelativeLayout) findViewById(R.id.btnForcePush);
		btnTeleport = (RelativeLayout) findViewById(R.id.btnTeleport);
		
		btnForcePush.setOnClickListener(listener);
		btnTeleport.setOnClickListener(listener);
		
	}
	
	public void setGameEngine(GameEngine e) {
		engine = e;
	}
	
	public void setGameState(GameState s) {
		state = s;
	}
	
	@SuppressWarnings("unused")
	public void show() {
		setVisibility(VISIBLE);
		
		if(state.hasPushUpgrade()) {
			btnForcePush.setEnabled(false);
			btnForcePush.setAlpha(0.5f);
		} else {
			btnForcePush.setEnabled(true);
			btnForcePush.setAlpha(1f);
		}
		
		if(state.hasTeleportUpgrade()) {
			btnTeleport.setEnabled(false);
			btnTeleport.setAlpha(0.5f);
		} else {
			btnTeleport.setEnabled(true);
			btnTeleport.setAlpha(1f);
		}
		
	}
	
	

	
}