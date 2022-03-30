package com.example.arifitna.use_case

import com.example.arifitna.model.room.AppRoomRepository
import com.example.focusstart.model.room.dto.PendingInt

class GetPendingIntUseCase (
    private val repository: AppRoomRepository
) {
    suspend fun getPendingInt() : List<PendingInt>  =  repository.allInt()
    suspend fun deletePendingInt() {
        repository.delete()
    }
}