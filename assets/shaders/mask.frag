#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif
varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
//uniform sampler2D u_mask;
uniform vec2 u_resolution;
void main()
{
    vec4 texColor = texture2D(u_texture, v_texCoords);
    //determine origin
    vec2 position = (gl_FragCoord.xy / u_resolution.xy) - 0.5;

    vec2 temp = vec2(gl_FragCoord.xy / u_resolution.xy);
    temp.y = 1. - temp.y;
    //vec4 maskValue = texture2D(u_mask, temp);

    //determine the vector length of the center position
    float len = length(position) - 0.05;

    gl_FragColor = vec4(vec3(1.0 - len), 1.0) * texColor * 1.2, 0.1;
    //gl_FragColor = vec4(texColor.xyz, maskValue.r);
}
