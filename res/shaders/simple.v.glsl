#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 normal;
layout(location = 2) in vec2 texCoord;

out vec3 _position;
out vec3 _normal;
out vec2 _texCoords;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

void main() {

    vec4 transformedPos = modelMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * transformedPos;
    
    _position = transformedPos.xyz;
    _normal = (modelMatrix * vec4(normal, 0.0)).xyz;
    _texCoords = texCoord;

}
