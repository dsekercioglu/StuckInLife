#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform sampler2D texture;
uniform vec2 texOffset;

varying vec4 vertColor;
varying vec4 vertTexCoord;

uniform float filterSize;

uniform float weights[5] = float[] (0.227027, 0.1945946, 0.1216216, 0.054054, 0.016216);

void main() {
    vec4 mainColor = texture2D(texture, vertTexCoord.st);
    vec4 color = mainColor * weights[0];
    for (int i = 1; i < 5; i++) {
        vec2 offset = vec2(i * texOffset.s * filterSize, 0);
        vec4 c = texture2D(texture, vertTexCoord.st + offset) + texture2D(texture, vertTexCoord.st - offset);
        color += c * weights[i];
    }
    gl_FragColor = color;
}