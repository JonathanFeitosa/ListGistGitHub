package br.com.listgistgithub.di

import br.com.listgistgithub.data.repository.GistRepositoryImpl
import br.com.listgistgithub.data.repository.GistRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<GistRepository> {
        GistRepositoryImpl(
            get()
        )
    }
}