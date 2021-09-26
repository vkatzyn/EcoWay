package com.bgu.ecoway.ui;

import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lineProgress;
import static com.mapbox.mapboxsdk.style.expressions.Expression.linear;
import static com.mapbox.mapboxsdk.style.expressions.Expression.rgb;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineGradient;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

import android.animation.Animator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bgu.ecoway.R;
import com.bgu.ecoway.viewmodel.MainViewModel;
import com.bgu.ecoway.data.AddressSuggestion;
import com.bgu.ecoway.data.RouteSuggestion;
import com.bgu.ecoway.databinding.FragmentEntryBinding;
import com.bgu.ecoway.network.NetworkConstants;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FragmentEntry extends Fragment implements OnMapReadyCallback, PermissionsListener {

    MainViewModel mainViewModel;
    private FragmentEntryBinding binding;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private List<Point> routeCoordinates;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        Mapbox.getInstance(requireContext(), NetworkConstants.MAPBOX_DEFAULT_PUBLIC_TOKEN);
        navController = NavHostFragment.findNavController(this);
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

        binding.mapLayout.buttonRoute.setOnClickListener(v -> {
            binding.busStopsLayout.busStopsLayout.setVisibility(View.GONE);
            binding.addressInputsLayout.addressInputsLayout.setVisibility(View.VISIBLE);
            binding.chooseOnMapLayout.chooseOnMapLayout.setVisibility(View.VISIBLE);
            binding.favoritesLayout.favoritesLayout.setVisibility(View.VISIBLE);
            binding.mapLayout.mapLayout.setVisibility(View.GONE);
        });

        binding.addressInputsLayout.endPointEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().toLowerCase().equals("южн")) {
                    binding.favoritesLayout.favoritesLayout.setVisibility(View.GONE);
                    binding.addressSuggestionsRecyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.addressSuggestionsRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.addressSuggestionsRecyclerView.setVisibility(View.GONE);
                binding.addressInputsLayout.endPointEt.setText("Южнобутовская улица 71");
                binding.routeOptionsLayout.setVisibility(View.VISIBLE);
                binding.addressInputsLayout.endPointEt.clearFocus();
                binding.mapLayout.mapLayout.setVisibility(View.VISIBLE);
                binding.mapLayout.buttonRoute.setVisibility(View.GONE);
                binding.chooseOnMapLayout.chooseOnMapLayout.setVisibility(View.GONE);
                binding.routeSuggestionsLayout.routeSuggestionsLayout.setVisibility(View.VISIBLE);
                return false;
            }
        });

        binding.mapLayout.buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.drawer.animate().translationX(0).setDuration(300);
//                binding.mapLayout.mapLayout.animate().alpha(0.7f).setDuration(300);
                binding.rectt.animate().alpha(0.7f).setDuration(300);
                binding.busStopsLayout.busStopsLayout.animate().alpha(0.7f).setDuration(300);
            }
        });

        binding.drawerLayout.bigbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.mainLayout.animate().alpha(0).setDuration(400).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        navController.navigate(R.id.action_fragmentEntry_to_strollsFragment);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        });

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
                        initRouteCoordinates();
                        // Create the LineString from the list of coordinates and then make a GeoJSON
// FeatureCollection so we can add the line to our map as a layer.
                        LineString lineString = LineString.fromLngLats(routeCoordinates);

                        FeatureCollection featureCollection = FeatureCollection.fromFeature(Feature.fromGeometry(lineString));

                        style.addSource(new GeoJsonSource("line-source", featureCollection,
                                new GeoJsonOptions().withLineMetrics(true)));

// The layer properties for our line. This is where we set the gradient colors, set the
// line width, etc
                        style.addLayer(new LineLayer("linelayer", "line-source").withProperties(
                                lineCap(Property.LINE_CAP_ROUND),
                                lineJoin(Property.LINE_JOIN_ROUND),
                                lineWidth(14f),
                                lineGradient(interpolate(
                                        linear(), lineProgress(),
                                        stop(0f, rgb(6, 1, 255)), // blue
                                        stop(0.1f, rgb(59, 118, 227)), // royal blue
                                        stop(0.3f, rgb(7, 238, 251)), // cyan
                                        stop(0.5f, rgb(0, 255, 42)), // lime
                                        stop(0.7f, rgb(255, 252, 0)), // yellow
                                        stop(1f, rgb(255, 30, 0)) // red
                                ))));
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
    private void initRouteCoordinates() {
// Create a list to store our line coordinates.
        routeCoordinates = new ArrayList<>();
        routeCoordinates.add(Point.fromLngLat(-77.044211, 38.852924));
        routeCoordinates.add(Point.fromLngLat(-77.045659, 38.860158));
        routeCoordinates.add(Point.fromLngLat(-77.044232, 38.862326));
        routeCoordinates.add(Point.fromLngLat(-77.040879, 38.865454));
        routeCoordinates.add(Point.fromLngLat(-77.039936, 38.867698));
        routeCoordinates.add(Point.fromLngLat(-77.040338, 38.86943));
        routeCoordinates.add(Point.fromLngLat(-77.04264, 38.872528));
        routeCoordinates.add(Point.fromLngLat(-77.03696, 38.878424));
        routeCoordinates.add(Point.fromLngLat(-77.032309, 38.87937));
        routeCoordinates.add(Point.fromLngLat(-77.030056, 38.880945));
        routeCoordinates.add(Point.fromLngLat(-77.027645, 38.881779));
        routeCoordinates.add(Point.fromLngLat(-77.026946, 38.882645));
        routeCoordinates.add(Point.fromLngLat(-77.026942, 38.885502));
        routeCoordinates.add(Point.fromLngLat(-77.028054, 38.887449));
        routeCoordinates.add(Point.fromLngLat(-77.02806, 38.892088));
        routeCoordinates.add(Point.fromLngLat(-77.03364, 38.892108));
        routeCoordinates.add(Point.fromLngLat(-77.033643, 38.899926));

    }

}