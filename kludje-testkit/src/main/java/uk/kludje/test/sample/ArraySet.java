/*
 * Copyright 2015 McDowell
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
 */

package uk.kludje.test.sample;

import uk.kludje.experimental.collect.CollectionAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ArraySet {
  public static void main(String[] args) {
    List<String> list = Arrays.asList("foo", "bar");
    Set<String> set = CollectionAdapter.asSet(list::iterator, list::size);
    System.out.println(set.contains("foo"));
    System.out.println(set.contains("baz"));
  }
}
