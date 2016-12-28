#version 330 core

#define M_PI 3.1415926535897932384626433832795

in vec3 _position;
in vec3 _normal;

out vec4 color;

vec3 lightPos = vec3(3, 3, 3);

void main(void) {

    vec3 normal = normalize(_normal);
    vec3 toLight = normalize(lightPos - _position);
    
    float brightness = max(dot(normal, toLight), 0.0);
    color = vec4(brightness, brightness, brightness, 1.0);
    
}
