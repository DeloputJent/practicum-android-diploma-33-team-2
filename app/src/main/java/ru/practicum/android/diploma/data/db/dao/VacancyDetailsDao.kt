package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.entity.VacancyDetailsEntity

@Dao
interface VacancyDetailsDao {
    @Insert(entity = VacancyDetailsEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(details: VacancyDetailsEntity)

    @Query("SELECT * FROM vacancy_details_table WHERE id = :vacancyId LIMIT 1")
    suspend fun getById(vacancyId: String): VacancyDetailsEntity?

    @Query("DELETE FROM vacancy_details_table WHERE id = :vacancyId")
    suspend fun deleteById(vacancyId: String)
}
