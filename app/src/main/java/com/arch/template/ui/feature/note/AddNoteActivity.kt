package com.arch.template.ui.feature.note

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.arch.presentation.model.NotePresentation
import com.arch.presentation.viewmodels.note.AddNoteViewModel
import com.arch.template.R
import com.arch.template.base.BaseActivity
import com.arch.template.databinding.ActivityAddNoteBinding
import com.arch.template.ui.feature.note.NoteListingActivity.Companion.noteKey
import com.arch.template.utils.extension.showLongToast
import com.arch.template.utils.extension.showShortToast
import com.arch.utils.Resource
import com.arch.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class AddNoteActivity : BaseActivity<ActivityAddNoteBinding, AddNoteViewModel>() {
    var note: NotePresentation? = null

    override val viewModel by viewModels<AddNoteViewModel>()

    override fun getLayoutRes() = R.layout.activity_add_note

    override fun initViewModel(viewModel: AddNoteViewModel) {
        binding.viewModel = viewModel

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.imgSave.setOnClickListener {
            saveNoteOperation()
        }

        binding.imgDelete.setOnClickListener {
            note?.noteId?.let {
                viewModel.deleteNote(it.toString())
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.noteSavingFlow.collect {
                when (it.status) {
                    Status.LOADING -> {
                        updateUI(true, loadingText = getString(R.string.saving))
                    }
                    Status.SUCCESS -> {
                        if (it.data == true) {
                            showShortToast(message = getString(R.string.saveSuccessMessage))
                            finishActivityAndUpdateListOfListActivity()
                        } else {
                            showErrorToast(it)
                        }
                        updateUI(false, loadingText = getString(R.string.saving))
                    }
                    Status.ERROR -> {
                        showErrorToast(it)
                        updateUI(false, loadingText = getString(R.string.saving))
                    }
                }
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.noteDeleteFlow.collect {
                when (it.status) {
                    Status.LOADING -> {
                        updateUI(true, loadingText = getString(R.string.deleting))
                    }
                    Status.SUCCESS -> {
                        if (it.data == true) {
                            showShortToast(message = getString(R.string.deleteSuccessMessage))
                            finishActivityAndUpdateListOfListActivity()
                        } else {
                            showErrorToast(it)
                        }
                        updateUI(false, loadingText = getString(R.string.deleting))
                    }
                    Status.ERROR -> {
                        showErrorToast(it)
                        updateUI(false, loadingText = getString(R.string.deleting))
                    }
                }
            }
        }

        if (intent.hasExtra(noteKey)) {
            note = intent.getParcelableExtra(noteKey)
            binding.etNote.setText(note?.noteContent)
            binding.etNote.post {
                binding.etNote.requestFocus()
                binding.etNote.setSelection(binding.etNote.length())
            }
            binding.imgDelete.visibility = View.VISIBLE
        }
    }

    private fun finishActivityAndUpdateListOfListActivity() {
        setResult(RESULT_OK)
        finish()
    }

    private fun updateUI(isLoading: Boolean, loadingText: String) {
        if (isLoading) {
            binding.rootActivityAddNote.setBackgroundColor(
                ContextCompat.getColor(
                    this@AddNoteActivity, R.color.LoadingBackground
                )
            )
            binding.txtSaving.text = loadingText
            binding.txtSaving.visibility = View.VISIBLE
            binding.LoadingDot.visibility = View.VISIBLE
            val animation: Animation = AnimationUtils.loadAnimation(
                applicationContext, R.anim.blink
            )
            binding.LoadingDot.startAnimation(animation)
        } else {
            binding.rootActivityAddNote.background = null
            binding.txtSaving.visibility = View.GONE
            binding.LoadingDot.visibility = View.GONE
        }
    }

    private fun showErrorToast(it: Resource<Boolean>) {
        it.error?.getFriendlyMessage()
            ?.let { it1 -> if (it1.isNotBlank()) showLongToast(message = it1) }
    }

    override fun onBackPressed() {
        if (binding.etNote.text.toString().isNotBlank()) {
            saveNoteOperation()
        }
        finishActivityAndUpdateListOfListActivity()
    }

    private fun saveNoteOperation() {
        viewModel.saveNote(note?.noteId ?: 0, binding.etNote.text?.trim().toString())

    }


}