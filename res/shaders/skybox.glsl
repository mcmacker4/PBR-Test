#shader vertex
#version 330 core

layout (location = 0) in vec3 position;

out vec3 texCoords;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void) {

    gl_Position = projectionMatrix * viewMatrix * vec4(position, 1.0);
    texCoords = position;

}

#shader fragment
#version 330 core

in vec3 texCoords;
out vec4 FragColor;

uniform samplerCube skybox;

void main(void) {
    FragColor = texture(skybox, texCoords);
}