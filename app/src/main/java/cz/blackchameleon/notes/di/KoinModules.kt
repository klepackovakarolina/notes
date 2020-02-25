package cz.blackchameleon.notes.di

import cz.blackchameleon.notes.data.NotesRepository
import cz.blackchameleon.notes.data.NotesSource
import cz.blackchameleon.notes.framework.NotesRequestService
import cz.blackchameleon.notes.framework.NotesSourceImplementation
import cz.blackchameleon.notes.presentation.notedetail.NoteDetailViewModel
import cz.blackchameleon.notes.presentation.noteslist.NotesListViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL: String = "http://private-9aad-note10.apiary-mock.com/"

val koinModule = module {
    single { provideDefaultOkhttpClient() }
    single { provideRetrofit(get()) }
    single { NotesRepository(get()) }
    single { provideRetrofitService<NotesRequestService>(get()) }
    single { NotesSourceImplementation(get()) as NotesSource }
}

val viewModelModule = module {
    viewModel { NoteDetailViewModel(get()) }
    viewModel { NotesListViewModel(get()) }
}

// Creates new Okhttp client for API calls
private fun provideDefaultOkhttpClient(): OkHttpClient =
    OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES)
        .build()


// Creates an instance of retrofit for given OkHttpClient
private fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

// Extension function for retrofit
private inline fun <reified T> provideRetrofitService(retrofit: Retrofit): T =
    retrofit.create(T::class.java)