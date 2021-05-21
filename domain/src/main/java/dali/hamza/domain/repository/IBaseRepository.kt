package dali.hamza.domain.repository

import dali.hamza.domain.models.IResponse
import kotlinx.coroutines.flow.Flow

interface IBaseRepository<T> {
    suspend fun getAllFlow(): Flow<IResponse>
    suspend fun getOneFlow(id: Int): Flow<IResponse>
    suspend fun insert(entity: T): Flow<IResponse>
    suspend fun delete(entity: T)
}