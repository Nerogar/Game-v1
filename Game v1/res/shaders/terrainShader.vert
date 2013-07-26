uniform float time;
varying vec4 verpos;
uniform vec2 center;

attribute int tileID;
attribute int tileID00;
attribute int tileID01;
attribute int tileID10;
attribute int tileID11;

void main(){
	verpos = gl_Vertex;
	
	if(tileID00 == 2 && tileID01 == 2 && tileID10 == 2 && tileID11 == 2){
		verpos += vec4(0, sin(time * 5.0 + verpos.x) / 5.0, 0, 0);
	}

	gl_Position = gl_ModelViewProjectionMatrix * verpos;

    gl_TexCoord[0] = gl_MultiTexCoord0;
}