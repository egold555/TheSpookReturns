#version 400

in vec3 textureCoords;
out vec4 out_Color;

uniform samplerCube cubeMap;
uniform float brightness;

void main(void){
    out_Color = texture(cubeMap, textureCoords) * brightness;
}