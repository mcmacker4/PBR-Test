package net.upgaming.pbrengine.models

import net.upgaming.pbrengine.util.toFloatBuffer
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*


class Model(val vao: Int, val vertexCount: Int) {

    companion object {

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

    class Data(val vertices: FloatArray, val normals: FloatArray, val texCoords: FloatArray)

}