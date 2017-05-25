val list1 = List(("192.168.0.1", "A", true, 'C'), ("192.168.0.2", "B", false, 'D'), ("192.168.0.3", "C", true, 'E'))
val list2 = List(("192.168.0.104", 2, 5.7), ("192.168.0.119", 2, 13.4), ("205.251.0.185", 24, 11.2), ("192.168.0.1", 153, 34.8))

val m1 = list1.view.map { case (key, v1, v2, v3) ⇒ (key, (v1, v2, v3)) }.toMap
val m2 = list2.view.map { case (key, v1, v2) ⇒ (key, (v1, v2)) }.toMap

m1.keySet.intersect(m2.keySet).map(key ⇒ (key, m1(key), m2(key)))

list1.collect { case (ip, x1, x2, x3) if m2 contains ip => (ip, (x1, x2, x3), m2(ip)) }