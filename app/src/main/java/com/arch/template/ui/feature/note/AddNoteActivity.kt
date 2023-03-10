package com.arch.template.ui.feature.note

import androidx.activity.viewModels
import com.arch.presentation.viewmodels.note.AddNoteViewModel
import com.arch.template.R
import com.arch.template.base.BaseActivity
import com.arch.template.databinding.ActivityAddNoteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteActivity : BaseActivity<ActivityAddNoteBinding, AddNoteViewModel>() {
    override val viewModel by viewModels<AddNoteViewModel>()

    override fun getLayoutRes() = R.layout.activity_add_note

    override fun initViewModel(viewModel: AddNoteViewModel) {
        binding.viewModel = viewModel

        binding.imgBack.setOnClickListener {
            finish()
        }

    }


}