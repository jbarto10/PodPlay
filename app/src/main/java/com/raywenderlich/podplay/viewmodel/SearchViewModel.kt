package com.raywenderlich.podplay.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.raywenderlich.podplay.repository.ItunesRepo
import com.raywenderlich.podplay.service.PodcastResponse
import com.raywenderlich.podplay.util.DateUtils

class SearchViewModel(application: Application) :
    AndroidViewModel(application) {
    var iTunesRepo: ItunesRepo? = null

    data class PodcastSummaryViewData(
        var name: String? = "",
        var lastUpdated: String? = "",
        var imageUrl: String? = "",
        var feedUrl: String? = "")

    private fun itunesPodcastToPodcastSummaryView(
        itunesPodcast: PodcastResponse.ItunesPodcast):
            PodcastSummaryViewData {
        return PodcastSummaryViewData(
            itunesPodcast.collectionCensoredName,
            DateUtils.jsonDateToShortDate(itunesPodcast.releaseDate),
            itunesPodcast.artworkUrl100,
            itunesPodcast.feedUrl)
    }

    // 1 search term
    suspend fun searchPodcasts(term: String):
            List<PodcastSummaryViewData> {
        // 2 iTunesRepo is used to perform the search asynchronously
        val results = iTunesRepo?.searchByTerm(term)
        // 3 Check if the results are not null and the call is successful
        if (results != null && results.isSuccessful) {
            // 4 Get the podcasts from the body
            val podcasts = results.body()?.results
            // 5 Check if the podcasts list is not empty
            if (!podcasts.isNullOrEmpty()) {
                // 6 Map them to PodcastSummaryViewData objects
                return podcasts.map { podcast ->
                    itunesPodcastToPodcastSummaryView(podcast)
                }
            }
        }
        // 7 If the results are null, then you return an empty list
        return emptyList()
    }

}