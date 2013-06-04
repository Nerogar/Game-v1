uniform mat4 transformationMatrix;

void main()
{
	gl_Position = gl_ModelViewProjectionMatrix * transformationMatrix * gl_Vertex;
	//gl_Position = transformationMatrix * gl_Position;
	gl_FrontColor = gl_Color;
}