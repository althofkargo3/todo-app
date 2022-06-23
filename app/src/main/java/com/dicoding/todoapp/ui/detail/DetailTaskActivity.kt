package com.dicoding.todoapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.ui.list.TaskActivity
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        detailViewModel = ViewModelProvider(this, factory).get(DetailTaskViewModel::class.java)

        //TODO 11 : Show detail task and implement delete action
        val tvTitle: TextView = findViewById(R.id.detail_ed_title)
        val tvDesc: TextView = findViewById(R.id.detail_ed_description)
        val tvDueDate: TextView = findViewById(R.id.detail_ed_due_date)
        val btnDelete: Button = findViewById(R.id.btn_delete_task)

        val id = intent.getIntExtra(TASK_ID, 0)

        detailViewModel.setTaskId(id)
        detailViewModel.task.observe(this) {
            tvTitle.text = it?.title
            tvDesc.text = it?.description
            tvDueDate.text =
                it?.dueDateMillis?.let { it1 -> DateConverter.convertMillisToString(it1) }
        }

        btnDelete.setOnClickListener {
            startActivity(Intent(this, TaskActivity::class.java))
            detailViewModel.deleteTask()
        }

    }
}