package com.tamara.a25b_11345b_pacmanrace.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.tamara.a25b_11345b_pacmanrace.R

class MapDialogFragment : DialogFragment(), OnMapReadyCallback {

    private var lat: Double = 0.0
    private var lon: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lat = arguments?.getDouble("EXTRA_LAT") ?: 0.0
        lon = arguments?.getDouble("EXTRA_LON") ?: 0.0
        setStyle(STYLE_NORMAL, R.style.CustomDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.dialog_map, container, false)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map_view_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        rootView.findViewById<ImageButton>(R.id.btn_close_map).setOnClickListener {
            dismiss()
        }

        return rootView
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val location = LatLng(lat, lon)
        googleMap.addMarker(MarkerOptions().position(location).title("High Score Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            (resources.displayMetrics.heightPixels * 0.6).toInt()
        )
    }

    companion object {
        fun newInstance(lat: Double, lon: Double): MapDialogFragment {
            val fragment = MapDialogFragment()
            val bundle = Bundle()
            bundle.putDouble("EXTRA_LAT", lat)
            bundle.putDouble("EXTRA_LON", lon)
            fragment.arguments = bundle
            return fragment
        }
    }
}
