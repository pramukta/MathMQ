(* Mathematica Package *)

(* Created by the Wolfram Workbench Oct 5, 2012 *)

BeginPackage["MathMQ`", {"JLink`"}]
(* Exported symbols added here with SymbolName::usage *) 


Begin["`Private`"]
(* Implementation of the package *)
$GLASSFISHLIB = "/home/prak/glassfish3/glassfish/lib";

InstallJava[];
AddToClassPath[FileNameJoin[$GLASSFISHLIB, "gf-client.jar"]]; 
LoadJavaClass["edu.georgetown.mathmq.QpidClient"];

End[]

EndPackage[]

