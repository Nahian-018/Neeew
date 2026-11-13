package com.example.workflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workflow.ui.theme.MyWorkflowTheme
import com.example.workflow.ui.viewmodel.WorkflowViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyWorkflowTheme {
                val viewModel: WorkflowViewModel = viewModel()
                WorkflowApp(viewModel = viewModel)
            }
        }
    }
}
