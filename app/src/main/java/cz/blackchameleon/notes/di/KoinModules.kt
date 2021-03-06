package cz.blackchameleon.notes.di

import cz.blackchameleon.notes.data.*
import cz.blackchameleon.notes.framework.DraftNoteSourceImpl
import cz.blackchameleon.notes.framework.NotesRequestService
import cz.blackchameleon.notes.framework.NotesSourceImpl
import cz.blackchameleon.notes.framework.OpenNoteSourceImpl
import cz.blackchameleon.notes.presentation.notedetail.NoteDetailViewModel
import cz.blackchameleon.notes.presentation.noteslist.NotesListViewModel
import cz.blackchameleon.notes.usecases.*
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

// Main API url used for requests
const val BASE_URL: String = "http://private-9aad-note10.apiary-mock.com/"

// Logically separated koin modules
val koinModule = module {
    single { provideDefaultOkhttpClient() }
    single { provideRetrofit(get()) }

    single { NotesRepository(get()) }
    single { OpenNoteRepository(get()) }
    single { DraftNoteRepository(get()) }

    single { provideRetrofitService<NotesRequestService>(get()) }

    single { NotesSourceImpl(get()) as NotesSource }
    single { OpenNoteSourceImpl() as OpenNoteSource }
    single { DraftNoteSourceImpl() as DraftNoteSource }
}

val viewModelModule = module {
    viewModel { NoteDetailViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { NotesListViewModel(get(), get(), get(), get()) }
}

val useCasesModule = module {
    single { CreateNote(get()) }
    single { DeleteNote(get()) }
    single { EditNote(get()) }
    single { GetNotesList(get()) }
    single { GetOpenNote(get()) }
    single { SetOpenNote(get()) }
    single { GetDraftNote(get()) }
    single { SetDraftNote(get()) }
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

// Extension function for creating retrofit service
private inline fun <reified T> provideRetrofitService(retrofit: Retrofit): T =
    retrofit.create(T::class.java)