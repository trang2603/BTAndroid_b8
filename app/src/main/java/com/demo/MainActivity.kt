package com.demo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RecycleViewAdapter
    private val viewModel: TimersViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = RecycleViewAdapter(viewModel.timers.value ?: listOf())
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = adapter

        viewModel.timers.observe(this, Observer { timers ->
            adapter.updateTimers(timers)
        })

        viewModel.elapsedTimeString.observe(this, Observer { elapsedTime ->
            binding.elapsedTime.text = elapsedTime
        })

        binding.btnStartPause.setOnClickListener {
            if (viewModel.isRunning) {
                viewModel.pauseTime()
                binding.btnStartPause.text = "Tiếp tục"
                binding.btnLapReset.text = "Đặt lại"
            } else {
                viewModel.startTimer()
                binding.btnStartPause.text = "Tạm dừng"
                binding.btnLapReset.text = "Vòng"
            }
        }
        binding.btnLapReset.setOnClickListener {
            if (viewModel.isRunning) {
                viewModel.addLap()
            } else {
                viewModel.resetTime()
                binding.btnStartPause.text = "Bắt đầu"
                binding.btnLapReset.text = "Vòng"
            }
        }
    }
}