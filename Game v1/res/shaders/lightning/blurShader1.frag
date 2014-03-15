#resolution
uniform sampler2D effectColorTex;

varying vec4 verpos;
varying vec2 varyingTexCoord0;

vec4 blur(){
	vec4 sum = vec4(0,0,0,0);
	//float dist = 0.0015;
	float dist = 1.0 / resolution.x;

/*
    sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x - 4.0 * dist, varyingTexCoord0.y)) * 0.05;
    sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x - 3.0 * dist, varyingTexCoord0.y)) * 0.09;
    sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x - 2.0 * dist, varyingTexCoord0.y)) * 0.12;
    sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x - dist, varyingTexCoord0.y)) * 0.15;
    sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y)) * 0.16;
    sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x + dist, varyingTexCoord0.y)) * 0.15;
    sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x + 2.0 * dist, varyingTexCoord0.y)) * 0.12;
    sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x + 3.0 * dist, varyingTexCoord0.y)) * 0.09;
    sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x + 4.0 * dist, varyingTexCoord0.y)) * 0.05;
*/

    sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x - 2.0 * dist, varyingTexCoord0.y)) * 0.05;
    sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x - dist, varyingTexCoord0.y)) * 0.2;
    sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y)) * 0.5;
    sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x + dist, varyingTexCoord0.y)) * 0.2;
    sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x + 2.0 * dist, varyingTexCoord0.y)) * 0.05;

	return sum;
}

vec4 nop(){
    return texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y));
}

void main(){
	gl_FragColor = blur();
}