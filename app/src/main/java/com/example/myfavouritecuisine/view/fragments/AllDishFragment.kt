package com.example.myfavouritecuisine.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import com.example.myfavouritecuisine.databinding.FragmentAllDishesBinding
import com.example.myfavouritecuisine.model.entities.FavDish
import com.example.myfavouritecuisine.utils.Constants
import com.example.myfavouritecuisine.view.activities.AddUpdateDishActivity
import com.example.myfavouritecuisine.view.activities.MainActivity
import com.example.myfavouritecuisine.view.adapter.FavDishAdapter
import com.example.myfavouritecuisine.viewmodel.FavDishViewModel
import kotlinx.coroutines.launch

class AllDishFragment : Fragment() {

    private var _binding: FragmentAllDishesBinding? = null

    private val viewModel: FavDishViewModel by viewModels { FavDishViewModel.Factory }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var favDishAdapter: FavDishAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllDishesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAllDishList()
        favDishAdapter = FavDishAdapter(this)
        binding.rvDishesList.adapter = favDishAdapter
    }

    fun observeAllDishList() {
        binding.rvDishesList.layoutManager = GridLayoutManager(requireContext(), 2)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allDishListState.collect {
                    when (it) {
                        Constants.UiState.Idle -> {
                            // Do nothing
                        }
                        Constants.UiState.Loading -> {
                            // Show progress bar
                        }
                        is Constants.UiState.Success -> {
                            // Hide progress bar and show data
                            if (it.data.isNotEmpty()) {
                                it.data.forEach { dish ->
                                    Log.d("mohnishUriCheck", "observeAllDishList: ${dish.title}")
                                }
                                binding.rvDishesList.visibility = View.VISIBLE
                                binding.tvNoDishesAddedYet.visibility = View.GONE
                                favDishAdapter.dishList(it.data)
                            } else {
                                binding.rvDishesList.visibility = View.GONE
                                binding.tvNoDishesAddedYet.visibility = View.VISIBLE
                            }
                        }
                        is Constants.UiState.Error -> {
                            // Hide progress bar and show error
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_dish -> {
                startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun dishDetails(favDish: FavDish) {
        findNavController().navigate(AllDishFragmentDirections.actionNavigationAllDishesToNavigationDishDetails(
            dishDetails = favDish
        ))
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
