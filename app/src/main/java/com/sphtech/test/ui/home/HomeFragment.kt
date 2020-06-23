package com.sphtech.test.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sphtech.shared.core.result.EventObserver
import com.sphtech.shared.util.viewModelProvider
import com.sphtech.test.base.BaseFragment
import com.sphtech.test.databinding.FragmentHomeBinding
import javax.inject.Inject

class HomeFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentHomeBinding

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var dataAmountAdapter: DataAmountAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = viewModelProvider(viewModelFactory)
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            viewModel = homeViewModel
            lifecycleOwner = this@HomeFragment
        }

        bindRecyclerData()

        homeViewModel.callDataAmountAPI()

        homeViewModel.dataAmountAPIObserver.observe(viewLifecycleOwner, Observer {
            dataAmountAdapter.dataChanged(it)
        })

        homeViewModel.loading.observe(viewLifecycleOwner, EventObserver {
            binding.strDataAmount.isRefreshing = it
        })

        binding.strDataAmount.setOnRefreshListener {
            homeViewModel.allList.clear()
            homeViewModel.callDataAmountAPI()
        }

        return binding.root
    }

    private fun bindRecyclerData() {
        dataAmountAdapter = DataAmountAdapter { _, _ ->
        }
        binding.rvDataAmount.adapter = dataAmountAdapter
    }
}
