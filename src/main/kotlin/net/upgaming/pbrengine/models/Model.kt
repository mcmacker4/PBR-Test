package net.upgaming.pbrengine.models

import net.upgaming.pbrengine.util.toFloatBuffer
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*
import java.io.File
import java.nio.file.Files


class Model(val vao: Int, val vertexCount: Int) {

    object Loader {

        private val vaos = arrayListOf<Int>()
        private val vbos = arrayListOf<Int>()

        fun load(data: Data): Model {
            val vao = createVAO()
            glBindVertexArray(vao)
            storeDataInAttributeArray(0, 3, data.vertices)
            storeDataInAttributeArray(1, 3, data.normals)
            storeDataInAttributeArray(2, 2, data.texCoords)
            glBindVertexArray(0)
            return Model(vao, data.vertices.size / 3)
        }
        
        fun loadVerticesOnly(vertices: FloatArray): Model {
            val vao = createVAO()
            glBindVertexArray(vao)
            storeDataInAttributeArray(0, 3, vertices)
            glBindVertexArray(0)
            return Model(vao, vertices.size / 3)
        }

        private fun storeDataInAttributeArray(index: Int, size: Int, data: FloatArray) {
            val buffer = data.toFloatBuffer()
            val vbo = createVBO()
            glBindBuffer(GL_ARRAY_BUFFER, vbo)
            glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)
            glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0)
            glEnableVertexAttribArray(index)
            glBindBuffer(GL_ARRAY_BUFFER, 0)
        }

        private fun createVAO(): Int {
            val vao = glGenVertexArrays()
            vaos.add(vao)
            return vao
        }

        private fun createVBO(): Int {
            val vbo = glGenBuffers()
            vbos.add(vbo)
            return vbo
        }

        fun cleanUp() {
            vbos.forEach(::glDeleteBuffers)
            vaos.forEach(::glDeleteVertexArrays)
        }

    }
    
    object OBJLoader {
        
        val vertices = arrayListOf<Vector3f>()
        val normals = arrayListOf<Vector3f>()
        val texCoords = arrayListOf<Vector2f>()
        val ordVertices = arrayListOf<Vector3f>()
        val ordNormals = arrayListOf<Vector3f>()
        val ordTexCoords = arrayListOf<Vector2f>()
        
        fun load(name: String): Model {
            clear()
            val path = "res/models/$name.obj"
            File(path).forEachLine {
                val parts = it.split(" ")
                when {
                    it.startsWith("v ") -> {
                        vertices.add(Vector3f(
                                parts[1].toFloat(),
                                parts[2].toFloat(),
                                parts[3].toFloat()
                        ))
                    }
                    it.startsWith("vn ") -> {
                        normals.add(Vector3f(
                                parts[1].toFloat(),
                                parts[2].toFloat(),
                                parts[3].toFloat()
                        ))
                    }
                    it.startsWith("vt ") -> {
                        texCoords.add(Vector2f(
                                parts[1].toFloat(),
                                parts[2].toFloat()
                        ))
                    }
                    it.startsWith("f ") -> {
                        for(i in 1..3) {
                            val ind = parts[i].split("/")
                            ordVertices.add(vertices[ind[0].toInt() - 1])
                            ordNormals.add(normals[ind[2].toInt() - 1])
                            ordTexCoords.add(texCoords[ind[1].toInt() - 1])
                        }
                    }
                }
            }
            val verticesArray = FloatArray(ordVertices.size * 3)
            val normalsArray = FloatArray(ordNormals.size * 3)
            val texCoordsArray = FloatArray(ordTexCoords.size * 2)
            var count = 0
            ordVertices.forEach {
                verticesArray[count++] = it.x
                verticesArray[count++] = it.y
                verticesArray[count++] = it.z
            }
            count = 0
            ordNormals.forEach { 
                normalsArray[count++] = it.x
                normalsArray[count++] = it.y
                normalsArray[count++] = it.z
            }
            count = 0
            ordTexCoords.forEach { 
                texCoordsArray[count++] = it.x
                texCoordsArray[count++] = it.y
            }
            return Model.Loader.load(Data(verticesArray, normalsArray, texCoordsArray))
        }
        
        private fun clear() {
            vertices.clear()
            normals.clear()
            ordVertices.clear()
            ordNormals.clear()
        }
        
    }

    class Data(val vertices: FloatArray, val normals: FloatArray, val texCoords: FloatArray)

}