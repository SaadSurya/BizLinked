package com.application.lumaque.bizlinked.fragments.bizlinked

import android.content.Context
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast

import com.application.lumaque.bizlinked.R
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment
import com.bumptech.glide.Glide

import java.util.ArrayList
import butterknife.BindView
import org.jetbrains.annotations.NotNull

class ImageSliderFragment : BaseFragment() {
    private var imageList = ArrayList<String>()

    private lateinit var vpImageSlider: ViewPager
    private lateinit var menuButton: ImageButton
    private lateinit var popup: PopupMenu

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
        init(rootView)
        container?.let {
            if (imageList.isNotEmpty()) {
                val adapter = ImagePagerAdapter(activityReference, imageList)
                vpImageSlider.adapter = adapter
            }
        }


    }

    private fun init(rootView: View?) {
        findViewById(rootView)
        val popup = PopupMenu(activityReference, menuButton)
        val menu = popup.menu
        popup.menuInflater.inflate(R.menu.menu_image_slide, menu)
        menuButton.setOnClickListener { _ ->
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.info -> {
                        Toast.makeText(activityReference, "info Clicked", Toast.LENGTH_SHORT).show()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.delete_image -> {
                        Toast.makeText(activityReference, "delete Clicked", Toast.LENGTH_SHORT).show()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.change_image -> {
                        Toast.makeText(activityReference, "changeClicked", Toast.LENGTH_SHORT).show()
                        return@setOnMenuItemClickListener true
                    }
                    else -> {
                        Toast.makeText(activityReference, "nothing Clicked", Toast.LENGTH_SHORT).show()
                        return@setOnMenuItemClickListener true
                    }
                }
            }
            popup.show()
        }
    }

    private fun findViewById(rootView: View?) {
        rootView?.let {
            vpImageSlider = it.findViewById(R.id.vp_slider)
            menuButton = it.findViewById(R.id.menu_button)
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
