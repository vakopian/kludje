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

package uk.kludje;

import java.util.Objects;

/**
 * A functional interface with null-safe checks.
 * <p>
 * For use with getter chains where one or more elements in the chain
 * can be null.
 * <p>
 * Example usage:
 * <pre>D d = Nullifier.eval(a, A::getB, B::getC, C::getD);</pre>
 * <p>
 * Implement {@link #$apply(Object)}; invoke {@link #apply(Object)}.
 *
 * @param <T> the input
 * @param <R> the result
 */
@FunctionalInterface
public interface Nullifier<T, R> {

  /**
   * Creates a null-safe chain of calls spanning possibly null call sites.
   * <p>
   * The functions may not be null, but the inputs and outputs may be.
   * <p>
   * A number of overloaded methods are provided with varying argument counts.
   *
   * @param f0  the initial function; MUST NOT be null
   * @param f1  a subsequent function; MUST NOT be null
   * @param <A> the initial type
   * @param <B> an intermediary type
   * @param <Z> the resultant type
   * @return a function that, given A, returns Z, or null if any element in the chain is null
   */
  public static <A, B, Z> Nullifier<A, Z> span(Nullifier<? super A, ? extends B> f0,
                                               Nullifier<? super B, ? extends Z> f1) {
    Objects.requireNonNull(f0, "0");
    Objects.requireNonNull(f1, "1");
    return a -> eval(a, f0, f1);
  }

  public static <A, B, C, Z> Nullifier<A, Z> span(Nullifier<? super A, ? extends B> f0,
                                                  Nullifier<? super B, ? extends C> f1,
                                                  Nullifier<? super C, ? extends Z> f2) {
    Objects.requireNonNull(f0, "0");
    Objects.requireNonNull(f1, "1");
    Objects.requireNonNull(f2, "2");
    return a -> eval(a, f0, f1, f2);
  }

  public static <A, B, C, D, Z> Nullifier<A, Z> span(Nullifier<? super A, ? extends B> f0,
                                                     Nullifier<? super B, ? extends C> f1,
                                                     Nullifier<? super C, ? extends D> f2,
                                                     Nullifier<? super D, ? extends Z> f3) {
    Objects.requireNonNull(f0, "0");
    Objects.requireNonNull(f1, "1");
    Objects.requireNonNull(f2, "2");
    Objects.requireNonNull(f3, "3");
    return a -> eval(a, f0, f1, f2, f3);
  }

  public static <A, B, C, D, E, Z> Nullifier<A, Z> span(Nullifier<? super A, ? extends B> f0,
                                                        Nullifier<? super B, ? extends C> f1,
                                                        Nullifier<? super C, ? extends D> f2,
                                                        Nullifier<? super D, ? extends E> f3,
                                                        Nullifier<? super E, ? extends Z> f4) {
    Objects.requireNonNull(f0, "0");
    Objects.requireNonNull(f1, "1");
    Objects.requireNonNull(f2, "2");
    Objects.requireNonNull(f3, "3");
    Objects.requireNonNull(f4, "4");
    return a -> eval(a, f0, f1, f2, f3, f4);
  }

  public static <A, Z> Z eval(A a,
                              Nullifier<? super A, ? extends Z> f0) {
    return f0.apply(a);
  }

  /**
   * Convenience method for evaluating a chain of {@link Nullifier} calls.
   * <p>
   * A number of overloaded methods are provided with varying argument counts.
   *
   * @param a the root object in the object graph (may be null)
   * @param f0 is passed "a"; MUST NOT be null
   * @param f1 is passed the result of "f0"; MUST NOT be null
   * @param <A> the root type
   * @param <B> an intermediary type
   * @param <Z> the result type
   * @return the result of the function chain or null
   * @see #eval(Object, Nullifier)
   * @see #eval(Object, Nullifier, Nullifier)
   * @see #eval(Object, Nullifier, Nullifier, Nullifier)
   * @see #eval(Object, Nullifier, Nullifier, Nullifier, Nullifier, Nullifier)
   */
  public static <A, B, Z> Z eval(A a,
                                 Nullifier<? super A, ? extends B> f0,
                                 Nullifier<? super B, ? extends Z> f1) {
    B b = f0.apply(a);
    return f1.apply(b);
  }

  public static <A, B, C, Z> Z eval(A a,
                                    Nullifier<? super A, ? extends B> f0,
                                    Nullifier<? super B, ? extends C> f1,
                                    Nullifier<? super C, ? extends Z> f2) {
    B b = f0.apply(a);
    C c = f1.apply(b);
    return f2.apply(c);
  }

  public static <A, B, C, D, Z> Z eval(A a,
                                       Nullifier<? super A, ? extends B> f0,
                                       Nullifier<? super B, ? extends C> f1,
                                       Nullifier<? super C, ? extends D> f2,
                                       Nullifier<? super D, ? extends Z> f3) {
    B b = f0.apply(a);
    C c = f1.apply(b);
    D d = f2.apply(c);
    return f3.apply(d);
  }

  public static <A, B, C, D, E, Z> Z eval(A a,
                                          Nullifier<? super A, ? extends B> f0,
                                          Nullifier<? super B, ? extends C> f1,
                                          Nullifier<? super C, ? extends D> f2,
                                          Nullifier<? super D, ? extends E> f3,
                                          Nullifier<? super E, ? extends Z> f4) {
    B b = f0.apply(a);
    C c = f1.apply(b);
    D d = f2.apply(c);
    E e = f3.apply(d);
    return f4.apply(e);
  }

  /**
   * If the argument is null, returns null; else invokes {@link #$apply(Object)}.
   * <p>
   * This method rethrows any exception thrown by {@link #$apply(Object)} as an unchecked exception.
   *
   * @param t the input which may be null
   * @return the result which may be null
   * @see Exceptions#throwChecked(Throwable)
   */
  default R apply(T t) {
    try {
      return (t == null) ? null : $apply(t);
    } catch (Exception e) {
      throw Exceptions.throwChecked(e);
    }
  }

  /**
   * Implement this method with a lambda expression/method reference.
   * <p>
   * Consumers should invoke {@link #apply(Object)} and NOT call this method directly.
   *
   * @param t the argument; not null if invoked by default {@link #apply(Object)}
   * @return the result
   * @throws Exception on error
   */
  R $apply(T t) throws Exception;

  /**
   * Chains two instances together.
   *
   * @param after the nullifier to invoke after this; MUST NOT be null
   * @param <V> the new result type
   * @return a new nullifier
   */
  default <V> Nullifier<T, V> andThenSpan(Nullifier<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return (T t) -> after.apply(apply(t));
  }
}
