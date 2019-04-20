package com.cseaeventmanagement;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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

import static android.Manifest.permission.READ_CONTACTS;
import static android.text.TextUtils.isEmpty;
import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;

//import android.annotation.TargetApi;

public class SignupActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
	private static final int REQUEST_READ_CONTACTS = 0;
	private RequestQueue q = null;
	private boolean SignupInQueue = false;
	// UI references.
	private AutoCompleteTextView mEmailView;
	private TextInputEditText mPasswordView;
	private AutoCompleteTextView mNameView;
	private TextInputEditText mConfirmPasswordView;
	private AutoCompleteTextView mRollNumberView;
	private Spinner mSpinnerdeptview;
	private Spinner mSpinnerprogview;
	private AutoCompleteTextView mPhoneNumberView;
	private View mProgressView;
	private View mSignupFormView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		// Set up the Signup form.
		mEmailView = findViewById(R.id.email);
		populateAutoComplete();
		mNameView = findViewById(R.id.name);
		mConfirmPasswordView = findViewById(R.id.confirmPassword);
		mRollNumberView = findViewById(R.id.rollNumber);
		mSpinnerdeptview = findViewById(R.id.dept);
		mSpinnerprogview = findViewById(R.id.prog);
		mPhoneNumberView = findViewById(R.id.phone_no);
		mPasswordView = findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
					attemptSignup();
					return true;
				}
				return false;
			}
		});

		Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptSignup();
			}
		});
		Network network = new BasicNetwork(new HurlStack());
		q = new RequestQueue(new NoCache(), network);
		q.start();
		mSignupFormView = findViewById(R.id.Signup_form);
		mProgressView = findViewById(R.id.Signup_progress);
	}

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
		if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
			Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
					.setAction(android.R.string.ok, new View.OnClickListener() {
						@Override
//						@TargetApi(Build.VERSION_CODES.M)
						public void onClick(View v) {
							requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
						}
					});
		} else {
			requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
		}
		return false;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		if (requestCode == REQUEST_READ_CONTACTS) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				populateAutoComplete();
			}
		}
	}

	private void attemptSignup() {
		if (SignupInQueue)
			return;
		mEmailView.setError(null);
		mPasswordView.setError(null);
		mNameView.setError(null);
		mConfirmPasswordView.setError(null);
		mRollNumberView.setError(null);
		mPhoneNumberView.setError(null);
		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();
		String name = mNameView.getText().toString();
		String confPass = mConfirmPasswordView.getText().toString();
		String rollNo = mRollNumberView.getText().toString();
		String phone = mPhoneNumberView.getText().toString();
		boolean cancel = false;
		View focusView = null;
		// Check for a valid password, if the user entered one.
		if (isEmpty(phone)) {
			mPhoneNumberView.setError(getString(R.string.error_empty_field));
			focusView = mPhoneNumberView;
			cancel = true;
		} else if (!phone.matches("[0-9]*")) {
			mPhoneNumberView.setError(getString(R.string.error_not_integer));
			focusView = mPhoneNumberView;
			cancel = true;
		}
		long iphone;

		iphone = Long.parseLong(phone);

		int iroll = 0;
		if (isEmpty(rollNo)) {
			mRollNumberView.setError(getString(R.string.error_empty_field));
			focusView = mRollNumberView;
			cancel = true;
		} else if (!rollNo.matches("[0-9]{9}")) {
			mRollNumberView.setError(getString(R.string.error_not_integer));
			focusView = mRollNumberView;
			cancel = true;
		} else {
			iroll = Integer.parseInt(rollNo);
			if (iroll < 170101000 || iroll > 201000999) {
				mRollNumberView.setError(getString(R.string.error_invalid_value));
				focusView = mRollNumberView;
				cancel = true;
			}
		}
		if (isEmpty(password)) {
			Log.d("hello", password);
			mPasswordView.setError(getString(R.string.error_empty_password));
			focusView = mPasswordView;
			cancel = true;
		}
		if (isEmpty(confPass)) {
			mConfirmPasswordView.setError(getString(R.string.error_empty_password));
			focusView = mConfirmPasswordView;
			cancel = true;
		}
		if (password == (confPass)) {
			mPasswordView.setError("Passwords do not match");
			focusView = mPasswordView;
			cancel = true;
		}
		// Check for a valid email address.
		if (isEmpty(email)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!isEmailValid(email)) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}
		if (isEmpty(name)) {
			mNameView.setError(getString(R.string.error_field_required));
			focusView = mNameView;
			cancel = true;
		} else if (!isNameValid(name)) {
			mNameView.setError(getString(R.string.error_invalid_email));
			focusView = mNameView;
			cancel = true;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			//private UserSignupTask mAuthTask = null;
			String mEmail = email.split("@", -1)[0];
			JSONObject obj = new JSONObject();
			try {
				String lastname, firstname;
				name = name.trim();
				int a = name.lastIndexOf(" ");
				if (a == -1) {
					lastname = " ";
					firstname = name;
				} else {
					lastname = name.substring(a + 1);
					firstname = name.substring(0, a);
				}
				String asd = "na";
				if (mSpinnerprogview.getSelectedItemId() != 0)
					asd = mSpinnerprogview.getSelectedItem().toString().toLowerCase();
				obj.accumulate("email", mEmail + "@iitg.ac.in");
				obj.accumulate("password", password);
				obj.accumulate("first_name", firstname);
				obj.accumulate("last_name", lastname);
				obj.accumulate("prog", mSpinnerdeptview.getSelectedItem().toString().toLowerCase());
				obj.accumulate("dept", asd);
				obj.accumulate("roll_no", iroll);
				obj.accumulate("phone_no", iphone);
			} catch (Exception e) {
				Log.d("hello", e.toString());
			}
			Log.d("hello", obj.toString());
			JsonObjectRequest jor = new JsonObjectRequest(
					Request.Method.POST,
					getString(R.string.ip) + "acceptor/",
					obj,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							boolean a = false;
							Log.d("hello", response.toString());
							try {
								JSONObject resp = new JSONObject(response.toString());
								a = resp.getBoolean("registration_status");
							} catch (Exception e) {
							}

							showProgress(false);
							if (a)
								finish();
							else
								Snackbar.make(findViewById(R.id.Signup_form), "Error signing up. Make sure you are not already signed up", Snackbar.LENGTH_SHORT)
										.setAction("Dismiss", new View.OnClickListener() {
											@Override
											public void onClick(View v) {

											}
										}).show();
						}
					},
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.d("hello", error.toString());
							showProgress(false);
							Snackbar.make(findViewById(R.id.Signup_form), "Error signing up. Check your network and try again", Snackbar.LENGTH_SHORT)
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

	private boolean isNameValid(String name) {
		boolean a;
		char[] namec = name.toCharArray();
		for (char c : namec) {
			a = isLetter(c) || isDigit(c) || c == ' ' || c == '\'';
			if (!a)
				return false;
		}
		return true;
	}

	private void showProgress(final boolean show) {
		int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
		mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		mSignupFormView.animate().setDuration(shortAnimTime).alpha(
				show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
				new ArrayAdapter<>(SignupActivity.this,
						android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

		mEmailView.setAdapter(adapter);
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

