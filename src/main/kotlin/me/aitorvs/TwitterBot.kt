package me.aitorvs

import me.aitorvs.extensions.minutes
import me.aitorvs.extensions.schedulePeriodic
import twitter4j.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener

@WebListener
class TwitterBot : ServletContextListener {

    private val twitter : Twitter by lazy { TwitterFactory.getSingleton() }
    private val twitterStream : TwitterStream by lazy { TwitterStreamFactory.getSingleton() }
    private val schedulers : ScheduledExecutorService by lazy { Executors.newSingleThreadScheduledExecutor() }

    override fun contextInitialized(p0: ServletContextEvent?) {
        println("Started!")
        rtInterests()
        schedulers.schedulePeriodic(5.minutes, Runnable {
            rtMentions()
        })
    }

    private fun rtMentions() {
        println("Checking for my mentions")

        try {
            // grab the mentions from my timeline that I haven't yet RT yet
            twitter.timelines()
                    .mentionsTimeline
                    .asSequence()
                    .filterNot(Status::isRetweeted)
                    .filterNot(Status::isRetweetedByMe)
                    .forEach {
                        println("RT ${it.text}")
//                        twitter.retweetStatus(it.id)
                    }
        } catch (e: TwitterException) {
            println("Failed RTing with error: $e")
        }
    }

    private fun rtInterests() {
        val l = SimpleStatusListener(
                doOnDeletionNotice = {},
                doOnWarning = {},
                doOnException = {},
                doOnLimitationNotice = {},
                doOnScrub = { _: Long, _: Long -> },
                doOnStatus = { status ->
                    status?.let {
                        if (!it.isRetweeted) {
                            println("RT interest: ${it.text}")
//                            twitter.retweetStatus(it.id)
                        }
                    }
                }
        )

        // add the listener
        // Use helper to fix bug on https://youtrack.jetbrains.com/issue/KT-11700
        Twitter4JHelper.addStatusListner(twitterStream, l)

        // create the filter and add it to the stream
        val filter = FilterQuery()
        val track = arrayOf("#AndroidDev")
        filter.track(*track).language("en")
        // filter creates an internal thread that checks the stream and calls the listeners
        twitterStream.filter(filter)
    }

    override fun contextDestroyed(p0: ServletContextEvent?) {
        println("shutting down")
        schedulers.shutdown()
    }

}