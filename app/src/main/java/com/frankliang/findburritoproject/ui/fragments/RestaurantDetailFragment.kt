package com.frankliang.findburritoproject.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.frankliang.findburritoproject.R
import com.frankliang.findburritoproject.databinding.FragmentRestaurantDetailBinding
import com.frankliang.findburritoproject.model.Restaurant
import com.frankliang.findburritoproject.ui.MainActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class RestaurantDetailFragment: Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentRestaurantDetailBinding
    private lateinit var restaurant: Restaurant
    var position = -1
    companion object {
        const val KEY_RESTAURANT = "key_restaurant"
        const val KEY_RESTAURANT_POS = "restaurant_pos"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        restaurant = requireArguments().getParcelable(KEY_RESTAURANT)!!
        position = requireArguments().getInt(KEY_RESTAURANT_POS)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_restaurant_detail, container, false)
        binding.restaurant = restaurant
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(position > -1) {
            binding.tvAddress.transitionName = requireContext().getString(R.string.trans_address, position)
            binding.tvInfo.transitionName = requireContext().getString(R.string.trans_info, position)
            binding.vBackground.transitionName = requireContext().getString(R.string.trans_container, position)
        }
        binding.executePendingBindings()
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        if(requireActivity() is MainActivity) {
            val activity = requireActivity() as MainActivity
            activity.updateAppBar(restaurant.name?: "", true)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val coordinate = restaurant.getCoordinate()
        val coord = LatLng(coordinate.latitude!!, coordinate.longitude!!)
        googleMap.addMarker(
            MarkerOptions()
            .position(coord))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 17f))
    }
}