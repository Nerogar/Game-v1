uniform float time;
uniform vec3 camDir;

varying vec4 verpos;
varying vec3 normal;

void main(){
	verpos = gl_Vertex;

	gl_Position = gl_ModelViewProjectionMatrix * verpos;
	normal = normalize(gl_NormalMatrix * gl_Normal);
    gl_TexCoord[0] = gl_MultiTexCoord0;
}