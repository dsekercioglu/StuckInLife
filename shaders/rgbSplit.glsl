#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform sampler2D texture;
uniform vec2 texOffset;

varying vec4 vertColor;
varying vec4 vertTexCoord;

uniform float alter;

void main() {
    vec2 altered = texOffset * alter;
    vec2 tc0 = vertTexCoord.st + vec2(-altered.s, 0);
    vec2 tc1 = vertTexCoord.st + vec2(0, 0);
    vec2 tc2 = vertTexCoord.st + vec2(+altered.s, 0);

    vec4 rt = texture2D(texture, tc0);
    vec4 gt = texture2D(texture, tc1);
    vec4 bt = texture2D(texture, tc2);


    vec4 sum = vec4(rt.rgb.r, gt.rgb.g, bt.rgb.b, 1.0);
    gl_FragColor = vec4(sum.rgb, 1.0);
}