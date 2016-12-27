package net.upgaming.pbrengine.util

import org.lwjgl.glfw.GLFW.glfwGetTime


class Timer {
    
    private var last: Double = glfwGetTime()
    var delta: Double = 0.0
        private set
    
    fun update() {
        val now = glfwGetTime()
        delta = now - last
        last = now
    }
    
}