uniform float time;
varying vec4 verpos;

void main(void)
{
	//verpos = gl_ModelViewMatrix * gl_Vertex;
	verpos = gl_Vertex;

	vec4 newPos = vec4(verpos.x, verpos.y, verpos.z, 1);

	//gl_Position = gl_ModelViewProjectionMatrix * newPos;
	gl_Position = gl_ProjectionMatrix * gl_ModelViewMatrix * newPos;
}