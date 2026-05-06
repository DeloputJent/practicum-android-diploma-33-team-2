package ru.practicum.android.diploma.di

import androidx.room.Room
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import ru.practicum.android.diploma.data.converters.SearchDtoConverter
import ru.practicum.android.diploma.data.converters.VacancyDbConvertor
import ru.practicum.android.diploma.data.converters.VacancyDetailDtoConverter
import ru.practicum.android.diploma.data.db.VacancyDatabase
import ru.practicum.android.diploma.data.network.HhApi
import ru.practicum.android.diploma.data.network.HhApiConstants

val dataModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            VacancyDatabase::class.java,
            "Vacancy_database.db"
        )
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(HhApiConstants.BASE_URL)
            .build()
    }
    single<HhApi> { get<Retrofit>().create(HhApi::class.java) }
    single { SearchDtoConverter() }
    single { VacancyDetailDtoConverter() }
    single { VacancyDbConvertor() }
    single { Gson() }

}
