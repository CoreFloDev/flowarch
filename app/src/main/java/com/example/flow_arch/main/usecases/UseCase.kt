package com.example.flow_arch.main.usecases

import com.example.flow_arch.main.arch.FlowTransformer

fun interface UseCase<A : Action>: () -> FlowTransformer<A, Result>
