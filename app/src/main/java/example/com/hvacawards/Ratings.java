package example.com.hvacawards;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ratings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    NavigationView navigation;

    private DatabaseReference mDatabase;
    RecyclerView projlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        drawerLayout = findViewById(R.id.drawerLayout3);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigation = findViewById(R.id.naviagtion);
        navigation.setNavigationItemSelectedListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Projects");

        projlist = findViewById(R.id.allprojects);
        int resId = R.anim.layout_right_in;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        projlist.setLayoutAnimation(animation);

        projlist.setHasFixedSize(true);
        projlist.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ModelProjects,ProjViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<ModelProjects, ProjViewHolder>(ModelProjects.class,
                        R.layout.project_row,
                        ProjViewHolder.class,
                        mDatabase) {

                    @Override
                    protected void populateViewHolder(final ProjViewHolder viewHolder, final ModelProjects model, int position) {
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.item_from_right);
                        viewHolder.itemView.startAnimation(animation);

                        final ArrayList<String> list = new ArrayList<String>(Arrays.asList(model.getImg0(),model.getImg1(),model.getImg2(),
                                model.getImg3(),model.getImg4(),model.getImg5(),model.getImg6(),model.getImg7(),model.getImg8(),model.getImg9()
                        ));

                        if (model.getStatus().equals("accepted")) {
                            viewHolder.setName(model.getName());
                            viewHolder.setCapacity(model.getCapacityTR());
                            viewHolder.setRating(model.getRating());
                        }else{
                            viewHolder.Layout_hide();
                        }
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Ratings.this,GiveRatings.class);
                                intent.putExtra("Name",model.getName());
                                intent.putExtra("Address",model.getAddress());
                                intent.putExtra("Area",model.getArea());
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
        projlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ProjViewHolder extends RecyclerView.ViewHolder{
        View mView;
        final LinearLayout.LayoutParams params;
        private final LinearLayout layoutmain;

        public ProjViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            layoutmain = itemView.findViewById(R.id.llmain);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        private void Layout_hide(){
            params.height = 0;
            layoutmain.setLayoutParams(params);
        }public void setName(String name){
            TextView proj_name = mView.findViewById(R.id.proj_title);
            proj_name.setText(name);
        }
        public void setCapacity(String capacity){
            TextView capacitytv = mView.findViewById(R.id.capacitytv);
            capacitytv.setText(capacity);
        }
        public void setRating(String rate){
            TextView rateit = mView.findViewById(R.id.rated);
            rateit.setText(rate);
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
        } else if(id == R.id.profile){
            startActivity(new Intent(Ratings.this,MyProfile.class));
        }else if (id == R.id.navprojects) {
            startActivity(new Intent(Ratings.this, UploadImages.class));
        } else if (id == R.id.navbank) {
            startActivity(new Intent(Ratings.this, BankActivity.class));
        } else if (id == R.id.navratings) {
            startActivity(new Intent(Ratings.this, Ratings.class));
        }
        else if (id == R.id.logout) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            startActivity(new Intent(Ratings.this, SignIn.class));
            finish();
        }
        return true;
    }
}
