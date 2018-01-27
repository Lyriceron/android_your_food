package com.aa.aa.yg;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aa.aa.yg.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUpActivity extends AppCompatActivity {

    MaterialEditText edtPhone, edtName, edtPassword;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);
        edtName = (MaterialEditText) findViewById(R.id.customerName);
        edtPassword = (MaterialEditText) findViewById(R.id.passWord);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        //init firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmptyEditText()) {
                    Toast.makeText(SignUpActivity.this, "You had one or more empty field", Toast.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog mDialog = new ProgressDialog(SignUpActivity.this);
                    mDialog.setMessage("Please waiting...");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                               //Check it already have in data
                            if(dataSnapshot.child(edtPhone.getText().toString()).exists()){
                                mDialog.dismiss();
                                Toast.makeText(SignUpActivity.this, "Phone Number already register", Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog.dismiss();
                                User user = new User(edtName.getText().toString(), edtPassword.getText().toString());
                                table_user.child(edtPhone.getText().toString()).setValue(user);
                                Toast.makeText(SignUpActivity.this, "Register Successfully !", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }

    private boolean isEmptyEditText(){
        if(edtPhone.getText().toString().isEmpty()
                || edtPassword.getText().toString().isEmpty()
                || edtName.getText().toString().isEmpty()) return true;
        return false;
    }
}
