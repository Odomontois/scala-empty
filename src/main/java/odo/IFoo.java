package odo;

/**
 * User: Oleg
 * Date: 01-Nov-15
 * Time: 16:36
 */
public interface IFoo {
    <E> Encoder1<E[]> arrayArg(Encoder1<? super E> elemEncoder);
}
