package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.entity.VacancyCardEntity

@Dao
interface VacancyDao {
    @Insert(entity = VacancyCardEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(favoriteTrack: VacancyCardEntity)

    @Query("SELECT * FROM vacancy_card_table")
    suspend fun getVacancies(): List<VacancyCardEntity>

    @Query("SELECT id FROM vacancy_card_table")
    suspend fun getVacanciesId(): List<String>

    @Delete(entity = VacancyCardEntity::class)
    suspend fun deleteVacancy(vacancyCardEntity: VacancyCardEntity)
}
