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
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

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
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
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
    private List<Point> routeCoordinates2;
    private List<Point> routeCoordinates3;
    private NavController navController;
    private double[] firstM;
    private double[] secondM;
    private double[] thirdM;
    Style stylee;

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
            applyStyles();
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
                hideKeyboardFrom(requireContext(), binding.addressInputsLayout.endPointEt);
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
                        stylee = style;
                        binding.rectt.animate().alpha(0).setDuration(400);
                        enableLocationComponent(style);
                        initRouteCoordinates();

                    }
                });
    }

    private void applyStyles() {
        // Create the LineString from the list of coordinates and then make a GeoJSON
// FeatureCollection so we can add the line to our map as a layer.
        LineString lineString1 = LineString.fromLngLats(routeCoordinates);
        LineString lineString2 = LineString.fromLngLats(routeCoordinates2);
        LineString lineString3 = LineString.fromLngLats(routeCoordinates3);

        FeatureCollection featureCollection1 = FeatureCollection.fromFeature(Feature.fromGeometry(lineString1));
        FeatureCollection featureCollection2 = FeatureCollection.fromFeature(Feature.fromGeometry(lineString2));
        FeatureCollection featureCollection3 = FeatureCollection.fromFeature(Feature.fromGeometry(lineString3));
        Style style = stylee;
        style.addSource(new GeoJsonSource("linee-source1", featureCollection1,
                new GeoJsonOptions().withLineMetrics(true)));

// The layer properties for our line. This is where we set the gradient colors, set the
// line width, etc
        style.addLayer(new LineLayer("lineelayer1", "linee-source1").withProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(7f),
                lineGradient(interpolate(
                        linear(), lineProgress(),
                        stop(0f, rgb((int) 255 * (1 - firstM[0]), (int) 255 * (firstM[0]), 0)),
                        stop(0.14f, rgb((int) 255 * (1 - firstM[1]), (int) 255 * (firstM[1]), 0)),
                        stop(0.28f, rgb((int) 255 * (1 - firstM[2]), (int) 255 * (firstM[2]), 0)),
                        stop(0.42f, rgb((int) 255 * (1 - firstM[3]), (int) 255 * (firstM[3]), 0)),
                        stop(0.56f, rgb((int) 255 * (1 - firstM[4]), (int) 255 * (firstM[4]), 0)),
                        stop(0.7f, rgb((int) 255 * (1 - firstM[5]), (int) 255 * (firstM[5]), 0)),
                        stop(0.84f, rgb((int) 255 * (1 - firstM[6]), (int) 255 * (firstM[6]), 0))
                ))));

        style.addSource(new GeoJsonSource("linee-source2", featureCollection2,
                new GeoJsonOptions().withLineMetrics(true)));

// The layer properties for our line. This is where we set the gradient colors, set the
// line width, etc
        style.addLayer(new LineLayer("lineelayer2", "linee-source2").withProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(4f),
                lineGradient(interpolate(
                        linear(), lineProgress(),
                        stop(0f, rgb((int) 255 * (1 - secondM[0]), (int) 255 * (secondM[0]), 0)),
                        stop(0.1f, rgb((int) 255 * (1 - secondM[1]), (int) 255 * (secondM[1]), 0)),
                        stop(0.2f, rgb((int) 255 * (1 - secondM[2]), (int) 255 * (secondM[2]), 0)),
                        stop(0.3f, rgb((int) 255 * (1 - secondM[3]), (int) 255 * (secondM[3]), 0)),
                        stop(0.4f, rgb((int) 255 * (1 - secondM[4]), (int) 255 * (secondM[4]), 0)),
                        stop(0.5f, rgb((int) 255 * (1 - secondM[5]), (int) 255 * (secondM[5]), 0)),
                        stop(0.6f, rgb((int) 255 * (1 - secondM[6]), (int) 255 * (secondM[6]), 0)),
                        stop(0.7f, rgb((int) 255 * (1 - secondM[7]), (int) 255 * (secondM[7]), 0)),
                        stop(0.8f, rgb((int) 255 * (1 - secondM[8]), (int) 255 * (secondM[8]), 0)),
                        stop(0.9f, rgb((int) 255 * (1 - secondM[9]), (int) 255 * (secondM[9]), 0))
                ))));

        style.addSource(new GeoJsonSource("linee-source3", featureCollection3,
                new GeoJsonOptions().withLineMetrics(true)));

