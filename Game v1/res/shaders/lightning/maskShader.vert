varying vec4 verpos;
varying vec2 varyingTexCoord0;

void main()
{
	varyingTexCoord0 = gl_MultiTexCoord0;

	verpos = gl_ModelViewMatrix * gl_Vertex;
	gl_Position = gl_ProjectionMatrix * verpos;
}