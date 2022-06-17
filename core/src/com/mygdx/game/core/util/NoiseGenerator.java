package com.mygdx.game.core.util;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class NoiseGenerator {

  public float[][] generateWhiteNoise(int width, int height) {
    var noise = new float[width][height];
    for (var y = 0; y < height; y++) {
      for (var x = 0; x < width; x++) {
        noise[x][y] = MathUtils.random();
      }
    }
    return noise;
  }

  private float interpolate(float x0, float x1, float alpha) {
    return x0 * (1 - alpha) + alpha * x1;
  }

  private float[][] generateSmoothNoise(float[][] baseNoise, int octave) {
    var width = baseNoise.length;
    var height = baseNoise[0].length;
    var smoothNoise = new float[width][height];

    var samplePeriod = 1 << octave; // calculates 2 ^ k
    var sampleFrequency = 1.0f / samplePeriod;
    for (var i = 0; i < width; i++) {
      int sample_i0 = (i / samplePeriod) * samplePeriod;
      int sample_i1 = (sample_i0 + samplePeriod) % width; // wrap around
      float horizontal_blend = (i - sample_i0) * sampleFrequency;

      for (var j = 0; j < height; j++) {
        int sample_j0 = (j / samplePeriod) * samplePeriod;
        int sample_j1 = (sample_j0 + samplePeriod) % height; // wrap around
        float vertical_blend = (j - sample_j0) * sampleFrequency;
        float top = interpolate(baseNoise[sample_i0][sample_j0], baseNoise[sample_i1][sample_j0], horizontal_blend);
        float bottom = interpolate(baseNoise[sample_i0][sample_j1], baseNoise[sample_i1][sample_j1], horizontal_blend);
        smoothNoise[i][j] = interpolate(top, bottom, vertical_blend);
      }
    }

    return smoothNoise;
  }

  private float[][] generatePerlinNoise(float[][] baseNoise, int octaveCount) {
    var width = baseNoise.length;
    var height = baseNoise[0].length;
    var smoothNoise = new float[octaveCount][][]; // an array of 2D arrays
    var persistence = 0.7f;

    for (var i = 0; i < octaveCount; i++) {
      smoothNoise[i] = generateSmoothNoise(baseNoise, i);
    }

    var perlinNoise = new float[width][height]; // an array of floats initialised to 0

    var amplitude = 1.0f;
    var totalAmplitude = 0.0f;

    for (int octave = octaveCount - 1; octave >= 0; octave--) {
      amplitude *= persistence;
      totalAmplitude += amplitude;

      for (var i = 0; i < width; i++) {
        for (var j = 0; j < height; j++) {
          perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
        }
      }
    }

    for (var i = 0; i < width; i++) {
      for (var j = 0; j < height; j++) {
        perlinNoise[i][j] /= totalAmplitude;
      }
    }

    return perlinNoise;
  }

  private byte[] generateHeightMap(int width, int height, int min, int max, int octaveCount) {
    var baseNoise = generateWhiteNoise(width, height);
    var noise = generatePerlinNoise(baseNoise, octaveCount);
    var bytes = new byte[baseNoise.length * baseNoise[0].length];
    var idx = 0;
    var range = max - min;
    for (var y = 0; y < height; y++) {
      for (var x = 0; x < width; x++) {
        bytes[idx++] = (byte) (noise[x][y] * range + min);
      }
    }
    return bytes;
  }

  public Pixmap generatePixmap(int width, int height, int min, int max, int octaveCount) {
    var bytes = generateHeightMap(width, height, min, max, octaveCount);
    var pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
    for (int i = 0, idx = 0; i < bytes.length; i++) {
      var val = bytes[i];
      pixmap.getPixels().put(idx++, val);
      pixmap.getPixels().put(idx++, val);
      pixmap.getPixels().put(idx++, val);
      pixmap.getPixels().put(idx++, (byte) 255);
    }
    return pixmap;
  }

  public Texture noise(int width, int height) {
    return new Texture(generatePixmap(width, height, 0, 100, 11));
  }

}
