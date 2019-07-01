package com.example.producer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

class MainActivity : AppCompatActivity() {

    private lateinit var sharedQueue: BlockingQueue<Any>
    private var executor: ExecutorService? = null
    private var producerTask: Runnable? = null
    private var consumerTask: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedQueue = LinkedBlockingQueue()
        executor = Executors.newCachedThreadPool()
        producer()
        consumer()
    }

    fun producer() {

        producerTask = Runnable {
            while (true) {
                try {
                    var value = 0
                    while (!Thread.currentThread().isInterrupted) {
                        value++
                        Log.i("Producer", value.toString())
                        sharedQueue.put(value)
                        Thread.sleep(1000)
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    Thread.currentThread().interrupt()
                }

            }
        }
        executor!!.execute(producerTask)

    }

    fun consumer() {

        consumerTask = Runnable {
            try {
                while (!Thread.currentThread().isInterrupted) {
                    val value = sharedQueue.take() as Int
                    Log.i("Consumer", value.toString())
                    Thread.sleep(2000)


                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
                Thread.currentThread().interrupt()
            }
        }
        executor!!.execute(consumerTask)
    }


    override fun onDestroy() {
        super.onDestroy()
        executor!!.shutdownNow()

    }


}
