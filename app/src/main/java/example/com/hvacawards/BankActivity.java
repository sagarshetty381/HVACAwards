package example.com.hvacawards;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class BankActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    EditText nameet,branchet,namebanket,ifscet,accnoet;
    public DatabaseReference mDatabase;
    String uid,nameacc,branch,namebank,ifsc,accno;
    Button submit;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    NavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        drawerLayout = findViewById(R.id.drawerLayout9);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigation = findViewById(R.id.naviagtion);
        navigation.setNavigationItemSelectedListener(this);

        nameet = findViewById(R.id.nameet);
        branchet = findViewById(R.id.branchet);
        namebanket = findViewById(R.id.namebanket);
        ifscet = findViewById(R.id.ifscet);
        accnoet = findViewById(R.id.accnoet);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("BankDetails").child(uid);

        submit = findViewById(R.id.banksubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameacc = nameet.getText().toString();
                branch = branchet.getText().toString();
                namebank = namebanket.getText().toString();
                ifsc = ifscet.getText().toString();
                accno = accnoet.getText().toString();

                if (TextUtils.isEmpty(nameacc)||TextUtils.isEmpty(branch)||TextUtils.isEmpty(namebank)||
                        TextUtils.isEmpty(ifsc)||TextUtils.isEmpty(accno)) {
                    Toast.makeText(BankActivity.this, "Fill the remaining fields!!", Toast.LENGTH_SHORT).show();
                }
                else if (ifsc.length() > 11 || ifsc.length() < 11){
                    ifscet.setError("Invalid IFSC code!!");
                }
                else if (accno.length()>15){
                    accnoet.setError("Invalid Account Number!");
                }
                else {
                    HashMap<String, String> datamap = new HashMap<String, String>();
                    datamap.put("AccountHolder", nameacc);
                    datamap.put("Branch", branch);
                    datamap.put("NameBank", namebank);
                    datamap.put("IFSCcode", ifsc);
                    datamap.put("AccNo", accno);
                    datamap.put("Userid", uid);

                    mDatabase.setValue(datamap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(BankActivity.this, "Bank details updated :)", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(BankActivity.this, MyProfile.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BankActivity.this, "Something went wrong while uplaoding :(", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.isChecked()) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        } else if(id == R.id.profile){
            startActivity(new Intent(BankActivity.this,MyProfile.class));
        }else if (id == R.id.navprojects) {
            startActivity(new Intent(BankActivity.this, UploadImages.class));
        } else if (id == R.id.navbank) {
            startActivity(new Intent(BankActivity.this, BankActivity.class));
        } else if (id == R.id.navratings) {
            startActivity(new Intent(BankActivity.this, Ratings.class));
        }
        else if (id == R.id.logout) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            startActivity(new Intent(BankActivity.this, SignIn.class));
            finish();
        }
        return true;
    }
}
