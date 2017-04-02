package katheria.vhp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import katheria.vhp.Http.HttpCall;
import katheria.vhp.R;
import katheria.vhp.ShowProgressDialog;

public class NewUserActivity extends AppCompatActivity {
    private EditText txtEmailAddress;
    private EditText txtPassword;
    private TextView txtshow;
    private FirebaseAuth firebaseAuth;
    private Button buttonRegister;
    private String Email;
    Context context = NewUserActivity.this;
    ShowProgressDialog showProgressDialog;
    private void defineDialog(Context context) {
        showProgressDialog = new ShowProgressDialog(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        defineDialog(NewUserActivity.this);
        txtEmailAddress = (EditText) findViewById(R.id.email);
        txtPassword = (EditText) findViewById(R.id.password);
        txtshow = (TextView) findViewById(R.id.show);
        buttonRegister = (Button) findViewById(R.id.register);
        firebaseAuth = FirebaseAuth.getInstance();
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog.show();
                //final ProgressDialog progressDialog = ProgressDialog.show(NewUserActivity.this, "Please wait...", "Processing...", true);
                (firebaseAuth.createUserWithEmailAndPassword(txtEmailAddress.getText().toString(), txtPassword.getText().toString()))
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                showProgressDialog.dismiss();

                                if (task.isSuccessful()) {
                                    Toast.makeText(NewUserActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                                    Email= txtEmailAddress.getText().toString();
                                    Bundle bundlenew = new Bundle();
                                    bundlenew.putString("Email",Email);
                                    Intent i = new Intent(NewUserActivity.this, RegisterActivity.class).putExtras(bundlenew);
                                    firebaseAuth.getCurrentUser().sendEmailVerification();
                                    startActivity(i);
                                }
                                else
                                {
                                    Log.e("ERROR", task.getException().toString());
                                    Toast.makeText(NewUserActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        txtEmailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new HttpCall().checkEmail(context, txtEmailAddress.getText().toString(), txtEmailAddress, buttonRegister);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        txtshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtshow.getText().toString().equals("Show")) {
                    txtshow.setText("Hide");
                    txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
                } else if (txtshow.getText().toString().equals("Hide")) {
                    txtshow.setText("Show");
                    txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

}
