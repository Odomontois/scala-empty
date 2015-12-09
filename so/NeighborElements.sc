val elems = List(1, 3, -1, 0, 2, -4, 6).sorted

(elems.tail, elems).zipped.map(_ - _).reverse