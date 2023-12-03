package com.sgo.SmyrnaGlobalOutreach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.sgo.SmyrnaGlobalOutreach.databinding.ActivityHomeBinding
import java.lang.Math.abs

class Home : AppCompatActivity() {

    private lateinit var  viewPager2: ViewPager2
    private lateinit var handler : Handler
    private lateinit var imageList:ArrayList<Int>
    private lateinit var adapter: ImageAdapter

    private lateinit var serviceRecyclerView: RecyclerView
    private lateinit var goalsRecyclerView: RecyclerView

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setUpTransformer()

        // Service recycler view
        val imageList = listOf(
            R.drawable.imagefarming,
            R.drawable.imagevenue,
            R.drawable.imagerehab
        )


        // service recycler view
        serviceRecyclerView = findViewById(R.id.serviceRecyclerView)
        serviceRecyclerView.setHasFixedSize(true)
        serviceRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val serviceAdapter = ServiceAdapter(imageList, object : ServiceAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> startActivity(Intent(this@Home, Produce::class.java))
                    1 -> startActivity(Intent(this@Home, OtherServices::class.java))
                    2 -> startActivity(Intent(this@Home, Rehab::class.java))
                    // Add more cases as needed for additional items
                }
            }
        })
        serviceRecyclerView.adapter = serviceAdapter

        val goalsImageList: MutableList<Int> = ArrayList()
        goalsImageList.add(R.drawable.home1)
        goalsImageList.add(R.drawable.home2)
        goalsImageList.add(R.drawable.home3)

        // goals recycler view
        goalsRecyclerView = findViewById(R.id.goalsRecyclerView)
        goalsRecyclerView.setHasFixedSize(true)
        goalsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        val goalsAdapter = GoalsAdapter(goalsImageList)
        goalsRecyclerView.adapter = goalsAdapter

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable , 2000)
            }
        })

        setupBottomNavigation()

    }


    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.farmBottomNav -> startActivity(Produce::class.java)
                R.id.VenueBottomNav -> startActivity(OtherServices::class.java)
                R.id.rehabBottomNav -> startActivity(Rehab::class.java)
                else -> {
                    // Handle other menu items if needed
                }
            }
            true
        }
    }

    private fun startActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        // Optionally, if you want to finish the current activity when starting a new one
        // finish()
    }


    override fun onPause() {
        super.onPause()

        handler.removeCallbacks(runnable)
    }

    override fun onResume()
    {
        super.onResume()

        handler.postDelayed(runnable , 2000)
    }

    private val runnable = Runnable {
        viewPager2.currentItem = viewPager2.currentItem + 1
    }

    private fun setUpTransformer(){
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }

        viewPager2.setPageTransformer(transformer)
    }

    private fun init()
    {
        viewPager2 = findViewById(R.id.viewPager2)
        handler = Handler(Looper.myLooper()!!)
        imageList = ArrayList()

        imageList.add(R.drawable.main)
        imageList.add(R.drawable.donate)
        imageList.add(R.drawable.rehab)



        adapter = ImageAdapter(imageList, viewPager2)

        viewPager2.adapter = adapter
        viewPager2.offscreenPageLimit = 3
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

     }
}
