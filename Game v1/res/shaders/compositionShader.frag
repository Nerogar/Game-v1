#resolution
uniform sampler2D worldColorTex;
uniform sampler2D effectColorTex;

uniform sampler2D guiColorTex;

varying vec4 verpos;
varying vec2 varyingTexCoord0;

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

float linearize(in float color){
	float f = 100.0;
	float n = .08;
	float z = (2 * n) / (f + n - color * (f - n));
	return z;
}

vec4 blur(){
	vec4 sum = vec4(0,0,0,0);
	float dist = 1.0 / resolution.y;
/*
	sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y - 4.0 * dist)) * 0.05;
	sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y - 3.0 * dist)) * 0.09;
	sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y - 2.0 * dist)) * 0.12;
	sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y - dist)) * 0.15;
	sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y)) * 0.16;
	sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y + dist)) * 0.15;
	sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y + 2.0 * dist)) * 0.12;
	sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y + 3.0 * dist)) * 0.09;
	sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y + 4.0 * dist)) * 0.05;
*/


	sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y - 2.0 * dist)) * 0.05;
	sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y - dist)) * 0.2;
	sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y)) * 0.5;
	sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y + dist)) * 0.2;
	sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y + 2.0 * dist)) * 0.05;

	return sum * 5.;
	//return sum * length(sum) * 1;
}

vec4 nop(){

	return texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y)) * 5.;

}

void main(){
	
	
	vec4 color = texture2D(worldColorTex, varyingTexCoord0.st);
	color += blur(); //effect
	
	vec4 guiColor = texture2D(guiColorTex, varyingTexCoord0.st);
	
	gl_FragColor = color * vec4(1.-guiColor.a, 1.-guiColor.a, 1.-guiColor.a, 1.) +  guiColor * vec4(guiColor.a, guiColor.a, guiColor.a, 1.);

}