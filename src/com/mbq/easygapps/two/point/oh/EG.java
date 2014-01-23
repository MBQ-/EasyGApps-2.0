package com.mbq.easygapps.two.point.oh;

import java.io.File;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mbq.easygapps.two.activities.FeatureRequest;
import com.mbq.easygapps.two.activities.GApps;
import com.mbq.easygapps.two.activities.ReportABug;
import com.mbq.easygapps.two.adapters.WelcomeAdapter;

public class EG extends FragmentActivity {
	public static class CategoriesFragment extends Fragment {

		public static final String ARG_CATEGORY = "category";

		public CategoriesFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.fragment_categories,
					container, false);

			int i = getArguments().getInt(ARG_CATEGORY);

			String List = getResources()
					.getStringArray(R.array.MenuDrawerStuff)[i];

			getActivity().setTitle(List);

			return rootView;

		}
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);

			view.isHorizontalFadingEdgeEnabled();
		}
	}

	private static final int PERIOD = 2000;

	Context context;

	Intent intent;

	TextView text;

	private long lastPressedTime;

	private String[] mCategories;

	private DrawerLayout mDrawerLayout;

	@SuppressWarnings("unused")
	// y
	private CharSequence mDrawerTitle;

	private ActionBarDrawerToggle mDrawerToggle;

	SharedPreferences mPreferences;

	private ListView mDrawerList;

	private CharSequence mTitle;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);

		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void onConfigurationChanged2(Configuration newConfig2) {
		try {
			super.onConfigurationChanged(newConfig2);
			if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

			} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

			}
		} catch (Exception ex) {
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		File folder = new File(Environment.getExternalStorageDirectory()
				+ "/EasyGApps-2.0/");
		boolean success = true;
		if (!folder.exists()) {
			success = folder.mkdir();
		}
		if (success) {

		} else {

		}

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.eg);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		mTitle = mDrawerTitle = getTitle();

		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mCategories = getResources().getStringArray(R.array.MenuDrawerStuff);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		text = new TextView(this);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mCategories));

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		getActionBar().setDisplayHomeAsUpEnabled(true);

		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(

		this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close)

		{
			@Override
			public void onDrawerClosed(View view) {

				Drawable icon = null;
				getActionBar().setIcon(icon);

				invalidateOptionsMenu();

			}

			@Override
			public void onDrawerOpened(View drawerView) {

				Drawable icon = null;
				getActionBar().setIcon(icon);

				invalidateOptionsMenu();

			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {

			selectItem(0);
		}

		ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

		// Set the ViewPagerAdapter into ViewPager
		viewPager.setAdapter(new WelcomeAdapter(getSupportFragmentManager()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.eg, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			switch (event.getAction()) {
			case KeyEvent.ACTION_DOWN:
				if (event.getDownTime() - lastPressedTime < PERIOD) {
					finish();
				} else {
					Toast.makeText(getApplicationContext(),
							"Press again to exit.", Toast.LENGTH_SHORT).show();
					lastPressedTime = event.getEventTime();
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {

			return true;
		}
		switch (item.getItemId()) {

		default:

		}
		;

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {

		super.onPostCreate(savedInstanceState);

		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		return super.onPrepareOptionsMenu(menu);
	}

	private void selectItem(int position) {

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		switch (position) {

		case 0:
			// ...
			break;

		case 1:
			Intent gapps = new Intent(this, GApps.class);
			gapps.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(gapps);
			break;

		case 2:
			Intent fr = new Intent(this, FeatureRequest.class);
			fr.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(fr);
			break;

		case 3:
			Intent br = new Intent(this, ReportABug.class);
			br.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(br);
			break;

		}

		ft.commit();

		mDrawerList.setItemChecked(position, true);

		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}
}
