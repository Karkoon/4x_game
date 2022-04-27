#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif
varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform vec2 u_resolution;
uniform sampler2D u_noise;
uniform float u_time;

void main()
{
	vec4 texColor = texture2D(u_texture, (v_texCoords.xy + (u_time * 0.05)));
    vec4 noise = texture2D(u_noise, (gl_FragCoord.xy/u_resolution.xy + abs(sin(u_time * 0.2)) * 0.2));

	//determine origin
	vec2 position = (gl_FragCoord.xy/u_resolution.xy) - 0.5;
	//determine the vector length of the center position
	float len = length(position);
    gl_FragColor = texColor *  vec4(vec3(1.), 0.7 * ((1. - noise.r) + abs(sin(u_time * 0.2) * 0.2))) * smoothstep(0.0, 1.0, (1.1 - len));
    //gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
}