package com.ifs21039.lostfound.presentation.lostfound

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ifs21039.lostfound.data.model.DelcomLostfound
import com.ifs21039.lostfound.data.remote.MyResult
import com.ifs21039.lostfound.data.remote.response.LostFoundDetailsResponse
import com.ifs21039.lostfound.databinding.ActivityLostFoundDetailBinding
import com.ifs21039.lostfound.helper.Utils.Companion.observeOnce
import com.ifs21039.lostfound.presentation.ViewModelFactory

class LostFoundDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLostFoundDetailBinding
    private val viewModel by viewModels<LostFoundViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var isChanged: Boolean = false
    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == LostFoundManageActivity.RESULT_CODE) {
            recreate()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLostFoundDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }
    private fun setupView() {
        showComponent(false)
        showLoading(false)
    }
    private fun setupAction() {
        val lostfoundId = intent.getIntExtra(KEY_LOSTFOUND_ID, 0)
        if (lostfoundId == 0) {
            finish()
            return
        }
        observeGetLostfound(lostfoundId)
        binding.appbarLostfoundDetail.setNavigationOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(KEY_IS_CHANGED, isChanged)
            setResult(RESULT_CODE, resultIntent)
            finishAfterTransition()
        }
    }
    private fun observeGetLostfound(lostfoundId: Int) {
        viewModel.getLostfound(lostfoundId).observeOnce { result ->
            when (result) {
                is MyResult.Loading -> {
                    showLoading(true)
                }
                is MyResult.Success -> {
                    showLoading(false)
                    loadLostfound(result.data.data.lostfound)
                }
                is MyResult.Error -> {
                    Toast.makeText(
                        this@LostFoundDetailActivity,
                        result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                    showLoading(false)
                    finishAfterTransition()
                }
            }
        }
    }
    private fun loadLostfound(lostfound: LostFoundDetailsResponse) {
        showComponent(true)
        binding.apply {
            tvLostfoundDetailTitle.text = lostfound.title
            tvLostfoundDetailDate.text = "Dibuat pada: ${lostfound.createdAt}"
            tvLostfoundDetailDesc.text = lostfound.description
            cbLostfoundDetailIsFinished.isChecked = lostfound.isCompleted == 1
            cbLostfoundDetailIsFinished.setOnCheckedChangeListener { _, isChecked ->
                viewModel.putLostfound(
                    lostfound.id,
                    lostfound.title,
                    lostfound.description,
                    isChecked
                ).observeOnce {
                    when (it) {
                        is MyResult.Error -> {
                            if (isChecked) {
                                Toast.makeText(
                                    this@LostFoundDetailActivity,
                                    "Gagal menyelesaikan todo: " + lostfound.title,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@LostFoundDetailActivity,
                                    "Gagal batal menyelesaikan todo: " + lostfound.title,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        is MyResult.Success -> {
                            if (isChecked) {
                                Toast.makeText(
                                    this@LostFoundDetailActivity,
                                    "Berhasil menyelesaikan todo: " + lostfound.title,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@LostFoundDetailActivity,
                                    "Berhasil batal menyelesaikan todo: " + lostfound.title,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            if ((lostfound.isCompleted == 1) != isChecked) {
                                isChanged = true
                            }
                        }
                        else -> {}
                    }
                }
            }
            ivLostfoundDetailActionDelete.setOnClickListener {
                val builder = AlertDialog.Builder(this@LostFoundDetailActivity)
                builder.setTitle("Konfirmasi Hapus Item Lost & Found")
                    .setMessage("Anda yakin ingin menghapus item ini?")
                builder.setPositiveButton("Ya") { _, _ ->
                    observeDeleteLostfound(lostfound.id)
                }
                builder.setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss() // Menutup dialog
                }
                val dialog = builder.create()
                dialog.show()
            }
            ivLostfoundDetailActionEdit.setOnClickListener {
                val delcomLostfound = DelcomLostfound(
                    lostfound.id,
                    lostfound.title,
                    lostfound.description,
                    lostfound.isCompleted == 1,
                    lostfound.cover
                )
                val intent = Intent(
                    this@LostFoundDetailActivity,
                    LostFoundManageActivity::class.java
                )
                intent.putExtra(LostFoundManageActivity.KEY_IS_ADD, false)
                intent.putExtra(LostFoundManageActivity.KEY_LOSTFOUND, delcomLostfound)
                launcher.launch(intent)
            }
        }
    }
    private fun observeDeleteLostfound(lostfoundId: Int) {
        showComponent(false)
        showLoading(true)
        viewModel.deleteLostfound(lostfoundId).observeOnce {
            when (it) {
                is MyResult.Error -> {
                    showComponent(true)
                    showLoading(false)
                    Toast.makeText(
                        this@LostFoundDetailActivity,
                        "Gagal menghapus item Lost & Found: ${it.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is MyResult.Success -> {
                    showLoading(false)
                    Toast.makeText(
                        this@LostFoundDetailActivity,
                        "Berhasil menghapus item Lost & Found",
                        Toast.LENGTH_SHORT
                    ).show()
                    val resultIntent = Intent()
                    resultIntent.putExtra(KEY_IS_CHANGED, true)
                    setResult(RESULT_CODE, resultIntent)
                    finishAfterTransition()
                }
                else -> {}
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.pbLostfoundDetail.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showComponent(status: Boolean) {
        binding.llLostfoundDetail.visibility =
            if (status) View.VISIBLE else View.GONE
    }
    companion object {
        const val KEY_LOSTFOUND_ID = "lostfound_id"
        const val KEY_IS_CHANGED = "is_changed"
        const val RESULT_CODE = 1001
    }
}