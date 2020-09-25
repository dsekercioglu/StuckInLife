#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform sampler2D texture;
uniform vec2 texOffset;

varying vec4 vertColor;
varying vec4 vertTexCoord;

uniform float threshold;

const vec4 lumcoeff = vec4(0.2126, 0.7152, 0.0722, 0);

void main() {
    vec4 color = texture2D(texture, vertTexCoord.st);
    gl_FragColor = vec4(color.rgb * 2 * sign(max(dot(color, lumcoeff) - threshold, 0.0)), 1.0);
}