// The layer properties for our line. This is where we set the gradient colors, set the
// line width, etc
        style.addLayer(new LineLayer("lineelayer3", "linee-source3").withProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(4f),
                lineGradient(interpolate(
                        linear(), lineProgress(),
                        stop(0f, rgb((int) 255 * (1 - thirdM[0]), (int) 255 * (thirdM[0]), 0)),
                        stop(0.125f, rgb((int) 255 * (1 - thirdM[1]), (int) 255 * (thirdM[1]), 0)),
                        stop(0.25f, rgb((int) 255 * (1 - thirdM[2]), (int) 255 * (thirdM[2]), 0)),
                        stop(0.375f, rgb((int) 255 * (1 - thirdM[3]), (int) 255 * (thirdM[3]), 0)),
                        stop(0.5f, rgb((int) 255 * (1 - thirdM[4]), (int) 255 * (thirdM[4]), 0)),
                        stop(0.625f, rgb((int) 255 * (1 - thirdM[5]), (int) 255 * (thirdM[5]), 0)),
                        stop(0.750f, rgb((int) 255 * (1 - thirdM[6]), (int) 255 * (thirdM[6]), 0)),
                        stop(0.875f, rgb((int) 255 * (1 - thirdM[7]), (int) 255 * (thirdM[7]), 0)),
                        stop(0.9f, rgb((int) 255 * (1 - thirdM[8]), (int) 255 * (thirdM[8]), 0))
                ))));

        CameraPosition.Builder builder = new CameraPosition.Builder();
        builder.target(new LatLng(55.538430, 37.530176)).zoom(14);
        CameraPosition cameraPosition = builder.build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1500);
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
        routeCoordinates.add(Point.fromLngLat(37.528245, 55.534180));
        routeCoordinates.add(Point.fromLngLat(37.528138, 55.534593));
        routeCoordinates.add(Point.fromLngLat(37.528760, 55.534714));
        routeCoordinates.add(Point.fromLngLat(37.528352, 55.535588));
        routeCoordinates.add(Point.fromLngLat(37.530755, 55.537033));
        routeCoordinates.add(Point.fromLngLat(37.534103, 55.537968));
        routeCoordinates.add(Point.fromLngLat(37.533309, 55.539231));
        routeCoordinates.add(Point.fromLngLat(37.532751, 55.542291));
        firstM = new double[] { 0.88, 0.85, 0.85, 0.72, 0.64, 0.75, 0.80 };

        routeCoordinates2 = new ArrayList<>();
        routeCoordinates2.add(Point.fromLngLat(37.528245, 55.534180));
        routeCoordinates2.add(Point.fromLngLat(37.528138, 55.534593));
        routeCoordinates2.add(Point.fromLngLat(37.526850, 55.534921));
        routeCoordinates2.add(Point.fromLngLat(37.526464, 55.536548));
        routeCoordinates2.add(Point.fromLngLat(37.527859, 55.537398));
        routeCoordinates2.add(Point.fromLngLat(37.527408, 55.538830));
        routeCoordinates2.add(Point.fromLngLat(37.528395, 55.539340));
        routeCoordinates2.add(Point.fromLngLat(37.527344, 55.540202));
        routeCoordinates2.add(Point.fromLngLat(37.530241, 55.541210));
        routeCoordinates2.add(Point.fromLngLat(37.532751, 55.542291));
        secondM = new double[] { 0.88, 0.85, 0.90, 0.92, 1.00, 1.00, 1.00, 0.96, 0.85, 0.80 };

        routeCoordinates3 = new ArrayList<>();
        routeCoordinates3.add(Point.fromLngLat(37.528245, 55.534180));
        routeCoordinates3.add(Point.fromLngLat(37.528138, 55.534593));
        routeCoordinates3.add(Point.fromLngLat(37.528760, 55.534714));
        routeCoordinates3.add(Point.fromLngLat(37.528352, 55.535588));
        routeCoordinates3.add(Point.fromLngLat(37.528266, 55.536742));
        routeCoordinates3.add(Point.fromLngLat(37.529425, 55.538223));
        routeCoordinates3.add(Point.fromLngLat(37.529253, 55.539656));
        routeCoordinates3.add(Point.fromLngLat(37.530927, 55.540482));
        routeCoordinates3.add(Point.fromLngLat(37.532751, 55.542291));
        thirdM = new double[] { 0.88, 0.85, 0.85, 0.72, 0.51, 0.75, 0.80, 0.80, 0.80 };

    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}