package example.com.hvacawards;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Services extends AppCompatActivity {

    RecyclerView servicesList;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Services");

        servicesList = findViewById(R.id.servicesrv);
        int resId = R.anim.layout_right_in;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        servicesList.setLayoutAnimation(animation);

        servicesList.setHasFixedSize(true);
        servicesList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Compmodel,ServiceViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<Compmodel, ServiceViewHolder>(
                        Compmodel.class,
                        R.layout.activity_services_row,
                        ServiceViewHolder.class,
                        mDatabase
                ) {
            @Override
            protected void populateViewHolder(ServiceViewHolder viewHolder, final Compmodel model, int position) {

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.item_from_right);
                viewHolder.itemView.startAnimation(animation);

                if (!model.getService().equals("")){
                    viewHolder.setMobile(model.getMobileNo());
                    viewHolder.setName(model.getFullname());
                    viewHolder.setService(model.getService());
                    viewHolder.setEmail(model.getEmailID());
                }else {
                    viewHolder.Layout_hide();
                }


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Services.this,compliantDetail.class).putExtra("Name",model.getFullname())
                                .putExtra("Mobile",model.getMobileNo()).putExtra("email",model.getEmailID())
                                .putExtra("complliant",model.getService()).putExtra("isComp","0")
                        );
                    }
                });
            }
        };
        servicesList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        View mView;
        final LinearLayout.LayoutParams params;
        private final LinearLayout linearLayout;

        public ServiceViewHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.llhelpserv);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            mView = itemView;
        }

        private void Layout_hide(){
            params.height = 0;
            linearLayout.setLayoutParams(params);
        }
        public void setName(String fulname){
            TextView fullname = mView.findViewById(R.id.servcustname);
            fullname.setText(fulname);
        }

        public void setMobile(String mobl){
            TextView mobile = mView.findViewById(R.id.servcustmobile);
            mobile.setText(mobl);
        }
        public void setEmail(String emal){
            TextView email = mView.findViewById(R.id.servcustemail);
            email.setText(emal);
        }
        public void setService(String serv){
            TextView service = mView.findViewById(R.id.servcustinstall);
            service.setText(serv);
        }
    }
}
