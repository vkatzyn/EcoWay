package com.bgu.ecoway;

import android.Manifest;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bgu.ecoway.databinding.ActivityMainBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private ActivityResultContracts.RequestMultiplePermissions requestPermissionContract;
    private ActivityResultLauncher<String[]> permissionResultLauncher;
    NavController navController;


    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        navController = Navigation.findNavController(binding.navHostFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        requestPermissionContract = new ActivityResultContracts.RequestMultiplePermissions();
        permissionResultLauncher = this.registerForActivityResult(requestPermissionContract, isGranted -> {
            if (isGranted.containsValue(false)) {
                permissionResultLauncher.launch(new String[] {Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION});
            }
            navController.navigate(R.id.action_global_fragmentEntry);
        });
        permissionResultLauncher.launch(new String[] {Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION});
    }
}