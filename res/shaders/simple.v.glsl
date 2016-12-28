#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 normal;
layout(location = 2) in vec2 texCoord;

out vec3 _position;
out vec3 _normal;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

uniform float roughness;

void main() {

    vec4 tpos = modelMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * tpos;
    _position = tpos.xyz;
    _normal = (modelMatrix * vec4(normal, 1.0)).xyz;

}
