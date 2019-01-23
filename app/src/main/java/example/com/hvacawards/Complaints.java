package example.com.hvacawards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Complaints extends AppCompatActivity {

    public DatabaseReference mDatabase;
    RecyclerView complaintList;
    CardView detailed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Compliants");

        detailed = findViewById(R.id.detailsCard);
        complaintList = findViewById(R.id.complist);
        int resId = R.anim.layout_right_in;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        complaintList.setLayoutAnimation(animation);

        complaintList.setHasFixedSize(true);
        complaintList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Compmodel,CompliantViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<Compmodel, CompliantViewHolder>(
                        Compmodel.class,
                        R.layout.activity_admin_row,
                        CompliantViewHolder.class,
                        mDatabase
                ) {
            @Override
            protected void populateViewHolder(CompliantViewHolder viewHolder, final Compmodel model, int position) {

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.item_from_right);
                viewHolder.itemView.startAnimation(animation);

                if (!model.getCompliant().equals("")) {
                    viewHolder.setComplaint(model.getCompliant());
                    viewHolder.setFulname(model.getFullname());
                    viewHolder.setMobile(model.getMobileNo());
                    viewHolder.setEmail(model.getEmailID());
                }else{
                    viewHolder.Layout_hide();
                }

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Complaints.this,compliantDetail.class).putExtra("Name",model.getFullname())
                                .putExtra("Mobile",model.getMobileNo()).putExtra("email",model.getEmailID())
                                .putExtra("complliant",model.getCompliant()).putExtra("details",model.getCompDetails())
                                .putExtra("isComp","1")
                        );
                    }
                });

            }
        };
        complaintList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class CompliantViewHolder extends RecyclerView.ViewHolder {
        View mView;
        final LinearLayout.LayoutParams params;
        private final LinearLayout linearLayout;

        public CompliantViewHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.llhelp);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            mView = itemView;
        }


        private void Layout_hide(){
            params.height = 0;
            linearLayout.setLayoutParams(params);
        }
        public void setFulname(String fullname){
            TextView name = mView.findViewById(R.id.custname);
            name.setText(fullname);
        }
        public void setComplaint(String comp){
           TextView compl = mView.findViewById(R.id.custcompliant);
           compl.setText(comp);
        }
        public void setMobile(String mobile){
            TextView mob = mView.findViewById(R.id.custmobile);
            mob.setText(mobile);
        }
        public void setEmail(String email){
            TextView mail = mView.findViewById(R.id.custemail);
            mail.setText(email);
        }
    }
}
