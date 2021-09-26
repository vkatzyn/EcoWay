package com.bgu.ecoway;

import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lineProgress;
import static com.mapbox.mapboxsdk.style.expressions.Expression.linear;
import static com.mapbox.mapboxsdk.style.expressions.Expression.rgb;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineGradient;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bgu.ecoway.adapters.StrollAdapter;
import com.bgu.ecoway.databinding.FragmentStrollsBinding;
import com.bgu.ecoway.network.NetworkConstants;
import com.bgu.ecoway.viewmodel.StrollsViewModel;
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
public class StrollsFragment extends Fragment implements OnMapReadyCallback, PermissionsListener {

    FragmentStrollsBinding binding;
    StrollsViewModel strollsViewModel;
    MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private List<Point> routeCoordinatesVorob;
    private List<Point> routeCoordinatesBlvd;
    private double[] scoresBlvd;
    private double[] scoresVorob;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        strollsViewModel = new ViewModelProvider(this).get(StrollsViewModel.class);
        Mapbox.getInstance(requireContext(), NetworkConstants.MAPBOX_DEFAULT_PUBLIC_TOKEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStrollsBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.getMapAsync(this);
        strollsViewModel.getStrolls().observe(getViewLifecycleOwner(), strolls -> {
            if (strolls != null && !strolls.isEmpty()) {
                binding.productPager.setAdapter(new StrollAdapter(strolls));
            }
        });
        binding.productPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (mapboxMap == null)
                    return;
                CameraPosition.Builder builder = new CameraPosition.Builder();
                switch (position) {
                    case 0:
                        builder.target(new LatLng(55.712611, 37.546792)).zoom(13);
                        break;
                    case 1:
                        builder.target(new LatLng(55.754306, 37.623260)).zoom(12.3);
                        break;
                    case 2:
                        break;
                }
                CameraPosition cameraPosition = builder.build();
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 3000);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onMapReady(@NonNull @NotNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(new Style.Builder().fromUri(Style.MAPBOX_STREETS),
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull @NotNull Style style) {
                        binding.rectt.animate().alpha(0).setDuration(600);
                        enableLocationComponent(style);
                        initRouteCoordinates();
                        LineString lineString = LineString.fromLngLats(routeCoordinatesBlvd);
                        LineString lineString2 = LineString.fromLngLats(routeCoordinatesVorob);

                        FeatureCollection featureCollection = FeatureCollection.fromFeature(Feature.fromGeometry(lineString));
                        FeatureCollection featureCollection2 = FeatureCollection.fromFeature(Feature.fromGeometry(lineString2));

                        style.addSource(new GeoJsonSource("line-source", featureCollection,
                                new GeoJsonOptions().withLineMetrics(true)));
                        style.addSource(new GeoJsonSource("line-source2", featureCollection2,
                                new GeoJsonOptions().withLineMetrics(true)));

// The layer properties for our line. This is where we set the gradient colors, set the
// line width, etc
                        style.addLayer(new LineLayer("linelayer", "line-source").withProperties(
                                lineCap(Property.LINE_CAP_ROUND),
                                lineJoin(Property.LINE_JOIN_ROUND),
                                lineWidth(7f),
                                lineGradient(interpolate(
                                        linear(), lineProgress(),
                                        stop(0f, rgb((int) 255 * (1 - scoresBlvd[0]), (int) 255 * (scoresBlvd[0]), 0)),
                                        stop(0.043f, rgb((int) 255 * (1 - scoresBlvd[1]), (int) 255 * (scoresBlvd[1]), 0)),
                                        stop(0.086f, rgb((int) 255 * (1 - scoresBlvd[2]), (int) 255 * (scoresBlvd[2]), 0)),
                                        stop(0.130f, rgb((int) 255 * (1 - scoresBlvd[3]), (int) 255 * (scoresBlvd[3]), 0)),
                                        stop(0.173f, rgb((int) 255 * (1 - scoresBlvd[4]), (int) 255 * (scoresBlvd[4]), 0)),
                                        stop(0.22f, rgb((int) 255 * (1 - scoresBlvd[5]), (int) 255 * (scoresBlvd[5]), 0)),
                                        stop(0.26f, rgb((int) 255 * (1 - scoresBlvd[6]), (int) 255 * (scoresBlvd[6]), 0)),
                                        stop(0.30f, rgb((int) 255 * (1 - scoresBlvd[7]), (int) 255 * (scoresBlvd[7]), 0)),
                                        stop(0.35f, rgb((int) 255 * (1 - scoresBlvd[8]), (int) 255 * (scoresBlvd[8]), 0)),
                                        stop(0.39f, rgb((int) 255 * (1 - scoresBlvd[9]), (int) 255 * (scoresBlvd[9]), 0)),
                                        stop(0.43f, rgb((int) 255 * (1 - scoresBlvd[10]), (int) 255 * (scoresBlvd[10]), 0)),
                                        stop(0.48f, rgb((int) 255 * (1 - scoresBlvd[11]), (int) 255 * (scoresBlvd[11]), 0)),
                                        stop(0.52f, rgb((int) 255 * (1 - scoresBlvd[12]), (int) 255 * (scoresBlvd[12]), 0)),
                                        stop(0.56f, rgb((int) 255 * (1 - scoresBlvd[13]), (int) 255 * (scoresBlvd[13]), 0)),
                                        stop(0.61f, rgb((int) 255 * (1 - scoresBlvd[14]), (int) 255 * (scoresBlvd[14]), 0)),
                                        stop(0.65f, rgb((int) 255 * (1 - scoresBlvd[15]), (int) 255 * (scoresBlvd[15]), 0)),
                                        stop(0.69f, rgb((int) 255 * (1 - scoresBlvd[16]), (int) 255 * (scoresBlvd[16]), 0)),
                                        stop(0.74f, rgb((int) 255 * (1 - scoresBlvd[17]), (int) 255 * (scoresBlvd[17]), 0)),
                                        stop(0.78f, rgb((int) 255 * (1 - scoresBlvd[18]), (int) 255 * (scoresBlvd[18]), 0)),
                                        stop(0.82f, rgb((int) 255 * (1 - scoresBlvd[19]), (int) 255 * (scoresBlvd[19]), 0)),
                                        stop(0.87f, rgb((int) 255 * (1 - scoresBlvd[20]), (int) 255 * (scoresBlvd[20]), 0)),
                                        stop(0.91f, rgb((int) 255 * (1 - scoresBlvd[21]), (int) 255 * (scoresBlvd[21]), 0)),
                                        stop(0.95f, rgb((int) 255 * (1 - scoresBlvd[22]), (int) 255 * (scoresBlvd[22]), 0))
                                ))));
                        style.addLayer(new LineLayer("linelayer2", "line-source2").withProperties(
                                lineCap(Property.LINE_CAP_ROUND),
                                lineJoin(Property.LINE_JOIN_ROUND),
                                lineWidth(7f),
                                lineGradient(interpolate(
                                        linear(), lineProgress(),
                                        stop(0f, rgb((int) 255 * (1 - scoresVorob[0]), (int) 255 * (scoresVorob[0]), 0)),
                                        stop(0.06f, rgb((int) 255 * (1 - scoresVorob[1]), (int) 255 * (scoresVorob[1]), 0)),
                                        stop(0.13f, rgb((int) 255 * (1 - scoresVorob[2]), (int) 255 * (scoresVorob[2]), 0)),
                                        stop(0.2f, rgb((int) 255 * (1 - scoresVorob[3]), (int) 255 * (scoresVorob[3]), 0)),
                                        stop(0.26f, rgb((int) 255 * (1 - scoresVorob[4]), (int) 255 * (scoresVorob[4]), 0)),
                                        stop(0.33f, rgb((int) 255 * (1 - scoresVorob[5]), (int) 255 * (scoresVorob[5]), 0)),
                                        stop(0.4f, rgb((int) 255 * (1 - scoresVorob[6]), (int) 255 * (scoresVorob[6]), 0)),
                                        stop(0.46f, rgb((int) 255 * (1 - scoresVorob[7]), (int) 255 * (scoresVorob[7]), 0)),
                                        stop(0.53f, rgb((int) 255 * (1 - scoresVorob[8]), (int) 255 * (scoresVorob[8]), 0)),
                                        stop(0.6f, rgb((int) 255 * (1 - scoresVorob[9]), (int) 255 * (scoresVorob[9]), 0)),
                                        stop(0.66f, rgb((int) 255 * (1 - scoresVorob[10]), (int) 255 * (scoresVorob[10]), 0)),
                                        stop(0.73f, rgb((int) 255 * (1 - scoresVorob[11]), (int) 255 * (scoresVorob[11]), 0)),
                                        stop(0.80f, rgb((int) 255 * (1 - scoresVorob[12]), (int) 255 * (scoresVorob[12]), 0)),
                                        stop(0.86f, rgb((int) 255 * (1 - scoresVorob[13]), (int) 255 * (scoresVorob[13]), 0)),
                                        stop(0.93f, rgb((int) 255 * (1 - scoresVorob[14]), (int) 255 * (scoresVorob[14]), 0))
                                ))));

                        CameraPosition.Builder builder = new CameraPosition.Builder();
                        builder.target(new LatLng(55.712611, 37.546792)).zoom(13);
                        CameraPosition cameraPosition = builder.build();
                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 3000);
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

    @Override
    public void onStart() {
        super.onStart();
        binding.mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.mapView.onDestroy();
    }

    private void initRouteCoordinates() {
// Create a list to store our line coordinates.
        routeCoordinatesVorob = new ArrayList<>();
        routeCoordinatesBlvd = new ArrayList<>();

        routeCoordinatesVorob.add(Point.fromLngLat(37.559166, 55.710189));
        routeCoordinatesVorob.add(Point.fromLngLat(37.557921, 55.709222));
        routeCoordinatesVorob.add(Point.fromLngLat(37.550411, 55.710044));
        routeCoordinatesVorob.add(Point.fromLngLat(37.543244, 55.711180));
        routeCoordinatesVorob.add(Point.fromLngLat(37.539339, 55.714710));
        routeCoordinatesVorob.add(Point.fromLngLat(37.538180, 55.718578));
        routeCoordinatesVorob.add(Point.fromLngLat(37.538609, 55.723025));
        routeCoordinatesVorob.add(Point.fromLngLat(37.541098, 55.726046));
        routeCoordinatesVorob.add(Point.fromLngLat(37.546291, 55.729043));
        routeCoordinatesVorob.add(Point.fromLngLat(37.548050, 55.726820));
        routeCoordinatesVorob.add(Point.fromLngLat(37.543973, 55.723871));
        routeCoordinatesVorob.add(Point.fromLngLat(37.543373, 55.719955));
        routeCoordinatesVorob.add(Point.fromLngLat(37.545604, 55.716281));
        routeCoordinatesVorob.add(Point.fromLngLat(37.549810, 55.713574));
        routeCoordinatesVorob.add(Point.fromLngLat(37.560710, 55.711349));
        routeCoordinatesVorob.add(Point.fromLngLat(37.559166, 55.710189));


        routeCoordinatesBlvd.add(Point.fromLngLat(37.611222, 55.747827));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.610363, 55.748757));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.600750, 55.751281));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.600557, 55.754614));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.598240, 55.757536));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.605385, 55.764683));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.606029, 55.765601));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.613282, 55.768124));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.621736, 55.767037));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.627680, 55.766482));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.633752, 55.766446));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.637636, 55.765251));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.643709, 55.761943));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.646112, 55.758611));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.648386, 55.755761));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.645597, 55.751100));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.642078, 55.750182));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.638516, 55.747018));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.636799, 55.746897));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.630920, 55.748467));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.621178, 55.748274));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.616286, 55.747550));
        routeCoordinatesBlvd.add(Point.fromLngLat(37.611222, 55.747827));
        scoresBlvd = new double[]{0.54, 0.52, 0.68, 0.72, 0.83, 0.92, 0.94, 0.92, 0.95, 0.88, 0.85, 0.78, 0.86, 0.84, 0.83, 0.75, 0.73, 0.84, 0.88, 0.77, 0.72, 0.64, 0.54};
        scoresVorob = new double[]{0.82, 0.99, 1.00, 0.99, 0.98, 0.99, 1.00, 0.99, 0.72, 0.63, 0.74, 0.79, 0.84, 0.82, 0.74};

    }
}