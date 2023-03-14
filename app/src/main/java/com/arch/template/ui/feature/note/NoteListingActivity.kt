package com.arch.template.ui.feature.note

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.arch.presentation.model.NotePresentation
import com.arch.presentation.viewmodels.note.NoteListingViewModel
import com.arch.template.R
import com.arch.template.base.BaseActivity
import com.arch.template.databinding.ActivityNoteListingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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

        layoutManager = binding.rvNoteListing.layoutManager as LinearLayoutManager

        binding.fabAction.setOnClickListener {
            openActivityForResult.launch(
                Intent(
                    this@NoteListingActivity,
                    AddNoteActivity::class.java
                )
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.noteListingPagingFlow.collect {
                noteListingAdapter.submitList(it)
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.positionFlow.collect {
                binding.rvNoteListing.post {
                    if (it <= noteListingAdapter.itemCount - 1)
                        binding.rvNoteListing.scrollToPosition(it)
                }
            }
        }
        fetchData(viewModel)
    }

    private val openActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                fetchData(viewModel)
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
        openActivityForResult.launch(
            Intent(
                this@NoteListingActivity,
                AddNoteActivity::class.java
            ).apply {
                putExtra(noteKey, notePresentation)
            }
        )
    }

    companion object {
        const val noteKey = "note"
    }
}