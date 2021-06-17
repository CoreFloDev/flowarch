package com.example.flow_arch.common.arch

import kotlinx.coroutines.flow.Flow

fun interface FlowTransformer<A, B> : (Flow<A>) -> Flow<B>
