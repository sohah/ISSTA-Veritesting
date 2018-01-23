/* Soot - a J*va Optimization Framework
 * Copyright (C) 2008 Eric Bodden
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */
import java.util.Map;
import java.util.List;
import java.util.Iterator;

import soot.Body;
import soot.Local;
import soot.SootMethod;
import soot.Unit;
import soot.util.Chain;
import soot.jimple.*;
import soot.shimple.*;
import soot.BodyTransformer;
import soot.G;
import soot.PackManager;
import soot.Transform;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.tagkit.BytecodeOffsetTag;




public class SimpleMain {

  public static int g_if_ret_distance = 0, g_if_ret_count = 0;
  public static int g_if_iv_distance = 0, g_if_iv_count = 0;
  public static int g_if_throw_distance = 0, g_if_throw_count = 0;
  public static int g_pureVeritestingRegionSize = 0, g_pureVeritestingRegionCount = 0;
  public static int numMethods = 0;
  public static final boolean debug = false;
  public static void main(String[] args) {
    // this jb pack does not work, perhaps, by design
    PackManager.v().getPack("jb").add(
        new Transform("jb.myTransform", new BodyTransformer() {
          protected void internalTransform(Body body, String phase, Map options) {
            SootMethod method = body.getMethod();
            Chain units = body.getUnits();
            Iterator it = units.snapshotIterator();
            while(it.hasNext()) 
              G.v().out.println("*it = "+it.next());
          }
        }));
    PackManager.v().getPack("stp").add(
        new Transform("stp.myTransform", new BodyTransformer() {
          protected void internalTransform(Body body, String phase, Map options) {
            // // prints out locals, but those dont have stack locations
            // Chain<Local> locals = body.getLocals();
            // G.v().out.println("locals = "+locals);
            // Iterator it = locals.iterator();
            // while(it.hasNext()) {
            //   Local l = (Local) it.next();
            //   G.v().out.println("l.name = " + l.getName() + 
            //     " l.type = " + l.getType() + 
            //     " l.num = " + l.getNumber() + 
            //     " l.getUB = " + l.getUseBoxes());
            // }
            if(debug) G.v().out.println("Starting analysis for "+body.getMethod().getName());
	    numMethods++;
            MyAnalysis m = new MyAnalysis(new ExceptionalUnitGraph(body));
            // use G.v().out instead of System.out so that Soot can
            // redirect this output to the Eclipse console
            // if(!body.getMethod().getName().contains("testMe3")) return;
            // G.v().out.println(body.getMethod());
            // Iterator it = body.getUnits().iterator();
            // while (it.hasNext()) {
            //   Unit u = (Unit) it.next();
            //   SimpleStmtSwitch SimpleStmtSwitch = new SimpleStmtSwitch();
            //   u.apply(SimpleStmtSwitch);
            //   //G.v().out.println(u);
            //   G.v().out.println("");
            // }
          } 
        } 
        )
    );
    soot.Main.main(args);
		G.v().out.println("if-iv-distance = " + g_if_iv_distance + " ("+g_if_iv_count + ")");
		G.v().out.println("if-ret-distance = " + g_if_ret_distance + " ("+g_if_ret_count + ")");
		G.v().out.println("if-throw-distance = " + g_if_throw_distance + " ("+g_if_throw_count + ")");
    G.v().out.println("pure-Veritesting-regions-size = " + g_pureVeritestingRegionSize + " (" + g_pureVeritestingRegionCount + ")");
    G.v().out.println("number-of-methods = "+numMethods);
  }
  
  public static class MyAnalysis /*extends ForwardFlowAnalysis */ {
    ExceptionalUnitGraph g;
    int index=0;
    int if_ret_distance = 0, if_ret_count = 0;
    int if_iv_distance = 0, if_iv_count = 0;
    int if_throw_distance = 0, if_throw_count = 0;
    int pureVeritestingRegionSize = 0, pureVeritestingRegionCount = 0;
    private final Object lock = new Object();

    public MyAnalysis(ExceptionalUnitGraph exceptionalUnitGraph) {
      g = exceptionalUnitGraph;
      doAnalysis();
    }
    
