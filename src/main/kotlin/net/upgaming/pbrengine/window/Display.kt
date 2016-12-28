package net.upgaming.pbrengine.window

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL


class Display(var width: Int, var height: Int, title: String) {
    
    val window: Long
    
    var mouseDX = 0
        private set
    var mouseDY = 0
        private set
    
    private var mouseX = 0
    private var mouseY = 0
    
    init {
        if(!glfwInit())
            throw IllegalStateException("Could not initialize GLFW")
        
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        
        window = glfwCreateWindow(width, height, title, NULL, NULL)
        
        val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())
        glfwSetWindowPos(
                window,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        )
        
        glfwSetKeyCallback(window, { window, key, scancode, action, mods -> 
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                markForDestruction()
        })
        
        glfwSetWindowSizeCallback(window, { window, width, height ->
            this.width = width
            this.height = height
            glViewport(0, 0, width, height)
        })
        
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
        
        glfwMakeContextCurrent(window)
        GL.createCapabilities()
        
        glfwSwapInterval(0)
        
        glClearColor(0.3f, 0.6f, 0.9f, 1f)
        glEnable(GL_DEPTH_TEST)
        
    }
    
    fun update() {
        mouseUpdate()
        glfwSwapBuffers(window)
        glfwPollEvents()
    }
    
    private fun mouseUpdate() {
        val amx = DoubleArray(1)
        val amy = DoubleArray(1)
        glfwGetCursorPos(window, amx, amy)
        val mx = amx[0].toInt()
        val my = amy[0].toInt()
        mouseDX = mx - mouseX
        mouseDY = my - mouseY
        mouseX = mx
        mouseY = my
    }
    
    fun shouldClose(): Boolean {
        return glfwWindowShouldClose(window)
    }
    
    fun markForDestruction() {
        glfwSetWindowShouldClose(window, true)
    }
    
    fun isKeyDown(glfwKey: Int): Boolean {
        return glfwGetKey(window, glfwKey) == GLFW_PRESS
    }
    
    fun isBtnDown(glfwBtn: Int): Boolean{
        return glfwGetMouseButton(window, glfwBtn) == GLFW_PRESS
    }

    fun destroy() {
        glfwDestroyWindow(window)
    }

}