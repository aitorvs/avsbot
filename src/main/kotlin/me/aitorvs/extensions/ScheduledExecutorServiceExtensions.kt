package me.aitorvs.extensions

import java.util.concurrent.ScheduledExecutorService

fun ScheduledExecutorService.schedulePeriodic(period: Interval<TimeUnit>, command: Runnable)
        = scheduleAtFixedRate(command, 0, period.inSeconds.longValue, java.util.concurrent.TimeUnit.SECONDS)