    private void printTags(Stmt stmt) {
      Iterator tags_it = stmt.getTags().iterator();
      while(tags_it.hasNext()) G.v().out.println(tags_it.next());
      G.v().out.println("  end tags");
    }
 
    public Unit getIPDom(Unit u) {
      MHGPostDominatorsFinder m = new MHGPostDominatorsFinder(g);
      Unit u_IPDom = (Unit) m.getImmediateDominator(u);
      return u_IPDom;
    }

    public void countDistanceToExitPoints(List<Unit> succs, Unit commonSucc, int ifIndex, int depth) {
      Unit thenUnit = succs.get(0);
      Unit elseUnit = succs.get(1);
      final int savedIndex = index;

      int foundExitPoint = 0;
      if(depth > 5) return;

      if(debug) G.v().out.println("Starting countDistanceToExitPoints for ifIndex = " + ifIndex);
      // Create thenExpr
      while(thenUnit != commonSucc) {
				BytecodeOffsetTag b = (BytecodeOffsetTag) ((Stmt)thenUnit).getTag("BytecodeOffsetTag");
				if((b != null) && (b.getBytecodeOffset() != 0)) index++;
        else { if(debug) G.v().out.printf(" null: ");
        }
                
        SimpleStmtSwitch simpleStmtSwitch = new SimpleStmtSwitch();
        if(debug)
          G.v().out.printf("%d: ", index);
        thenUnit.apply(simpleStmtSwitch);
        if(simpleStmtSwitch.isInvokeVirtual() || simpleStmtSwitch.isInvokeInterface()) {
          if_iv_distance += (index - savedIndex);
          // G.v().out.println("adding "+index + " - " + savedIndex + " to if_iv_distance");
          if_iv_count++;
          if(savedIndex > ifIndex) {
            if_iv_distance += (index - ifIndex);
            // G.v().out.println("adding "+index+ " - " +ifIndex + " to if_iv_distance");
            if_iv_count++;
          }
          foundExitPoint = 1;
        }
        else if(simpleStmtSwitch.isReturn()) {
          if_ret_distance += (index - savedIndex);
          if(debug) G.v().out.println("t: adding "+index + " - " + savedIndex + " to if_ret_distance");
          if_ret_count++;
          if(savedIndex > ifIndex) {
            if_ret_distance += (index - ifIndex);
            if(debug) G.v().out.println("t: adding "+index + " - " + ifIndex + " to if_ret_distance");
            if_ret_count++;
          }
          foundExitPoint = 1;
        }
        else if(simpleStmtSwitch.isThrow()) {
          if_throw_distance += (index - savedIndex);
          if_throw_count++;
          if(savedIndex > ifIndex) {
            if_throw_distance += (index - ifIndex);
            if_throw_count++;
          }
          foundExitPoint = 1;
        }
        List<Unit> s = g.getUnexceptionalSuccsOf(thenUnit);
        if(s.size() > 1) {
          Unit ipdom_thenUnit = getIPDom(thenUnit);
          countDistanceToExitPoints(s, ipdom_thenUnit, ifIndex, depth+1);
          thenUnit = ipdom_thenUnit; 
        } else if(s.size() == 1) { 
          thenUnit = g.getUnexceptionalSuccsOf(thenUnit).get(0);
				} else break;
      }

      if(elseUnit != commonSucc)
        index = savedIndex;

      // Create elseExpr, similar to thenExpr
      while(elseUnit != commonSucc) {
				BytecodeOffsetTag b = (BytecodeOffsetTag) ((Stmt)elseUnit).getTag("BytecodeOffsetTag");
				if( (b != null) && (b.getBytecodeOffset() != 0)) index++;
        else { if(debug) G.v().out.printf(" null: ");
        }

        SimpleStmtSwitch simpleStmtSwitch= new SimpleStmtSwitch();
        if(debug) G.v().out.printf("%d: ", index);
        elseUnit.apply(simpleStmtSwitch);
        if(simpleStmtSwitch.isInvokeVirtual() || simpleStmtSwitch.isInvokeInterface()) {
          if_iv_distance += (index - savedIndex);
          // G.v().out.println("adding "+index + " - " + savedIndex + " to if_iv_distance");
          if_iv_count++;
          if(savedIndex > ifIndex) {
            if_iv_distance += (index - ifIndex);
          // G.v().out.println("adding "+index + " - " + ifIndex + " to if_iv_distance");
            if_iv_count++;
          }
          foundExitPoint = 1;
        }
        else if(simpleStmtSwitch.isReturn()) {
          foundExitPoint = 1;
          if_ret_distance += (index - savedIndex);
          if(debug) G.v().out.println("e: adding "+index + " - " + savedIndex + " to if_ret_distance");
          if_ret_count++;
          if(savedIndex > ifIndex) {
            if_ret_distance += (index - ifIndex);
            if(debug) G.v().out.println("e: adding "+index + " - " + ifIndex + " to if_ret_distance");
            if_ret_count++;
          }
        }
        else if(simpleStmtSwitch.isThrow()) {
          if_throw_distance += (index - savedIndex);
          if_throw_count++;
          if(savedIndex > ifIndex) {
            if_throw_distance += (index - ifIndex);
            if_throw_count++;
          }
          foundExitPoint = 1;
        }
        List<Unit> s = g.getUnexceptionalSuccsOf(elseUnit);
        if(s.size() > 1) {
          Unit ipdom_elseUnit = getIPDom(elseUnit);
          countDistanceToExitPoints(s, ipdom_elseUnit, ifIndex, depth+1);
          elseUnit = ipdom_elseUnit; 
        } 
        else if(s.size() == 1) {
          elseUnit = g.getUnexceptionalSuccsOf(elseUnit).get(0);
        } else {
          break;
        }
      }
      if(debug) G.v().out.println("foundExitPoint = "+foundExitPoint);
      //new Exception().printStackTrace();
      if(foundExitPoint == 0) {
        if(debug) G.v().out.println("pure veritesting region ("+index+", "+savedIndex+") "+foundExitPoint);
        pureVeritestingRegionCount++;
        pureVeritestingRegionSize += (index - savedIndex);
      }
    }

    

