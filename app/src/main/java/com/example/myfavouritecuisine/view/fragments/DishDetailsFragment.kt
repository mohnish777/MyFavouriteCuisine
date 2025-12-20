package com.example.myfavouritecuisine.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.myfavouritecuisine.R
import com.example.myfavouritecuisine.databinding.FragmentDishDetailsBinding

class DishDetailsFragment : Fragment() {
    val args: DishDetailsFragmentArgs by navArgs()

    private var mBinding: FragmentDishDetailsBinding?  = null
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
        args.dishDetails.let { favDish ->
            mBinding?.let {
                try {
                    Glide
                        .with(requireActivity())
                        .load(favDish?.image)
                        .centerCrop()
                        .into(it.ivDishImage)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                mBinding?.tvTitle?.text = favDish?.title
                mBinding?.tvType?.text = favDish?.type
                mBinding?.tvCategory?.text = favDish?.category
                mBinding?.tvIngredients?.text = favDish?.ingredients
                mBinding?.tvCookingDirection?.text = resources.getString(
                    R.string.lbl_estimate_cooking_time,
                    favDish?.cookingTime.toString()
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }
}
