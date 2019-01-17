package com.application.lumaque.bizlinked.fragments.bizlinked

import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.application.lumaque.bizlinked.R
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment
import com.bumptech.glide.Glide

import java.util.ArrayList
import butterknife.BindView
import org.jetbrains.annotations.NotNull

class ImageSliderFragment : BaseFragment() {
    private var imageList = ArrayList<String>()

    private lateinit var vpImageSlider: ViewPager

    override fun onCustomBackPressed() {
        activityReference.onPageBack()
    }

    override fun getMainLayout(): Int {
        return R.layout.fragment_image_slider
    }

    override fun onFragmentViewReady(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, rootView: View?) {
        val bundle: Bundle = arguments!!
        bundle.let {
            imageList = it.getStringArrayList("IMAGELINKS")
        }
        container?.let {
            vpImageSlider = rootView!!.findViewById(R.id.vp_slider)
            if (imageList.isNotEmpty()) {
                val adapter = ImagePagerAdapter(activityReference, imageList)
                vpImageSlider.adapter = adapter
            }
        }

    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}

class ImagePagerAdapter(private var mContext: Context, private val imageLinks: List<String>)
    : PagerAdapter() {
    private val mLayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return imageLinks.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = mLayoutInflater.inflate(R.layout.image_layout, container, false)
        val imageView = itemView.findViewById(R.id.pager_image) as ImageView
        Glide.with(mContext).load(imageLinks[position]).into(imageView)



        container.addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}
