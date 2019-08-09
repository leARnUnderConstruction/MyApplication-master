package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DataActivity extends AppCompatActivity {

    private TableLayout mTableLayout;
    private ProgressDialog mProgressBar;
    private View alertLayout;
    private Button addNewButton;
    private List<ReturnData> returnDataList = new ArrayList<>();
    public Button dialogBoxButton;
    EditText dialogEmail;

    AlertDialog.Builder builder;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        LayoutInflater inflater = getLayoutInflater();
        alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
        dialogEmail = alertLayout.findViewById(R.id.dialog_email);
        addNewButton = (Button)findViewById(R.id.addnew);
        dialogBoxButton = (Button)alertLayout.findViewById(R.id.dialog_submit);
        mProgressBar = new ProgressDialog(this);
        // setup the table
        mTableLayout = (TableLayout) findViewById(R.id.table_response);
        mTableLayout.setStretchAllColumns(true);
        HttpsTrustManager.allowAllSSL();


        mProgressBar.setCancelable(true);
        mProgressBar.setMessage("Fetching Data..");
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressBar.show();
        getData("https://devfrontend.gscmaven.com/wmsweb/webapi/email/");
        builder = new AlertDialog.Builder(this);




        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(addNewButton.getContext());

                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                final AlertDialog dialog = alert.create();
                dialog.show();

                dialogBoxButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = dialogEmail.getText().toString().trim();
                        Boolean validate = validateEmail(email);
                        if(validate == Boolean.TRUE) {
                            insertData(email);
                        }
                        dialog.dismiss();
                        Intent i = new Intent(DataActivity.this, DataActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();

                    }
                });

            }
        });
    }





    private void getData(String url) {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray=new JSONArray(response);


                            for (int i=0;i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);

                                Integer idtableEmail=jsonObject.optInt("idtableEmail");
                                String tableEmailEmailAddress=jsonObject.optString("tableEmailEmailAddress");
                                Boolean tableEmailValidate = jsonObject.optBoolean("tableEmailValidate");

                                ReturnData returnData = new ReturnData(idtableEmail, tableEmailEmailAddress, tableEmailValidate);

                                returnDataList.add(returnData);
                            }
                            Log.d("superuniques", ""+returnDataList.size());
                            loadData();
                            mProgressBar.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyError",error.toString());
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestqueue(stringRequest);

    }

    public void updateData(String email, int id) {
        String donor_url = "https://devfrontend.gscmaven.com/wmsweb/webapi/email/"+id;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tableEmailEmailAddress", email);
            jsonObject.put("tableEmailValidate", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, donor_url,  jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                int s1=jsonObject.optInt("idtableEmail");
                String s2=jsonObject.optString("tableEmailEmailAddress");
                Boolean s3 = jsonObject.optBoolean("tableEmailValidate");

                Toast.makeText(getApplicationContext(),""+ s1+"  "+s2+"  "+s3,Toast.LENGTH_SHORT).show();

                Intent i = new Intent(DataActivity.this, DataActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyError",error.toString());
                error.printStackTrace();
            }
        });

        MySingleton.getInstance(getApplicationContext()).addToRequestqueue(jsonObjectRequest);
    }

    public void insertData(String email) {

        String donor_url = "https://devfrontend.gscmaven.com/wmsweb/webapi/email/";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tableEmailEmailAddress", email);
            jsonObject.put("tableEmailValidate", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, donor_url,  jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                int s1=jsonObject.optInt("idtableEmail");
                String s2=jsonObject.optString("tableEmailEmailAddress");
                Boolean s3 = jsonObject.optBoolean("tableEmailValidate");

                Toast.makeText(getApplicationContext(),""+ s1+"  "+s2+"  "+s3,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyError",error.toString());
                error.printStackTrace();
            }
        });

        MySingleton.getInstance(getApplicationContext()).addToRequestqueue(jsonObjectRequest);

    }

    private void deleteData(Integer id) {

        String url= "https://devfrontend.gscmaven.com/wmsweb/webapi/email/"+id;
        StringRequest stringRequest=new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("MarketingError",error.toString());
                error.printStackTrace();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestqueue(stringRequest);
    }




    public Boolean validateEmail(String email) {
        String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +

                "\\@" +

                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +

                "(" +

                "\\." +

                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +

                ")+";
        Matcher matcher= Pattern.compile(validemail).matcher(email);

        if (matcher.matches()){
            Toast.makeText(getApplicationContext(),"Proceeding...",Toast.LENGTH_LONG).show();
            return true;


        }
        else {
            Toast.makeText(getApplicationContext(),"Enter Valid Email-Id",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void loadData() {
        int leftRowMargin=0;
        int topRowMargin=20;
        int rightRowMargin=0;
        int bottomRowMargin = 0;
        int textSize = 0, smallTextSize =0;

        textSize = (int) getResources().getDimension(R.dimen.font_size_verysmall);
        smallTextSize = (int) getResources().getDimension(R.dimen.font_size_small);



        int rows = returnDataList.size();
        getSupportActionBar().setTitle("Response Data (" + String.valueOf(rows) + ")");
        TextView textSpacer = null;

        mTableLayout.removeAllViews();

        // -1 means heading row
        for(int i = -1; i < returnDataList.size(); i ++) {
            ReturnData row = null;
            if (i > -1)
                row = returnDataList.get(i);

            // data columns
            final TextView serialNo = new TextView(this);
            serialNo.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            serialNo.setGravity(Gravity.CENTER);
            serialNo.setPadding(5, 15, 0, 15);
            if (i == -1) {
                serialNo.setText("SERIAL NO.");
                serialNo.setBackgroundColor(Color.parseColor("#000000"));
                serialNo.setTextColor(Color.parseColor("#f0f0f0"));
                serialNo.setGravity(Gravity.CENTER);

                serialNo.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            } else {
                serialNo.setBackgroundColor(Color.parseColor("#f8f8f8"));
                serialNo.setText(String.valueOf(row.getIdtableEmail()));
                serialNo.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                serialNo.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                serialNo.setGravity(Gravity.CENTER);


            }

            final TextView email = new TextView(this);
            email.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            email.setGravity(Gravity.CENTER);
            email.setPadding(5, 15, 0, 15);
            if (i == -1) {
                email.setText("EMAIL");
                email.setBackgroundColor(Color.parseColor("#f8f8f8"));
                email.setTextColor(Color.parseColor("#000000"));

                email.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            } else {
                email.setBackgroundColor(Color.parseColor("#ffffff"));
                email.setText(row.getTableEmailEmailAddress());
                email.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                email.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            final LinearLayout actionLayout = new LinearLayout(this);
            actionLayout.setOrientation(LinearLayout.VERTICAL);
            actionLayout.setBackgroundColor(Color.parseColor("#f8f8f8"));

            final TextView edit = new TextView(this);

            //final Button edit = new Button(this);
            edit.setPadding(0, 15, 0, 15);

            if (i == -1) {
                edit.setText("ACTION");
                edit.setPadding(0, 15, 0, 15);
                edit.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                edit.setBackgroundColor(Color.parseColor("#000000"));
                edit.setTextColor(Color.parseColor("#f0f0f0"));
                edit.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                edit.setGravity(Gravity.CENTER);
            } else {
                edit.setText("EDIT");
                edit.setPadding(0, 15, 0, 15);
                edit.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                edit.setTextColor(Color.parseColor("#000000"));
                edit.setBackgroundColor(Color.parseColor("#C0C0C0"));

                edit.setGravity(Gravity.CENTER);

                final int j = i;
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(addNewButton.getContext());

                        // this is set the view from XML inside AlertDialog
                        alert.setView(alertLayout);
                        // disallow cancel of AlertDialog on click of back button and outside touch
                        alert.setCancelable(false);
//
                        final AlertDialog dialog = alert.create();
                        dialog.show();

                        dialogBoxButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String email = dialogEmail.getText().toString().trim();
                                Boolean validate = validateEmail(email);
                                if(validate == Boolean.TRUE) {
                                    updateData(email, returnDataList.get(j).getIdtableEmail());
                                }
                                dialog.dismiss();
                                Intent i = new Intent(DataActivity.this, DataActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();

                            }
                        });

                    }
                });
            }
            actionLayout.addView(edit);


            if (i > -1) {
                final TextView delete = new TextView(this);
                final int j = i;
                delete.setText("DELETE");
                delete.setPadding(0, 15, 0, 15);
                delete.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                delete.setTextColor(Color.parseColor("#000000"));
                delete.setBackgroundColor(Color.parseColor("#808080"));

                delete.setGravity(Gravity.CENTER);


                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        builder.setMessage("Do you want to delete data ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        int id1=returnDataList.get(j).getIdtableEmail();
                            deleteData(id1);

                        Intent i = new Intent(DataActivity.this, DataActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();


                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.dismiss();
                                    }
                                });
                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("Confirm");
                        alert.show();



                    }
                });
                actionLayout.addView(delete);
            }
            // add table row
            final TableRow tr = new TableRow(this);
            tr.setId(i + 1);
            TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
            tr.setPadding(0,0,0,0);
            tr.setLayoutParams(trParams);



            tr.addView(serialNo);
            tr.addView(email);
            tr.addView(actionLayout);
            mTableLayout.addView(tr, trParams);

            if (i > -1) {

                // add separator row
                final TableRow trSep = new TableRow(this);
                TableLayout.LayoutParams trParamsSep = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
                trParamsSep.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);

                trSep.setLayoutParams(trParamsSep);
                TextView tvSep = new TextView(this);
                TableRow.LayoutParams tvSepLay = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                tvSepLay.span = 3;
                tvSep.setLayoutParams(tvSepLay);
                tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
                tvSep.setHeight(1);

                trSep.addView(tvSep);
                mTableLayout.addView(trSep, trParamsSep);
            }
        }
    }
}

