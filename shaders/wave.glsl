#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform sampler2D texture;
uniform vec2 texOffset;

varying vec4 vertColor;
varying vec4 vertTexCoord;

uniform float alter;
uniform float power;
uniform float offset;

void main() {
    vec2 altered = texOffset * alter;
    float shift = sin(offset + vertTexCoord.st.y * alter) * power;
    vec2 drawPixel = vertTexCoord.st + vec2(altered.s * shift, 0);
    gl_FragColor = texture2D(texture, drawPixel);
}
