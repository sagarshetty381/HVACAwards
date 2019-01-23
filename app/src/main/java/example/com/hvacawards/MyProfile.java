package example.com.hvacawards;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MyProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    NavigationView navigation;

    CardView awards,rating;
    private DatabaseReference mDatabase,Acceptance,Pendings;
    private FirebaseDatabase mFirebaseDatabase;
    String uid,uemail,result="";

    TextView nametv,addresstv,promotv,emailtv,mobiletv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_profile);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }

        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
                !=PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},5);
            }
        }

        startService(new Intent(MyProfile.this,FirebaseBackgroundService.class));

        overridePendingTransition(R.anim.slide_in,R.anim.slide_in);

        drawerLayout = findViewById(R.id.drawerLayout4);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigation = findViewById(R.id.naviagtion);
        navigation.setNavigationItemSelectedListener(this);

        awards = findViewById(R.id.awards);
        rating = findViewById(R.id.ratingcard);

        awards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfile.this,BankActivity.class));
                finish();
                awards.setVisibility(View.GONE);
            }
        });
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        uemail = user.getEmail();

        Acceptance = FirebaseDatabase.getInstance().getReference().child("Accepted");
        Pendings = FirebaseDatabase.getInstance().getReference().child("Pending");

        Acceptance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid)){
                    AcceptModel acceptModel = dataSnapshot.child(uid).getValue(AcceptModel.class);
                    result = acceptModel.getStatus();
                    if (result.equals("true")){
                        rating.setVisibility(View.GONE);
                    }else if(result.equals("false")){
                        rating.setVisibility(View.GONE);
                    }
                }else{
                    rating.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> datamap = new HashMap<String, String>();
                datamap.put("Userid",uid);
                Acceptance.child(uid).child("Status").setValue("false");
                Pendings.push().setValue(datamap);
                rating.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(MyProfile.this);
                builder.setMessage("A request has been sent to the server. You can rate once the request is accepted.");
                builder.setCancelable(true);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
            }
        });
        Log.e("hghsa",uemail);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseDatabase.getReference().child("Users").child(uid);

        nametv = findViewById(R.id.name);
        addresstv = findViewById(R.id.address);
        promotv = findViewById(R.id.promoprofile);
        emailtv = findViewById(R.id.email);
        mobiletv = findViewById(R.id.mobileno);

        mDatabase. addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                String name = userProfile.getFullName();
                String address = userProfile.getAddress();
                String promo = userProfile.getPromoCode();
                String mobile = userProfile.getMobileNo();

                if (mobile == null){
                    TextView visi = findViewById(R.id.tret);
                    visi.setVisibility(View.GONE);
                }
                if (address == null){
                    TextView dis = findViewById(R.id.vis2);
                    dis.setVisibility(View.GONE);
                }

                nametv.setText(name);
                addresstv.setText(address);
                promotv.setText(promo);
                emailtv.setText(uemail);
                mobiletv.setText(mobile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
            startActivity(new Intent(MyProfile.this,MyProfile.class));
        } else if (id == R.id.myprojects){
            startActivity(new Intent(MyProfile.this,MyProjects.class));
        } else if (id == R.id.navprojects) {
            startActivity(new Intent(MyProfile.this, UploadImages.class));
        } else if (id == R.id.navbank) {
            startActivity(new Intent(MyProfile.this, BankActivity.class));
        } else if (id == R.id.navratings) {
            if (result.equals("true")) {
                startActivity(new Intent(MyProfile.this, Ratings.class));
            }else{
                Toast.makeText(this, "Sorry, you don't have permission to access this.", Toast.LENGTH_SHORT).show();
            }
        }else if (id == R.id.navlogout) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            startActivity(new Intent(MyProfile.this, SignIn.class));
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            finish();
        }
        return true;
    }
}