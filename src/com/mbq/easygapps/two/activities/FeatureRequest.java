package com.mbq.easygapps.two.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.mbq.easygapps.two.point.oh.R;

public class FeatureRequest extends Activity {

	private EditText recipient;
	private EditText subject;
	private EditText body;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feature_request);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		getActionBar().setHomeButtonEnabled(true);

		recipient = (EditText) findViewById(R.id.recipient);
		subject = (EditText) findViewById(R.id.subject);
		body = (EditText) findViewById(R.id.body);

	}

	protected void sendFeatureRequest() {

		String[] recipients = { recipient.getText().toString() };
		Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
		// prompts email clients only
		email.setType("message/rfc822");

		email.putExtra(Intent.EXTRA_EMAIL, recipients);
		email.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
		email.putExtra(Intent.EXTRA_TEXT, body.getText().toString());

		try {
			// the user can choose the email client
			startActivity(Intent.createChooser(email,
					"Choose an email client from..."));

		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(FeatureRequest.this, "No email client installed.",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.feature_request, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.sendRequest:
			sendFeatureRequest();
			break;

		case android.R.id.home:
			super.onBackPressed();
			break;

		default:

		}
		;

		return super.onOptionsItemSelected(item);
	}

}
