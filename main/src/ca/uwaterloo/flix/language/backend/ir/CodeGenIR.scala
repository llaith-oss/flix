package ca.uwaterloo.flix.language.backend.ir

import ca.uwaterloo.flix.language.ast.{Name, BinaryOperator, SourceLocation, UnaryOperator}
import ca.uwaterloo.flix.runtime.Value

// TODO: Rename this to: ReducedIR
sealed trait CodeGenIR

object CodeGenIR {

  case class ValuePool(strings: StringPool) {
    def valueOf(int: Int, tpe: CodeGenIR.Type): Value = ???
  }

  case class StringPool(xs: Array[String])

  sealed trait Definition

  object Definition {

    /**
     * An AST node that represents the definition of a function.
     *
     * @param name the resolved name of the function.
     * @param args the arguments of the function, for debugging purposes.
     * @param body the expression body of the function.
     * @param tpe the (lambda) type of the function.
     * @param loc the source location of the function definition.
     */
    case class Function(name: Name.Resolved, args: List[String], body: CodeGenIR.Expression, tpe: CodeGenIR.Type.Lambda, loc: SourceLocation) extends CodeGenIR.Definition {
      // TODO: Properly convert the function type. For now we only support returning Int and taking 0 or more Int args.
      val descriptor: String = s"""(${"I" * tpe.args.size })I"""
    }

  }

  sealed trait Expression

  // TODO: Consider whether we want Exp8, Exp16, Exp32, Exp64 or if that information is associated with the type?

  object Expression {

    // NB: Offset is *always* in bits.

    case class LoadBool(e: CodeGenIR.Expression, offset: Int) extends CodeGenIR.Expression

    case class LoadInt8(e: CodeGenIR.Expression, offset: Int) extends CodeGenIR.Expression

    case class LoadInt16(e: CodeGenIR.Expression, offset: Int) extends CodeGenIR.Expression

    case class LoadInt32(e: CodeGenIR.Expression, offset: Int) extends CodeGenIR.Expression

    case class StoreBool(e: CodeGenIR.Expression, offset: Int, v: CodeGenIR.Expression) extends CodeGenIR.Expression

    case class StoreInt8(e: CodeGenIR.Expression, offset: Int, v: CodeGenIR.Expression) extends CodeGenIR.Expression

    case class StoreInt16(e: CodeGenIR.Expression, offset: Int, v: CodeGenIR.Expression) extends CodeGenIR.Expression

    case class StoreInt32(e: CodeGenIR.Expression, offset: Int, v: CodeGenIR.Expression) extends CodeGenIR.Expression


    /**
      * An AST node that represents a constant integer literal.
      *
      * @param value the integer value.
      * @param tpe the type of the integer.
      * @param loc the source location of the integer.
      */
    // TODO: We currently only support 32 bit ints. Larger (smaller) values will overflow (underflow).
    case class Const(value: scala.Int, tpe: CodeGenIR.Type, loc: SourceLocation) extends CodeGenIR.Expression

    /**
     * A typed AST node representing a local variable expression (i.e. a parameter or let-bound variable).
     *
     * @param localVar the local variable being referenced.
     * @param tpe the type of the variable.
     * @param loc the source location of the variable.
     */
    case class Var(localVar: CodeGenIR.LocalVar, tpe: CodeGenIR.Type, loc: SourceLocation) extends CodeGenIR.Expression

    /**
     * A typed AST node representing a function call.
     *
     * @param name the name of the function being called.
     * @param args the function arguments.
     * @param tpe the return type of the function.
     * @param loc the source location.
     */
    case class Apply(name: Name.Resolved, args: List[CodeGenIR.Expression], tpe: CodeGenIR.Type, loc: SourceLocation) extends CodeGenIR.Expression

    /**
     * A typed AST node representing a let expression.
     *
     * @param localVar the bound variable.
     * @param exp1 the value of the bound variable.
     * @param exp2 the body expression in which the bound variable is visible.
     * @param tpe the type of the expression (which is equivalent to the type of the body expression).
     * @param loc the source location.
     */
    case class Let(localVar: CodeGenIR.LocalVar, exp1: CodeGenIR.Expression, exp2: CodeGenIR.Expression, tpe: CodeGenIR.Type, loc: SourceLocation) extends CodeGenIR.Expression

