package com.serproteam.agencetest.ui.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.serproteam.agencetest.R
import com.serproteam.agencetest.core.ReplaceFragment
import com.serproteam.agencetest.databinding.FragmentHomeBinding
import org.json.JSONException
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var logi = "walletLog"
    val EMAIL = "raulrosado91@gmail.com"
    lateinit var callbackManager : CallbackManager
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    var replaceFragment: ReplaceFragment = ReplaceFragment()
    lateinit var fragmentTransaction :FragmentTransaction

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        configInicio()
        return binding.root
    }

    private fun configInicio() {
        var callbackManager = CallbackManager.Factory.create();

        binding.loginButton.setReadPermissions(Arrays.asList(EMAIL))
        // Callback registration
//        binding.loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
//            override fun onSuccess(loginResult: LoginResult?) {
//                val userId = loginResult?.accessToken?.userId
//                Log.d(logi, "onSuccess: userId $userId")
//
//                val bundle = Bundle()
//                bundle.putString("fields", "id, email, first_name, last_name, gender,age_range")
//
//
//                //Graph API to access the data of user's facebook account
//                val request = GraphRequest.newMeRequest(
//                    loginResult?.accessToken
//                ) { fbObject, response ->
//                    Log.v(logi, response.toString())
//
//
//                    //For safety measure enclose the request with try and catch
//                    try {
//
//                        Log.d(logi, "onSuccess: fbObject $fbObject")
//
//                        val firstName = fbObject.getString("first_name")
//                        val lastName = fbObject.getString("last_name")
//                        val gender = fbObject.getString("gender")
//                        val email = fbObject.getString("email")
//
//                        Log.d(logi, "onSuccess: firstName $firstName")
//                        Log.d(logi, "onSuccess: lastName $lastName")
//                        Log.d(logi, "onSuccess: gender $gender")
//                        Log.d(logi, "onSuccess: email $email")
//
//                    } //If no data has been retrieve throw some error
//                    catch (e: JSONException) {
//
//                    }
//
//                }
//                //Set the bundle's data as Graph's object data
//                request.setParameters(bundle)
//
//                //Execute this Graph request asynchronously
//                request.executeAsync()
//
//            }
//
//            override fun onCancel() {
//                Log.d(logi, "onCancel: called")
//            }
//
//            override fun onError(error: FacebookException?) {
//                Log.d(logi, "onError: called")
//            }
//        })

        binding.btnGoogle.setOnClickListener {
            replaceFragment.replace(ListFragment(),fragmentTransaction)
        }
        binding.btnLogin.setOnClickListener {
            replaceFragment.replace(ListFragment(),fragmentTransaction)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        Log.v(logi, "onActivityResult:"+ requestCode + ":" + resultCode + ":" + data)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}