package com.example.domain.base

interface BaseCoroutineUseCase<P, T> {

    suspend operator fun invoke(param: P): T
}