    /**
      * An AST node that represents a unary expression.
      *
      * @param op the unary operator.
      * @param exp the expression.
      * @param tpe the type of the expression.
      * @param loc the source location of the expression.
      */
    case class Unary(op: UnaryOperator, exp: CodeGenIR.Expression, tpe: CodeGenIR.Type, loc: SourceLocation) extends CodeGenIR.Expression

    /**
      * An AST node that represents a binary expression.
      *
      * @param op the binary operator.
      * @param exp1 the left expression.
      * @param exp2 the right expression.
      * @param tpe the type of the expression.
      * @param loc the source location of the expression.
      */
    case class Binary(op: BinaryOperator, exp1: CodeGenIR.Expression, exp2: CodeGenIR.Expression, tpe: CodeGenIR.Type, loc: SourceLocation) extends CodeGenIR.Expression

    /**
      * An AST node that represents an if-then-else expression.
      *
      * @param exp1 the conditional expression.
      * @param exp2 the consequent expression.
      * @param exp3 the alternative expression.
      * @param tpe the type of the consequent and alternative expression.
      * @param loc the source location of the expression.
      */
    case class IfThenElse(exp1: CodeGenIR.Expression, exp2: CodeGenIR.Expression, exp3: CodeGenIR.Expression, tpe: CodeGenIR.Type, loc: SourceLocation) extends CodeGenIR.Expression

    case class Tag(name: Name.Resolved, tag: String, exp: CodeGenIR.Expression, tpe: CodeGenIR.Type, loc: SourceLocation) extends CodeGenIR.Expression

    // THIS RETURNS A BOOLEAN
    case class TagOf(exp: CodeGenIR.Expression, name: Name.Resolved, tag: String, tpe: CodeGenIR.Type, loc: SourceLocation) extends CodeGenIR.Expression

    case class Tuple(elms: List[CodeGenIR.Expression], tpe: CodeGenIR.Type, loc: SourceLocation) extends CodeGenIR.Expression

    // TODO: ElementAt( (1,2), 0) = 1
    case class TupleAt(base: CodeGenIR.Expression, offset: Int, tpe: CodeGenIR.Type, loc: SourceLocation) extends CodeGenIR.Expression

    case class Set(elms: List[CodeGenIR.Expression], tpe: CodeGenIR.Type, loc: SourceLocation) extends CodeGenIR.Expression

    case class Error(loc: SourceLocation) extends CodeGenIR.Expression

  }

  /**
    * A common super-type for types.
    */
  sealed trait Type

  object Type {

    /**
      * The type of booleans.
      */
    case object Bool extends CodeGenIR.Type

    /**
      * The type of 8-bit signed integers.
      */
    case object Int8 extends CodeGenIR.Type

    /**
      * The type of 16-bit signed integers.
      */
    case object Int16 extends CodeGenIR.Type

    /**
      * The type of 32-bit signed integers.
      */
    case object Int32 extends CodeGenIR.Type

    /**
      * The type of 64-bit signed integers.
      */
    case object Int64 extends CodeGenIR.Type

    case class Tag(name: Name.Resolved, ident: Name.Ident, tpe: CodeGenIR.Type) extends CodeGenIR.Type

    case class Enum(cases: Map[String, CodeGenIR.Type.Tag]) extends CodeGenIR.Type

    case class Tuple(elms: List[CodeGenIR.Type]) extends CodeGenIR.Type

    case class Lambda(args: List[CodeGenIR.Type], retTpe: CodeGenIR.Type) extends CodeGenIR.Type

  }

  /**
   * A local variable that is being referenced.
   *
   * @param offset the (0-based) local variable slot in the JVM method.
   * @param name the name of the variable, for debugging purposes
   */
  case class LocalVar(offset: Int, name: String) extends CodeGenIR


}
