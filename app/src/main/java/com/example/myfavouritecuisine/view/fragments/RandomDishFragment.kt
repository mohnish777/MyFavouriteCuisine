package com.example.myfavouritecuisine.view.fragments

import android.app.Dialog
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.myfavouritecuisine.R
import com.example.myfavouritecuisine.databinding.DialogCustomProgressBinding
import com.example.myfavouritecuisine.databinding.FragmentRandomDishBinding
import com.example.myfavouritecuisine.model.entities.FavDish
import com.example.myfavouritecuisine.model.entities.Recipes
import com.example.myfavouritecuisine.utils.Constants
import com.example.myfavouritecuisine.viewmodel.FavDishViewModel
import com.example.myfavouritecuisine.viewmodel.RandomDishViewModel
import kotlinx.coroutines.launch

class RandomDishFragment : Fragment() {
    private var _binding: FragmentRandomDishBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RandomDishViewModel by viewModels { RandomDishViewModel.Factory }
    private val favDishViewModel: FavDishViewModel by viewModels { FavDishViewModel.Factory }

    private var randomDish: Recipes? = null
    private var isFavorited = false
    private var mProgressDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRandomDishBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipeRefresh()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getRandomRecipeFromApi()
                viewModel.randomDishState.collect {
                    when(it) {
                        is Constants.UiState.Success -> {
                            setRandomDishUi(it.data.recipes.first())
                            binding.swipeRefreshLayout.isRefreshing = false
                            mProgressDialog?.dismiss()
                            binding.rlRandomDishDetailMain.visibility = View.VISIBLE
                            binding.tvNoDataFound.visibility = View.GONE
                        }

                        is Constants.UiState.Error -> {
                            // Show error message
                            binding.swipeRefreshLayout.isRefreshing = false
                            mProgressDialog?.dismiss()
                            binding.rlRandomDishDetailMain.visibility = View.GONE
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.tvNoDataFound.text = it.message
                        }
                        Constants.UiState.Idle -> {
                            // Do nothing
                            binding.rlRandomDishDetailMain.visibility = View.GONE
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.tvNoDataFound.text = getString(R.string.no_data_found)
                        }
                        Constants.UiState.Loading -> {
                            // Show progress bar
                            binding.rlRandomDishDetailMain.visibility = View.GONE
                            showCustomDialog()

                        }
                    }
                }
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getRandomRecipeFromApi()
        }
    }

    fun setRandomDishUi(recipe: Recipes) {
        isFavorited = false
        randomDish = recipe
        updateFavouriteIcon()
        Glide
            .with(requireActivity())
            .load(recipe.image)
            .centerCrop()
            .into(binding.ivDishImage)
        binding.tvTitle.text = recipe.title
        binding.tvType.text = recipe.dishTypes.first()
        binding.tvCategory.text = if (recipe.cuisines.isNotEmpty()) recipe.cuisines.first() else "No Category"
        binding.tvIngredients.text = recipe.extendedIngredients.joinToString(separator ="\n") { it.original }
        binding.tvCookingDirection.text = Html.fromHtml(recipe.instructions, Html.FROM_HTML_MODE_COMPACT)
        binding.tvCookingTime.text = resources.getString(R.string.lbl_estimate_cooking_time, recipe.readyInMinutes.toString())
        binding.ivFavoriteDish.setOnClickListener {
            favouriteDish()
        }
    }

    fun showCustomDialog() {
        if(!binding.swipeRefreshLayout.isRefreshing) {
            mProgressDialog = Dialog(requireActivity())
            val binding: DialogCustomProgressBinding = DialogCustomProgressBinding.inflate(layoutInflater)
            mProgressDialog?.setContentView(binding.root)
            mProgressDialog?.show()
        }
    }

    fun favouriteDish() {
        if(isFavorited) {
            Toast.makeText(requireContext(), getString(R.string.already_added_to_fav, randomDish?.title ?: "Dish"), Toast.LENGTH_SHORT).show()
            return
        }
        isFavorited = true
        randomDish?.let { recipe ->
            val favDish = FavDish(
                image = recipe.image,
                imageSource = Constants.DISH_IMAGE_SOURCE_ONLINE,
                title = recipe.title,
                type = recipe.dishTypes.first(),
                category = if (recipe.cuisines.isNotEmpty()) recipe.cuisines.first() else "No Category",
                ingredients = recipe.extendedIngredients.joinToString(separator ="\n") { it.original },
                cookingTime = recipe.readyInMinutes.toString(),
                directionToCook = recipe.instructions,
                favoriteDish = true
            )
            favDishViewModel.insert(favDish)
            updateFavouriteIcon()
        }
    }
    fun updateFavouriteIcon() {
        if(isFavorited) {
            binding.ivFavoriteDish.setImageResource(R.drawable.ic_favourite_dish)
        } else {
            binding.ivFavoriteDish.setImageResource(R.drawable.ic_favorite_unselected)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
