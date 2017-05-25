trait ResultSet {
  def next: Boolean
}

class Row(rs: ResultSet)

def allRows(rs: ResultSet): Stream[Row] = for {
  hasNext <- Stream.continually(rs.next)
  row = if(hasNext) Some(new Row(rs)) else None
} yield row.get
