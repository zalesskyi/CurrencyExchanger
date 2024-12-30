package com.example.domain.base

import kotlinx.coroutines.flow.Flow

interface BaseFlowUseCase<T> {

    operator fun invoke(): Flow<Result<T>>
}