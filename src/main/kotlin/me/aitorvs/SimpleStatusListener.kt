package me.aitorvs

import twitter4j.StallWarning
import twitter4j.Status
import twitter4j.StatusDeletionNotice
import twitter4j.StatusListener


typealias OnWarning = (warning: StallWarning?) -> Unit
typealias OnException = (e: Exception?) -> Unit
typealias OnDeletionNotice = (notice: StatusDeletionNotice?) -> Unit
typealias OnStatus = (status: Status?) -> Unit
typealias OnScrubGeo = (p0: Long, p1: Long) -> Unit
typealias OnTrackLimitationNotice = (p0: Int) -> Unit

class SimpleStatusListener(
        public var doOnWarning: OnWarning,
        var doOnException: OnException,
        var doOnDeletionNotice: OnDeletionNotice,
        var doOnStatus: OnStatus,
        var doOnScrub: OnScrubGeo,
        var doOnLimitationNotice: OnTrackLimitationNotice
): StatusListener {
    override fun onTrackLimitationNotice(p0: Int) = doOnLimitationNotice(p0)

    override fun onException(p0: java.lang.Exception?) = doOnException(p0)

    override fun onStatus(p0: Status?) = doOnStatus(p0)

    override fun onScrubGeo(p0: Long, p1: Long) = doOnScrub(p0, p1)

    override fun onDeletionNotice(p0: StatusDeletionNotice?) = doOnDeletionNotice(p0)

    override fun onStallWarning(p0: StallWarning?) = doOnWarning(p0)
}