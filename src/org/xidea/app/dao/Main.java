package org.xidea.app.dao;

import org.xidea.app.dao.R;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SlidingDrawer;
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
	MediaController mediaController ;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		mediaController = (MediaController) findViewById(R.id.mediaController);
		mDrawerFragment = (NavigationFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		title = getTitle();

		// Set up the drawer.
		mDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		

		@SuppressWarnings("deprecation")
		final SlidingDrawer slidingDrawer = (SlidingDrawer) findViewById(R.id.slidingdrawer);  
		final ImageButton handle =(ImageButton)findViewById(R.id.handle);
        /* 
         * 打开抽屉 
         */  
        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener()  
        {  
            public void onDrawerOpened()  
            {  
                /* 
                 * 给抽屉把手换图片 
                 */  
                handle.setImageResource(android.R.drawable.arrow_down_float);  
            	//slidingDrawer.unlock();
            }  
        });  
        /* 
         * 关闭抽屉 
         */  
        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {  
            public void onDrawerClosed()  
            {  
                /* 
                 * 给抽屉把手换图片 
                 */  
                handle.setImageResource(android.R.drawable.arrow_up_float);  
            	//slidingDrawer.lock();
            }  
        });
	}

	@Override
	public void onDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		String title = mDrawerFragment.getTitle(position);
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						ContentFragment.newInstance(position ,title)).commit();
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
