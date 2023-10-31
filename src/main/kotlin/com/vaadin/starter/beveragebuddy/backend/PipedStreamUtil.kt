package com.vaadin.starter.beveragebuddy.backend

import com.google.gson.Gson
import java.io.InputStream
import java.io.OutputStreamWriter
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

object PipedStreamUtil {
    private class NamedVirtualThreadFactory(val name: String) : ThreadFactory {
        private val id = AtomicInteger()
        override fun newThread(r: Runnable): Thread = Thread.ofVirtual()
            .name("$name-${id.incrementAndGet()}")
            .unstarted(r)
    }

    private val executorService by lazy {
        Executors.newThreadPerTaskExecutor(
            NamedVirtualThreadFactory("PipedStreamUtil")
        )
    }

    fun getInputStream(userCallback: (PipedOutputStream) -> Unit): InputStream {
        val pipedOutputStream = PipedOutputStream()
        val pipedInputStream = object : PipedInputStream(pipedOutputStream) {
            var exception: Exception? = null // possible exception from child thread
            override fun close() = exception?.let { throw it } ?: super.close()
        }
        executorService.execute { // start child thread, necessary to prevent deadlock
            try {
                userCallback(pipedOutputStream)
            } catch (userException: Exception) {
                pipedInputStream.exception = userException // pass exception to parent thead
            } finally {
                pipedOutputStream.close()
            }
        }
        return pipedInputStream
    }
}

fun Gson.toJsonAsync(json: Any): InputStream = PipedStreamUtil.getInputStream { pipedOutputStream ->
    OutputStreamWriter(pipedOutputStream, Charsets.UTF_8).use { writer ->
        toJson(json, writer)
    }
}
