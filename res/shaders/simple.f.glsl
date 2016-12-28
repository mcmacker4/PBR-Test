#version 330 core

#define M_PI 3.1415926535897932384626433832795

in vec3 _position;
in vec3 _normal;
in vec2 _texCoords;

out vec4 FragColor;

uniform samplerCube skybox;
uniform vec3 cameraPos;

uniform struct Material {
    vec3 color;
    float roughness;
    float metallic;
} material;

uniform struct PointLight {
    vec3 position;
    vec3 color;
} pointLights[4];



vec3 fresnelSchlick(float cosTheta, vec3 F0, float roughness) {
    return F0 + (max(vec3(1.0 - roughness), 0.0) - F0) * pow(1.0 - cosTheta, 5.0);
}

float DistributionGGX(vec3 N, vec3 H, float roughness) {
    float a      = roughness*roughness;
    float a2     = a*a;
    float NdotH  = max(dot(N, H), 0.0);
    float NdotH2 = NdotH*NdotH;
	
    float nom   = a2;
    float denom = (NdotH2 * (a2 - 1.0) + 1.0);
    denom = M_PI * denom * denom;
	
    return nom / denom;
}

float GeometrySchlickGGX(float NdotV, float roughness) {
    float r = (roughness + 1.0);
    float k = (r*r) / 8.0;

    float nom   = NdotV;
    float denom = NdotV * (1.0 - k) + k;
	
    return nom / denom;
}

float GeometrySmith(vec3 N, vec3 V, vec3 L, float roughness) {
    float NdotV = max(dot(N, V), 0.0);
    float NdotL = max(dot(N, L), 0.0);
    float ggx2  = GeometrySchlickGGX(NdotV, roughness);
    float ggx1  = GeometrySchlickGGX(NdotL, roughness);
	
    return ggx1 * ggx2;
}

void main(void) {

    vec3 N = normalize(_normal);
    vec3 V = normalize(cameraPos - _position);
    
    vec3 F0 = vec3(0.04);
    F0 = mix(F0, material.color, material.metallic);
    vec3 F = fresnelSchlick(max(dot(N, V), 0.0), F0, material.roughness);
    
    vec3 kS = F;
    vec3 kD = vec3(1.0) - kS;
    
    kD *= 1.0 - material.metallic;
    
    vec3 Lo = vec3(0.0);
    for(int i = 0; i < 4; i++) {
        
        vec3 L = normalize(pointLights[i].position - _position);
        vec3 H = normalize(V + L);
        
        float distance = length(pointLights[i].position - _position);
        float attenuation = 1.0 / distance * distance;
        vec3 radiance = pointLights[i].color * attenuation;
        
        float NDF = DistributionGGX(N, H, material.roughness);
        float G = GeometrySmith(N, V, L, material.roughness);
        
        vec3 nominator = NDF * G * F;
        float denominator = 4 * max(dot(V, N), 0.0) * max(dot(L, N), 0.0) + 0.001;
        vec3 brdf = nominator / denominator;
        
        float NdotL = max(dot(N, L), 0.0);
        Lo += (kD  * material.color / M_PI + brdf) * radiance * NdotL;
        
    }
    
    vec3 ambient = vec3(0.03) * material.color;
    vec3 color = ambient + Lo;
    
    color = color / (color + vec3(1.0));
    color = pow(color, vec3(1.0/2.2)); 
    
    FragColor = vec4(color, 1.0);
}
