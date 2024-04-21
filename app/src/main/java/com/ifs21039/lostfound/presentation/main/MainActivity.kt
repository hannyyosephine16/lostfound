package com.ifs21039.lostfound.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ifs21039.lostfound.presentation.profile.ProfileActivity
import com.ifs21039.lostfound.R
import com.ifs21039.lostfound.adapter.ItemsAdapter
import com.ifs21039.lostfound.data.remote.MyResult
import com.ifs21039.lostfound.data.remote.response.DelcomItemsResponse
import com.ifs21039.lostfound.databinding.ActivityMainBinding
import com.ifs21039.lostfound.presentation.ViewModelFactory
import com.ifs21039.lostfound.presentation.login.LoginActivity
import com.ifs21039.lostfound.presentation.lostfound.LostFoundDetailActivity
import com.ifs21039.lostfound.presentation.lostfound.LostFoundManageActivity


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == LostFoundManageActivity.RESULT_CODE) {
            recreate()
        }
        if (result.resultCode == LostFoundDetailActivity.RESULT_CODE) {
            result.data?.let {
                val isChanged = it.getBooleanExtra(
                    LostFoundDetailActivity.KEY_IS_CHANGED,
                    false
                )
                if (isChanged) {
                    recreate()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAction()
    }

    private fun setupView() {
        showComponentNotEmpty(false)
        showEmptyError(false)
        showLoading(true)
        binding.appbarMain.overflowIcon =
            ContextCompat
                .getDrawable(this, R.drawable.ic_more_vert_24)
        observeGetLostfounds()
    }

    private fun setupAction() {
        binding.appbarMain.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mainMenuProfile -> {
                    openProfileActivity()
                    true
                }

                R.id.mainMenuLogout -> {
                    viewModel.logout()
                    openLoginActivity()
                    true
                }

                else -> false
            }
        }
        binding.fabMainAddLostfound.setOnClickListener {
            openAddLostfoundActivity()
        }
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                openLoginActivity()
            } else {
                observeGetLostfounds()
            }
        }
    }

    private fun observeGetLostfounds() {
        viewModel.getTodos().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is MyResult.Loading -> {
                        showLoading(true)
                    }

                    is MyResult.Success -> {
                        showLoading(false)
                        loadLostfoundsToLayout(result.data)
                    }

                    is MyResult.Error -> {
                        showLoading(false)
                        showEmptyError(true)
                    }
                }
            }
        }
    }

    private fun loadLostfoundsToLayout(response: DelcomItemsResponse) {
        val todos = response.data.lostfounds
        val layoutManager = LinearLayoutManager(this)
        binding.rvMainLostfounds.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(
            this,
            layoutManager.orientation
        )
        binding.rvMainLostfounds.addItemDecoration(itemDecoration)
        // Gunakan operator Elvis untuk menangani kasus null
        todos?.let {
            if (it.isEmpty()) {
                showEmptyError(true)
                binding.rvMainLostfounds.adapter = null
            } else {
                showComponentNotEmpty(true)
                showEmptyError(false)
                val adapter = ItemsAdapter()
                adapter.submitOriginalList(it)
                binding.rvMainLostfounds.adapter = adapter
                // Sisanya tetap sama
            }
        } ?: run {
            // Jika todos itu null, lakukan sesuatu
            // Misalnya, tampilkan pesan kesalahan atau gambar yang menunjukkan bahwa terjadi kesalahan
            showEmptyError(true)
            binding.rvMainLostfounds.adapter = null
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbMain.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    private fun openProfileActivity() {
        val intent = Intent(applicationContext, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun showComponentNotEmpty(status: Boolean) {
        binding.svMain.visibility =
            if (status) View.VISIBLE else View.GONE
        binding.rvMainLostfounds.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    private fun showEmptyError(isError: Boolean) {
        binding.tvMainEmptyError.visibility =
            if (isError) View.VISIBLE else View.GONE
    }

    private fun openLoginActivity() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun openAddLostfoundActivity() {
        val intent = Intent(
            this@MainActivity,
            LostFoundManageActivity::class.java
        )
        intent.putExtra(LostFoundManageActivity.KEY_IS_ADD, true)
        launcher.launch(intent)
    }
}