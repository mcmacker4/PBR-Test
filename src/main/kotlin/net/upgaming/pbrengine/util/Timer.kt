package net.upgaming.pbrengine.util

import org.lwjgl.glfw.GLFW.glfwGetTime


class Timer {
    
    private var last: Double = glfwGetTime()
    private var lastFPS = glfwGetTime()
    private var count = 0
    var delta: Double = 0.0
        private set
    var FPS: Int = 0
        private set
    
    fun update() {
        val now = glfwGetTime()
        delta = now - last
        
        count++
        
        if(now - lastFPS > 1000) {
            FPS = count
            count = 0
            lastFPS = now
        }
        
        last = now
    }
    
}