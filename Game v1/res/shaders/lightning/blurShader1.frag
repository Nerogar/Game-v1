#resolution
uniform sampler2D effectColorTex;

varying vec4 verpos;
varying vec2 varyingTexCoord0;

vec4 blur(){
	vec4 sum = vec4(0,0,0,0);
	//float dist = 0.0015;
	float dist = 1.0 / resolution.x;

	float min = dist / 2.0;
	float max = 1.0 - (dist / 2.0);

    sum += texture2D(effectColorTex, vec2(clamp(varyingTexCoord0.x - 2.0 * dist, min, max), varyingTexCoord0.y)) * 0.05;
    sum += texture2D(effectColorTex, vec2(clamp(varyingTexCoord0.x - dist, min, max), varyingTexCoord0.y)) * 0.2;
    sum += texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y)) * 0.5;
    sum += texture2D(effectColorTex, vec2(clamp(varyingTexCoord0.x + dist, min, max), varyingTexCoord0.y)) * 0.2;
    sum += texture2D(effectColorTex, vec2(clamp(varyingTexCoord0.x + 2.0 * dist, min, max), varyingTexCoord0.y)) * 0.05;

	return sum;
}

vec4 nop(){
    return texture2D(effectColorTex, vec2(varyingTexCoord0.x, varyingTexCoord0.y));
}

void main(){
	gl_FragColor = blur();
}