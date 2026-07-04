package com.example.finalexamapp.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "step_logs")
data class StepLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val stepCount: Int,
    val distanceKm: Double,
    val caloriesBurned: Double,
    val timestamp: Long
)

data class TimeframeSummary(
    val period: String,
    val totalSteps: Int,
    val totalDistance: Double,
    val totalCalories: Double
)

@Dao
interface StepLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: StepLog)

    @Query("SELECT * FROM step_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<StepLog>>

    @Query("SELECT strftime('%Y-%m-%d', datetime(timestamp/1000, 'unixepoch')) as period, SUM(stepCount) as totalSteps, SUM(distanceKm) as totalDistance, SUM(caloriesBurned) as totalCalories FROM step_logs GROUP BY period ORDER BY period DESC")
    fun getDailySummaries(): Flow<List<TimeframeSummary>>

    @Query("SELECT strftime('%Y-%W', datetime(timestamp/1000, 'unixepoch')) as period, SUM(stepCount) as totalSteps, SUM(distanceKm) as totalDistance, SUM(caloriesBurned) as totalCalories FROM step_logs GROUP BY period ORDER BY period DESC")
    fun getWeeklySummaries(): Flow<List<TimeframeSummary>>

    @Query("SELECT strftime('%Y-%m', datetime(timestamp/1000, 'unixepoch')) as period, SUM(stepCount) as totalSteps, SUM(distanceKm) as totalDistance, SUM(caloriesBurned) as totalCalories FROM step_logs GROUP BY period ORDER BY period DESC")
    fun getMonthlySummaries(): Flow<List<TimeframeSummary>>
}

@Database(entities = [StepLog::class], version = 5, exportSchema = false)
abstract class FitnessDatabase : RoomDatabase() {
    abstract fun stepLogDao(): StepLogDao
    companion object {
        @Volatile
        private var INSTANCE: FitnessDatabase? = null
        fun getDatabase(context: Context): FitnessDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitnessDatabase::class.java,
                    "stride_fuel_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
