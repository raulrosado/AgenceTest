package com.serproteam.agencetest.ui.Fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
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
class DetalleFragment : Fragment(), OnMapReadyCallback{
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
    lateinit var map: GoogleMap

    val position = LatLng(41.015137, 28.979530)

    var markerOptions = MarkerOptions().position(position)

    lateinit var marker : Marker

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

        with(binding.mapView) {
            // Initialise the MapView
            onCreate(null)
            // Set the map ready callback to receive the GoogleMap object
            getMapAsync{
                MapsInitializer.initialize(requireContext())
                setMapLocation(it)
            }
        }

        configInicio()
        return binding.root
    }

    private fun setMapLocation(map : GoogleMap) {
        with(map) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))
            mapType = GoogleMap.MAP_TYPE_NORMAL
            setOnMapClickListener {
                if(::marker.isInitialized){
                    marker.remove()
                }
                markerOptions.position(it)
                marker = addMarker(markerOptions)!!
                Toast.makeText(requireContext(), "Click on.....", Toast.LENGTH_SHORT).show()
            }
        }
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

        binding.addCardDetail.setOnClickListener {
            var dialog = AlertDialog.Builder(requireActivity())
            dialog.setCancelable(false)
            dialog.setTitle(resources.getString(R.string.AddtoCar))
            dialog.setMessage(resources.getString(R.string.AddtoCarMens))
            dialog.setPositiveButton(
                resources.getString(R.string.Add),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    cartList.add(producto)
                    cartViewModel.addProduct(cartList, requireContext())
                })
            dialog.setNegativeButton(
                resources.getString(R.string.cancel),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialog.create().hide()
                })
            dialog.create().show()
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//    }

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
    }

    override fun onMapReady(p0: GoogleMap) {
    }

}