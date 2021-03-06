/*
 *
 * Copyright $year McDowell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * /
 */

package uk.kludje.test;

import uk.kludje.Meta;

import java.time.LocalDate;

public class PersonPojo {
  private static final Meta<PersonPojo> META =
      Meta.meta(PersonPojo.class)
          .longs($ -> $.id)
          .objects($ -> $.name, $ -> $.dateOfBirth);

  private final long id;
  private final String name;
  private final LocalDate dateOfBirth;

  public PersonPojo(long id, String name, LocalDate dateOfBirth) {
    this.id = id;
    this.name = name;
    this.dateOfBirth = dateOfBirth;
  }

  public String getName() {
    return name;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  @Override
  public boolean equals(Object obj) {
    return META.equals(this, obj);
  }

  @Override
  public int hashCode() {
    return META.hashCode(this);
  }

  @Override
  public String toString() {
    return META.toString(this);
  }
}
