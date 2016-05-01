package scala.scalanative
package compiler
package pass

import util.unreachable
import nir._

/** Eliminates returns of Unit values and replaces
  * them with void.
  */
class UnitLowering extends Pass {
  override def preInst = {
    case Inst(n, op) if op.resty == Type.Unit =>
      Seq(
          Inst(op),
          Inst(n, Op.Copy(Val.Unit))
      )
  }

  override def preCf = {
    case Cf.Ret(v) if v.ty == Type.Unit =>
      Cf.Ret(Val.None)
  }

  override def preVal = {
    case Val.Unit =>
      Rt.unit
  }

  override def preType = {
    case Type.Unit =>
      Rt.BoxedUnit

    case Type.Function(params, Type.Unit) =>
      Type.Function(params, Type.Void)
  }
}
