package net.upgaming.pbrengine.models

import net.upgaming.pbrengine.util.toFloatBuffer
import net.upgaming.pbrengine.util.toIntBuffer
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*
import java.io.File


open class Model(val vao: Int, val vertexCount: Int) {

    object Loader {

        private val vaos = arrayListOf<Int>()
        private val vbos = arrayListOf<Int>()

        fun load(data: Data): Model {
            val vao = createVAO()
            glBindVertexArray(vao)
            storeIndices(data.indices)
            storeDataInAttributeArray(0, 3, data.vertices)
            storeDataInAttributeArray(1, 3, data.normals)
            storeDataInAttributeArray(2, 2, data.texCoords)
            glBindVertexArray(0)
            return Model(vao, data.indices.size)
        }
        
        fun loadVerticesOnly(vertices: FloatArray): Model {
            val vao = createVAO()
            glBindVertexArray(vao)
            storeDataInAttributeArray(0, 3, vertices)
            glBindVertexArray(0)
            return Model(vao, vertices.size / 3)
        }
        
        private fun storeIndices(indices: IntArray) {
            val buffer = indices.toIntBuffer()
            val vbo = createVBO()
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo)
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)
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
        
        val sIndices = arrayListOf<String>()
        
        fun load(name: String): Model {
            clear()
            ClassLoader.getSystemClassLoader()
                    .getResourceAsStream("models/$name.obj").bufferedReader().forEachLine {
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
                        (1..3).mapTo(sIndices) { parts[it] }
                    }
                }
            }

            val verticesArray = FloatArray(vertices.size * 3)
            val normalsArray = FloatArray(normals.size * 3)
            val texCoordsArray = FloatArray(texCoords.size * 2)
            val indices = arrayListOf<Int>()
            
            for(s in sIndices) {
                val parts = s.split("/")
                val index = parts[0].toInt() - 1
                indices.add(index)
                verticesArray[index*3] = vertices[index].x
                verticesArray[index*3+1] = vertices[index].y
                verticesArray[index*3+2] = vertices[index].z
                val nIndex = parts[2].toInt() - 1
                normalsArray[index*3] = normals[nIndex].x
                normalsArray[index*3+1] = normals[nIndex].y
                normalsArray[index*3+2] = normals[nIndex].z
                val tIndex = parts[1].toInt() - 1
                texCoordsArray[index*2] = texCoords[tIndex].x
                texCoordsArray[index*2+1] = 1 - texCoords[tIndex].y
            }
            
            return Model.Loader.load(Data(verticesArray, normalsArray, texCoordsArray, indices.toIntArray()))
        }
        
        private fun clear() {
            vertices.clear()
            normals.clear()
            texCoords.clear()
            sIndices.clear()
        }
        
    }

    class Data(val vertices: FloatArray, val normals: FloatArray, val texCoords: FloatArray, val indices: IntArray)

}