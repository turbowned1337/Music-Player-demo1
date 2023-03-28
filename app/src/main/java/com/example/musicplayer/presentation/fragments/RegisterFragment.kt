package com.example.musicplayer.presentation.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentRegisterBinding
import com.example.musicplayer.domain.entities.User
import com.example.musicplayer.presentation.LoginViewModel
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val loginViewModel by sharedViewModel<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.root.applyInsetter {
                type(navigationBars = true) {
                    margin()
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val inputFields = arrayOf(
            binding.loginEditText,
            binding.emailEditText,
            binding.passwordEditText,
            binding.repeatPasswordEditText
        )

        loginViewModel.validator.setValidationListeners(inputFields)

        binding.loginButton.setOnClickListener {
            loginViewModel.validator.validateFields(inputFields)
            if (loginViewModel.validator.hasNoErrors(inputFields)) {
                val data = User(
                    0,
                    binding.loginEditText.text.toString(),
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )
                loginViewModel.registerUser(data)
                findNavController().navigate(R.id.mainScreenFragment)
            }
        }

        binding.newAccountButton.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        binding.registerCheckBox.setOnClickListener {
            loginViewModel.setRegisterButtonEnabled(binding.registerCheckBox.isChecked)
        }

        loginViewModel.isRegisterButtonEnabled.observe(viewLifecycleOwner, {
            binding.loginButton.isEnabled = it
            binding.registerCheckBox.isChecked = it
        })
    }
}
