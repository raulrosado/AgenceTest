package com.serproteam.agencetest.ui.Fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.facebook.login.LoginManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
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
    //    lateinit var productModel: ViewModel
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

    private fun configInicio() {
        var arrayProducts = cartViewModel.getProducts()
        if (arrayProducts.size > 0) {
            Log.v(
                logi,
                "respuesta del observer" + arrayProducts[0].name + " cantidad: " + arrayProducts.size
            )
            binding.recyclerProducts.visibility = View.VISIBLE
            productAdapter = ProductsAdapter(requireContext(), arrayProducts, this)
            binding.recyclerProducts.adapter = productAdapter
        } else {
            binding.recyclerProducts.visibility = View.VISIBLE
        }
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
                cartList.add(item)
                cartViewModel.addProduct(cartList,requireContext())
            })
        dialog.setNegativeButton(
            resources.getString(R.string.cancel),
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialog.create().hide()
            })
        dialog.create().show()
    }
}
