package com.serproteam.agencetest.ui.Fragment

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.serproteam.agencetest.R
import com.serproteam.agencetest.core.ReplaceFragment
import com.serproteam.agencetest.data.model.Product
import com.serproteam.agencetest.databinding.FragmentDetalleBinding
import com.serproteam.agencetest.ui.viewmodel.CartViewModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetalleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetalleFragment : Fragment(), OnMapReadyCallback , GoogleMap.OnMyLocationClickListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentDetalleBinding? = null
    private val binding get() = _binding!!
    var replaceFragment: ReplaceFragment = ReplaceFragment()
    lateinit var fragmentTransaction: FragmentTransaction
    private val cartViewModel: CartViewModel by viewModels()
    val logi = "Dev"
    var idOrden: Int = 0
    lateinit var producto: Product
    var cartList = ArrayList<Product>()

    lateinit var mapView: MapView
    lateinit var gmap: GoogleMap
    var LOCATION_REQUEST_CODE = 111
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"

    val position = LatLng(41.015137, 28.979530)
    var markerOptions = MarkerOptions().position(position)
    lateinit var marker: Marker
    var permiso = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            idOrden = it.getInt("order")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetalleBinding.inflate(inflater, container, false)
        fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        idOrden = requireArguments().getInt("order")
        Log.v("raulDev", "id Seleccionado:$idOrden")

        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION
                ,Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object: MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if(report.areAllPermissionsGranted()){
                            permiso = true
                        }
                    }
                }
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener {
                permiso = false
            }
            .check()


        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
        }

        mapView = binding.mapView
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)

        configInicio()
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }

        mapView.onSaveInstanceState(mapViewBundle)
    }

    private fun configInicio() {

        var arrayProducts = cartViewModel.getProducts()
        if (arrayProducts.size > 0) {
            Log.v(
                logi,
                "respuesta del observer" + arrayProducts[0].name + " cantidad: " + arrayProducts.size
            )
            producto = arrayProducts.get(idOrden - 1)
            val uri = "@drawable/" + producto.image.toString()
            val imageResource: Int =
                getResources().getIdentifier(uri, null, requireActivity().getPackageName())
            binding.imgProduct.setImageResource(imageResource)
            binding.nameProduct.text = producto.name
            binding.priceProduct.text = "$ " + producto.price
            binding.descriptionProduct.text = producto.description
        }

        cartViewModel.getCart(requireContext())
        cartViewModel.Cart.observe(viewLifecycleOwner, Observer {
            if (it!!.size > 0) {
                Log.v(logi, "tiene algo")
                requireActivity().findViewById<TextView>(R.id.cantProductCart).visibility =
                    View.VISIBLE
                requireActivity().findViewById<TextView>(R.id.cantProductCart).text =
                    it!!.size.toString()
            } else {
                Log.v(logi, "no tiene nada")
                requireActivity().findViewById<TextView>(R.id.cantProductCart).visibility =
                    View.GONE
            }
        })

        binding.addCardDetail.setOnClickListener {
            var dialog = AlertDialog.Builder(requireActivity())
            dialog.setCancelable(false)
            dialog.setTitle(resources.getString(R.string.AddtoCar))
            dialog.setMessage(resources.getString(R.string.AddtoCarMens))
            dialog.setPositiveButton(
                resources.getString(R.string.Add),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    cartViewModel.addProduct(producto, requireContext())
                })
            dialog.setNegativeButton(
                resources.getString(R.string.cancel),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialog.create().hide()
                })
            dialog.create().show()
        }


    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gmap = googleMap
        val uiSettings: UiSettings = gmap.getUiSettings()
        uiSettings.isIndoorLevelPickerEnabled = true
        uiSettings.isMyLocationButtonEnabled = true
        uiSettings.isMapToolbarEnabled = true
        uiSettings.isCompassEnabled = true
        uiSettings.isZoomControlsEnabled = true
        uiSettings.setRotateGesturesEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);

        val ny = LatLng(40.7143528, -74.0059731)
        val markerOptions = MarkerOptions()
        markerOptions.position(ny)
        gmap.addMarker(markerOptions)
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(ny, 18f), 4000, null)
        gmap.setOnMyLocationClickListener(this)
        enableLocation()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetalleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetalleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        const val REQUEST_CODE_LOCATION = 0
    }

    private fun enableLocation() {
        if (!::gmap.isInitialized) return
        if (permiso==true) {
            gmap.isMyLocationEnabled = true
            gmap.uiSettings.isMyLocationButtonEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.gotoSetting),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_CODE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gmap.isMyLocationEnabled = true
                gmap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.gotoSettingInfo),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
            }
        }
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(requireContext(), resources.getString(R.string.youarein) + " ${p0.latitude}, ${p0.longitude}", Toast.LENGTH_SHORT).show()
    }

}