package com.cjx.mvi

/**
 * MVI 架构中的 Event (或 Side Effect) 接口。
 * 用于处理一次性的、不应被重复消费的事件，如 Toast、Navigation 等。
 */
interface IEvent