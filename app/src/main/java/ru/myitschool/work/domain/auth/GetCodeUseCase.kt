package ru.myitschool.work.domain.auth

import ru.myitschool.work.data.repo.AuthRepository

class GetCodeUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): String? {
        return repository.getCode()
    }
}