package com.bgu.ecoway.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bgu.ecoway.MainViewModel;
import com.bgu.ecoway.R;
import com.bgu.ecoway.data.AddressSuggestion;
import com.bgu.ecoway.data.RouteSuggestion;
import com.bgu.ecoway.databinding.FragmentEntryBinding;
import com.bgu.ecoway.network.NetworkConstants;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FragmentEntry extends Fragment implements OnMapReadyCallback, PermissionsListener {

    MainViewModel mainViewModel;
    private FragmentEntryBinding binding;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        Mapbox.getInstance(requireContext(), NetworkConstants.MAPBOX_DEFAULT_PUBLIC_TOKEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentEntryBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(mainViewModel);
        binding.mapLayout.mapView.onCreate(savedInstanceState);
        binding.mapLayout.mapView.getMapAsync(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel.getAddressSuggestions().observe(getViewLifecycleOwner(), this::updateDataInAddressSuggestionsAdapter);
        mainViewModel.getRouteSuggestions().observe(getViewLifecycleOwner(), this::updateDataInRouteSuggestionsAdapter);

    }

    @Override
    public void onMapReady(@NonNull @NotNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(new Style.Builder().fromUri(Style.MAPBOX_STREETS),
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull @NotNull Style style) {
                        enableLocationComponent(style);
                    }
                });
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {

// Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

// Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(requireContext(), loadedMapStyle).build());

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(requireActivity());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> list) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(style -> enableLocationComponent(style));
        } else {
            requireActivity().finish();
        }
    }

    private void updateDataInAddressSuggestionsAdapter(List<AddressSuggestion> suggestions) {
        mainViewModel.getAddressSuggestionsAdapter().setData(suggestions);
    }

    private void updateDataInRouteSuggestionsAdapter(List<RouteSuggestion> suggestions) {
        mainViewModel.getRouteSuggestionsAdapter().setData(suggestions);
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.mapLayout.mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapLayout.mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapLayout.mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.mapLayout.mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.mapLayout.mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapLayout.mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.mapLayout.mapView.onDestroy();
    }


}