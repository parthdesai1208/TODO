package com.arch.template.ui.feature.note

import android.app.Activity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.arch.presentation.viewmodels.note.AddNoteViewModel
import com.arch.template.R
import com.arch.template.base.BaseActivity
import com.arch.template.databinding.ActivityAddNoteBinding
import com.arch.template.utils.extension.showLongToast
import com.arch.template.utils.extension.showShortToast
import com.arch.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddNoteActivity : BaseActivity<ActivityAddNoteBinding, AddNoteViewModel>() {
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

        lifecycleScope.launchWhenResumed {
            viewModel.noteSavingFlow.collect {
                when (it.status) {
                    Status.LOADING -> {

                    }
                    Status.SUCCESS -> {
                        if (it.data == true) {
                            showShortToast(message = getString(R.string.saveSuccessMessage))
                        }
                    }
                    Status.ERROR -> {
                        it.error?.getFriendlyMessage()
                            ?.let { it1 -> if (it1.isNotBlank()) showLongToast(message = it1) }
                    }
                }
            }
        }

    }

    override fun onBackPressed() {
        if (binding.etNote.text.toString().isNotBlank()) saveNoteOperation()
        finish()
    }

    private fun saveNoteOperation() {
        viewModel.saveNote(binding.etNote.text?.trim().toString())
        setResult(Activity.RESULT_OK)
        finish()
    }


}