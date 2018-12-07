package mans.abdullah.abdullah_mansour.barberboy;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerificationActivity extends AppCompatActivity
{
    Button done_btn;
    FirebaseAuth auth;
    FirebaseUser user;

    Toast toast;

    LayoutInflater inflater;
    View view;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        done_btn = findViewById(R.id.done_btn);

        auth = FirebaseAuth.getInstance();

        inflater = LayoutInflater.from(getApplicationContext());
        view = inflater.inflate(R.layout.custom_toast, null);
        textView = view.findViewById(R.id.text);
        textView.setTextColor(Color.WHITE);

        toast = new Toast(getApplicationContext());

        done_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
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
                            textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                            textView.setText("Please Verify Your Email");

                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(view);
                            toast.show();
                        }
                }
            }
        });
    }

    @Override
    public void onBackPressed()
    {

    }
}
