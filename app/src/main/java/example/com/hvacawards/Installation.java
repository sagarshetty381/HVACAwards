package example.com.hvacawards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Installation extends AppCompatActivity {

    public DatabaseReference mDatabase;
    RecyclerView instalLList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Installations");

        instalLList = findViewById(R.id.installList);
        int resId = R.anim.layout_right_in;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        instalLList.setLayoutAnimation(animation);

        instalLList.setHasFixedSize(true);
        instalLList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Compmodel,InstallViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<Compmodel, InstallViewHolder>(
                        Compmodel.class,
                        R.layout.activity_install_row,
                        InstallViewHolder.class,
                        mDatabase
                ) {
            @Override
            protected void populateViewHolder(InstallViewHolder viewHolder, final Compmodel model, int position) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.item_from_right);
                viewHolder.itemView.startAnimation(animation);

                if (!model.getInstallation().equals("")) {
                    viewHolder.setInstall(model.getInstallation());
                    viewHolder.setFulname(model.getFullname());
                    viewHolder.setMobile(model.getMobileNo());
                    viewHolder.setEmail(model.getEmailID());
                }else {
                    viewHolder.Layout_hide();
                }

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Installation.this,compliantDetail.class).putExtra("Name",model.getFullname())
                                .putExtra("Mobile",model.getMobileNo()).putExtra("email",model.getEmailID())
                                .putExtra("complliant",model.getInstallation()).putExtra("isComp","0")
                        );
                    }
                });
            }
        };
        instalLList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class InstallViewHolder extends RecyclerView.ViewHolder {
        View mView;
        final LinearLayout.LayoutParams layoutParams;
        private final LinearLayout linearLayout;

        public InstallViewHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.llhelpin);
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            mView = itemView;
        }

        private void Layout_hide(){
            layoutParams.height = 0;
            linearLayout.setLayoutParams(layoutParams);
        }

        public void setFulname(String fullname){
            TextView name = mView.findViewById(R.id.incustname);
            name.setText(fullname);
        }
        public void setInstall(String install){
            TextView ins = mView.findViewById(R.id.incustinstall);
            ins.setText(install);
        }
        public void setMobile(String mobile){
            TextView mob = mView.findViewById(R.id.incustmobile);
            mob.setText(mobile);
        }
        public void setEmail(String email) {
            TextView mail = mView.findViewById(R.id.incustemail);
            mail.setText(email);
        }
    }
}
