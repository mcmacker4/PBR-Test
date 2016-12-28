#version 330 core

#define M_PI 3.1415926535897932384626433832795

in vec3 _position;
in vec3 _normal;

out vec4 color;

uniform samplerCube skybox;
uniform vec3 campos;

void main(void) {

    vec3 normal = normalize(_normal);
    vec3 toCam = campos - _position;
    
    vec3 reflected = reflect(-toCam, normal);
    
    color = texture(skybox, reflected);
    
}
