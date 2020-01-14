package com.example.githubsearch

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.example.githubsearch.adapter.GitHubRepositoryAdapter
import com.example.githubsearch.model.GithubResponse
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_list_of_repositories.*

class ListOfRepositoriesActivity : AppCompatActivity() {

    var page = 1
    var isLoading = false
    //val limit = 10

    lateinit var adapter: GitHubRepositoryAdapter
    //lateinit var layoutManager: LinearLayoutManager


    private var gsonSerializer = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_repositories)

        val layoutManager = LinearLayoutManager(this)

        repositoryListRecyclerView.layoutManager = layoutManager
        adapter = GitHubRepositoryAdapter(ArrayList())

        repositoryListRecyclerView.adapter = adapter

        val dividerItemDecoration =
            DividerItemDecoration(repositoryListRecyclerView.context, layoutManager.orientation)
        repositoryListRecyclerView.addItemDecoration(dividerItemDecoration)

        initiateSearch()


        repositoryListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                //if (dy > 0) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()

                val total = adapter.itemCount

                if (!isLoading && !recyclerView.canScrollVertically(1) && SCROLL_STATE_IDLE == newState) {
                    if ((visibleItemCount + pastVisibleItem) >= total) {
                        page++
                        getNextPage()
                    }
                }
                //}
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }


    private fun initiateSearch() {

        val searchKeyword = intent.extras?.get("keyword")
        Fuel.get("https://api.github.com/search/repositories?q=$searchKeyword")
            .response { _, _, result ->
                result.fold({ data ->
                    val githubReponse: GithubResponse =
                        gsonSerializer.fromJson(String(data), GithubResponse::class.java)
                    runOnUiThread {
                        repositoryListRecyclerView.adapter =
                            GitHubRepositoryAdapter(githubReponse.items)
                        progressBar.visibility = View.GONE
                    }
                }, {
                    Toast.makeText(
                        this,
                        "Ups, something went wrong, try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                })
            }
    }

    private fun getNextPage() {
        isLoading = true
        progressBar.visibility = View.VISIBLE
        val searchKeyword = intent.extras?.get("keyword")
        Fuel.get("https://api.github.com/search/repositories?q=$searchKeyword&page=$page")
            .response { _, _, result ->
                result.fold({ data ->
                    val githubResponse: GithubResponse =
                        gsonSerializer.fromJson(String(data), GithubResponse::class.java)
                    runOnUiThread {
                        (repositoryListRecyclerView.adapter as GitHubRepositoryAdapter).apply {
                            this.gitHubRepositories.addAll(githubResponse.items)
                            this.notifyItemRangeInserted(
                                gitHubRepositories.size - githubResponse.items.size,
                                githubResponse.items.size
                            )
                        }
                        progressBar.visibility = View.GONE
                        isLoading = false
                    }
                }, {
                    Toast.makeText(
                        this,
                        "Ups, something went wrong, try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                })
            }
    }
}

