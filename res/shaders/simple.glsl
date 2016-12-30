#shader vertex
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

#shader fragment
#version 330 core

#define M_PI 3.1415926535897932384626433832795

in vec3 _position;
in vec3 _normal;
in vec2 _texCoords;
in vec3 _toLightVectors[4];

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

const uint NumSamples = 128u;


//vec3 fresnelSchlick(float cosTheta, float roughness, vec3 F0) {
//    return F0 + (max(vec3(1.0 - roughness), F0) - F0) * pow(1.0 - cosTheta, 5.0);
//}

float radicalInverse_VdC(uint bits) {
    bits = (bits << 16u) | (bits >> 16u);
    bits = ((bits & 0x55555555u) << 1u) | ((bits & 0xAAAAAAAAu) >> 1u);
    bits = ((bits & 0x33333333u) << 2u) | ((bits & 0xCCCCCCCCu) >> 2u);
    bits = ((bits & 0x0F0F0F0Fu) << 4u) | ((bits & 0xF0F0F0F0u) >> 4u);
    bits = ((bits & 0x00FF00FFu) << 8u) | ((bits & 0xFF00FF00u) >> 8u);
    return float(bits) * 2.3283064365386963e-10; // / 0x100000000
}

vec2 hammersley2d(uint i, uint N) {
    return vec2(float(i)/float(N), radicalInverse_VdC(i));
}

float GeometrySchlickGGX(float NdotV, float roughness)
{
    float r = (roughness + 1.0);
    float k = (r*r) / 8.0;

    float nom   = NdotV;
    float denom = NdotV * (1.0 - k) + k;
    
    return nom / denom;
	
}

float GeometrySmith(float NoV, float NoL, float roughness)
{
    float ggx2  = GeometrySchlickGGX(NoV, roughness);
    float ggx1  = GeometrySchlickGGX(NoL, roughness);
	
    return ggx1 * ggx2;
}

vec3 ImportanceSampleGGX(vec2 Xi, float Roughness, vec3 N ) {
    float a = Roughness * Roughness;
    float Phi = 2 * M_PI * Xi.x;
    float CosTheta = sqrt( (1 - Xi.y) / ( 1 + (a*a - 1) * Xi.y ) );
    float SinTheta = sqrt( 1 - CosTheta * CosTheta );
    
    vec3 H = vec3(
        SinTheta * cos( Phi ),
        SinTheta * sin( Phi ),
        CosTheta
    );
    
    vec3 UpVector = abs(N.z) < 0.999 ? vec3(0,0,1) : vec3(1,0,0);
    vec3 TangentX = normalize( cross( UpVector, N ) );
    vec3 TangentY = cross( N, TangentX );
    // Tangent to world space
    return TangentX * H.x + TangentY * H.y + N * H.z;
}

vec3 PrefilterEnvMap( float Roughness, vec3 R ) {
    vec3 N = R;
    vec3 V = R;
    vec3 PrefilteredColor = vec3(0);
    float TotalWeight = 0.0;
    for( uint i = 0u; i < NumSamples; i++ ) {
        vec2 Xi = hammersley2d( i, NumSamples );
        vec3 H = ImportanceSampleGGX( Xi, Roughness, N );
        vec3 L = 2 * dot( V, H ) * H - V;
        float NoL = clamp( dot( N, L ), 0.0, 1.0 );
        if( NoL > 0 ) {
            PrefilteredColor += texture(skybox, L).xyz * NoL;
            TotalWeight += NoL;
        }
    }
    return PrefilteredColor / TotalWeight;
}

vec2 IntegrateBRDF( float roughness, float NoV, vec3 N ) {
    vec3 V = vec3(
        sqrt( 1.0f - NoV * NoV ), // sin
        0,
        NoV // cos
    );
    float A = 0;
    float B = 0;
    for( uint i = 0u; i < NumSamples; i++ ) {
        vec2 Xi = hammersley2d( i, NumSamples );
        vec3 H = ImportanceSampleGGX( Xi, roughness, N );
        vec3 L = 2 * dot( V, H ) * H - V;
        float NoL = clamp( L.z, 0.0, 1.0 );
        float NoH = clamp( H.z, 0.0, 1.0 );
        float VoH = clamp( dot( V, H ), 0.0, 1.0 );
        if( NoL > 0 ) {
            float G = GeometrySmith( NoV, NoL, roughness );
            float G_Vis = G * VoH / (NoH * NoV);
            float Fc = pow( 1 - VoH, 5 );
            A += (1 - Fc) * G_Vis;
            B += Fc * G_Vis;
        }
    }
    return vec2( A, B ) / NumSamples;
}

vec3 ApproximateSpecularIBL( vec3 SpecularColor , float Roughness, vec3 N, vec3 V ) {
    float NoV = clamp( dot( N, V ), 0.0, 1.0 );
    vec3 R = 2 * dot( V, N ) * N - V;
    vec3 PrefilteredColor = PrefilterEnvMap( Roughness, R );
    vec2 EnvBRDF = IntegrateBRDF( Roughness, NoV, N );
    return PrefilteredColor * ( SpecularColor * EnvBRDF.x + EnvBRDF.y );
}

void main(void) {

    vec3 N = normalize(_normal);
    vec3 V = normalize(cameraPos - _position);
    
    vec3 color = ApproximateSpecularIBL(material.color, material.roughness, N, V);
    color = pow(color, vec3(1.0/2.2));
    
    FragColor = vec4(color, 1.0);

}
