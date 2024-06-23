package com.algorand.android.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.algorand.android.models.Validator
import com.algorand.android.models.ValidatorPool
import kotlinx.coroutines.flow.Flow

@Dao
interface ValidatorDao {
    @Query("SELECT * FROM validators ORDER BY id desc")
    fun getAllValidatorsAsFlow(): Flow<List<Validator>>

    @Query("SELECT * FROM validators WHERE id = :validatorId")
    fun getValidatorByIdAsFlow(validatorId: Long): Flow<Validator?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertValidator(validator: Validator): Long

    @Query("SELECT * FROM validatorPools WHERE validatorId = :validatorId ORDER BY poolAppId desc")
    fun getAllValidatorPoolsAsFlow(validatorId: Long): Flow<List<ValidatorPool>>

    @Query("SELECT * FROM validatorPools WHERE poolAppId = :poolAppId ORDER BY poolId desc")
    fun getValidatorPoolByIdAsFlow(poolAppId: Long): Flow<ValidatorPool?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertValidatorPool(validatorPool: ValidatorPool): Long
}
