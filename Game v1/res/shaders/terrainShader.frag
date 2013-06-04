varying vec4 verpos;
uniform float time;
uniform vec2 center;
//uniform float width;
uniform sampler2D texture1;

void main(){
	/*vec4 color;
	//color = vec4(0.808, 0.488, 0.055, 1); //gold
	color = vec4(0.008, 0.328, 0.680, 1); //blue
	float pi = 3.14159;
	float twopi = pi * 2.0;
	float speed = 500.0;
	float width = 50.0;
	float waveMult = 0.0;
	float distMult = 0.0;

	vec2 pos = verpos - center;
	float dist = sqrt(pos.x * pos.x + pos.y * pos.y) + width;
	dist = ((dist - time * speed) / (width / twopi));

	if(dist >=0 && dist < pi * 2){
		waveMult = (-cos(dist)) / 2 + 0.5;
	}

	distMult = (1 / time / 30);

	float colorR = color.r * waveMult * distMult;
	float colorG = color.g * waveMult * distMult;
	float colorB = color.b * waveMult * distMult;
	float colorA = color.a * waveMult * distMult;

	gl_FragColor = vec4(colorR, colorG, colorB, colorA);*/

	float heightMult = (verpos.y / 5) + 0.4;
	gl_FragColor = texture2D(texture1, gl_TexCoord[0].st) * heightMult;
}