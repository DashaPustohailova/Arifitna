package com.example.arifitna.use_case

import com.example.arifitna.model.room.AppRoomRepository
import com.example.arifitna.model.room.dto.PendingInt

class SavePendingIntUseCase(
    private val repository: AppRoomRepository
) {
    suspend fun save(randomInt: Int) {
        repository.insert(PendingInt(id = randomInt))
    }
}