package com.bangkit2023.isnangram.main.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.transition.TransitionInflater
import com.bangkit2023.isnangram.R
import com.bangkit2023.isnangram.databinding.FragmentRegisterBinding
import com.bangkit2023.isnangram.main.viewmodel.ViewModelFactory
import com.bangkit2023.isnangram.utils.Result
import com.google.android.material.snackbar.Snackbar

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        setAnimation()
        setRegisterButtonEnable()
        setupTextChangeListeners()
        return binding.root
    }

    private fun setRegisterButtonEnable() {
        val nameState = binding.edRegisterName.text?.isNotEmpty() == true
        val emailState = binding.edRegisterEmail.text?.isNotEmpty() == true
        val passwdState = binding.edRegisterPassword.text?.isNotEmpty() == true
        binding.btnRegister.isEnabled = nameState && emailState && passwdState
    }

    private fun setAnimation() {
        val name = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val password =
            ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(name, email, password)
        }

        AnimatorSet().apply {
            playSequentially(together, button)
            start()
        }
    }

    private fun setupTextChangeListeners() {
        binding.edRegisterName.addTextChangedListener(textChangeListener)
        binding.edRegisterEmail.addTextChangedListener(textChangeListener)
        binding.edRegisterPassword.addTextChangedListener(textChangeListener)
    }

    private val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            setRegisterButtonEnable()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActions()
    }

    private fun setActions() {
        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            handleRegister(name, email, password)
        }

        binding.btnLogin.setOnClickListener {
            backToLoginPage()
        }
    }

    private fun backToLoginPage() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.auth_container, LoginFragment(), LoginFragment::class.java.simpleName)
            addSharedElement(binding.edRegisterEmail, "email")
            addSharedElement(binding.edRegisterPassword, "password")
            addSharedElement(binding.btnRegister, "button")
            commit()
        }
    }

    private fun handleRegister(name: String, email: String, password: String) {
        authViewModel.userRegister(name, email, password).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    binding.progressCircular.visibility = View.GONE
                    Toast.makeText(requireContext(), "Register success", Toast.LENGTH_SHORT).show()
                    (activity as AuthActivity).navigateToLogin()
                }
                is Result.Error -> {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.registration_error_message),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is Result.Loading -> {
                    // Show loading state if needed
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
