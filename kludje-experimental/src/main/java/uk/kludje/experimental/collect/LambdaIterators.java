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

package uk.kludje.experimental.collect;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.*;

/**
 * Adapts other types/functional interfaces to {@link java.util.Iterator} instances.
 */
public final class LambdaIterators {

  private LambdaIterators() {
  }

  public static <E> Iterator<E> iterator(BooleanSupplier hasNext,
                                         Supplier<E> next) {
    Objects.requireNonNull(hasNext, "hasNext");
    Objects.requireNonNull(next, "next");

    class LambdaIterator extends BaseIterator<E> {
      @Override
      public boolean hasNext() {
        return hasNext.getAsBoolean();
      }

      @Override
      protected E nextElement() {
        return next.get();
      }
    }

    return new LambdaIterator();
  }

  public static <E> Iterator<E> mutableIterator(BooleanSupplier hasNext,
                                                Supplier<E> next,
                                                Runnable remove) {
    Objects.requireNonNull(hasNext, "hasNext");
    Objects.requireNonNull(next, "next");
    Objects.requireNonNull(remove, "remove");

    class MutableLambdaIterator extends MutableIterator<E> {

      @Override
      public boolean hasNext() {
        return hasNext.getAsBoolean();
      }

      @Override
      protected E nextElement() {
        return next.get();
      }

      @Override
      protected void removeElement() {
        remove.run();
      }
    }

    return new MutableLambdaIterator();
  }

  public static <E> Iterator<E> indexIterator(IntPredicate hasNext,
                                              IntFunction<E> next) {
    Objects.requireNonNull(hasNext, "hasNext");
    Objects.requireNonNull(next, "next");

    class IndexIterator extends BaseIterator<E> {
      private int index;

      @Override
      public boolean hasNext() {
        return hasNext.test(index);
      }

      @Override
      protected E nextElement() {
        return next.apply(index++);
      }
    }

    return new IndexIterator();
  }

  public static <E> Iterator<E> mutableIndexIterator(IntPredicate hasNext,
                                                     IntFunction<E> next,
                                                     IntConsumer remove) {
    Objects.requireNonNull(hasNext, "hasNext");
    Objects.requireNonNull(next, "next");
    Objects.requireNonNull(remove, "remove");

    class MutableIndexIterator extends MutableIterator<E> {
      private int index;

      @Override
      public boolean hasNext() {
        return hasNext.test(index);
      }

      @Override
      protected E nextElement() {
        return next.apply(index++);
      }

      @Override
      protected void removeElement() {
        remove.accept(index);
      }
    }

    return new MutableIndexIterator();
  }

  @SafeVarargs
  public static <E> Iterator<E> asIterator(E... elements) {
    Objects.requireNonNull(elements, "elements");
    return indexIterator(i -> i < elements.length, i -> elements[i]);
  }

  @SafeVarargs
  public static <E> Iterator<E> asIterator(IntConsumer remove, E... elements) {
    Objects.requireNonNull(elements, "elements");
    return mutableIndexIterator(i -> i < elements.length, i -> elements[i], remove);
  }

  private static abstract class BaseIterator<E> implements Iterator<E> {
    @Override
    public E next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      return nextElement();
    }

    protected abstract E nextElement();
  }

  private static abstract class MutableIterator<E> extends BaseIterator<E> {
    private boolean canRemove = false;

    @Override
    public E next() {
      E e = super.next();
      canRemove = true;
      return e;
    }

    @Override
    public void remove() {
      if (canRemove) {
        canRemove = false;
        removeElement();
      } else {
        throw new IllegalStateException();
      }
    }

    protected abstract void removeElement();
  }
}