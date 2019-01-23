package example.com.hvacawards;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

public class SignIn extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private DatabaseReference mDatabasetest;
    Button btnSignup, btnLogin,btnReset;


    Integer val = 0;
    private static final int RC_SIGN_IN = 234;
    private static final String TAG = "simplifiedcoding";
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    SignInButton googleBT;
    String fullname="";
    String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);

        auth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mDatabasetest = FirebaseDatabase.getInstance().getReference();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleBT = findViewById(R.id.sign);
        googleBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        if (auth.getCurrentUser() != null) {
            if (auth.getCurrentUser().getEmail().equals("admin@gmail.com")){
                startActivity(new Intent(SignIn.this,AdminHome.class));
                finish();
            }else {
                startActivity(new Intent(SignIn.this, MyProfile.class));
                finish();
            }
        }

        inputEmail = findViewById(R.id.emaillogin);
        inputPassword = findViewById(R.id.passwordlogin);
        progressBar = findViewById(R.id.progressBar);
        btnSignup = findViewById(R.id.btn_signup);
        btnLogin = findViewById(R.id.btn_login);
        btnReset = findViewById(R.id.btn_reset_password);

        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignUp.class));
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    }else {
                                        Toast.makeText(SignIn.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    if (email.equals("admin@gmail.com") && password.equals("admin123")){
                                            startActivity(new Intent(SignIn.this, AdminHome.class));
                                            finish();
                                    }else{
                                        Intent intent = new Intent(SignIn.this,MyProfile.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                        });
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
                Toast.makeText(SignIn.this,
                        "Check your internet!", Toast.LENGTH_SHORT).show();
            }
//            e.getMessage()
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        progressBar.setVisibility(View.VISIBLE);
            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithCredential:success");
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                final String uid = user.getUid();
                                final String emailId = user.getEmail();
                                final String name = user.getDisplayName();
                                Log.e("hshc",name);
                                mDatabasetest.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(uid)){

                                        }else{
                                            Random random = new Random();
                                            StringBuilder promocode = new StringBuilder(5);
                                            for (int i = 0; i < 5; i++) {
                                                promocode.append(CHARS.charAt(random.nextInt(CHARS.length())));
                                            }
                                            String promo = promocode.toString();
                                            HashMap<String,String> usermap = new HashMap<String, String>();
                                            usermap.put("PromoCode",promo);
                                            usermap.put("EmailID",emailId);
                                            usermap.put("UserId",uid);
                                            usermap.put("FullName",name);
                                            usermap.put("Address","");
                                            usermap.put("MobileNo","");
                                            usermap.put("ReferencePromo","");
                                            mDatabasetest.child("Users").child(uid).setValue(usermap);
                                        }
                                        progressBar.setVisibility(View.GONE);

                                        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                                        int defaultValue = getPreferences(MODE_PRIVATE).getInt("value",val);
                                        ++defaultValue;
                                        getPreferences(MODE_PRIVATE).edit().putInt("value",defaultValue).apply();
                                        val = getPreferences(MODE_PRIVATE).getInt("value",val);
                                        String ans = val.toString();
                                        if (ans.equals("1")){
                                            startActivity(new Intent(SignIn.this,MainActivity.class)
                                                    .putExtra("isGoogle","1"));
                                            finish();
                                        }else{
                                            startActivity(new Intent(SignIn.this,MyProfile.class));
                                            finish();
                                        }

                                        Toast.makeText(SignIn.this, "User Signed In", Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                Toast.makeText(SignIn.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}