package com.mbq.easygapps.two.adapters;

import com.mbq.easygapps.two.fragments.AppChangelog;
import com.mbq.easygapps.two.fragments.Welcome;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class WelcomeAdapter extends FragmentPagerAdapter {

	final int PAGE_COUNT = 2;
	// Tab Titles
	private String tabtitles[] = new String[] { "Welcome", "Changelog" };
	Context context;

	public WelcomeAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {

		case 0:
			Welcome welcome = new Welcome();
			return welcome;

		case 1:
			AppChangelog ac = new AppChangelog();
			return ac;

		}
		return null;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return tabtitles[position];
	}
}