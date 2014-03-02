uniform vec3 camDir;
uniform mat3 matEyeSpace;
uniform float time;
uniform sampler2D texture1;

varying vec4 verpos;
varying vec3 normal;

void main(){
	//vec4 color;
	//color = vec4(0.808, 0.488, 0.055, 1); //gold
	//color = vec4(1, 0.5, 0.5, 1); //blue

	vec4 textureColor = texture2D(texture1, gl_TexCoord[0].st);

	vec3 lightDir = normalize(matEyeSpace * vec3(1 ,-1, 0));

	float light = max(0.1, dot(normalize(normal), lightDir));

	gl_FragColor = vec4((textureColor * light).xyz, textureColor.a);
	//gl_FragColor = vec4(light, light, light, 1);
	//gl_FragColor = vec4(normalize(vec3(verpos.xyz - lightPos)), 1);
}