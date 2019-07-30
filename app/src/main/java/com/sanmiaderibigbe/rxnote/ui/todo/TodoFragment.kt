package com.sanmiaderibigbe.rxnote.ui.todo


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.sanmiaderibigbe.rxnote.R
import com.sanmiaderibigbe.rxnote.ui.di.Injection
import com.sanmiaderibigbe.rxnote.ui.login.LoginViewModel
import com.sanmiaderibigbe.rxnote.ui.repo.Status
import kotlinx.android.synthetic.main.fragment_todo.*


class TodoFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var navController: NavController

    override fun onStart() {
        super.onStart()

    }

    private fun initViewModel() {
        val viewModelFactory = Injection.provideLoginModelFactory(activity?.application!!)
        loginViewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory)[LoginViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        navController = findNavController()
        getAuthenticationState()

        btn_sign_out.setOnClickListener {
            loginViewModel.signOut()
        }
    }


    private fun getAuthenticationState() {
        loginViewModel.getUserLoginResource().observe(viewLifecycleOwner, Observer {
            when (it.status) {

                Status.LOADING -> {}
                Status.ERROR -> {}
                Status.LOADED ->
                    when(it.data){
                        LoginViewModel.AuthenticationState.AUTHENTICATED -> {}
                        LoginViewModel.AuthenticationState.UNAUTHENTICATED -> {navController.navigate(TodoFragmentDirections.actionTodoFragmentToLoginFragment())}
                    }
                Status.SUCCESS -> {}
            }
        })
//    }

    }
}
