package com.serproteam.agencetest.ui.Fragment

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.serproteam.agencetest.R
import com.serproteam.agencetest.core.ReplaceFragment
import com.serproteam.agencetest.core.TinyDB
import com.serproteam.agencetest.data.model.User
import com.serproteam.agencetest.databinding.FragmentHomeBinding
import com.serproteam.agencetest.ui.HomeActivity
import com.serproteam.agencetest.ui.viewmodel.UserViewModel
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
    var logi = "DEV"
    private val callbackManager = CallbackManager.Factory.create()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    var replaceFragment: ReplaceFragment = ReplaceFragment()
    lateinit var fragmentTransaction: FragmentTransaction
    val GOOGLEID = 100
    lateinit var tinyDB: TinyDB
    private val userViewModel: UserViewModel by viewModels()
    lateinit var nameUser: String;
    lateinit var lastNameUser: String;


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
        FacebookSdk.sdkInitialize(requireActivity());
        configInicio()
        return binding.root
    }

    private fun configInicio() {
        tinyDB = TinyDB(requireContext())

        if (!tinyDB.getString("token").toString().isEmpty()) {
            startActivity(Intent(requireContext(), HomeActivity::class.java))
        }

        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if (isLoggedIn == true) {
            Log.v(logi, "login:" + accessToken?.source?.name)
            Log.v(logi, "login")
            Log.d(logi, "facebook:user:${accessToken?.applicationId}")
            Log.d(logi, "facebook:user:${accessToken?.token}")
            Log.d(logi, "facebook:user:${accessToken?.userId}")
            var profileManager = ProfileManager.getInstance().loadCurrentProfile()
            Log.v(logi, "login:" + profileManager.toString())
        }

        binding.loginButton.setPermissions("email", "public_profile")
        // Callback registration
        binding.loginButton.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onCancel() {
                    // App code
                    Log.d(logi, "facebook:cancel")
                }

                override fun onError(exception: FacebookException) {
                    // App code
                    Log.d(logi, "facebook:error")
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.errorLoginFacebook),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onSuccess(result: LoginResult?) {
                    Log.d(logi, "facebook:success")
                    Log.d(logi, "facebook:onSuccess:${result}")
                    binding.progressBar2.visibility = View.VISIBLE
                    result?.let {
                        val token = it.accessToken
                        Log.d(logi, "facebook:user:${token.applicationId}")
                        Log.d(logi, "facebook:user:${token.token}")
                        Log.d(logi, "facebook:user:${token.userId}")
                        val credential = FacebookAuthProvider.getCredential(token.token)
                        FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Log.d(logi, "facebook:user:" + it.result?.user?.displayName)
                                    Log.d(logi, "facebook:user:" + it.result?.user?.email)

                                    var name = it.result?.user?.displayName?.split(" ")

                                    if (name!!.size > 3) {
                                        nameUser = "${name[0]} ${name[1]}"
                                        lastNameUser = "${name[2]} ${name[3]}"
                                    }
                                    if (name!!.size == 3) {
                                        nameUser = "${name[0]}"
                                        lastNameUser = "${name[1]} ${name[2]}"
                                    }

                                    var user = User(
                                        token.userId,
                                        nameUser,
                                        lastNameUser,
                                        it.result!!.user!!.email!!,
                                        token.token
                                    )
                                    userViewModel.insertUser(requireContext(), user)
                                    binding.progressBar2.visibility = View.GONE
                                    startActivity(
                                        Intent(
                                            requireContext(),
                                            HomeActivity::class.java
                                        )
                                    )
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        resources.getString(R.string.errorLoginFacebook),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                }
            })

        binding.btnGoogle.setOnClickListener {
            binding.progressBar2.visibility = View.VISIBLE
            var googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient: GoogleSignInClient =
                GoogleSignIn.getClient(requireActivity(), googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLEID)
        }


        binding.btnLogin.setOnClickListener {
            startActivity(Intent(requireContext(), HomeActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        Log.v(
            logi,
            "result code:" + resultCode.toString() + " |||| " + requestCode.toString() + "||||| " + data.toString()
        )
        if (requestCode == GOOGLEID) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                Log.v(
                    logi,
                    "account:" + account?.givenName + " apellidos:" + account?.familyName + " email:" + account?.email+ " photo:" + account?.photoUrl
                )
                if (account != null) {
                    var user = User(
                        account.id!!,
                        account.givenName!!,
                        account.familyName!!,
                        account.email!!,
                        account.idToken!!
                    )
                    userViewModel.insertUser(requireContext(), user)

                    val credential: AuthCredential =
                        GoogleAuthProvider.getCredential(account.idToken, null)

                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                binding.progressBar2.visibility = View.GONE
                                startActivity(Intent(requireContext(), HomeActivity::class.java))
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    resources.getString(R.string.errorLoginGoogle),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }

            } catch (e: ApiException) {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.errorLoginGoogle),
                    Toast.LENGTH_SHORT
                ).show()
            }

        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
            Log.v(logi, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data)
        }
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

