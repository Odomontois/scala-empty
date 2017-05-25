import shapeless._
import shapeless.ops.tuple
trait LowLevelFlatten extends Poly1 {
  implicit def anyFlat[T] = at[T](x=> Tuple1(x))
}

object concat extends Poly2 {
  implicit def atTuples[T1, T2](implicit prepend: tuple.Prepend[T1, T2]): Case.Aux[T1, T2, prepend.Out] =
    at[T1,T2]((t1,t2) => prepend(t1,t2))
}

object flatten extends LowLevelFlatten {
  implicit def tupleFlat[T, M](implicit
                               mapper: tuple.Mapper.Aux[T, flatten.type, M],
                               reducer: tuple.LeftReducer[M, concat.type]
                                ): Case.Aux[T, reducer.Out] =
    at[T](t => reducer(mapper(t)))
}

List(((9, 679, 16), ((2, 275), 1)), ((250, 976, 13), ((2, 218), 1))).map(flatten)