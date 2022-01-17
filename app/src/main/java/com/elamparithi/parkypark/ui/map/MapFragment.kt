package com.elamparithi.parkypark.ui.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.elamparithi.parkypark.R
import com.elamparithi.parkypark.databinding.FragmentMapBinding
import com.elamparithi.parkypark.ui.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : BaseFragment<MapFragmentViewModel, FragmentMapBinding>() {

    private var bounds: LatLngBounds.Builder = LatLngBounds.builder()
    private var mGoogleMap: GoogleMap? = null

    companion object {
        @JvmStatic
        fun newInstance() = MapFragment()
    }

    override fun getViewModelClass() = MapFragmentViewModel::class.java

    override fun setupView(view: View) {

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        lifecycleScope.launchWhenCreated {
            // Get map
            mGoogleMap = mapFragment.awaitMap()

            // Wait for map to finish loading
            mGoogleMap?.awaitMapLoad()

            viewModel.isGoogleMapAvailableToUse(true)
            // addClusteredMarkers(googleMap)
        }
    }

    override fun observeData() {

        viewModel.getParkingLotLocations().observe(viewLifecycleOwner) {
            mGoogleMap?.clear()
            it.forEach { parkingLot ->
                val latLng = LatLng(parkingLot.location.latitude, parkingLot.location.longitude)
                bounds.include(latLng)
                val addMarker = mGoogleMap?.addMarker {
                    position(latLng)
                    title(parkingLot.name)
                }
                addMarker?.showInfoWindow()
            }
            mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20))
        }

        viewModel.getUserLocation().observe(viewLifecycleOwner) {
            it?.let { location ->
                val latLng = LatLng(location.latitude, location.longitude)
                bounds.include(latLng)
                val addMarker = mGoogleMap?.addMarker {
                    position(latLng)
                    title("you are here")
                }
                addMarker?.showInfoWindow()
                mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20))
            }

        }
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMapBinding {
        return FragmentMapBinding.inflate(inflater, container, false)
    }
}