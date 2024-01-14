package com.margotjonathan.todo.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.margotjonathan.todo.R
import com.margotjonathan.todo.data.Api
import com.margotjonathan.todo.data.TasksListViewModel
import com.margotjonathan.todo.databinding.FragmentTaskListBinding
import com.margotjonathan.todo.detail.DetailActivity
import com.margotjonathan.todo.user.UserActivity
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.reflect.typeOf
import android.Manifest
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "taskList"
private const val ARG_PARAM2 = "adapter"

/**
 * A simple [Fragment] subclass.
 * Use the [TaskListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    /*
    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    */
    private val viewModel: TasksListViewModel by viewModels()
    private val adapterListener : TaskListListener = object : TaskListListener {
        override fun onClickDelete(task: Task) {
            viewModel.remove(task)
        }
        override fun onClickEdit(task: Task) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("task", task)
            editTask.launch(intent)
        }
        override fun onLongClickTask(task: Task) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, task.title + "\n" + task.description)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }
    private val adapter = TaskListAdapter(adapterListener)
    private val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // dans cette callback on récupèrera la task et on l'ajoutera à la liste
        val task = result.data?.getSerializableExtra("task") as Task?
        if (task != null) {
            viewModel.add(task)
        }
    }
    private val editTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task?
        if (task != null) {
            viewModel.edit(task)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // La permission a été accordée, vous pouvez maintenant accéder au stockage externe
                // ... Ajoutez votre logique pour sélectionner une image ici ...
            } else {
                // La permission a été refusée
                // ... Ajoutez votre logique pour gérer le refus ici ...
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }*/
    }

    /*
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("taskList", ArrayList(viewModel.tasksStateFlow.value))
    }
     */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTaskListBinding.inflate(inflater)
        val recyclerView = binding.taskRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        val addTaskButton = binding.addTaskButton
        addTaskButton.setOnClickListener {
            // Ajoutez cette vérification de permission avant de lancer l'activité de sélection d'image
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                createTask.launch(intent)
            } else {
                // Demandez la permission si elle n'est pas accordée
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        val userImageView = binding.userImageView
        userImageView.setOnClickListener {
            val intent = Intent(context, UserActivity::class.java)
            startActivity(intent)
        }

        /*
        if (savedInstanceState != null && savedInstanceState.containsKey("taskList")){
            taskList = savedInstanceState.getSerializable("taskList") as ArrayList<Task>
            adapter.submitList(taskList)
        }
        */

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            viewModel.tasksStateFlow.collect { newList ->
                // cette lambda est exécutée à chaque fois que la liste est mise à jour dans le VM
                // -> ici, on met à jour la liste dans l'adapter
                adapter.submitList(newList)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val imageView = view?.findViewById<ImageView>(R.id.user_image_view)
        val userTextView = view?.findViewById<TextView>(R.id.user_text_view)
        lifecycleScope.launch {
            val user = Api.userWebService.fetchUser().body()!!
            userTextView?.text = user.name
            imageView?.load(user.avatar) {
                error(R.drawable.ic_launcher_background)
            }
        }
        viewModel.refresh()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TaskListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TaskListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}