package com.bobkevic.jackson.datatype.deserializers;

/*-
 * #%L
 * jackson-datatype-datastore
 * %%
 * Copyright (C) 2017 Ilja Bobkevic
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static com.bobkevic.jackson.datatype.Caster.cast;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Value;
import java.io.IOException;
import java.util.Map;

class FullEntityDeserializer extends StdDeserializer<FullEntity> {

  static final FullEntityDeserializer INSTANCE = new FullEntityDeserializer();

  private FullEntityDeserializer() {
    super(FullEntity.class);
  }

  static FullEntity mapToFullEntity(final Map<String, Value<?>> valueMap) {
    final FullEntity.Builder<IncompleteKey> builder = FullEntity.newBuilder();
    valueMap.forEach(builder::set);
    return builder.build();
  }

  @Override
  public FullEntity deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException {
    final MapType type =
        ctxt.getTypeFactory().constructMapType(Map.class, String.class, Value.class);
    final JsonDeserializer<Object> deserializer =
        ctxt.findContextualValueDeserializer(type, null);

    return mapToFullEntity(cast(deserializer.deserialize(p, ctxt)));
  }
}
