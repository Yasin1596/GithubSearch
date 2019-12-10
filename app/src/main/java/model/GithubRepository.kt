package model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

class GithubRepository {
    var name: String = ""
    var language: String = ""
    var description: String = ""

    @SerializedName("full_name")
    var fullName: String = ""

    @SerializedName("html_url")
    var htmlUrl: String = ""

    @SerializedName("updated_at")
    var updatedAt: String? = null

    @SerializedName("stargazers_count")
    var stargazersCount: Int? = null

}