package com.aryopraset.woapp.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.aryopraset.woapp.R;
import com.aryopraset.woapp.databinding.ActivityEditProfileBinding;
import com.aryopraset.woapp.utils.LiveDataUtils;
import com.aryopraset.woapp.viewModels.HomepageViewModel;

public class EditProfile extends AppCompatActivity {
    private int userId;
    HomepageViewModel homepageViewModel;
    ActivityEditProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userId = getIntent().getIntExtra(MainActivity.USER_ID, 1);
        homepageViewModel = new ViewModelProvider.AndroidViewModelFactory(EditProfile.this.getApplication()).create(HomepageViewModel.class);

        homepageViewModel.getUser(userId).observe(this, user->{
            binding.nameEt.setText(user.getName());
            binding.emailEt.setText(user.getEmail());
        });

        binding.saveBtn.setOnClickListener(v->{
            String name = binding.nameEt.getText().toString().trim();
            String email = binding.emailEt.getText().toString().trim();

            if(email.isEmpty() || name.isEmpty()){
                Toast.makeText(this, "All Fields must be filled", Toast.LENGTH_LONG).show();
            }else if(!email.endsWith("@gmail.com")){
                Toast.makeText(this, "Email must ends with @gmail.com", Toast.LENGTH_LONG).show();
            }else{
                LiveDataUtils.observeOnce(homepageViewModel.getUser(userId), this, user->{
                    user.setName(name);
                    user.setEmail(email);
                    homepageViewModel.updateUser(user);
                    finish();
                });
            }
        });

        binding.backButton.setOnClickListener(v->{
            finish();
        });
    }
}