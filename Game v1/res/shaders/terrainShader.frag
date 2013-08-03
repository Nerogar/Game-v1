varying vec4 verpos;
uniform float time;
uniform sampler2D texture1;

uniform vec2 buildQuadA;
uniform vec2 buildQuadB;
uniform bool buildQuadRender;
uniform vec4 buildQuadColor;

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
	vec4 textureColor = texture2D(texture1, gl_TexCoord[0].st) * heightMult;

	if(buildQuadRender){
		if((verpos.x > buildQuadA.x && verpos.x < buildQuadB.x) || (verpos.x < buildQuadA.x && verpos.x > buildQuadB.x)){
			if((verpos.z > buildQuadA.y && verpos.z < buildQuadB.y) || (verpos.z < buildQuadA.y && verpos.z > buildQuadB.y)){
				float brightness = (textureColor.r + textureColor.g + textureColor.b) / 3.0;
				float n = 1.0;
				textureColor = vec4((brightness + n*buildQuadColor.r)/(n+1), (brightness + buildQuadColor.g)/(n+1), (brightness + buildQuadColor.b)/(n+1), textureColor.w);
			}
		}
	}

	gl_FragColor = textureColor;

}