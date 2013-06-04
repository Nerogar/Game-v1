uniform float time;
varying vec4 verpos;
uniform vec2 center;

attribute int tileID;

void main(void)
{
	verpos = gl_Vertex;
	
	if(tileID == 2){
		verpos += vec4(0, sin(time*5f + verpos.x) / 5f, 0, 0);
	}
	

	gl_Position = gl_ModelViewProjectionMatrix * verpos;

    gl_TexCoord[0] = gl_MultiTexCoord0;
}