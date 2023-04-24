package com.example.waterplants.ui.system

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.waterplants.R
import com.example.waterplants.databinding.FragmentSystemBinding
import java.time.Duration
import java.time.Instant
import java.time.temporal.TemporalAmount

class SystemFragment : Fragment() {

    private var _binding: FragmentSystemBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val systemViewModel =
            ViewModelProvider(this).get(SystemViewModel::class.java)

        _binding = FragmentSystemBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Bind data fields to textViews
        systemViewModel.systemWaterLevel.observe(viewLifecycleOwner) {
            binding.textSystemWaterLevel.text = "${getString(R.string.system_water_level)} $it"
        }

        systemViewModel.systemPlants.observe(viewLifecycleOwner) {
            binding.textSystemHose1.text = "${it[0].pin?.minus(3)}"
            binding.textSystemHose2.text = "${it[1].pin?.minus(3)}"
            binding.textSystemHose3.text = "${it[2].pin?.minus(3)}"

            binding.textSystemNext1.text = "${it[0].hourOfDay ?: getString(R.string.system_empty)}"
            binding.textSystemNext2.text = "${it[1].hourOfDay ?: getString(R.string.system_empty)}"
            binding.textSystemNext3.text = "${it[2].hourOfDay ?: getString(R.string.system_empty)}"
            if (it[0].hourOfDay == null || it[0].hourOfDay!! < 0
                || it[0].hourOfDay!! > 23 || it[0].intervalDays == null) {
                binding.textSystemNext1.text = "${getString(R.string.system_empty)}"
            } else {
                val h : Int = it[0].hourOfDay!!
                val d : Int = it[0].intervalDays!!
                var now = Instant.now()
                //var daysElapsed: Long = now.epochSecond / (60*60*24)
                var hoursElapsed: Long = now.epochSecond / (60*60)
                var daysElapsed: Long = hoursElapsed / 24
                var cyclesElapsed: Long = daysElapsed / d
                var daysToCycleStart: Long = cyclesElapsed * d
                //var hoursToCycleStart: Long = daysToCycleStart * 24
                var daysIntoCycle: Long = daysElapsed % d
                var hoursIntoDay: Long = hoursElapsed % 24
                if (daysIntoCycle == 0L && hoursIntoDay < h) {
                    daysToCycleStart += d
                }
                val next = Instant.ofEpochSecond((daysToCycleStart * 24 + h) * 60 * 60)
                if (next < now)
                    throw Exception("Duration error!")
                val difference = Duration.between(now, next).toHours()
                val days = difference / 24
                val hours = difference % 24
                if (days >= d)
                    throw Exception("Wrong calculation of days!")
                binding.textSystemNext1.text = "$days days, $hours hours"
            }

            binding.textSystemAmount1.text = "${it[0].amount ?: getString(R.string.system_empty)}"
            binding.textSystemAmount2.text = "${it[1].amount ?: getString(R.string.system_empty)}"
            binding.textSystemAmount3.text = "${it[2].amount ?: getString(R.string.system_empty)}"

            binding.textSystemInterval1.text = "${it[0].intervalDays ?: getString(R.string.system_empty)}"
            binding.textSystemInterval2.text = "${it[1].intervalDays ?: getString(R.string.system_empty)}"
            binding.textSystemInterval3.text = "${it[2].intervalDays ?: getString(R.string.system_empty)}"

            if (it[0].watered == null) {
                binding.textSystemWatered1.text = "${getString(R.string.system_empty)}"
            } else {
                if (it[0].watered!!) {
                    binding.textSystemWatered1.text = "${getString(R.string.yes)}"
                } else {
                    binding.textSystemWatered1.text = "${getString(R.string.no)}"
                }
            }
            if (it[1].watered == null) {
                binding.textSystemWatered2.text = "${getString(R.string.system_empty)}"
            } else {
                if (it[1].watered!!) {
                    binding.textSystemWatered2.text = "${getString(R.string.yes)}"
                } else {
                    binding.textSystemWatered2.text = "${getString(R.string.no)}"
                }
            }
            if (it[2].watered == null) {
                binding.textSystemWatered3.text = "${getString(R.string.system_empty)}"
            } else {
                if (it[2].watered!!) {
                    binding.textSystemWatered3.text = "${getString(R.string.yes)}"
                } else {
                    binding.textSystemWatered3.text = "${getString(R.string.no)}"
                }
            }
        }

        // Buttons
        val buttonUpdate: Button = binding.buttonSystemUpdate
        buttonUpdate.setOnClickListener { systemViewModel.askForSystemStatus() }

        val buttonReceive: Button = binding.buttonSystemReceive
        // pin, intervalDays, amount, nextHr, watered
        buttonReceive.setOnClickListener { systemViewModel.processMessage("a4,3,20,11,0,5,7,30,12,1,6,7,40,13,1,50") }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}