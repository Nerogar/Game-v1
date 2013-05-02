uniform float time;
varying vec4 verpos;
uniform vec2 center;

void main(void)
{
	verpos = gl_Vertex;
	gl_Position = gl_ProjectionMatrix * gl_ModelViewMatrix * verpos;

    gl_TexCoord[0] = gl_MultiTexCoord0;
}