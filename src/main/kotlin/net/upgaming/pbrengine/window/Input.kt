package net.upgaming.pbrengine.window

import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.glfwGetKey

/**
 * Created by mcmacker4
 */
class Input(val display: Display) {

    fun isKeyDown(glfwKey: Int): Boolean {
        return glfwGetKey(display.window, glfwKey) == GLFW_PRESS
    }

    fun getMouseDX(): Int {
        return display.mouseDX
    }

    fun getMouseDY(): Int {
        return display.mouseDY
    }

}