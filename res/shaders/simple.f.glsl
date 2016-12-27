#version 330 core

in vec3 _pos;

out vec4 color;

void main() {

    color = vec4(_pos.x + 0.5, _pos.y + 0.5, _pos.z + 0.5, 1.0);

}
