package com.arch.template.ui.feature.note

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.arch.presentation.viewmodels.note.NoteListingViewModel
import com.arch.template.R
import com.arch.template.base.BaseActivity
import com.arch.template.databinding.ActivityNoteListingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListingActivity : BaseActivity<ActivityNoteListingBinding, NoteListingViewModel>() {
    override val viewModel by viewModels<NoteListingViewModel>()

    override fun getLayoutRes() = R.layout.activity_note_listing

    override fun initViewModel(viewModel: NoteListingViewModel) {
        binding.viewModel = viewModel


        binding.fabAction.setOnClickListener {
            startActivity(Intent(this@NoteListingActivity, AddNoteActivity::class.java))
        }

        lifecycleScope.launchWhenStarted {

        }
    }


}