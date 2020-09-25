#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform sampler2D texture;
uniform vec2 texOffset;

varying vec4 vertColor;
varying vec4 vertTexCoord;

uniform float minimum;
uniform float maximum;
uniform float centerX;
uniform float centerY;

float sq(float x) {
    return x * x;
}
float limit(float val, float minVal, float maxVal) {
    return min(max(val, minVal), maxVal);
}
void main() {
    float scale = texOffset.s / texOffset.t;
    float sqXDiff = sq(vertTexCoord.st.s - centerX * texOffset.s);
    float sqYDiff = sq(vertTexCoord.st.t - centerY * texOffset.t) * scale;
    float power = 1 - limit((sqrt(sqXDiff + sqYDiff) - minimum) / (maximum - minimum), 0, 0.5);
    gl_FragColor = texture2D(texture, vertTexCoord.st) * power;
}
