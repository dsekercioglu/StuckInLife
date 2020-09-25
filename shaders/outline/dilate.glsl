#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform sampler2D texture;
uniform vec2 texOffset;

varying vec4 vertColor;
varying vec4 vertTexCoord;

uniform bool horizontal;
uniform float threshold;

const vec4 lumcoeff = vec4(0.2126, 0.7152, 0.0722, 0);

void main() {
    vec4 color = texture2D(texture, vertTexCoord.st);
    for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
            color *= sign(texture2D(texture, vertTexCoord.st + vec2(i, j) * texOffset));
        }
    }
    gl_FragColor = color;
}