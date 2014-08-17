package org.xidea.app.dao;

import org.xidea.app.dao.R;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SlidingDrawer;
import android.widget.ToggleButton;
import android.support.v4.widget.DrawerLayout;

public class Main extends ActionBarActivity implements
		NavigationFragment.DrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationFragment mDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence title;

	public static Main instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		instance = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		mDrawerFragment = (NavigationFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		title = getTitle();
		
		GestureHelper.enableFliper(findViewById(R.id.container), new GestureDetector.OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent e) {
				
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent e) {
				
			}
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				if(Math.abs(velocityX) > Math.abs(velocityY)){
					int position = ConfigController.getSection()+(velocityX>0?1:-1);
					onDrawerItemSelected(position>0?position:0);
				}
				return false;
			}
			
			@Override
			public boolean onDown(MotionEvent e) {
				return false;
			}
		});

		// Set up the drawer.
		mDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		initSlidingDrawer();
	}
	public void jump(int offset) {
			int position = ConfigController.getSection()+offset;
			onDrawerItemSelected(position>0?position:0);
	}
	

	@SuppressWarnings("deprecation")
	private void initSlidingDrawer() {
		final SlidingDrawer slidingDrawer = (SlidingDrawer) findViewById(R.id.slidingdrawer);
		final ImageButton handle = (ImageButton) findViewById(R.id.handle);
		slidingDrawer
				.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
					public void onDrawerOpened() {
						handle.setImageResource(android.R.drawable.arrow_down_float);
					}
				});
		slidingDrawer
				.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
					public void onDrawerClosed() {
						handle.setImageResource(android.R.drawable.arrow_up_float);
					}
				});
		{
			ToggleButton reading = (ToggleButton) findViewById(R.id.reading);
			reading.setChecked(ConfigController.isReading());
			reading.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ConfigController.setReading(((ToggleButton) v).isChecked());
					onDrawerItemSelected(ConfigController.getSection());
				}
			});
		}
		{
			ToggleButton autoFlip = (ToggleButton) findViewById(R.id.autoFlip);
			autoFlip.setChecked(ConfigController.isAutoFlip());
			autoFlip.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ConfigController.setAutoFlip(((ToggleButton) v).isChecked());
					onDrawerItemSelected(ConfigController.getSection());
				}
			});
		}
		{
			ToggleButton sourceOnly = (ToggleButton) findViewById(R.id.sourceOnly);
			sourceOnly.setChecked(ConfigController.isSourceOnly());
			sourceOnly.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ConfigController.setSourceOnly(((ToggleButton) v)
							.isChecked());
					onDrawerItemSelected(ConfigController.getSection());
				}
			});
		}
	}

	@Override
	public void onDrawerItemSelected(int position) {
		try {
			mDrawerFragment.selectItem(position, false);
			this.title = mDrawerFragment.getTitle(position);
		} catch (Exception e) {
			onDrawerItemSelected(0);
			return;
		}
		try {
			getActionBar().setTitle(title);

			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager
					.beginTransaction()
					.replace(
							R.id.container,
							ContentFragment.newInstance(position,
									title.toString())).commit();

			ConfigController.setSection(position);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(title);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
