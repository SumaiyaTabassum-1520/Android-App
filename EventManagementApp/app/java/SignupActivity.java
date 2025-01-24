package com.example.eventmanagementapp;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends Activity {
    private Button toggleButton;
    private TextView tvTitle, tvToggleLevel;
    private TableRow Name, Email,Phone,Repass;
    private EditText etName, etEmail, etPhone, etUserId, etPass, etRPass;
    private CheckBox cbRememberUserId, cbRememberLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        toggleButton = findViewById(R.id.btnToggle);
        tvTitle = findViewById(R.id.tvTitle);
        tvToggleLevel = findViewById(R.id.tvToggleLevel);
        Name = findViewById(R.id.rowName);
        Email = findViewById(R.id.rowEmail);
        Phone=findViewById(R.id.rowPhone);
        Repass=findViewById(R.id.rowRepass);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etUserId = findViewById(R.id.etUID);
        etPass = findViewById(R.id.etpass);
        etRPass = findViewById(R.id.etRepass);
        cbRememberUserId = findViewById(R.id.cbRememberUserId);
        cbRememberLogin = findViewById(R.id.cbRememberLogin);


        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toggleValue = toggleButton.getText().toString();
                //boolean isLogin = toggleValue.equalsIgnoreCase("Login");
                if(toggleValue.equalsIgnoreCase("Login")){
                    toggleButton.setText("Signup");
                    tvTitle.setText("Login");
                    tvToggleLevel.setText("Don't have an account?");
                    Name.setVisibility(View.GONE);
                    Email.setVisibility(View.GONE);
                    Phone.setVisibility(View.GONE);
                    Repass.setVisibility(View.GONE);

                }else {
                    toggleButton.setText("Login");
                    tvTitle.setText("Signup");
                    tvToggleLevel.setText("Already have an account?");
                    Name.setVisibility(View.VISIBLE);
                    Email.setVisibility(View.VISIBLE);
                    Phone.setVisibility(View.VISIBLE);
                    Repass.setVisibility(View.VISIBLE);
                }
            }
        });


        findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.btnGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toggleValue = toggleButton.getText().toString();
                String userId = etUserId.getText().toString().trim();
                String password = etPass.getText().toString().trim();
                boolean isUserIdChecked = cbRememberUserId.isChecked();
                boolean isLoginChecked = cbRememberLogin.isChecked();

                if(toggleValue.equalsIgnoreCase("Login")){

                    String name = etName.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();
                    String phone = etPhone.getText().toString().trim();
                    String rPass = etRPass.getText().toString().trim();

                    String errMsg = "";
                    if(!password.equals(rPass)){
                        errMsg += "Passwords didn't match\n";
                    }
                    if(errMsg.isEmpty()) {

                        SharedPreferences sp = getSharedPreferences("user_account", MODE_PRIVATE);
                        SharedPreferences.Editor spEditor = sp.edit();
                        spEditor.putString("user_id", userId);
                        spEditor.putString("password", password);
                        spEditor.putString("name", name);
                        spEditor.putBoolean("rem_user", isUserIdChecked);
                        spEditor.putBoolean("rem_login", isLoginChecked);
                        spEditor.apply();
                        Intent i = new Intent(SignupActivity.this, activity_create_event.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(SignupActivity.this, errMsg, Toast.LENGTH_LONG).show();
                    }
                } else {
                    SharedPreferences sp = getSharedPreferences("user_account", MODE_PRIVATE);
                    String storedUserId = sp.getString("user_id", "");
                    String storedPassword = sp.getString("password", "");
                    String errMsg = "";
                    if(!userId.equals(storedUserId)){
                        errMsg += "UserId didn't exist\n";
                    }
                    if(!password.equals(storedPassword)){
                        errMsg += "Password didn't match\n";
                    }

                    if(errMsg.isEmpty()) {
                        Intent i = new Intent(SignupActivity.this, activity_create_event.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(SignupActivity.this, errMsg, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }
}
