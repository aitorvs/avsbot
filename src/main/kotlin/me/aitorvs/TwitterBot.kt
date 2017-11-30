package me.aitorvs

import me.aitorvs.extensions.minutes
import me.aitorvs.extensions.schedulePeriodic
import twitter4j.Status
import twitter4j.Twitter
import twitter4j.TwitterException
import twitter4j.TwitterFactory
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener

@WebListener
class TwitterBot : ServletContextListener {

    private val twitter : Twitter by lazy { TwitterFactory.getSingleton() }
    private val schedulers : ScheduledExecutorService by lazy { Executors.newSingleThreadScheduledExecutor() }

    override fun contextInitialized(p0: ServletContextEvent?) {
        println("Started!")
        schedulers.schedulePeriodic(15.minutes, Runnable {
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
                    .forEach { twitter.retweetStatus(it.id) }
        } catch (e: TwitterException) {
            println("Failed RTing with error: $e")
        }
    }

    override fun contextDestroyed(p0: ServletContextEvent?) {
        println("shutting down")
        schedulers.shutdown()
    }

}