package com.example.githubsearch

import adapter.GitHubRepositoryAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_list_of_repositories.*
import model.GithubResponse

class ListOfRepositoriesActivity : AppCompatActivity()  {

    private var gsonSerializer = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_repositories)

        val layoutManager = LinearLayoutManager(this)

        repositoryListRecyclerView.layoutManager = layoutManager
        repositoryListRecyclerView.adapter = GitHubRepositoryAdapter(ArrayList())

        val dividerItemDecoration = DividerItemDecoration(repositoryListRecyclerView.context, layoutManager.orientation)
        repositoryListRecyclerView.addItemDecoration(dividerItemDecoration)

        initiateSearch()
    }

    private fun initiateSearch() {

        val searchKeyword = intent.extras?.get("keyword")
        Fuel.get("https://api.github.com/search/repositories?q=$searchKeyword")
            .response { _, _, result ->
                result.fold({ data ->
                    val githubReponse : GithubResponse = gsonSerializer.fromJson(String(data), GithubResponse::class.java)
                    runOnUiThread {
                        repositoryListRecyclerView.adapter = GitHubRepositoryAdapter(githubReponse.items)
                    }
                }, {
                    Toast.makeText(this,  "Ups, something went wrong, try again later.", Toast.LENGTH_SHORT).show()
                })
            }
    }

}
