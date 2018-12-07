package mans.abdullah.abdullah_mansour.barberboy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
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

public class SignInActivity extends AppCompatActivity
{
    FirebaseAuth auth;
    FirebaseUser user;

    EditText email_field, password_field;
    Button sign_in2;

    String email, password;

    Toast toast;

    LayoutInflater inflater;
    View view;

    TextView textView;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email_field = findViewById(R.id.email_field_signin);
        password_field = findViewById(R.id.password_field_signin);
        sign_in2 = findViewById(R.id.sign_in_btn2);

        auth = FirebaseAuth.getInstance();

        inflater = LayoutInflater.from(getApplicationContext());
        view = inflater.inflate(R.layout.custom_toast, null);
        textView = view.findViewById(R.id.text);
        textView.setTextColor(Color.WHITE);

        toast = new Toast(getApplicationContext());

        sign_in2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                email = email_field.getText().toString();
                password = password_field.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                {
                    //Toast.makeText(getApplicationContext(), "Please Enter A Valid Data", Toast.LENGTH_SHORT).show();
                    textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    textView.setText("Please Enter A Valid Data");

                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(view);
                    toast.show();
                } else
                    {
                        progressDialog = new ProgressDialog(SignInActivity.this);
                        progressDialog.setMessage("Signing In ...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        progressDialog.setCancelable(false);

                        SignIn(email, password);
                    }
            }
        });
    }

    public void SignIn (String email, String password)
    {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            user = auth.getCurrentUser();


                            if (user != null)
                            {
                                user.reload();

                                if (user.isEmailVerified())
                                {
                                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                                    startActivity(intent);
                                } else
                                    {
                                        //Toast.makeText(getApplicationContext(), "Please Verify Your Email \n Check your Mail Inbox", Toast.LENGTH_SHORT).show();
                                        textView.setText("Please Verify Your Email \n Check your Mail Inbox");

                                        toast.setDuration(Toast.LENGTH_SHORT);
                                        toast.setView(view);
                                        toast.show();
                                        progressDialog.dismiss();
                                    }
                            }
                        } else
                            {
                                //Toast.makeText(getApplicationContext(), "Please Sign Up Firstly", Toast.LENGTH_SHORT).show();
                                textView.setText("Please Sign Up Firstly");

                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setView(view);
                                toast.show();
                                progressDialog.dismiss();
                            }
                    }
                });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        toast.cancel();
    }
}
