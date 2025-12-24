package com.example.myfavouritecuisine.view.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.myfavouritecuisine.R
import com.example.myfavouritecuisine.databinding.FragmentDishDetailsBinding
import com.example.myfavouritecuisine.viewmodel.FavDishViewModel
import kotlinx.coroutines.launch

class DishDetailsFragment : Fragment() {
    val args: DishDetailsFragmentArgs by navArgs()
    private val viewModel: FavDishViewModel by viewModels { FavDishViewModel.Factory }

    private var mBinding: FragmentDishDetailsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentDishDetailsBinding.inflate(inflater, container, false)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.dishDetails?.let { favDish ->
            viewModel.observeFavoriteStatus(favDish.id)
            mBinding?.let {
                try {
                    Glide
                        .with(requireActivity())
                        .load(favDish.image)
                        .centerCrop()
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable?>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable?>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                resource?.let {
                                    Palette.from(resource.toBitmap()).generate() { palette ->
                                        val intColor = palette?.vibrantSwatch?.rgb ?: 0
                                        mBinding?.rlDishDetail?.setBackgroundColor(intColor)
                                    }
                                }
                                return false
                            }
                        })
                        .into(it.ivDishImage)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                it.tvTitle.text = favDish.title
                it.tvType.text = favDish.type
                it.tvCategory.text = favDish.category
                it.tvIngredients.text = favDish.ingredients
                it.tvCookingDirection.text = resources.getString(
                    R.string.lbl_estimate_cooking_time,
                    favDish.cookingTime
                )

                it.ivFavoriteDish.setOnClickListener {
                    viewModel.updateDish(
                        favDish.copy(
                            favoriteDish = !viewModel.favouriteDishDetailState.value
                        )
                    )
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favouriteDishDetailState.collect {
                mBinding?.ivFavoriteDish?.setImageResource(
                    if (it) R.drawable.ic_favourite_dish else R.drawable.ic_favorite_unselected
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}
