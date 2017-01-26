package net.upgaming.pbrengine.util

import org.joml.Vector3f
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer


fun FloatArray.toFloatBuffer(): FloatBuffer {
    val buffer = MemoryUtil.memAllocFloat(size)
    buffer.put(this)
    buffer.flip()
    return buffer
}

fun IntArray.toIntBuffer(): IntBuffer {
    val buffer = MemoryUtil.memAllocInt(size)
    buffer.put(this)
    buffer.flip()
    return buffer
}

operator fun Vector3f.unaryMinus(): Vector3f {
    return Vector3f(-x, -y, -z)
}