    public void doAnalysis() {
      List<Unit> heads = g.getHeads(); 
      if(heads.size()==1) {
        Unit u = (Unit) heads.get(0);
        while(true) {
					if(u == null) break;
					BytecodeOffsetTag b = (BytecodeOffsetTag) ((Stmt)u).getTag("BytecodeOffsetTag");
					if((b != null) && (b.getBytecodeOffset() != 0)) index++;
          //printTags((Stmt)u);
          SimpleStmtSwitch simpleStmtSwitch = new SimpleStmtSwitch();
          if(debug) G.v().out.printf("%d: ", index);
          u.apply(simpleStmtSwitch);
          List<Unit> succs = g.getUnexceptionalSuccsOf(u);
          if(succs.size()==1) {
            u = succs.get(0);
            continue;
          } else if (succs.size()==0) break;
          else {
            // G.v().out.printf("  #succs = %d\n", succs.size());
            Unit commonSucc = getIPDom(u);
            countDistanceToExitPoints(succs, commonSucc, index, 1);
            u = commonSucc;
          }
          if(debug) G.v().out.println("");
        }
				if(debug) {
          G.v().out.println("if-iv-distance = " + if_iv_distance + " ("+if_iv_count + ")");
				  G.v().out.println("if-ret-distance = " + if_ret_distance + " ("+if_ret_count + ")");
				  G.v().out.println("if-throw-distance = " + if_throw_distance + " ("+if_throw_count + ")");
          G.v().out.println("#pure Veritesting regions = " + pureVeritestingRegionCount + ", size = " + pureVeritestingRegionSize);
          G.v().out.println("\n\n");
        }
        g_if_iv_distance += if_iv_distance;
        g_if_iv_count += if_iv_count;
        g_if_ret_distance += if_ret_distance;
        g_if_ret_count += if_ret_count;
        g_if_throw_distance += if_throw_distance;
        g_if_throw_count += if_throw_count;
        g_pureVeritestingRegionCount += pureVeritestingRegionCount;
        g_pureVeritestingRegionSize += pureVeritestingRegionSize;
      }
    }

  }
  
}
