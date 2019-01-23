package example.com.hvacawards;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SignUp extends AppCompatActivity {

    private EditText inputEmail,inputPassword,inputconfirmpass,refpromo,firstname,surname,inputaddress,inputmobile,inputpincode;
    private Button btnSignIn, btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    public DatabaseReference mDatabase,mDatabasetest,mDatabaseUsers;
    String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    Spinner citySpinner, stateSpinner;
    ArrayList<String> stringArrayState;
    ArrayList<String> stringArrayCity;
    String spinnerStateValue, city,spinnerCityValue,uid;

    private static final int RC_SIGN_IN = 234;
    private static final String TAG = "simplifiedcoding";
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    SignInButton googleBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleBT = findViewById(R.id.signin);
        googleBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        firstname = findViewById(R.id.firstnamein);
        surname = findViewById(R.id.lastnamein);
        refpromo = findViewById(R.id.refpromocode);
        inputconfirmpass = findViewById(R.id.confirmpassword);
        inputaddress = findViewById(R.id.addressin);
        inputpincode = findViewById(R.id.pincodein);
        inputmobile = findViewById(R.id.mobilein);

        btnSignIn = findViewById(R.id.sign_in_button);
        btnSignUp = findViewById(R.id.sign_up_button);

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);

        init();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this,SignIn.class));
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
                final String username =  firstname.getText().toString().trim();
                final String lastname = surname.getText().toString().trim();
                final String refpromocode = refpromo.getText().toString().trim();
                final String address = inputaddress.getText().toString().trim();
                final String pincode = inputpincode.getText().toString().trim();
                final String mobile = inputmobile.getText().toString().trim();
                final String confirmpass = inputconfirmpass.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                else if (TextUtils.isEmpty(password)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (password.length() < 6) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!email.matches(emailPattern)){
                    progressBar.setVisibility(View.GONE);
                    inputEmail.setError("Email address is not valid!");
                }
                else if (!(pincode.length() == 6)){
                    progressBar.setVisibility(View.GONE);
                    inputpincode.setError("Must be of 6 digits only!");
                }
                else if (mobile.length()>10 || mobile.length()<10){
                    progressBar.setVisibility(View.GONE);
                    inputmobile.setError("invalid input");
                }
                else if (TextUtils.isEmpty(username)||TextUtils.isEmpty(lastname)||
                        TextUtils.isEmpty(pincode)||TextUtils.isEmpty(mobile)||TextUtils.isEmpty(address)||TextUtils.isEmpty(confirmpass)){
                    Toast.makeText(getApplicationContext(),"Some fields are missing!!",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignUp.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    } else if (!password.equals(confirmpass)) {
                                        inputconfirmpass.setError("Password does not match!");
                                    } else {
                                        Random random = new Random();
                                        StringBuilder promocode = new StringBuilder(5);
                                        for (int i = 0; i < 5; i++) {
                                            promocode.append(CHARS.charAt(random.nextInt(CHARS.length())));
                                        }
                                        String promo = promocode.toString();
                                        String fullname = username + " " + lastname;
                                        String fulladdress = address + ","  + spinnerCityValue + "," + spinnerStateValue + "," + pincode;

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        uid = user.getUid();
                                        String emailId = user.getEmail();

                                        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                                        HashMap<String,String> usermap = new HashMap<String, String>();
                                        usermap.put("UserId",uid);
                                        usermap.put("FullName",fullname);
                                        usermap.put("Address",fulladdress);
                                        usermap.put("EmailID",emailId);
                                        usermap.put("MobileNo",mobile);
                                        usermap.put("PromoCode",promo);
                                        usermap.put("ReferencePromo",refpromocode);
                                        mDatabaseUsers.setValue(usermap);

                                        progressBar.setVisibility(View.GONE);

                                        startActivity(new Intent(SignUp.this,MainActivity.class).putExtra("mobileNo",mobile)
                                                .putExtra("Name",fullname).putExtra("isGoogle","0"));
                                        finish();
                                    }
                                }
                            });
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    void init() {
        citySpinner = findViewById(R.id.citySpinner);
        stateSpinner = findViewById(R.id.stateSpinner);

        stringArrayState = new ArrayList<String>();
        stringArrayCity = new ArrayList<String>();

        final ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(this, R.layout.spinnertextview, stringArrayCity);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapterCity);

        try {
            JSONObject obj = new JSONObject(loadJSONFromAssetState());
            JSONArray m_jArry = obj.getJSONArray("statelist");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);

                String state = jo_inside.getString("State");
                String id = jo_inside.getString("id");

                stringArrayState.add(state);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinnertextview, stringArrayState);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(adapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                String Text = stateSpinner.getSelectedItem().toString();

                spinnerStateValue = String.valueOf(stateSpinner.getSelectedItem());
                stringArrayCity.clear();

                try {
                    JSONObject obj = new JSONObject(loadJSONFromAssetCity());
                    JSONArray m_jArry = obj.getJSONArray("citylist");

                    for (int i = 0; i < m_jArry.length(); i++) {
                        JSONObject jo_inside = m_jArry.getJSONObject(i);
                        String state = jo_inside.getString("State");
                        String cityid = jo_inside.getString("id");

                        if (spinnerStateValue.equalsIgnoreCase(state)) {
                            city = jo_inside.getString("city");
                            stringArrayCity.add(city);
                        }

                    }
                    adapterCity.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerCityValue = String.valueOf(citySpinner.getSelectedItem());
                Log.e("SpinnerCityValue", spinnerCityValue);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        final String username =  firstname.getText().toString().trim();
        final String lastname = surname.getText().toString().trim();
        final String refpromocode = refpromo.getText().toString().trim();
        final String address = inputaddress.getText().toString().trim();
        final String pincode = inputpincode.getText().toString().trim();
        final String mobile = inputmobile.getText().toString().trim();

        if (!(pincode.length() == 6)){
            progressBar.setVisibility(View.GONE);
            inputpincode.setError("Must be of 6 digits only!");
        }
        else if (mobile.length()>10 || mobile.length()<10){
            progressBar.setVisibility(View.GONE);
            inputmobile.setError("invalid input");
        }
        else if (TextUtils.isEmpty(username)||TextUtils.isEmpty(lastname)||
                TextUtils.isEmpty(pincode)||TextUtils.isEmpty(mobile)||TextUtils.isEmpty(address)){
            Toast.makeText(getApplicationContext(),"Some fields are missing!!",Toast.LENGTH_SHORT).show();
        }else {
            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Log.d(TAG, "signInWithCredential:success");
                                Random random = new Random();
                                StringBuilder promocode = new StringBuilder(5);
                                for (int i = 0; i < 5; i++) {
                                    promocode.append(CHARS.charAt(random.nextInt(CHARS.length())));
                                }
                                String promo = promocode.toString();
                                String fullname = username + " " + lastname;
                                String fulladdress = address + ","  + spinnerCityValue + "," + spinnerStateValue + "," + pincode;

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                uid = user.getUid();
                                String emailId = user.getEmail();

                                mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                                HashMap<String,String> usermap = new HashMap<String, String>();
                                usermap.put("UserId",uid);
                                usermap.put("FullName",fullname);
                                usermap.put("Address",fulladdress);
                                usermap.put("EmailID",emailId);
                                usermap.put("MobileNo",mobile);
                                usermap.put("PromoCode",promo);
                                usermap.put("ReferencePromo",refpromocode);
                                mDatabaseUsers.setValue(usermap);

                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(SignUp.this, MainActivity.class).putExtra("isGoogle","0"));
                                finish();
                                Toast.makeText(SignUp.this, "User Signed In", Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                Toast.makeText(SignUp.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public String loadJSONFromAssetState() {
        String json = null;
        try {
            InputStream is = getAssets().open("state.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String loadJSONFromAssetCity() {
        String json = null;
        try {
            InputStream is = getAssets().open("cityState.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}