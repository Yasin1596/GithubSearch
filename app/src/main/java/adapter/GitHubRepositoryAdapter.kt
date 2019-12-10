package adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubsearch.R
import kotlinx.android.synthetic.main.repository_list_item.view.*
import model.GithubRepository

class GitHubRepositoryAdapter (private val gitHubRepositories : ArrayList<GithubRepository>) : RecyclerView.Adapter<CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val singleListItem = layoutInflater.inflate(R.layout.repository_list_item, parent, false)
        return CustomViewHolder(singleListItem)
    }

    override fun getItemCount(): Int {
        return gitHubRepositories.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val repository = gitHubRepositories[position]

        holder.view.fullNameTextView.text = repository.fullName
        holder.view.descriptionTextView.text = repository.description
        holder.view.lastUpdatedTextView.text = repository.updatedAt
        holder.view.languageTextView.text = repository.language
        holder.view.stargazersCountTextView.text = repository.stargazersCount.toString()

        holder.view.setOnClickListener {
            val openRepositoryIntent = Intent(Intent.ACTION_VIEW)
            openRepositoryIntent.data = Uri.parse(repository.htmlUrl)
            holder.view.context.startActivity(openRepositoryIntent)
        }
    }

}

class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
}