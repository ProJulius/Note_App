package com.example.note_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.note_app.databinding.ActivityMainBinding
import com.example.note_app.view.calendar_page.CalendarFragment
import com.example.note_app.view.home_page.HomeFragment
import com.example.note_app.view.list_page.ListFragment
import com.example.note_app.view.transformer.DepthPageTransformer

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager2()
        setupMeowBottomNavigation()

        binding.viewPager2.setCurrentItem(1, false)
        binding.viewPager2.setPageTransformer(DepthPageTransformer())
    }
    private fun setupMeowBottomNavigation() {
        binding.bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.icon_calendar))
        binding.bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.icon_home))
        binding.bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.icon_list))

        binding.bottomNavigation.setOnClickMenuListener {
            binding.viewPager2.setCurrentItem(it.id - 1, false)
        }
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.bottomNavigation.show(position + 1, true)
            }
        })
        binding.bottomNavigation.setOnShowListener { }
    }
    private fun setupViewPager2() {
        binding.viewPager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 3

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> CalendarFragment()
                    1 -> HomeFragment()
                    2 -> ListFragment()
                    else -> HomeFragment()
                }
            }
        }
    }
}