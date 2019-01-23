package example.com.hvacawards;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    NavigationView navigation;

    RecyclerView pendingrv;
    DatabaseReference userdata;
    DatabaseReference mDatabaseSet,getmDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        drawerLayout = findViewById(R.id.drawerLayout7);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigation = findViewById(R.id.navigationadmin);
        navigation.setNavigationItemSelectedListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        userdata = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseSet = FirebaseDatabase.getInstance().getReference().child("Accepted");
        getmDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Pending");

        pendingrv = findViewById(R.id.requestrv);
        pendingrv.setHasFixedSize(true);
        pendingrv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<reqModel,PendingViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<reqModel, PendingViewHolder>(
                        reqModel.class,
                        R.layout.activity_request_row,
                        PendingViewHolder.class,
                        getmDatabaseRef) {
                    @Override
                    protected void populateViewHolder(final PendingViewHolder viewHolder, final reqModel model, final int position) {
                        userdata.child(model.getUserid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                                viewHolder.setUserid(userProfile.getFullName());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        viewHolder.allowbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDatabaseSet.child(model.getUserid()).child("Status").setValue("true");
                                getRef(position).removeValue();
                                pendingrv.getAdapter().notifyItemRemoved(position);
                                pendingrv.getAdapter().notifyItemRangeChanged(position,getItemCount());
                            }
                        });
                    }
                };
        pendingrv.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PendingViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Button allowbtn,denybtn;

        public PendingViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            allowbtn = mView.findViewById(R.id.allow);
            denybtn = mView.findViewById(R.id.deny);

        }
        public void setUserid(String idofuser){
            TextView username = mView.findViewById(R.id.addusername);
            username.setText(idofuser);
        }
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
        } else if(id == R.id.adminhome){
            startActivity(new Intent(AdminHome.this,AdminHome.class));
        } else if (id == R.id.compliants){
            startActivity(new Intent(AdminHome.this,Complaints.class));
        } else if (id == R.id.installations) {
            startActivity(new Intent(AdminHome.this, Installation.class));
        } else if (id == R.id.services) {
            startActivity(new Intent(AdminHome.this, Services.class));
        } else if (id == R.id.pendingproj){
            startActivity(new Intent(AdminHome.this,PendingProjects.class));
        } else if (id == R.id.projects){
            startActivity(new Intent(AdminHome.this,AdminProject.class));
        } else if (id == R.id.logout) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            startActivity(new Intent(AdminHome.this, SignIn.class));
            finish();
        }
        return true;
    }
}