import java.util.Map;
import java.util.Iterator;

import soot.Body;
import soot.Unit;
import soot.jimple.*;
import soot.shimple.*;
import soot.BodyTransformer;
import soot.G;
import soot.PackManager;
import soot.Transform;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.Type;
import soot.Value;

class SimpleStmtSwitch extends AbstractStmtSwitch {
	boolean isReturn, isInvokeVirtual, isInvokeInterface, isThrow;

  SimpleStmtSwitch() {
    isReturn = false;
    isInvokeVirtual = false;
    isInvokeInterface = isThrow = false;
  }

  public void caseAssignStmt(AssignStmt stmt) {
    if(SimpleMain.debug) G.v().out.println("  AssignStmt: "+stmt);
  }

  public void caseBreakpointStmt(BreakpointStmt stmt) {
    if(SimpleMain.debug) G.v().out.println("  BreakpointStmt: "+stmt);
  }
  public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
    if(SimpleMain.debug) G.v().out.println("  EnterMonitorStmt: "+stmt);
  }
  public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
    if(SimpleMain.debug) G.v().out.println("  ExitMonitorStmt: "+stmt);
  }
  public void caseGotoStmt(GotoStmt stmt) {
    if(SimpleMain.debug) G.v().out.println("  GotoStmt: "+stmt);
  }
  public void caseIdentityStmt(IdentityStmt stmt) {
    if(SimpleMain.debug) G.v().out.println("  IdentityStmt: "+stmt);
  }

  public void caseIfStmt(IfStmt stmt) {
    if(SimpleMain.debug) G.v().out.println("  IfStmt: " + stmt);
  }

  public void caseInvokeStmt(InvokeStmt stmt) {
    if(SimpleMain.debug) G.v().out.println("  InvokeStmt: "+stmt);
		stmt.getInvokeExpr().apply( new ExprSwitch() {
			public void caseDynamicInvokeExpr(DynamicInvokeExpr e) {
				isInvokeVirtual = true;
			}
			public void caseVirtualInvokeExpr(VirtualInvokeExpr e) {
				isInvokeVirtual = true;
			}
			public void caseInterfaceInvokeExpr(InterfaceInvokeExpr e) {
				isInvokeInterface = true;
			}
			
      public void	caseAddExpr(AddExpr v)  {}
	    public void	caseAndExpr(AndExpr v)  {}
	    public void	caseCastExpr(CastExpr v)  {}
	    public void	caseCmpExpr(CmpExpr v)  {}
	    public void	caseCmpgExpr(CmpgExpr v)  {}
	    public void	caseCmplExpr(CmplExpr v)  {}
	    public void	caseDivExpr(DivExpr v)  {}
	    public void	caseEqExpr(EqExpr v)  {}
	    public void	caseGeExpr(GeExpr v)  {}
	    public void	caseGtExpr(GtExpr v)  {}
	    public void	caseInstanceOfExpr(InstanceOfExpr v)  {}
	    public void	caseLeExpr(LeExpr v)  {}
	    public void	caseLengthExpr(LengthExpr v)  {}
	    public void	caseLtExpr(LtExpr v)  {}
	    public void	caseMulExpr(MulExpr v)  {}
	    public void	caseNeExpr(NeExpr v)  {}
	    public void	caseNegExpr(NegExpr v)  {}
	    public void	caseNewArrayExpr(NewArrayExpr v)  {}
	    public void	caseNewExpr(NewExpr v)  {}
	    public void	caseNewMultiArrayExpr(NewMultiArrayExpr v)  {}
	    public void	caseOrExpr(OrExpr v)  {}
	    public void	caseRemExpr(RemExpr v)  {}
	    public void	caseShlExpr(ShlExpr v)  {}
	    public void	caseShrExpr(ShrExpr v)  {}
	    public void	caseSpecialInvokeExpr(SpecialInvokeExpr v)  {}
	    public void	caseStaticInvokeExpr(StaticInvokeExpr v)  {}
	    public void	caseSubExpr(SubExpr v)  {}
	    public void	caseUshrExpr(UshrExpr v)  {}
	    public void	caseXorExpr(XorExpr v)  {}
	    public void	defaultCase(Object obj)  {}
		}
		);
  }

  public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
    if(SimpleMain.debug) G.v().out.println("  LookupSwitchStmt: "+stmt);
  }
  public void caseNopStmt(NopStmt stmt) {
    if(SimpleMain.debug) G.v().out.println("  NopStmt: "+stmt);
  }
  public void caseRetStmt(RetStmt stmt) {
    if(SimpleMain.debug) G.v().out.println("  RetStmt: "+stmt);
		isReturn = true;
  }
  public void caseReturnStmt(ReturnStmt stmt) {
    if(SimpleMain.debug) G.v().out.println("  ReturnStmt: "+stmt);
		isReturn = true;
  }
  public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
    if(SimpleMain.debug) G.v().out.println("  ReturnVoidStmt: "+stmt);
		isReturn = true;
  }
  public void caseTableSwitchStmt(TableSwitchStmt stmt) {
    if(SimpleMain.debug) G.v().out.println("  TableSwitchStmt: "+stmt);
  }
  public void caseThrowStmt(ThrowStmt stmt) {
    if(SimpleMain.debug) G.v().out.println("  ThrowStmt: "+stmt);
		isThrow = true;
  }
  public void defaultCase(Object o) {
    if(SimpleMain.debug) G.v().out.println("  defaultCase: "+o);
  }

	public boolean isReturn() { return isReturn; }
	public boolean isThrow() { return isThrow; }
	public boolean isInvokeVirtual() { return isInvokeVirtual; }
	public boolean isInvokeInterface() { return isInvokeInterface; }


}
