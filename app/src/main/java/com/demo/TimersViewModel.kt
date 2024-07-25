package com.demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimersViewModel : ViewModel() {
    private val _timers = MutableLiveData<List<TimerItem>>()
    val timers: LiveData<List<TimerItem>> get() = _timers

    private val _elapsedTimeString = MutableLiveData<String>()
    val elapsedTimeString: LiveData<String> get() = _elapsedTimeString

    //khong co gia tri nao troi qua khi dong ho bam gio moi duoc khoi dong
    private var elapsedTime = 0L
    private var lapTimes = mutableListOf<TimerItem>()
    private var lapTime = mutableListOf<Timer>()
    var isRunning = false
    var isPaused = false
    private var roundCount = 1
    private var job: Job? = null

    init {
        _timers.value = lapTimes
        _elapsedTimeString.value = "00:00.00"
    }

    private fun updateElapsedTimeString() {
        _elapsedTimeString.value = formaElapsedTime(elapsedTime)
    }

    //bat dau dem thoi gian
    fun startTimer() {
        if (isPaused) {
            isPaused = false
        }
        //neu dong ho o trang thai tam dung thi elapsedTime = 0L (khi bat dau lai thoi gian dem lai tu dau)
        else {
            elapsedTime = 0L
        }
        isRunning = true
        job = viewModelScope.launch(Dispatchers.Main) {
            //neu tiep tuc sau khi dung thi dong ho dem time tu thoi diem da troi qua truoc do
            val startTime = System.currentTimeMillis() - elapsedTime
            while (isRunning) {
                elapsedTime = System.currentTimeMillis() - startTime
                updateElapsedTimeString()
                _timers.value = _timers.value //notify observers
                delay(10)
            }
        }
    }

    fun pauseTime() {
        isRunning = false
        isPaused = true
        job?.cancel()
    }

    fun resetTime() {
        isRunning = false
        isPaused = false
        lapTimes.clear()
        roundCount = 1
        elapsedTime = 0L
        job?.cancel()
        _timers.value = lapTimes
        _elapsedTimeString.value = "00:00.00"
    }

    fun addLap() {
        lapTimes.add(TimerItem(roundCount, formaElapsedTime(elapsedTime)))
        roundCount++
        _timers.value = lapTimes
    }

    private fun formaElapsedTime(elapsedTime: Long): String {
        val minutes = (elapsedTime / 1000) / 60
        val seconds = (elapsedTime / 1000) % 60
        val miliseconds = (elapsedTime % 1000) / 10
        return String.format("%02d:%02d.%02d", minutes, seconds, miliseconds)
    }

}