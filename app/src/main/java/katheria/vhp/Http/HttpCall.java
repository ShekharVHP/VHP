package katheria.vhp.Http;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import katheria.vhp.Activity.AccountActivity;
import katheria.vhp.Activity.LoginActivity;
import katheria.vhp.Activity.RegisterActivity;
import katheria.vhp.Fragment.HomeFragment;
import katheria.vhp.Model.Model_register;
import katheria.vhp.R;
import katheria.vhp.ShowProgressDialog;

import static java.security.AccessController.getContext;


/**
 * Created by shekh on 30-Jan-17.
 */

public class HttpCall extends AppCompatActivity{

    int result;
    ShowProgressDialog showProgressDialog;

    private void defineDialog(Context context) {
        showProgressDialog = new ShowProgressDialog(context);
    }

    public void getDistrictList (final Context context, String state, final Spinner spndistrict) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("state", state);
        defineDialog(context);

        EndPoints.getDistrictList(requestParams,new JsonHttpResponseHandler(){




            @Override
            public void onStart() {
                showProgressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                showProgressDialog.dismiss();

                new DataParser().parseDistrictList(context, response,spndistrict);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Toast.makeText(context, "Please try again later", Toast.LENGTH_SHORT).show();
                showProgressDialog.dismiss();
            }


                }
        );
    }

    public void checkUniquePhone(final Context context, final String phone, final EditText ph, final Button next) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("phone", phone);

        defineDialog(context);

        EndPoints.checkUniquePhone(requestParams, new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                    //    showProgressDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                      //  showProgressDialog.dismiss();

                        result = new DataParser().praseUniqueEmail(context, response);
                        if (result == 0 && !ph.getText().toString().contains(" ")) {
                            ph.setError(null);
                            next.setVisibility(View.VISIBLE);



                        } else if (result == 1) {

                            next.setVisibility(View.GONE);
                            ph.setError("Mobile No Already Registered");

                        } else if (ph.getText().toString().contains(" ")) {
                            next.setVisibility(View.GONE);
                            ph.setError("Phone Number should not contain spaces");

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, JSONObject errorResponse) {
                        showProgressDialog.dismiss();
                    }
                }
        );
    }
    public void checkEmail(final Context context, String email,
                              final EditText Email, final Button next) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("email", email);

        EndPoints.checkEmail(requestParams, new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {



                        result = new DataParser().parseCheckEmail(context, response);
                        if (result == 0 && !Email.getText().toString().contains(" ")) {

                            next.setVisibility(View.VISIBLE);
                            Email.setError(null);
                        } else if (result == 1) {
                            next.setVisibility(View.GONE);
                            Email.setError("Email Already Registered");
                        } else if (Email.getText().toString().contains(" ")) {
                            Email.setError("Email should not contain spaces");
                            next.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    }
                }
        );
    }

    public void checkGoogleEmail(final Context context, final String email) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("email", email);

        defineDialog(context);

        EndPoints.checkEmail(requestParams, new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                            showProgressDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                          showProgressDialog.dismiss();

                        result = new DataParser().parseCheckEmail(context, response);
                        if (result == 1) {
                            Intent intent = new Intent(context, AccountActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("Email", email);
                            context.startActivity(intent);



                        } else  {
                            Intent i = new Intent(context, RegisterActivity.class);
                            i.putExtra("Email", email);
                            context.startActivity(i);

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, JSONObject errorResponse) {
                        showProgressDialog.dismiss();
                    }
                }
        );
    }

    public void register(final Context context, final Model_register model_register) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("email", model_register.email);
        requestParams.put("phone", model_register.phone);
        requestParams.put("name", model_register.name);
        requestParams.put("state", model_register.state);
        requestParams.put("district", model_register.district);
        requestParams.put("block", model_register.block);
        requestParams.put("village", model_register.address);
        requestParams.put("designation", model_register.designation+" "+model_register.designation2);


        defineDialog(context);

        EndPoints.register(requestParams, new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        showProgressDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        showProgressDialog.dismiss();

                        result = new DataParser().parseRegister(context,
                                response);
                        if (result == 1) {

                            Toast.makeText(context,"Successfully registered ,\n Login with your email and password",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);


                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, JSONObject errorResponse) {
                        Log.e("error", errorResponse.toString() + " ");

                        showProgressDialog.dismiss();
                    }

                }
        );

        return;
    }

    public void inputnewuser(final Context context, final Model_register model_register) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("phone", model_register.phone);
        requestParams.put("name", model_register.name);
        requestParams.put("state", model_register.state);
        requestParams.put("district", model_register.district);
        requestParams.put("block", model_register.block);
        requestParams.put("village", model_register.address);
        requestParams.put("designation", model_register.designation+" "+model_register.designation2);


        defineDialog(context);

        EndPoints.register(requestParams, new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        showProgressDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        showProgressDialog.dismiss();

                        result = new DataParser().parseRegister(context,
                                response);
                        if (result == 1) {

                            Toast.makeText(context,"Successfully saved",Toast.LENGTH_LONG).show();
                            HomeFragment fragment = new HomeFragment();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container,fragment);
                            fragmentTransaction.commit();



                            /*Intent intent = new Intent(context, AccountActivity.class);
                            context.startActivity(intent);*/


                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, JSONObject errorResponse) {
                        Log.e("error", errorResponse.toString() + " ");

                        showProgressDialog.dismiss();
                    }

                }
        );

        return;
    }
    public void getUserDetails(final Context context, String email) {
        RequestParams requestParams = new RequestParams();
       // Log.e("ABC2",email);
        requestParams.put("email", email);

        defineDialog(context);


        EndPoints.getUserDetails(requestParams, new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        showProgressDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        showProgressDialog.dismiss();
                        new DataParser().getUserDetails(context, response);
                        Log.e("ABC1","returned");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, JSONObject errorResponse) {

                    }
                }
        );
    }


}
