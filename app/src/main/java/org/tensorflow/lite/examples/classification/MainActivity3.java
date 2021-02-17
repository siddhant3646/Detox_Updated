package org.tensorflow.lite.examples.classification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity3 extends AppCompatActivity {
    private EditText emailTV, passwordTV,nameTV,cpasswordTV;
    private Button regBtn;
    private FirebaseAuth mAuth;
    TextView t;
    Button b1;
    EditText e1,e2,e3,e4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        t=(TextView)findViewById(R.id.textView8);
        b1=(Button)findViewById(R.id.register);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(MainActivity3.this,Login.class);
                startActivity(i1);
            }
        });
        mAuth = FirebaseAuth.getInstance();

        initializeUI();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
    }

    private void registerNewUser() {
        String email, password,name,confirmPassword;
        email = emailTV.getText().toString();
        password = passwordTV.getText().toString();
        name = nameTV.getText().toString();
        confirmPassword=cpasswordTV.getText().toString();

        if (TextUtils.isEmpty(name)) {
            nameTV.setError("Please enter name");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            emailTV.setError("Please enter email");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordTV.setError("Please enter password");
            return;
        }
        if((password.length() < 6))
        {
            passwordTV.setError("Please enter 6 digit password");
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            cpasswordTV.setError("Please enter password again");
            return;
        }
        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword))
        {
            if(password.equals(confirmPassword))
            {
                // Lopp will Countinue //
            }
            else {
                cpasswordTV.setError("Please enter same password!");
                passwordTV.setError("Please enter same password!");
                return;
            }
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity3.this, Login.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void initializeUI() {
        emailTV = findViewById(R.id.email);
        passwordTV = findViewById(R.id.password);
        regBtn = findViewById(R.id.register);
        nameTV=findViewById(R.id.name);
        cpasswordTV=findViewById(R.id.con_pass);
    }
}