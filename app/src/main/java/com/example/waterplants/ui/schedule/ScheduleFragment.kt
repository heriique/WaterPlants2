package com.example.waterplants.ui.schedule

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.waterplants.databinding.FragmentScheduleBinding
import com.example.waterplants.model.Model

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val scheduleViewModel =
            ViewModelProvider(this).get(ScheduleViewModel::class.java)

        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root



        val gridView = binding.gridViewSchedule
        val tileAdapter = TileAdapter(this.requireContext(), Model.getInstance(null)!!.appPlants,
            Model.getInstance(null)!!.appChosenPlants, scheduleViewModel.selectedHose)
        gridView.adapter = tileAdapter
        gridView.setOnItemClickListener{adapterView, parent, position, l->
            run {
                Toast.makeText(this.requireContext(), "Click on $position", Toast.LENGTH_SHORT)
                    .show()
                scheduleViewModel.select(position)
                tileAdapter.notifyDataSetChanged()
            }
        }

        val buttonHose = arrayListOf<Button>()
        buttonHose.add(binding.buttonHose1)
        buttonHose.add(binding.buttonHose2)
        buttonHose.add(binding.buttonHose3)
        for (i in 0..2) {
            buttonHose[i].setOnClickListener {
                if (scheduleViewModel.selectedHose.value != i) {
                    scheduleViewModel.selectedHose.value = i
                    for (j in 0..2) {
                        if (i == j)
                            buttonHose[j].setBackgroundColor(Color.GREEN)
                        else
                            buttonHose[j].setBackgroundColor(0xFF6200EE.toInt()) //purple_500
                    }
                    tileAdapter.notifyDataSetChanged()
                }

            }
        }
        buttonHose[0].setBackgroundColor(Color.GREEN)

        binding.buttonScheduleSend.setOnClickListener {
            scheduleViewModel.write()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}