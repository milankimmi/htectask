package com.htec.task

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.htec.task.adapter.SwipeManageAdapter
import com.htec.task.adapter.PostRecycleViewAdapter
import com.htec.task.di.component.DaggerMainActivityComponent
import com.htec.task.di.module.ActivityContextModule
import com.htec.task.di.qualifier.ActivityContext
import com.htec.task.datamodel.Post
import com.htec.task.datamodel.User
import com.htec.task.utils.ConnectivityUtil
import com.htec.task.utils.DialogUtil
import com.htec.task.utils.SharedUtil
import com.htec.task.viewmodel.PostViewModel
import com.htec.task.viewmodel.UserViewModel
import com.htec.task.viewmodel.ViewModelFactory
import java.io.IOException
import java.lang.StringBuilder
import javax.inject.Inject

class MainActivity : AppCompatActivity(), PostRecycleViewAdapter.PostClickListener {

    @Inject
    @ActivityContext
    lateinit var context: Context

    @Inject
    lateinit var recyclerAdapter: PostRecycleViewAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var postViewModel: PostViewModel
    lateinit var userViewModel: UserViewModel

    lateinit var recycleView: RecyclerView
    lateinit var progressLoader: ProgressBar
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var actionFlag: Boolean = false
    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        doDaggerInjection()

        postViewModel = ViewModelProvider(this, viewModelFactory).get(PostViewModel::class.java)
        userViewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        recycleView = findViewById(R.id.postRecyclerView)
        progressLoader = findViewById(R.id.progressBarLoader)

        recycleView.adapter = recyclerAdapter
        recycleView.layoutManager = LinearLayoutManager(context)

        swipeRefreshLayout.isRefreshing = true
        swipeRefreshLayout.setOnRefreshListener {
            // When user pulls the list down, then force refreshing the data from the server.
            fetchPosts(true)
        }

        swipeToDeletion()
    }

    override fun onResume() {
        super.onResume()

        alertDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }

        if (!actionFlag) {
            fetchPosts()
        } else {
            fetchPosts(true)
            actionFlag = false
        }
    }

    private fun doDaggerInjection() {
        DaggerMainActivityComponent
            .builder()
            .activityContextModule(ActivityContextModule(this, this))
            .applicationComponent((application as MyApplication).provideAppComponent())
            .build()
            .injectMainActivity(this)
    }

    private fun fetchPosts(forceRefresh: Boolean = false) {
        if (ConnectivityUtil.hasActiveInternetConnection(this) ||
            (SharedUtil.hasLocalCache(this) && !SharedUtil.isLocalCacheExpired(this))
        ) {
            postViewModel.getPosts(forceRefresh).observe(this, Observer { repositoryResult ->
                if (repositoryResult.hasResult()) {
                    repositoryResult.success?.let {
                        recyclerAdapter.setData(it as ArrayList<Post>)
                    }
                } else {
                    // Error handling - can be solved on many ways
                    repositoryResult.error?.let { throwable ->
                        if (throwable is IOException) {
                            showNetworkConnectionDialog()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Something went wrong: ${throwable.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                swipeRefreshLayout.isRefreshing = false
            })
        } else {
            showNetworkConnectionDialog()
        }
    }

    private fun updateUserInteraction(enable: Boolean) {
        recyclerAdapter.isClickable = enable
        progressLoader.visibility = if (enable) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }

    private fun showPostOwner(user: User, post: Post, position: Int) {
        // If `AlertDialog` has already opened then close it. On that way we prevent showing
        // multiple dialogs when user did fast double-clicking.
        alertDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }

        var dialogMessage = StringBuilder("Name: " + user.name)
            .append("\n")
            .append("E-mail: " + user.email)
            .toString()

        alertDialog = DialogUtil.showCustomDialog(context,
            title = post.title,
            message = dialogMessage,
            neutralButtonText = applicationContext.getString(R.string.dialog_delete_button),
            cancelable = false,
            onNeutralClickListener = DialogInterface.OnClickListener { dialog, _ ->
                recyclerAdapter.deletePostLocally(position)
                postViewModel.deletePost(post)
                dialog.dismiss()
            },
            onPositiveClickListener = DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
            }
        )
    }

    private fun showNetworkConnectionDialog() {
        alertDialog = DialogUtil.showCustomDialog(
            this,
            title = applicationContext.resources.getString(R.string.dialog_network_connection_title),
            message = applicationContext.resources.getString(R.string.dialog_network_connection_message),
            cancelable = false,
            positiveButtonText = applicationContext.getString(R.string.dialog_network_connection_positive_button),
            negativeButtonText = applicationContext.getString(R.string.dialog_network_connection_negative_button),
            neutralButtonText = applicationContext.getString(R.string.dialog_network_connection_neutral_button),
            onPositiveClickListener = DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
                fetchPosts(true)
            },
            onNegativeClickListener = DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
                swipeRefreshLayout.isRefreshing = false
            },
            onNeutralClickListener = DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
                actionFlag = true
                swipeRefreshLayout.isRefreshing = false
                val intent: Intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(intent)
            }
        )
    }

    private fun swipeToDeletion() {
        // Lambda callback expression
        val swipeCallback: (adapterPosition: Int, post: Post?) -> Unit = { position, post ->
            recyclerAdapter.deletePostLocally(position)
            post?.let { postViewModel.deletePost(it) }
        }

        val itemTouchHelperCallback = SwipeManageAdapter(
            swipeCallback,
            recyclerAdapter,
            context,
            ItemTouchHelper.ACTION_STATE_IDLE,
            ItemTouchHelper.LEFT
        )

        var itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycleView)
    }

    override fun onPostClicked(position: Int, post: Post) {
        updateUserInteraction(false)
        userViewModel.getUser(post.userId).observe(this, Observer { repositoryResult ->
            if (repositoryResult.hasResult()) {
                repositoryResult.success?.let {
                    showPostOwner(it, post, position)
                    updateUserInteraction(true)
                }
            } else {
                repositoryResult.error?.let { throwable ->
                    var message: String? = if (throwable is IOException) {
                        context.getString(R.string.dialog_network_connection_message)
                    } else {
                        throwable.message
                    }
                    Toast.makeText(this@MainActivity, "${message}", Toast.LENGTH_SHORT).show()
                }
                updateUserInteraction(true)
            }
        })
    }
}