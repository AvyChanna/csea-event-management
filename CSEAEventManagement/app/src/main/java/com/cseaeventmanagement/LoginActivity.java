package com.cseaeventmanagement;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NoCache;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.Manifest.permission.READ_CONTACTS;

//import android.annotation.TargetApi;

//A login screen that offers login via email/password.
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
	private static final int REQUEST_READ_CONTACTS = 0;
	private RequestQueue q = null;
	private boolean LoginInQueue = false;
	// UI references.
	private JSONObject resp;
	private AutoCompleteTextView mEmailView;
	private TextInputEditText mPasswordView;
	private View mProgressView;
	private View mLoginFormView;
	private String random;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// Set up the login form.
		mEmailView = findViewById(R.id.email);
		populateAutoComplete();

		mPasswordView = findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		TextView mSignupView = findViewById(R.id.SignupLabel);
		mSignupView.setOnClickListener(new TextView.OnClickListener() {
			@Override
			public void onClick(View textView) {
				Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
				startActivity(intent);
			}
		});
		Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);

		Network network = new BasicNetwork(new HurlStack());
		q = new RequestQueue(new NoCache(), network);
		q.start();


//		TextView ok = findViewById(R.id.changePassword);
//		ok.setOnClickListener(new View.OnClickListener(){
//			@Override
//			public void onClick(View v)
//			{
//				View focusView=null;
//				boolean cancel=false;
//				TextView email = findViewById(R.id.email);
//				if(TextUtils.isEmpty(email.getText().toString()))
//				{
//					focusView = email;
//					email.setError("Tell your email ID");
//					cancel = true;
//				}
//				if(cancel)
//					focusView.requestFocus();
//				else
//				{
//					sendEmail(email.getText().toString());
//				}
//			}
//		});
	}

//	public void sendEmail(String email)
//	{
//		String[] TO = {email};
//		String[] CC = {"haharihi@gmail.com"};
//		Intent emailIntent = new Intent(Intent.ACTION_SEND);
//		emailIntent.setData(Uri.parse("mailto:"));
//		emailIntent.setType("text/plain");
//
//		random = randomString();
//
//		emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//		emailIntent.putExtra(Intent.EXTRA_CC, CC);
//		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Password Change");
//		emailIntent.putExtra(Intent.EXTRA_TEXT, "Your random token is "+random+"\n It is advisable to change your password");
//
//		try {
//			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//			finish();
//			Context context = getApplicationContext();
//			CharSequence text = "Recovery mail sent to your outlook";
//			int duration = Toast.LENGTH_SHORT;
//			Toast toast = Toast.makeText(context, text, duration);
//			toast.show();
//
//		} catch (android.content.ActivityNotFoundException ex) {
//			Context context = getApplicationContext();
//			CharSequence text = "Error sending mail";
//			int duration = Toast.LENGTH_SHORT;
//			Toast toast = Toast.makeText(context, text, duration);
//			toast.show();
//		}
//
//
//
//	}

