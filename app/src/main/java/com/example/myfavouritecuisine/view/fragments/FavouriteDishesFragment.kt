package com.example.myfavouritecuisine.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myfavouritecuisine.R
import com.example.myfavouritecuisine.databinding.FragmentFavouriteDishesBinding
import com.example.myfavouritecuisine.model.entities.FavDish
import com.example.myfavouritecuisine.utils.Constants
import com.example.myfavouritecuisine.view.activities.MainActivity
import com.example.myfavouritecuisine.view.adapter.FavDishAdapter
import com.example.myfavouritecuisine.viewmodel.FavDishViewModel
import kotlinx.coroutines.launch

class FavouriteDishesFragment : Fragment() {

    private var _binding: FragmentFavouriteDishesBinding? = null

    private val binding get() = _binding!!

    private val viewModel: FavDishViewModel by viewModels {
        FavDishViewModel.Factory
    }

    private lateinit var favDishAdapter: FavDishAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavouriteDishesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadFavouriteDishes()
        favDishAdapter = FavDishAdapter(this)
        binding.rvFavouriteDishesList.adapter = favDishAdapter
        observeAllDishList()
    }

    fun observeAllDishList() {
        binding.rvFavouriteDishesList.layoutManager = GridLayoutManager(requireContext(), 2)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favouriteDishListState.collect {
                    when (it) {
                        is Constants.UiState.Error -> {
                            // Show error message
                        }

                        Constants.UiState.Idle -> {
                            // Do nothing
                        }

                        Constants.UiState.Loading -> {
                            // Show progress bar
                        }

                        is Constants.UiState.Success -> {
                            if (it.data.isNotEmpty()) {
                                binding.tvNoFavouriteDishesAvailable.visibility = View.GONE
                                binding.rvFavouriteDishesList.visibility = View.VISIBLE
                                favDishAdapter.dishList(it.data)
                            } else {
                                binding.tvNoFavouriteDishesAvailable.visibility = View.VISIBLE
                                binding.rvFavouriteDishesList.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
    }

    fun dishDetails(favDish: FavDish) {
        if(findNavController().currentDestination?.id == R.id.navigation_favourite_dishes) {
            findNavController().navigate(
                FavouriteDishesFragmentDirections.actionNavigationFavouriteDishesToNavigationDishDetails(
                    dishDetails = favDish
                )
            )
        }

        if(requireActivity() is MainActivity) {
            (activity as MainActivity).hideBottomNavigationView()
        }
    }

    override fun onResume() {
        super.onResume()
        if(requireActivity() is MainActivity) {
            (activity as MainActivity).showBottomNavigationView()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
