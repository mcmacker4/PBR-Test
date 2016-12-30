package net.upgaming.pbrengine.graphics

import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.*
import java.io.File
import java.nio.FloatBuffer
import java.nio.file.Files

class ShaderProgram(private val program: Int) {
    
    fun start() {
        glUseProgram(program)
    }
    
    fun stop() {
        glUseProgram(program)
    }
    
    fun loadFloat(varname: String, value: Float) {
        val loc = glGetUniformLocation(program, varname)
        glUniform1f(loc, value)
    }
    
    fun loadInt(varname: String, value: Int) {
        val loc = glGetUniformLocation(program, varname)
        glUniform1i(loc, value)
    }
    
    fun loadVector3f(varname: String, vec: Vector3f) {
        val loc = glGetUniformLocation(program, varname)
        glUniform3f(loc, vec.x, vec.y, vec.z)
    }
    
    fun loadMatrix4f(varname: String, buffer: FloatBuffer) {
        val loc = glGetUniformLocation(program, varname)
        glUniformMatrix4fv(loc, false, buffer)
    }
    
    fun delete() {
        glDeleteProgram(program)
    }
    
    companion object {
        
        fun load(name: String): ShaderProgram {
            
            val sources = hashMapOf<String, String>()
            
            var currentShader = ""
            var currentSrc = ""
            
            Files.lines(File("res/shaders/$name.glsl").toPath()).forEach { 
                if(it.startsWith("#shader")) {
                    if(!currentShader.isEmpty())
                        sources.put(currentShader, currentSrc)
                    currentShader = it.split(" ")[1]
                    currentSrc = String()
                } else {
                    currentSrc += it + "\n"
                }
            }
            
            if(!currentShader.isEmpty())
                sources.put(currentShader, currentSrc)
            
            return load(sources["vertex"]!!, sources["fragment"]!!)
        }
        
        fun load(vsrc: String, fsrc: String): ShaderProgram {
            val program = glCreateProgram()
            val vshader = createShader(GL_VERTEX_SHADER, vsrc)
            val fshader = createShader(GL_FRAGMENT_SHADER, fsrc)
            glAttachShader(program, vshader)
            glAttachShader(program, fshader)
            glLinkProgram(program)
            if(glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE)
                throw Exception(glGetProgramInfoLog(program))
            glValidateProgram(program)
            if(glGetProgrami(program, GL_VALIDATE_STATUS) != GL_TRUE)
                throw Exception(glGetProgramInfoLog(program))
            glDeleteShader(vshader)
            glDeleteShader(fshader)
            return ShaderProgram(program)
        }

        private fun createShader(type: Int, src: String): Int {
            val shader = glCreateShader(type)
            glShaderSource(shader, src)
            glCompileShader(shader)
            if(glGetShaderi(shader, GL_COMPILE_STATUS) != GL_TRUE)
                throw Exception(glGetShaderInfoLog(shader))
            return shader
        }

    }
    
}