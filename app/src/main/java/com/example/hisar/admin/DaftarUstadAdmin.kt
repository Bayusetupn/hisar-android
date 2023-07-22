package com.example.hisar.admin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hisar.databinding.FragmentDaftarUstadAdminBinding

class DaftarUstadAdmin : Fragment() {

    private var _binding: FragmentDaftarUstadAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDaftarUstadAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.add.setOnClickListener{
            startActivity(Intent(context,TambahUstad::class.java))
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}