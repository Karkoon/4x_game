package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.config.FieldConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GdxBaseTest {

  //TODO fix in #15
  @Test
  void gdx_files_should_not_be_null() {
    assertNotNull(Gdx.files);
  }

  @Test
  void mocking_field_should_pass() {
    var field = mock(FieldConfig.class);
    when(field.getName()).thenReturn("mocked");
    assertEquals("mocked", field.getName());
  }

}
