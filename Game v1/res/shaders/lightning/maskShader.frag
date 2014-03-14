uniform sampler2D worldDepthTex;
uniform sampler2D effectColorTex;
uniform sampler2D effectDepthTex;

varying vec4 verpos;
varying vec2 varyingTexCoord0;

void main(){
	vec4 color;
	float worldDepth = texture2D(worldDepthTex, varyingTexCoord0.st);
	float effectDepth = texture2D(effectDepthTex, varyingTexCoord0.st);
	
	
	if(worldDepth > effectDepth){
		color = texture2D(effectColorTex, varyingTexCoord0.st);
	}else{
		color = 0.0;
	}

	gl_FragColor = color;

}