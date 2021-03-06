package br.com.listgistgithub.ui.home.viewmodel

import android.content.Context
import androidx.lifecycle.liveData
import br.com.listgistgithub.R
import br.com.listgistgithub.data.model.Favorite
import br.com.listgistgithub.data.repository.FavoriteRepository
import br.com.listgistgithub.data.repository.GistRepository
import br.com.listgistgithub.model.Gist
import br.com.listgistgithub.ui.base.BaseViewModel
import br.com.listgistgithub.utils.NetworkResponse
import br.com.listgistgithub.utils.hasInternet
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val gistRepository: GistRepository,
    private val favoriteRepository: FavoriteRepository
) : BaseViewModel() {

    private var listFavorite: MutableList<Favorite> = mutableListOf()

    fun getGirts(context: Context, page: Int, perPage: Int = 30) = liveData(IO) {
        emit(NetworkResponse.Loading)
        if (hasInternet(context)) {
            try {
                emit(NetworkResponse.Success(data = gistRepository.getGists(page, perPage)))
            } catch (exception: Exception) {
                emit(NetworkResponse.Error(exception = exception.message ?: context.getString(R.string.error_default)))
            }
        } else
            emit(NetworkResponse.Error(exception = context.getString(R.string.error_connection)))
    }

    fun insertFavorite(context: Context, gist: Gist) {
        launch {
            try {
                favoriteRepository.insertFavorite(context, gist)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteFavorite(context: Context, ownerId: String) {
        launch {
            try {
                favoriteRepository.deleteFavoriteById(context, ownerId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getFavorites(context: Context) {
        launch {
            withContext(IO) {
                val favorites = favoriteRepository.getFavorites(context)
                listFavorite = favorites
            }
        }
    }

    fun setGistsFavorites(gists: List<Gist>) {
        listFavorite.forEach { favorite ->
            val gist = gists.find { it.id == favorite.ownerId }
            if (gist != null) gist.isFavorite = true
        }
    }
}
