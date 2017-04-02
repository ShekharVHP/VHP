package katheria.vhp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import katheria.vhp.Http.HttpCall;
import katheria.vhp.Model.Model_userDetails;
import katheria.vhp.R;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener ,OnClickListener{

    private static int RC_SIGN_IN = 0 ;
    private static String TAG = "LOGIN_ACTIVITY";
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    Context context = LoginActivity.this;
    public FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEmailField,mPasswordField;
    TextView register;
    private String Email ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        register = (TextView) findViewById(R.id.register);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    if (mAuth.getInstance().getCurrentUser().isEmailVerified())
                    {
                    Email = user.getEmail();
                    new HttpCall().checkGoogleEmail(context, Email);
                }


                else
                {
                    Toast.makeText(LoginActivity.this, "Email\n" + user.getEmail()+" not verified.\nPlease verify it first then login", Toast.LENGTH_LONG).show();
                    mAuth.getCurrentUser().sendEmailVerification();
                    FirebaseAuth.getInstance().signOut();
                }
            }

                else

                    Log.d("AUTH","User logged out");
            }
        };


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        mEmailField = (EditText) findViewById(R.id.email);
        mPasswordField = (EditText) findViewById(R.id.password);
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,NewUserActivity.class));
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener !=null)
            mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess())
            {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            }
            else
                Log.d(TAG,"Google login failed ");

        }
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("AUTH","sign in with credentials: complete "+ task.isSuccessful());
            }
        });
    }



    private void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

      private void emailSignIn()

    {
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(email)||  TextUtils.isEmpty(password))
        {
            Toast.makeText(LoginActivity.this,"Fields are empty ",Toast.LENGTH_SHORT).show();
        }
        else
        {

            final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait...", "Proccessing...", true);

            (mAuth.signInWithEmailAndPassword(email,password))
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if (task.isSuccessful()) {
                               /* if (mAuth.getCurrentUser().isEmailVerified())
                                {

                                    Email = mAuth.getCurrentUser().getEmail();
                                   // new HttpCall().checkGoogleEmail(context, Email);
                                    }

                                else
                                {
                                    Toast.makeText(LoginActivity.this, "Email not verified", Toast.LENGTH_SHORT).show();
                                    mAuth.getCurrentUser().sendEmailVerification();
                                    FirebaseAuth.getInstance().signOut();
                                }*/}

                             else {
                                Log.e("ERROR", task.getException().toString());
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }

                            }


                    });}

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG," Connection Failed");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.sign_in_button:
                signIn();
                break;
            case R.id.email_sign_in_button:
                emailSignIn();
                break;
        }


    }

}

