package example.com.hvacawards;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyProjects extends AppCompatActivity {

    DatabaseReference mDatabase;
    RecyclerView projectsrv;
    String uid;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_projects);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Projects");

        projectsrv = findViewById(R.id.myprojectsrv);
        int resId = R.anim.layout_right_in;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        projectsrv.setLayoutAnimation(animation);

        projectsrv.setHasFixedSize(true);
        projectsrv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ModelProjects,MyprojectsVoewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<ModelProjects, MyprojectsVoewHolder>(
                        ModelProjects.class,
                        R.layout.project_row,
                        MyprojectsVoewHolder.class,
                        mDatabase
                ) {
            @Override
            protected void populateViewHolder(final MyprojectsVoewHolder viewHolder,final ModelProjects model,final int position) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.item_from_right);
                viewHolder.itemView.startAnimation(animation);

                if (model.getUserid().equals(uid)){
                    viewHolder.setName(model.getName());
                    viewHolder.setCapacity(model.getCapacityTR());
                    viewHolder.setRate(model.getRating());
                    viewHolder.setStatus(model.getStatus());
                }else {
                    viewHolder.Layout_hide();
                }
                final ArrayList<String> list = new ArrayList<String>(Arrays.asList(model.getImg0(),model.getImg1(),model.getImg2(),
                        model.getImg3(),model.getImg4(),model.getImg5(),model.getImg6(),model.getImg7(),model.getImg8(),model.getImg9()
                ));

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MyProjects.this,ProjectDetails.class);
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
        projectsrv.setAdapter(firebaseRecyclerAdapter);
    }

    public static class MyprojectsVoewHolder extends RecyclerView.ViewHolder {
        View mView;
        final LinearLayout.LayoutParams params;
        private final LinearLayout layoutmain;
        private final LinearLayout layout;

        public MyprojectsVoewHolder(View itemView) {
            super(itemView);

            layoutmain = itemView.findViewById(R.id.llmain);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

             mView = itemView;

            layout = mView.findViewById(R.id.layoutstatus);
            layout.setVisibility(View.VISIBLE);
        }
        private void Layout_hide(){
            params.height = 0;
            layoutmain.setLayoutParams(params);
        }

        public void setName(String proname){
            TextView projname = mView.findViewById(R.id.proj_title);
            projname.setText(proname);
        }
        public void setCapacity(String procap){
            TextView projcap = mView.findViewById(R.id.capacitytv);
            projcap.setText(procap);
        }
        public void setRate(String rateit){
            TextView projrate = mView.findViewById(R.id.rated);
            projrate.setText(rateit);
        }
        public void setStatus(String status){
            TextView state = mView.findViewById(R.id.curstatus);
            if (status.equals("accepted")){
                state.setTextColor(Color.parseColor("#32cd32"));
            }else{
                state.setTextColor(Color.parseColor("#ff0000"));
            }
            state.setText(status);
        }
    }
}
