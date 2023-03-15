package com.arch.template.ui.feature.note

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arch.presentation.model.NotePresentation
import com.arch.presentation.viewmodels.note.NoteListingViewModel
import com.arch.template.R
import com.arch.template.base.BaseActivity
import com.arch.template.databinding.ActivityNoteListingBinding
import com.arch.template.utils.extension.showLongToast
import com.arch.template.utils.extension.showShortToast
import com.arch.utils.Resource
import com.arch.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteListingActivity : BaseActivity<ActivityNoteListingBinding, NoteListingViewModel>(),
    NoteListingAdapter.OnNoteClickListener {
    override val viewModel by viewModels<NoteListingViewModel>()
    private lateinit var layoutManager: LinearLayoutManager

    override fun getLayoutRes() = R.layout.activity_note_listing

    private val noteListingAdapter by lazy {
        NoteListingAdapter()
    }

    override fun initViewModel(viewModel: NoteListingViewModel) {
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        noteListingAdapter.addItemClickListener(this)
        binding.rvNoteListing.adapter = noteListingAdapter
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteNote(noteListingAdapter.getNoteByPosition(viewHolder.absoluteAdapterPosition).noteId.toString())

                val list = noteListingAdapter.currentList.toMutableList()
                list.removeAt(viewHolder.absoluteAdapterPosition)
                noteListingAdapter.submitList(list)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvNoteListing)

        layoutManager = binding.rvNoteListing.layoutManager as LinearLayoutManager

        binding.fabAction.setOnClickListener {
            openActivityForResult.launch(
                Intent(
                    this@NoteListingActivity, AddNoteActivity::class.java
                )
            )
        }
        lifecycleScope.launch {
            // Repeat when the lifecycle is RESUMED, cancel when PAUSED
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                //in order to collect multiple flows we have to launch in separate coroutine block
                launch {
                    viewModel.noteListingPagingFlow.collect {
                        noteListingAdapter.submitList(it)
                    }
                }
                launch {
                    viewModel.positionFlow.collect {
                        binding.rvNoteListing.post {
                            if (it <= noteListingAdapter.itemCount - 1) binding.rvNoteListing.scrollToPosition(
                                it
                            )
                        }
                    }
                }
                launch {
                    viewModel.noteDeleteFlow.collect {
                        when (it.status) {
                            Status.LOADING -> {}
                            Status.SUCCESS -> {
                                if (it.data == true) {
                                    showShortToast(message = getString(R.string.deleteSuccessMessage))
                                }
                            }
                            Status.ERROR -> {
                                showErrorToast(it)
                            }
                        }
                        viewModel.clearNoteDeleteFlow() //necessary to clear flow because after deleting items, onConfiguration change success toast will emit again & again
                    }
                }
            }
        }
        fetchData(viewModel) //first time
    }

    private fun showErrorToast(it: Resource<Boolean>) {
        it.error?.getFriendlyMessage()
            ?.let { it1 -> if (it1.isNotBlank()) showLongToast(message = it1) }
    }

    private val openActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                fetchData(viewModel) //after coming from addNoteActivity
            }
        }

    private fun fetchData(viewModel: NoteListingViewModel) {
        viewModel.getNote()
        binding.rvNoteListing.post {
            binding.rvNoteListing.scrollToPosition(0)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.setScrollPosition(layoutManager.findFirstCompletelyVisibleItemPosition())
    }

    override fun onNoteClick(notePresentation: NotePresentation) {
        openActivityForResult.launch(Intent(
            this@NoteListingActivity, AddNoteActivity::class.java
        ).apply {
            putExtra(noteKey, notePresentation)
        })
    }

    companion object {
        const val noteKey = "note"
    }
}