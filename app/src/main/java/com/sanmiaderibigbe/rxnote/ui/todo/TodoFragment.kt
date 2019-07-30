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


class TodoFragment : Fragment() {


    private lateinit var loginViewModel: LoginViewModel

    override fun onStart() {
        super.onStart()
        val viewModelFactory = Injection.provideLoginModelFactory(activity?.application!!)
        loginViewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory)[LoginViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        getAuthenticationState(findNavController())
    }

    private fun getAuthenticationState(findNavController: NavController) {
        loginViewModel.getUserLoginResource().observe(viewLifecycleOwner, Observer {
            when (it.data) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {}
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> {findNavController.navigate(TodoFragmentDirections.actionTodoFragmentToLoginFragment())}
                null -> {}
            }
        })
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
    }
}
