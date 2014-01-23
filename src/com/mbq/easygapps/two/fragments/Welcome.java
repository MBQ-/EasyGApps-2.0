package com.mbq.easygapps.two.fragments;

import com.mbq.easygapps.two.point.oh.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Welcome extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.welcome_fragment, container,
				false);

		return view;
	}
}
