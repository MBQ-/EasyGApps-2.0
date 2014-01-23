package com.mbq.easygapps.two.activities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mbq.easygapps.two.point.oh.R;
import com.npi.blureffect.mbq.Blur;
import com.npi.blureffect.mbq.ImageUtils;
import com.npi.blureffect.mbq.ScrollableImageView;

@SuppressLint("SdCardPath")
public class GApps extends Activity {
	Context context;

	Intent intent;

	TextView text;

	private ProgressDialog stockDialog;

	private ProgressDialog paDialog;

	private ProgressDialog bDialog;

	public static final int stock_gapps = 0;

	public static final int pa_gapps = 1;

	public static final int banks_gapps = 2;

	private static String stock = "http://api.mbqonxda.net/EasyGapps/GApps/Stock/gapps-kk-20140105-signed.zip";

	private static String pa = "http://api.mbqonxda.net/EasyGapps/GApps/ParanoidAndroid/pa_gapps-stock-4.4.2-20140111-signed.zip";

	private static String BanKs = "http://api.mbqonxda.net/EasyGapps/GApps/BanKs/1-15_GApps_Standard_4.4.2_signed.zip";

	private static final String BLURRED_IMG_PATH = "blurred_image.png";
	private static final int TOP_HEIGHT = 160;
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gapps);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		getActionBar().setHomeButtonEnabled(true);

		// Get the screen width
		final int screenWidth = ImageUtils.getScreenWidth(this);

		// Find the view
		mBlurredImage = (ImageView) findViewById(R.id.blurred_image);
		mNormalImage = (ImageView) findViewById(R.id.normal_image);
		mBlurredImageHeader = (ScrollableImageView) findViewById(R.id.blurred_image_header);
		mList = (ListView) findViewById(R.id.list);

		mList.setOnItemClickListener(new LVItemClickListener());

		// prepare the header ScrollableImageView
		mBlurredImageHeader.setScreenWidth(screenWidth);

		// Try to find the blurred image
		final File blurredImage = new File(getFilesDir() + BLURRED_IMG_PATH);
		if (!blurredImage.exists()) {

			// launch the progressbar in ActionBar
			setProgressBarIndeterminateVisibility(true);

			new Thread(new Runnable() {

				@Override
				public void run() {

					// No image found => let's generate it!
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 2;
					Bitmap image = BitmapFactory.decodeResource(getResources(),
							R.drawable.ic_xenon_hd_ic, options);
					Bitmap newImg = Blur.fastblur(GApps.this, image, 12);
					ImageUtils.storeImage(newImg, blurredImage);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							updateView(screenWidth);

							// And finally stop the progressbar
							setProgressBarIndeterminateVisibility(false);
						}
					});

				}
			}).start();

		} else {

			// The image has been found. Let's update the view
			updateView(screenWidth);

			if (savedInstanceState == null) {

				selectItem(0);
			}
		}

		String[] strings = getResources().getStringArray(R.array.GAppsChoices);

		// Prepare the header view for our list
		headerView = new View(this);
		headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				TOP_HEIGHT));
		mList.addHeaderView(headerView);
		mList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
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

				// Calculate the ratio between the scroll amount and the list
				// header weight to determinate the top picture alpha
				alpha = (float) -headerView.getTop() / (float) TOP_HEIGHT;
				// Apply a ceil
				if (alpha > 1) {
					alpha = 1;
				}

				// Parallax effect : we apply half the scroll amount to our
				// three views
				mBlurredImage.setTop(headerView.getTop() / 2);
				mNormalImage.setTop(headerView.getTop() / 2);
				mBlurredImageHeader.handleScroll(headerView.getTop() / 2);

			}
		});
	}

	class DownloadFileFromURL extends AsyncTask<String, String, String> {

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(stock_gapps);
		}

		@Override
		protected String doInBackground(String... f_url) {
			int count;
			try {
				URL url = new URL(f_url[0]);
				URLConnection conection = url.openConnection();
				conection.connect();

				int lenghtOfFile = conection.getContentLength();

				InputStream input = new BufferedInputStream(url.openStream(),
						4096);

				OutputStream output = new FileOutputStream(
						"/sdcard/EasyGApps-2.0/GApps/Stock-Android/Latest-Android-GApps.zip");

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));

					// writing data to file
					output.write(data, 0, count);
				}

				// flushing output
				output.flush();

				// closing streams
				output.close();
				input.close();

			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}

			return null;
		}

		protected void onProgressUpdate(String... progress) {

			stockDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String file_url) {
			dismissDialog(stock_gapps);
		}
	}

	class DownloadFileFromURL2 extends AsyncTask<String, String, String> {

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(pa_gapps);
		}

		@Override
		protected String doInBackground(String... f_url) {
			int count;
			try {
				URL url = new URL(f_url[0]);
				URLConnection conection = url.openConnection();
				conection.connect();

				int lenghtOfFile = conection.getContentLength();

				InputStream input = new BufferedInputStream(url.openStream(),
						4096);

				OutputStream output = new FileOutputStream(
						"/sdcard/EasyGApps-2.0/GApps/ParanoidAndroid/Latest-PA-GApps.zip");

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));

					// writing data to file
					output.write(data, 0, count);
				}

				// flushing output
				output.flush();

				// closing streams
				output.close();
				input.close();

			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}

			return null;
		}

		protected void onProgressUpdate(String... progress) {

			paDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String file_url) {
			dismissDialog(pa_gapps);
		}
	}

	class DownloadFileFromURL3 extends AsyncTask<String, String, String> {

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(banks_gapps);
		}

		@Override
		protected String doInBackground(String... f_url) {
			int count;
			try {
				URL url = new URL(f_url[0]);
				URLConnection conection = url.openConnection();
				conection.connect();

				int lenghtOfFile = conection.getContentLength();

				InputStream input = new BufferedInputStream(url.openStream(),
						4096);

				OutputStream output = new FileOutputStream(
						"/sdcard/EasyGApps-2.0/GApps/BanKs/Latest-BanKs-GApps.zip");

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));

					// writing data to file
					output.write(data, 0, count);
				}

				// flushing output
				output.flush();

				// closing streams
				output.close();
				input.close();

			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}

			return null;
		}

		protected void onProgressUpdate(String... progress) {

			bDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String file_url) {
			dismissDialog(banks_gapps);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case stock_gapps:
			stockDialog = new ProgressDialog(this);
			stockDialog
					.setMessage("Downloading the latest stock Android GApps.");
			stockDialog.setIndeterminate(false);
			stockDialog.setMax(100);
			stockDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			stockDialog.setCancelable(true);
			stockDialog.setCanceledOnTouchOutside(false);
			stockDialog.show();
			return stockDialog;
		case pa_gapps:
			paDialog = new ProgressDialog(this);
			paDialog.setMessage("Downloading the latest PA GApps.");
			paDialog.setIndeterminate(false);
			paDialog.setMax(100);
			paDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			paDialog.setCancelable(true);
			paDialog.setCanceledOnTouchOutside(false);
			paDialog.show();
			return paDialog;
		case banks_gapps:
			bDialog = new ProgressDialog(this);
			bDialog.setMessage("Downloading the latest BanKs GApps.");
			bDialog.setIndeterminate(false);
			bDialog.setMax(100);
			bDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			bDialog.setCancelable(true);
			bDialog.setCanceledOnTouchOutside(false);
			bDialog.show();
			return bDialog;
		default:
		}
		return null;
	}

	private void selectItem(int position) {
		switch (position) {

		case 0:
			File folder = new File(Environment.getExternalStorageDirectory()
					+ "/EasyGApps-2.0/" + "GApps/");
			File folder2 = new File(Environment.getExternalStorageDirectory()
					+ "/EasyGApps-2.0/GApps/" + "ParanoidAndroid/");
			File folder3 = new File(Environment.getExternalStorageDirectory()
					+ "/EasyGApps-2.0/GApps/" + "Stock-Android/");
			File folder4 = new File(Environment.getExternalStorageDirectory()
					+ "/EasyGApps-2.0/GApps/" + "BanKs/");

			boolean success = true;
			if (!folder.exists()) {
				success = folder.mkdir();
				success = folder2.mkdir();
				success = folder3.mkdir();
				success = folder4.mkdir();

			} else if (!folder.exists() == true) {
				// Do nothing :)
			}

			// Otherwise, the Header just chills.
			break;

		case 1:
			new DownloadFileFromURL().execute(stock);
			break;

		case 2:
			new DownloadFileFromURL2().execute(pa);
			break;

		case 3:
			new DownloadFileFromURL3().execute(BanKs);
			break;

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.eg, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			super.onBackPressed();
			break;

		default:

		}
		;

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		return super.onPrepareOptionsMenu(menu);
	}

	private void updateView(final int screenWidth) {
		Bitmap bmpBlurred = BitmapFactory.decodeFile(getFilesDir()
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
}
