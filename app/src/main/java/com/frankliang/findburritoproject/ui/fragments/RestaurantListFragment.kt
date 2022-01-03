package com.frankliang.findburritoproject.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.frankliang.findburritoproject.R
import com.frankliang.findburritoproject.databinding.FragmentRestaurantListBinding
import com.frankliang.findburritoproject.model.Coordinate
import com.frankliang.findburritoproject.model.Restaurant
import com.frankliang.findburritoproject.ui.MainActivity
import com.frankliang.findburritoproject.ui.MainViewModel
import com.frankliang.findburritoproject.util.SnackBarUtil
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener

class RestaurantListFragment : Fragment(), RestaurantAdapter.OnRestaurantClickListener {
    companion object {
        const val GOOGLE_ERROR_DIALOG_RESULT_CODE = 1
    }
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentRestaurantListBinding
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var navController: NavController
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkLocationPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_restaurant_list, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun checkLocationPermission() {
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    loadDataBasedOnLocation()
                } else {
                    SnackBarUtil.showSnackBarWithSetting(
                        requireActivity(), binding.root,
                        R.string.warning_location_permission_required
                    )
                }
            }

        if (checkIfPermissionGranted()) {
            loadDataBasedOnLocation()
        } else {
            requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    fun loadDataBasedOnLocation() {
        if(!isPlayServicesAvailable()) {
            Toast.makeText(requireContext(), getString(R.string.warning_no_google_play_service), Toast.LENGTH_LONG).show()
            return
        }
        //TODO: WHAT exactly does this do?
        val token = object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                return this
            }

            override fun isCancellationRequested(): Boolean {
                return false
            }
        }
        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
            token
        )
            .addOnSuccessListener { location ->
                viewModel.loadInitialData(Coordinate(location.latitude, location.longitude))
            }
    }

    private fun checkIfPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        initRecyclerView()
        postponeEnterTransition()
        viewModel.restaurantList.observe(viewLifecycleOwner) {
            restaurantAdapter.submitData(it)
            (view.parent as? ViewGroup)?.doOnPreDraw {
                startPostponedEnterTransition()
            }
        }
    }

    private fun initRecyclerView() {
        restaurantAdapter = RestaurantAdapter(this)
        binding.rvRestaurantList.apply {
            val linearLayoutManager = LinearLayoutManager(requireContext())
            layoutManager = linearLayoutManager
            adapter = restaurantAdapter
            addOnScrollListener(object : EndlessScrollListener(linearLayoutManager) {
                override fun onLoadMore(page: Int) {
                    viewModel.loadMoreData(page)
                }
            })
        }
    }

    private fun isPlayServicesAvailable(): Boolean {
        val gApi = GoogleApiAvailability.getInstance()
        val resultCode = gApi.isGooglePlayServicesAvailable(requireContext())
        if(gApi.isUserResolvableError(resultCode)) {
            gApi.getErrorDialog(this, resultCode, GOOGLE_ERROR_DIALOG_RESULT_CODE)?.show()
        }
        return resultCode == ConnectionResult.SUCCESS
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            val activity = requireActivity() as MainActivity
            activity.updateAppBar(getString(R.string.title_burrito_places), false)
        }

        if(checkIfPermissionGranted()) {
            //in case user moved too far and data should be refreshed
            loadDataBasedOnLocation()
        }
    }

    override fun onRestaurantClick(
        restaurant: Restaurant,
        extras: FragmentNavigator.Extras,
        position: Int
    ) {
        val bundle = bundleOf(
            RestaurantDetailFragment.KEY_RESTAURANT to restaurant,
            RestaurantDetailFragment.KEY_RESTAURANT_POS to position
        )
        navController.navigate(
            R.id.action_restaurantListFragment_to_restaurantDetailFragment, bundle,
            null, extras
        )
    }
}