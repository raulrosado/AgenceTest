package com.serproteam.agencetest.ui.Fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.serproteam.agencetest.R
import com.serproteam.agencetest.adapter.ProductsAdapter
import com.serproteam.agencetest.adapter.ProductsAdapter.OnProductlickListener
import com.serproteam.agencetest.core.ReplaceFragment
import com.serproteam.agencetest.data.model.Product
import com.serproteam.agencetest.databinding.FragmentListBinding
import com.serproteam.agencetest.ui.viewmodel.CartViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment(), OnProductlickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    var replaceFragment: ReplaceFragment = ReplaceFragment()
    lateinit var fragmentTransaction: FragmentTransaction
    lateinit var productAdapter: ProductsAdapter
    var logi = "Dev"
    var cartList = ArrayList<Product>()
    var visibles = 0
    var lastVisible = 0
    var lastvisiblePosition = 0

    private val cartViewModel: CartViewModel by viewModels()
    lateinit var viewGlobal: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewGlobal = view
        configInicio()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun configInicio() {
        var arrayProducts = cartViewModel.getProducts()
        if (arrayProducts.size > 0) {
            Log.v(
                logi,
                "respuesta del observer" + arrayProducts[0].name + " cantidad: " + arrayProducts.size
            )

            binding.recyclerProducts.visibility = View.VISIBLE
            productAdapter = ProductsAdapter(
                requireContext(),
                arrayProducts, binding.recyclerProducts, this
            )

            val resId = R.anim.learn_layout_animation_fall_down
            val animation = AnimationUtils.loadLayoutAnimation(
                activity, resId
            )
            binding.recyclerProducts.setLayoutAnimation(
                animation
            )
            binding.recyclerProducts.setHasFixedSize(true)

            binding.recyclerProducts.adapter = productAdapter
            productAdapter.addItem(arrayProducts.subList(0, 8))
            visibles = 8

        } else {
            binding.recyclerProducts.visibility = View.VISIBLE
        }

        /***
         *  Adding OnScroll listener for parent RecyclerView
         */
        binding.recyclerProducts.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            Log.v(logi, "oldScrooll: ${oldScrollY}  scrool:${scrollY}")
            if (oldScrollY < -60) {
                val layoutManager = binding.recyclerProducts.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                var falta = arrayProducts.size - totalItemCount
                Log.v(
                    logi,
                    "totalItemCount: $visibles" + " arrayProducts.size: " + arrayProducts.size + "falta:" + falta
                )
                Log.v(logi, "firstVisibleItemPosition: $firstVisibleItemPosition")

                lastVisible = visibles
                lastVisible = visibles +3
                Log.v(logi, "falta: " + lastVisible)
                Log.v(logi, "falta: +3" )
                if (lastVisible > arrayProducts.size) {
                    lastVisible = arrayProducts.size
                }

                if (visibles < arrayProducts.size) {
                    Log.v(
                        logi,
                        " ES MENOR"
                    )
//                    Log.v(
//                        logi,
//                        "${visibles} < ${lastVisible}"
//                    )
//                    if (lastVisible > arrayProducts.size) {
//                        lastVisible = arrayProducts.size
//                        Log.v(logi, "es mayor")
//                    }
//                    if (visibles > arrayProducts.size) {
//                        visibles = arrayProducts.size
//                        Log.v(logi, "es mayor")
//                    }
                    if (visibles < arrayProducts.size) {
                        productAdapter.addItem(arrayProducts.subList(visibles, lastVisible))
                    }
                    visibles = lastVisible
                    if (visibles > arrayProducts.size - 1) {
                        visibles = arrayProducts.size
                        Log.v(logi, "es mayor")
                    }
                }

            }
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
    }

    fun setupRecyclerView() {
        binding.recyclerProducts.layoutManager = GridLayoutManager(context, 2)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onProductClickListener(item: Product, context: Context, position: Int) {
        val bundle = Bundle()
        bundle.putInt("order", item.id)
        Navigation.findNavController(viewGlobal).navigate(R.id.detalleFragment, bundle)
    }

    override fun onProductAddClickListener(item: Product, context: Context, position: Int) {
        var dialog = AlertDialog.Builder(requireActivity())
        dialog.setCancelable(false)
        dialog.setTitle(resources.getString(R.string.AddtoCar))
        dialog.setMessage(resources.getString(R.string.AddtoCarMens))
        dialog.setPositiveButton(
            resources.getString(R.string.Add),
            DialogInterface.OnClickListener { dialogInterface, i ->
                cartViewModel.addProduct(item, requireContext())
            })
        dialog.setNegativeButton(
            resources.getString(R.string.cancel),
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialog.create().hide()
            })
        dialog.create().show()
    }
}
