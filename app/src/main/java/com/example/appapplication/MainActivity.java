package com.example.appapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.appapplication.Retrofit.IMyService;
import com.example.appapplication.Retrofit.RetrofitClient;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    TextView txt_create_account;
    MaterialEditText edt_login_email, edt_login_password;
    Button btn_login;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        //Init View
        edt_login_email = (MaterialEditText) findViewById(R.id.edt_email);
        edt_login_password = (MaterialEditText) findViewById(R.id.edt_password);


        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(edt_login_email.getText().toString(),
                        edt_login_password.getText().toString());
            }
        });

        txt_create_account = (TextView) findViewById(R.id.txt_create_account);
        txt_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, registerActivity.class));
//                final View regitser_layout = LayoutInflater.from(MainActivity.this)
//                        .inflate(R.layout.register_layout, null);
//                new MaterialStyledDialog.Builder(MainActivity.this)
//                        .setIcon(R.drawable.ic_user)
//                        .setTitle("REGISTRATION")
//                        .setDescription("Please fill all fields")
//                        .setCustomView(regitser_layout)
//                        .setNegativeText("CANCEL")
//                        .onNegative(new MaterialDialog.SingleButtonCallback() {
//                            @Override
//                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .setPositiveText("REGISTER")
//                        .onPositive(new MaterialDialog.SingleButtonCallback() {
//                            @Override
//                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                MaterialEditText edt_register_email = (MaterialEditText) regitser_layout.findViewById(R.id.edt_email);
//                                MaterialEditText edt_register_fname = (MaterialEditText) regitser_layout.findViewById(R.id.edt_fname);
//                                MaterialEditText edt_register_password = (MaterialEditText) regitser_layout.findViewById(R.id.edt_password);
//
//                                if(TextUtils.isEmpty(edt_register_email.getText().toString()))
//                                {
//                                    Toast.makeText(MainActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//
//                                if(TextUtils.isEmpty(edt_register_fname.getText().toString()))
//                                {
//                                    Toast.makeText(MainActivity.this, "Enter First Name", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//
//                                if(TextUtils.isEmpty(edt_register_password.getText().toString()))
//                                {
//                                    Toast.makeText(MainActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//
//                                registerUser(edt_register_email.getText().toString(),
//                                        edt_register_fname.getText().toString(),
//                                        edt_register_fname.getText().toString(),
//                                        edt_register_fname.getText().toString(),
//                                        edt_register_password.getText().toString());
//
//                            }
//                        }).show();
            }
        });

    }

    private void registerUser(String email, String firstname, String lastname, String username, String password) {
        compositeDisposable.add(iMyService.registerUser(email, firstname, lastname, username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(MainActivity.this,""+response,Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void loginUser(String email, String password) {
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        compositeDisposable.add(iMyService.loginUser(email, password)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(String response) throws Exception {
                Toast.makeText(MainActivity.this,""+response,Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
