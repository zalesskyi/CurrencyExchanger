package com.example.domain.base

interface BaseUseCase<P, T> {

    operator fun invoke(param: P): T
}