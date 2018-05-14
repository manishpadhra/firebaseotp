package com.ztobit.firebaseotp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GetPhoneNumber extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_phone_number);
        TextView tv = (TextView)findViewById(R.id.tv);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.getPhoneNumber();

        String phone = (FirebaseAuth.getInstance().getCurrentUser()).toString();
        String ph = user.getPhoneNumber().toString();
        tv.setText(ph);

    }
}
