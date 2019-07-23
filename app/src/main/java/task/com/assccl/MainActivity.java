package task.com.assccl;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static ViewPager viewPager;
    private static TabLayout tabLayout;
    RelativeLayout depositLayout;
    ProgressDialog progressDialog = null;
    private Context ctx;
    String usdToInr;
    static TextView inrText;
    static EditText enteredUSD;
    static TextView totalDollars, totalInr;
    EditText otpFirst, otpSecond, otpThird, otpFourth, otpFive;
    Boolean backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFirstPage();
        ctx = this;
        progressDialog = new ProgressDialog(this);

    }

    private void loadFirstPage(){

        setContentView(R.layout.activity_main);
        depositLayout = (RelativeLayout) findViewById(R.id.depositLayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("All");
        tabLayout.getTabAt(1).setText("Deposit");
        tabLayout.getTabAt(2).setText("Withdraw");

        depositLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDepositMoney();
            }
        });
        backPressed = true;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DepositFragment());
        adapter.addFrag(new DepositFragment());
        adapter.addFrag(new DepositFragment());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
            //mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            // return null to display only the icon
            return null;
        }
    }

    private void openDepositMoney(){
        setContentView(R.layout.deposit_money_layout);
        enteredUSD = (EditText) findViewById(R.id.amount);
        totalDollars = (TextView) findViewById(R.id.totalDollars);
        totalInr = (TextView) findViewById(R.id.totalInr);
        inrText = (TextView) findViewById(R.id.inrText);
        ImageView close = (ImageView) findViewById(R.id.close);
        TextView confirm = (TextView) findViewById(R.id.proceed);
        backPressed = false;
        progressDialog.setContentView(R.layout.progress);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();
        retrofit2.Call<CurrencyModel.CurrencyModelResponse> call = new Retrofit.Builder().baseUrl("https://api.exchangeratesapi.io")
                .addConverterFactory(GsonConverterFactory.create()).build().create(ApiInterface.class).getInr("USD");
        call.enqueue(new Callback<CurrencyModel.CurrencyModelResponse>() {
            @Override
            public void onResponse(Call<CurrencyModel.CurrencyModelResponse> call, Response<CurrencyModel.CurrencyModelResponse> response) {
                usdToInr = response.body().getRates().getINR();
                progressDialog.dismiss();
                inrText.setText(" "+ usdToInr.substring(0,2) + " " + "INR");
            }

            @Override
            public void onFailure(Call<CurrencyModel.CurrencyModelResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enteredUSD.length() !=0) {
                    openConfirmDeposit();
                } else {
                    Toast.makeText(ctx, "Deposit amount need to be entered", Toast.LENGTH_LONG).show();
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFirstPage();
            }
        });

        enteredUSD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(enteredUSD.length() !=0){
                    totalDollars.setText(" "+ enteredUSD.getText().toString());
                    totalInr.setText(" "+ Integer.toString(Integer.parseInt(usdToInr.substring(0,2)) * Integer.parseInt(enteredUSD.getText().toString())));
                } else {
                    totalDollars.setText("");
                    totalInr.setText("");
                }
            }
        });
    }

    private void openConfirmDeposit(){
        setContentView(R.layout.confirm_deposit);
        TextView inrAmount = (TextView) findViewById(R.id.inrAmount);
        TextView usdAmount = (TextView) findViewById(R.id.usdAmount);
        TextView usdToInr = (TextView) findViewById(R.id.totalInr);
        inrAmount.setText(totalInr.getText().toString());
        usdAmount.setText(totalDollars.getText().toString());
        usdToInr.setText(inrText.getText().toString());
        TextView confirm = (TextView) findViewById(R.id.confirm);
        backPressed = false;
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDepositMoney();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestOTP();

                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.otp_dialog);
                TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
                TextView submit = (TextView) dialog.findViewById(R.id.submit);
                TextView resendOtp = (TextView) dialog.findViewById(R.id.resendOtp);
                dialog.show();

                resendOtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestOTP();
                    }
                });

                otpFirst=(EditText)dialog.findViewById(R.id.one);
                otpSecond=(EditText)dialog.findViewById(R.id.two);
                otpThird=(EditText)dialog.findViewById(R.id.three);
                otpFourth=(EditText)dialog.findViewById(R.id.four);
                otpFive=(EditText)dialog.findViewById(R.id.five);

                otpFirst.addTextChangedListener(new TextWatcher() {
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (otpFirst.getText().toString().length() == 1) {
                            otpSecond.setEnabled(true);
                            otpSecond.requestFocus();
                        }
                    }
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }
                    public void afterTextChanged(Editable s) {
                        if (otpFirst.getText().toString().length() == 0) {
                            otpFirst.requestFocus();
                        }
                    }
                });
                otpSecond.addTextChangedListener(new TextWatcher() {
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (otpSecond.getText().toString().length() == 1) {
                            otpThird.setEnabled(true);
                            otpFirst.setEnabled(false);
                            otpThird.requestFocus();
                        }
                    }
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                        if(otpSecond.getText().toString().length() == 0){
                            otpSecond.setEnabled(true);
                            otpSecond.requestFocus();
                        }
                    }
                    public void afterTextChanged(Editable s) {
                        if (otpSecond.getText().toString().length() == 0) {
                            otpFirst.setEnabled(true);
                            otpFirst.requestFocus();
                            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                                    .showSoftInput(otpFirst, InputMethodManager.SHOW_FORCED);
                        }
                    }
                });
                otpThird.addTextChangedListener(new TextWatcher() {
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (otpThird.getText().toString().length() == 1) {
                            otpFourth.setEnabled(true);
                            otpFirst.setEnabled(false);
                            otpSecond.setEnabled(false);
                            otpFourth.requestFocus();
                        }
                    }
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                        if(otpThird.getText().toString().length() == 0){
                            otpThird.setEnabled(true);
                            otpThird.requestFocus();
                        }
                    }
                    public void afterTextChanged(Editable s) {
                        if (otpThird.getText().toString().length() == 0) {
                            otpSecond.setEnabled(true);
                            otpSecond.requestFocus();
                            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                                    .showSoftInput(otpSecond, InputMethodManager.SHOW_FORCED);
                        }
                    }
                });
                otpFourth.addTextChangedListener(new TextWatcher() {
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (otpFourth.getText().toString().length() == 1) {
                            otpFive.setEnabled(true);
                            otpFirst.setEnabled(false);
                            otpSecond.setEnabled(false);
                            otpThird.setEnabled(false);
                            otpFive.requestFocus();
                        }
                    }
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                        if(otpFourth.getText().toString().length() == 0){
                            otpFourth.setEnabled(true);
                            otpFourth.requestFocus();
                        }
                    }
                    public void afterTextChanged(Editable s) {
                        if (otpFourth.getText().toString().length() == 0)     //size as per your requirement
                        {
                            otpThird.setEnabled(true);
                            otpThird.requestFocus();
                            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                                    .showSoftInput(otpThird, InputMethodManager.SHOW_FORCED);
                        }
                    }
                });
                otpFive.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        otpFirst.setEnabled(false);
                        otpSecond.setEnabled(false);
                        otpThird.setEnabled(false);
                        otpFourth.setEnabled(false);
                        otpFive.setSelection(otpFive.getText().length());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        if(otpFive.getText().toString().length() == 0){
                            otpFive.setEnabled(true);
                            otpFive.requestFocus();
                        }

                    }


                    @Override
                    public void afterTextChanged(Editable s) {
                        if (otpFive.getText().toString().length() == 0)     //size as per your requirement
                        {
                            otpFourth.setEnabled(true);
                            otpFourth.requestFocus();
                            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                                    .showSoftInput(otpFourth, InputMethodManager.SHOW_FORCED);
                        }
                    }
                });


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //if(otpFirst.length() == otpSecond.length() == otpThird.length() == otpFourth.length() == otpFive.length() == null)
                        if(otpFirst.getText().toString().equals("5") && otpSecond.getText().toString().equals("4") && otpThird.getText().toString().equals("3")
                                && otpFourth.getText().toString().equals("2") && otpFive.getText().toString().equals("1")) {
                            dialog.dismiss();
                            openDepositSuccess();
                        } else {
                            Toast.makeText(MainActivity.this, "Enter valid OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    private void requestOTP(){
        retrofit2.Call<String> call = new Retrofit.Builder().baseUrl("https://mgage.solutions")
                .addConverterFactory(GsonConverterFactory.create()).build().create(ApiInterface.class).getOTP();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(MainActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDepositSuccess(){
        setContentView(R.layout.deposit_success);
        TextView depositedAmount = (TextView) findViewById(R.id.depositedAmount);
        TextView usdAmount = (TextView) findViewById(R.id.usdAmount);
        TextView usdToInr = (TextView) findViewById(R.id.totalInr);
        depositedAmount.setText(totalInr.getText().toString());
        usdAmount.setText(totalDollars.getText().toString());
        usdToInr.setText(inrText.getText().toString());
        ImageView close = (ImageView) findViewById(R.id.close);
        backPressed = false;
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFirstPage();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(backPressed) {
            super.onBackPressed();
        }
    }
}
