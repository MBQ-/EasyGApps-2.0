package com.mbq.easygapps.two.fragments;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.mbq.easygapps.two.point.oh.R;
import com.npi.blureffect.mbq.Blur;
import com.npi.blureffect.mbq.ImageUtils;
import com.npi.blureffect.mbq.ScrollableImageView;

public class AppChangelog extends Fragment {
	private static final String BLURRED_IMG_PATH = "blurred_image.png";
	private static final int TOP_HEIGHT = 260;
	private ListView mList;
	private ImageView mBlurredImage;
	private View headerView;
	private ImageView mNormalImage;
	private ScrollableImageView mBlurredImageHeader;
	private float alpha;
	
	private class LVItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);

			view.isHorizontalFadingEdgeEnabled();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.changelog_fragment, container,
				false);	
		
		final int screenWidth = ImageUtils.getScreenWidth(getActivity());

		mBlurredImage = (ImageView) view.findViewById(R.id.blurred_image2);
		mNormalImage = (ImageView) view.findViewById(R.id.normal_image2);
		mBlurredImageHeader = (ScrollableImageView) view.findViewById(R.id.blurred_image_header2);
		mList = (ListView) view.findViewById(R.id.list2);

		mList.setOnItemClickListener(new LVItemClickListener());

		mBlurredImageHeader.setScreenWidth(screenWidth);
		
		final File blurredImage = new File(getActivity().getFilesDir() + BLURRED_IMG_PATH);
		if (!blurredImage.exists()) {

			getActivity().setProgressBarIndeterminateVisibility(true);

			new Thread(new Runnable() {

				@Override
				public void run() {

					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 2;
					Bitmap image = BitmapFactory.decodeResource(getResources(),
							R.drawable.ic_xenon_hd_ic, options);
					Bitmap newImg = Blur.fastblur(getActivity(), image, 12);
					ImageUtils.storeImage(newImg, blurredImage);
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							updateView(screenWidth);

							getActivity().setProgressBarIndeterminateVisibility(false);
						}
					});

				}
			}).start();

		} else {
			updateView(screenWidth);

			if (savedInstanceState == null) {

				selectItem(0);
			}
		}	
		
		String[] strings = getResources().getStringArray(R.array.Changelog);

		headerView = new View(getActivity());
		headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				TOP_HEIGHT));
		mList.addHeaderView(headerView);
		mList.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item2,
				strings));
		mList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			/**
			 * Listen to the list scroll. This is where magic happens ;)
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				alpha = (float) -headerView.getTop() / (float) TOP_HEIGHT;
				
				if (alpha > 1) {
					alpha = 1;
				}

				mBlurredImage.setTop(headerView.getTop() / 2);
				mNormalImage.setTop(headerView.getTop() / 2);
				mBlurredImageHeader.handleScroll(headerView.getTop() / 2);

			}
		});

		return view;
	}

	private void updateView(final int screenWidth) {
		Bitmap bmpBlurred = BitmapFactory.decodeFile(getActivity().getFilesDir()
				+ BLURRED_IMG_PATH);
		bmpBlurred = Bitmap
				.createScaledBitmap(
						bmpBlurred,
						screenWidth,
						(int) (bmpBlurred.getHeight() * ((float) screenWidth) / (float) bmpBlurred
								.getWidth()), false);

		mBlurredImage.setImageBitmap(bmpBlurred);

		mBlurredImageHeader.setoriginalImage(bmpBlurred);
	}

	private void selectItem(int position) {
		switch (position) {

		case 0:
			
			break;

		case 1:
			
			break;

		case 2:
			
			break;

		case 3:
			
			break;

		}
	}
}