//	public String randomString()
//	{
//		int leftLimit = 97; // letter 'a'
//		int rightLimit = 122; // letter 'z'
//		int targetStringLength = 10;
//		Random random = new Random();
//		StringBuilder buffer = new StringBuilder(targetStringLength);
//		for (int i = 0; i < targetStringLength; i++) {
//			int randomLimitedInt = leftLimit + (int)
//					(random.nextFloat() * (rightLimit - leftLimit + 1));
//			buffer.append((char) randomLimitedInt);
//		}
//		String generatedString = buffer.toString();
//
//		return generatedString;
//
//	}

	private void populateAutoComplete() {
		if (!mayRequestContacts()) {
			return;
		}

		getLoaderManager().initLoader(0, null, this);
	}

	private boolean mayRequestContacts() {
		if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
			return true;
		}
		if (shouldShowRequestPermissionRationale(READ_CONTACTS))
			Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
					.setAction(android.R.string.ok, new OnClickListener() {
						@Override
						public void onClick(View v) {
							requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
						}
					});
		else requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
		return false;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == REQUEST_READ_CONTACTS) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				populateAutoComplete();
			}
		}
	}

	private void attemptLogin() {
		if (LoginInQueue)
			return;
		mEmailView.setError(null);
		mPasswordView.setError(null);
		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();
		boolean cancel = false;
		View focusView = null;
		// Check for a valid password, if the user entered one.
		if (TextUtils.isEmpty(password)) {
			mPasswordView.setError(getString(R.string.error_empty_password));
			focusView = mPasswordView;
			cancel = true;
		}
		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!isEmailValid(email)) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}
		if (cancel) {
			focusView.requestFocus();
		} else {
			//private UserLoginTask mAuthTask = null;
			final String mEmail = email.split("@", -1)[0];
			final JSONObject obj = new JSONObject();
			try {
				obj.accumulate("username", mEmail);
				obj.accumulate("password", password);
			} catch (Exception e) {
				Log.d("hello", e.toString());
			}
			JsonObjectRequest jor = new JsonObjectRequest(
					Request.Method.POST,
					getString(R.string.ip) + "app-login/",
					obj,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							Log.d("hello", response.toString());
							showProgress(false);
							try {
								resp = new JSONObject(response.toString());
							} catch (Exception e) {
								Log.d("hello", "Malformed JSON");
							}
							checkResponse();
						}
					},
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.d("hello", error.toString());
							showProgress(false);
							Snackbar.make(findViewById(R.id.login_form), "Error signing in. Check your network and try again", Snackbar.LENGTH_SHORT)
									.setAction("Dismiss", new View.OnClickListener() {
										@Override
										public void onClick(View v) {

										}
									}).show();
						}
					}
			);
			q.add(jor);
			showProgress(true);
		}
	}

	private boolean isEmailValid(String email) {
		if (!(email.endsWith("@iitg.ernet.in") || email.endsWith("@iitg.ac.in")))
			return false;
		return email.split("@", -1).length == 2;
	}

	private void showProgress(final boolean show) {
		LoginInQueue = show;
		int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

		mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		mLoginFormView.animate().setDuration(shortAnimTime).alpha(
				show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			}
		});

		mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
		mProgressView.animate().setDuration(shortAnimTime).alpha(
				show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			}
		});

	}

	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		return new CursorLoader(this,
				// Retrieve data rows for the device user's 'profile' contact.
				Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
						ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

				// Select only email addresses.
				ContactsContract.Contacts.Data.MIMETYPE +
						" = ?", new String[]{ContactsContract.CommonDataKinds.Email
				.CONTENT_ITEM_TYPE},

				// Show primary email addresses first. Note that there won't be
				// a primary email address if the user hasn't specified one.
				ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		List<String> emails = new ArrayList<>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			emails.add(cursor.getString(ProfileQuery.ADDRESS));
			cursor.moveToNext();
		}
		addEmailsToAutoComplete(emails);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader) {
	}

	private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
		//Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
		ArrayAdapter<String> adapter =
				new ArrayAdapter<>(LoginActivity.this,
						android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
		mEmailView.setAdapter(adapter);
	}

	private void checkResponse() {
		int error_code = -1;
		String username = "";
		String name = "";
		int roll = -1;
		String branch = "";
		int year = -1;
		String stream = "";
		long phone = -1;
		try {
			error_code = resp.getInt("error_code");
		} catch (Exception e) {
		}
		try {
			username = resp.getString("username");
		} catch (Exception e) {
		}
		try {
			name = resp.getString("full_name");
		} catch (Exception e) {
		}
		try {
			roll = resp.getInt("user_roll");
		} catch (Exception e) {
		}
		try {
			branch = resp.getString("user_branch");
		} catch (Exception e) {
		}
		try {
			year = resp.getInt("user_year");
		} catch (Exception e) {
		}
		try {
			stream = resp.getString("user_stream");
		} catch (Exception e) {
		}
		try {
			phone = resp.getLong("user_phone");
		} catch (Exception e) {
		}
		if (error_code != 0) {
			Snackbar.make(findViewById(R.id.login_form), "Error signing in", Snackbar.LENGTH_SHORT)
					.setAction("Dismiss", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
						}
					}).show();
			return;
		}
		SharedPreferences sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedpreferences.edit();

		editor.clear();
		editor.apply();

		editor.putString("username", username);
		editor.putString("password",mPasswordView.getText().toString());
		editor.putString("name", name);
		editor.putLong("phone", phone);
		editor.putInt("roll", roll);
		editor.putInt("yos", year);
		editor.putString("branch", branch);
		editor.putString("stream", stream);
		editor.apply();
		finish();
	}

	private interface ProfileQuery {
		String[] PROJECTION = {
				ContactsContract.CommonDataKinds.Email.ADDRESS,
				ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
		};

		int ADDRESS = 0;
		int IS_PRIMARY = 1;
	}
}

