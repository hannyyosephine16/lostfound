package com.ifs21039.lostfound.presentation.lostfound

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ifs21039.lostfound.data.model.DelcomLostfound
import com.ifs21039.lostfound.data.remote.MyResult
import com.ifs21039.lostfound.databinding.ActivityLostFoundManageBinding
import com.ifs21039.lostfound.helper.Utils.Companion.observeOnce
import com.ifs21039.lostfound.presentation.ViewModelFactory

class LostFoundManageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLostFoundManageBinding
    private val viewModel by viewModels<LostFoundViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLostFoundManageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAtion()
    }
    private fun setupView() {
        showLoading(false)
    }
    private fun setupAtion() {
        val isAddLostfound = intent.getBooleanExtra(KEY_IS_ADD, true)
        if (isAddLostfound) {
            manageAddLostfound()
        } else {
            val delcomLostfound = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                    intent.getParcelableExtra(KEY_LOSTFOUND, DelcomLostfound::class.java)
                }
                else -> {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra<DelcomLostfound>(KEY_LOSTFOUND)
                }
            }
            if (delcomLostfound == null) {
                finishAfterTransition()
                return
            }
            manageEditLostfound(delcomLostfound)
        }
        binding.appbarLostfoundManage.setNavigationOnClickListener {
            finishAfterTransition()
        }
    }
    private fun manageAddLostfound() {
        binding.apply {
            appbarLostfoundManage.title = "Tambah Lost & Found"
            btnLostfoundManageSave.setOnClickListener {
                val title = etLostfoundManageTitle.text.toString()
                val description = etLostfoundManageDesc.text.toString()
                if (title.isEmpty() || description.isEmpty()) {
                    AlertDialog.Builder(this@LostFoundManageActivity).apply {
                        setTitle("Oh No!")
                        setMessage("Tidak boleh ada data yang kosong!")
                        setPositiveButton("Oke") { _, _ -> }
                        create()
                        show()
                    }
                    return@setOnClickListener
                }
                observePostLostfound(title, description)
            }
        }
    }
    private fun observePostLostfound(title: String, description: String) {
        viewModel.postLostfound(title, description).observeOnce { result ->
            when (result) {
                is MyResult.Loading -> {
                    showLoading(true)
                }
                is MyResult.Success -> {
                    showLoading(false)
                    val resultIntent = Intent()
                    setResult(RESULT_CODE, resultIntent)
                    finishAfterTransition()
                }
                is MyResult.Error -> {
                    AlertDialog.Builder(this@LostFoundManageActivity).apply {
                        setTitle("Oh No!")
                        setMessage(result.error)
                        setPositiveButton("Oke") { _, _ -> }
                        create()
                        show()
                    }
                    showLoading(false)
                }
            }
        }
    }
    private fun manageEditLostfound(lostfound: DelcomLostfound) {
        binding.apply {
            appbarLostfoundManage.title = "Ubah Item Lost & Found"
            etLostfoundManageTitle.setText(lostfound.title)
            etLostfoundManageDesc.setText(lostfound.description)
            btnLostfoundManageSave.setOnClickListener {
                val title = etLostfoundManageTitle.text.toString()
                val description = etLostfoundManageDesc.text.toString()
                if (title.isEmpty() || description.isEmpty()) {
                    AlertDialog.Builder(this@LostFoundManageActivity).apply {
                        setTitle("Oh No!")
                        setMessage("Tidak boleh ada data yang kosong!")
                        setPositiveButton("Oke") { _, _ -> }
                        create()
                        show()
                    }
                    return@setOnClickListener
                }
                observePutLostfound(lostfound.id, title, description, lostfound.isFinished)
            }
        }
    }
    private fun observePutLostfound(
        lostfoundId: Int,
        title: String,
        description: String,
        isFinished: Boolean,
    ) {
        viewModel.putLostfound(
            lostfoundId,
            title,
            description,
            isFinished
        ).observeOnce { result ->
            when (result) {
                is MyResult.Loading -> {
                    showLoading(true)
                }
                is MyResult.Success -> {
                    showLoading(false)

                    val resultIntent = Intent()
                    setResult(RESULT_CODE, resultIntent)
                    finishAfterTransition()
                }
                is MyResult.Error -> {
                    AlertDialog.Builder(this@LostFoundManageActivity).apply {
                        setTitle("Oh No!")
                        setMessage(result.error)
                        setPositiveButton("Oke") { _, _ -> }
                        create()
                        show()
                    }
                    showLoading(false)
                }
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.pbLostfoundManage.visibility =
            if (isLoading) View.VISIBLE else View.GONE

        binding.btnLostfoundManageSave.isActivated = !isLoading

        binding.btnLostfoundManageSave.text =
            if (isLoading) "" else "Simpan"
    }
    companion object {
        const val KEY_IS_ADD = "is_add"
        const val KEY_LOSTFOUND = "lostfound"
        const val RESULT_CODE = 1002
    }
}
