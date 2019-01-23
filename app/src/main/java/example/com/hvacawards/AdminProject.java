
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
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminProject extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    NavigationView navigation;

    private DatabaseReference mDatabase;
    RecyclerView acceptlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_project);

        drawerLayout = findViewById(R.id.drawerLayout10);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigation = findViewById(R.id.navigationadmin);
        navigation.setNavigationItemSelectedListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Projects");

        acceptlist = findViewById(R.id.acceptedprojects);
        int resId = R.anim.layout_right_in;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        acceptlist.setLayoutAnimation(animation);

        acceptlist.setHasFixedSize(true);
        acceptlist.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<ModelProjects,AcceptViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<ModelProjects, AcceptViewHolder>(ModelProjects.class,
                        R.layout.project_row,
                        AcceptViewHolder.class,
                        mDatabase) {

                    @Override
                    protected void populateViewHolder(final AcceptViewHolder viewHolder, final ModelProjects model, final int position) {
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.item_from_right);
                        viewHolder.itemView.startAnimation(animation);

                        final ArrayList<String> list = new ArrayList<String>(Arrays.asList(model.getImg0(), model.getImg1(), model.getImg2(),
                                model.getImg3(), model.getImg4(), model.getImg5(), model.getImg6(), model.getImg7(), model.getImg8(), model.getImg9()
                        ));
                        if (model.getStatus().equals("accepted")) {
                            viewHolder.setName(model.getName());
                            viewHolder.setCapacity(model.getCapacityTR());
                            viewHolder.setRating(model.getRating());
                        }else {
                            viewHolder.Layout_hide();

                        }
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(AdminProject.this,ProjectDetails.class);
                                intent.putExtra("Name",model.getName());
                                intent.putExtra("Address",model.getAddress());
                                intent.putExtra("Area",model.getArea());
                                intent.putExtra("Latti",model.getLattitude());
                                intent.putExtra("Longi",model.getLongitude());
                                intent.putExtra("Rating",model.getRating());
                                intent.putExtra("HVAC",model.getHVACfields());
                                intent.putExtra("City",model.getCity());
                                intent.putExtra("State",model.getState());
                                intent.putExtra("pushid",model.getPushId());
                                intent.putExtra("Userid",model.getUserid());
                                intent.putExtra("Capacity",model.getCapacityTR());

                                List s1 = new ArrayList();
                                s1.add(null);
                                list.removeAll(s1);
                                intent.putExtra("urls",list);

                                startActivity(intent);
                            }
                        });
                    }
                };
        acceptlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class AcceptViewHolder extends RecyclerView.ViewHolder{
        View mView;
        final LinearLayout.LayoutParams params;
        private final LinearLayout layoutmain;


        public AcceptViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            layoutmain = itemView.findViewById(R.id.llmain);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        }
        private void Layout_hide(){
            params.height = 0;
            layoutmain.setLayoutParams(params);
        }
        public void setName(String name){
            TextView proj_name = mView.findViewById(R.id.proj_title);
            proj_name.setText(name);
        }
        public void setCapacity(String capacity){
            TextView capacitytv = mView.findViewById(R.id.capacitytv);
            capacitytv.setText(capacity + " TR");
        }
        public void setRating(String rate){
            TextView rating = mView.findViewById(R.id.rated);
            rating.setText(rate);
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
            startActivity(new Intent(AdminProject.this,AdminHome.class));
        } else if (id == R.id.compliants){
            startActivity(new Intent(AdminProject.this, Complaints.class));
        } else if (id == R.id.installations) {
            startActivity(new Intent(AdminProject.this, Installation.class));
        } else if (id == R.id.services) {
            startActivity(new Intent(AdminProject.this, Services.class));
        } else if (id == R.id.pendingproj){
            startActivity(new Intent(AdminProject.this,PendingProjects.class));
        } else if (id == R.id.projects){
            startActivity(new Intent(AdminProject.this,AdminProject.class));
        }else if (id == R.id.logout) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            startActivity(new Intent(AdminProject.this, SignIn.class));
            finish();
        }
        return true;
    }
}
